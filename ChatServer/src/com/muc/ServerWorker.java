package com.muc;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class ServerWorker extends Thread{

    private final Socket clientSocket;
    private String login = null;
    private final Server server;
    private  OutputStream outputStream;
    private HashSet<String> topicSet = new HashSet<>();

    public ServerWorker(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();        // input stream relate to client socket --- client to server
        this.outputStream = clientSocket.getOutputStream();     // output stream related to client socket --- server to client

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while( (line = reader.readLine()) != null ) {       // when client enters ctrl+c it returns NULL
            String[] tokens = StringUtils.split(line);   // splits based on spaces

            if(tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if("quit".equalsIgnoreCase(cmd) || "logoff".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                } else {
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        if(line == null) {
            handleLogoff();
        }
    }

    private void handleLeave(String[] tokens) {
        if(tokens.length > 1) {
            String topic = tokens[1];
            topicSet.remove(topic);
        }
    }

    public boolean isMemberOfTopic(String topic) {
        return topicSet.contains(topic);
    }

    private void handleJoin(String[] tokens) {
        if(tokens.length > 1) {
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    // format: "msg" "login" body...
    // format: "msg" "#topic" body...
    private void handleMessage(String[] tokens) throws IOException {
        String sendTo = tokens[1];
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';

        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker: workerList) {
            if(isTopic) {
                if(worker.isMemberOfTopic(sendTo)) {
                    String outMsg = "msg " + sendTo + ":" + login + " " + body;
                    worker.send(outMsg);
                }
            }
            else {
                if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            }
        }
    }

    private void handleLogoff() throws IOException {
        server.removeWorker(this);
        System.out.println("User logged off successfully : " + login);
        List<ServerWorker> workerList = server.getWorkerList();

        // send other users current user's status
        String onlineMsg = "offline " + login + "\n";
        for(ServerWorker worker : workerList) {
            if(!login.equals(worker.getLogin()))
                worker.send(onlineMsg);
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if(tokens.length == 2) {
            String login = tokens[1];

            if(!(login.equals(""))) {
                String msg = "ok login\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("User logged in successfully : " + login);

                List<ServerWorker> workerList = server.getWorkerList();

                // send current user all other online logins
                for(ServerWorker worker : workerList) {
                    if(worker.getLogin() == null)
                        continue;
                    String msg2 = "online " + worker.getLogin() + "\n";
                    if(!login.equals(worker.getLogin()))
                        outputStream.write(msg2.getBytes());
                }

                // send other users current user's status
                String onlineMsg = "online " + login + "\n";
                for(ServerWorker worker : workerList) {
                    if(!login.equals(worker.getLogin()))
                        worker.send(onlineMsg);
                }
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    private void send(String msg) throws IOException {
        if(login != null)
            outputStream.write(msg.getBytes());
    }
}
