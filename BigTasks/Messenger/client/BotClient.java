package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.*;

import java.io.Console;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BotClient extends Client {

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        String userName = "date_bot_"+(int) (Math.random()*100);
        return userName ;
    }

    public class BotSocketThread extends SocketThread {
        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (!message.contains(": ")) return;
             String [] mas =  message.split(":",2);
             String username = mas[0].trim();
             String text = mas[1].trim();
             SimpleDateFormat simpleDateFormat;
             Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
             switch (text){
                 case ("дата"):
                     simpleDateFormat = new SimpleDateFormat("d.MM.YYYY");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("день"):
                     simpleDateFormat = new SimpleDateFormat("d");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("месяц"):
                     simpleDateFormat = new SimpleDateFormat("MMMM");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("год"):
                     simpleDateFormat = new SimpleDateFormat("YYYY");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("время"):
                     simpleDateFormat = new SimpleDateFormat("H:mm:ss");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("час"):
                     simpleDateFormat = new SimpleDateFormat("H");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("минуты"):
                     simpleDateFormat = new SimpleDateFormat("m");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
                 case ("секунды"):
                     simpleDateFormat = new SimpleDateFormat("s");
                     sendTextMessage(String.format("Информация для %s: %s",username,simpleDateFormat.format(calendar.getTime())));
                     break;
             }




        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }
    }
    public static void main(String [] args){
        BotClient botClient = new BotClient();
        botClient.run();
    }



}
