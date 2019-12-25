package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String, Connection> map : connectionMap.entrySet()
        ) {
            try {
                map.getValue().send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage(String.format("Сообщение для %S не было доставлено.", map.getKey()));
            }
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ConsoleHelper.writeMessage("Установлено соединение с сервером" + socket.getRemoteSocketAddress());
                Connection connection = new Connection(socket);
                String userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED,userName));
                notifyUsers(connection,userName);
                serverMainLoop(connection,userName);
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED,userName));



            } catch (IOException e) {
                ConsoleHelper.writeMessage("Произошла ошибка: " + e);
            } catch (ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка: " + e);
            }


        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            Message mes = null;
            String str = null;
            do {
                connection.send(new Message(MessageType.NAME_REQUEST));
                mes = connection.receive();
                str = mes.getData();
            } while (!(mes.getType() == MessageType.USER_NAME) || str.isEmpty() || connectionMap.containsKey(str));
            connectionMap.put(str, connection);
            connection.send(new Message(MessageType.NAME_ACCEPTED));
            return str;
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
                String name = entry.getKey();
                /*Connection conn = entry.getValue();*/
                if (!name.equals(userName)) {
                    connection.send(new Message(MessageType.USER_ADDED, name));
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    String text = String.format("%s: %s", userName, message.getData());
                    sendBroadcastMessage(new Message(MessageType.TEXT, text));
                } else
                    ConsoleHelper.writeMessage("Сообщения не является текстом");
            }
        }


        public static void main(String[] args) {

            int socket = ConsoleHelper.readInt();
            try (ServerSocket serverSocket = new ServerSocket(socket)) {
                System.out.println("Server started");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Handler(clientSocket).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}
