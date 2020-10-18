package com.javatar2;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }


    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            InputStream input = new FileInputStream(ClassLoader.getSystemResource("application-client.properties").getFile());
            prop.load(input);
            // use program arguments
//            if (args.length < 2) return;
//            String hostname = args[0];
//            int port = Integer.parseInt(args[1]);

            String hostname =prop.getProperty("hostname");
            int port = Integer.parseInt(prop.getProperty("port"));

            ChatClient client = new ChatClient(hostname, port);
            client.execute();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
