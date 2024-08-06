/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Copyright © February 2024 Fadimana Kilci - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Fadimana Kilci  <fadimekilci07@gmail.com>, August 2024
 */

package com.sparksign;

import com.sparksign.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private final String                host;
    private final int                   port;
    private AtomicInteger retryLimit;
    private final ExecutorService service;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Main(String host, int port) {
        this.host                       = host;
        this.port                       = port;
        this.retryLimit                 = new AtomicInteger(3);
        this.service                    = Executors.newCachedThreadPool();
    }

    public void start() {
        service.execute(this::connect);
    }

    private void handleServer() {
        service.execute(() -> new Thread(() -> {
            while (true) {
                try {
                    Message message = (Message) inputStream.readObject();
                    System.out.println("Received from message broker to Client: " +
                            message.getContent());
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Message broker disconnected.");
                    retryConnection();
                    break;
                }
            }
        }).start());
    }

    public void connect() {
        service.execute(() -> {
            try {
                socket = new Socket(host, port);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Client connected to message broker");
                clearRetry();
                handleServer();
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + host);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            } catch (IOException e) {
                retryConnection();
            }
        });
    }

    public void disconnect() {
        try {
            retryLimit.set(0);
            socket.close();
            System.out.println("DISCONNECTED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearRetry() {
        retryLimit.set(3);
    }

    private void retryConnection() {
        try {
            System.err.println("Failed to connect to message broker. Remaining retry " + retryLimit.get() + "...");
            Thread.sleep(2000);
            if(retryLimit.getAndDecrement() != 0) {
                connect();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void sendMessage(String content) {
        if(socket.isConnected()) {
            try {
                Message message = new Message(content);
                outputStream.writeObject(message);
                outputStream.flush();
                System.out.println("Publish Client to Message Broker: " + message.getId() + " - " + message.getContent() + " - " + message.getTimestamp());
            } catch (Exception e) {
                System.err.println("Failed to send message!");
            }
        } else {
            // Local DB
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Main client = new Main("localhost", 1234);
        client.start();

        TimeUnit.SECONDS.sleep(2);

        client.sendMessage("{\"message_type\":0,\"command_type\":3002,\"date_time\":1701301149," +
                "\"is_blocking\":false}");

//        TimeUnit.SECONDS.sleep(15);
//        client.disconnect();
    }
}