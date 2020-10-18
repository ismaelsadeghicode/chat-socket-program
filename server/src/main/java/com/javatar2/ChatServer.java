package com.javatar2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<String>();
    private Set<UserThread> userThreads = new HashSet<UserThread>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try  {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
//            if (args.length < 1) {
//                System.out.println("Syntax: java com.javatar2.ChatServer <port-number>");
//                System.exit(0);
//            }
//        int port = Integer.parseInt(args[0]);
            Properties prop = new Properties();
            InputStream input = new FileInputStream(ClassLoader.getSystemResource("application.properties").getFile());
            prop.load(input);
            int port = Integer.parseInt(prop.getProperty("port"));
            ChatServer server = new ChatServer(port);
            server.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    void addUserName(String userName) {
        userNames.add(userName);
    }

    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

}
