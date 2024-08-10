# 🖥️ Client Module

Welcome to the **Client** module of the Custom Message Broker! This module demonstrates how to connect to the message broker, send messages, and receive responses.
<br><br>

## 🚀 Getting Started

Follow these steps to get your client up and running:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/fadimanakilci/custom-message-broker-client.git
   cd custom-message-broker-client
   ```

2. **Build the Project**

   Ensure you have Maven installed to build the project:
   ```bash
   mvn clean install
   ```
   
3. **Run the Client**

   Start the client application with:
    ```bash
   mvn exec:java -Dexec.mainClass="com.sparksign.messagebrokerclient.Main"
   ```
<br><br>

## 📡 Key Features

- **Two-Way Communication:** Efficiently send and receive messages to and from the server.
- **Real-Time Interaction:** Engage in live message exchanges with the server.
- **Custom Configuration:** Easily adapt settings to fit your specific needs.
<br><br>

## 💻 Example Usage

Here's a basic Java example for sending and receiving messages:
```java
client.sendMessage("{\"message_type\":0,\"command_type\":3002,\"date_time\":1701301149," +
            "\"is_blocking\":false}");
```
<br><br>

## 📜 Configuration

Adjust your MessageBrokerClient settings in `main`:

- **Host:** The address of the message broker server.
- **Port:** The port number the server is listening on (default: `1234`).

<br><br>

## 🔄 Example Interaction

1. **Start the Server:** Ensure the server is running as per the server module `README`.
2. **Run the Client:** Use the commands above to send messages and receive responses.




