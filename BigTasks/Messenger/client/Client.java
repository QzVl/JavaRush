package com.javarush.task.task30.task3008.client;

import com.javarush.task.task24.task2411.*;
import com.javarush.task.task30.task3008.*;

import java.io.Console;
import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    public void run(){
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Возникла ошибка"+e);
                return;
            }
            notify();
        }
        if (clientConnected) ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
        else ConsoleHelper.writeMessage ("Произошла ошибка во время работы клиента.");

        while (clientConnected) {
            String text = ConsoleHelper.readString();
            if (text.equals("exit")) break;
            if (shouldSendTextFromConsole()) sendTextMessage(text);
        }

    }


    protected String getServerAddress(){
        ConsoleHelper.writeMessage("Введите адрес сервера: ");
        String address = ConsoleHelper.readString();
        return address;

    }

    protected int getServerPort(){
        ConsoleHelper.writeMessage("Введите порт сервера: ");
        return ConsoleHelper.readInt();
    }

    protected String getUserName(){
        ConsoleHelper.writeMessage("Введите имя пользователя: ");
        return ConsoleHelper.readString();
    }
    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    protected void sendTextMessage(String text){
        try {
            connection.send(new Message(MessageType.TEXT,text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Сообщения не отправлено, произошла ошибка"+e);
            clientConnected = false;
        }
    }



    protected boolean shouldSendTextFromConsole(){
        return true;
    }



    public class SocketThread extends Thread{

        protected void processIncomingMessage(String message){
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage(userName+" присоеденился к чату.");

        }

        protected void informAboutDeletingNewUser(String userName){
            ConsoleHelper.writeMessage(userName+" покинул чат.");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            Client.this.clientConnected  = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }


        protected void clientHandshake() throws IOException, ClassNotFoundException{
            while (true){
                Message message = connection.receive();
                 if (message.getType() == MessageType.NAME_REQUEST){
                    connection.send(new Message(MessageType.USER_NAME,getUserName()));
                }else if (message.getType() == MessageType.NAME_ACCEPTED){ notifyConnectionStatusChanged(true);
                     return;
                }
                 else  throw new IOException("Unexpected MessageType");

            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException{
           while (true){
               Message message = connection.receive();

               if(message.getType() == (MessageType.TEXT)) processIncomingMessage(message.getData());
               else if (message.getType() == (MessageType.USER_ADDED)) informAboutAddingNewUser(message.getData());
               else if (message.getType() == (MessageType.USER_REMOVED)) informAboutDeletingNewUser(message.getData());
               else{
                   throw new IOException("Unexpected MessageType");
           }

           }

        }

        @Override
        public void run() {
            try {
                String address = getServerAddress();
                int port = getServerPort();
                Socket socket = new Socket(address,port);
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
            } catch (ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }


        }
    }

    public static void main(String[] args){
        Client client = new Client();
        client.run();
    }

}
