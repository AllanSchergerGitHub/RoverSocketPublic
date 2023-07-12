/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robocam.Socket;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The UI is the client; the Rover computer is the server.
 * This code is only used on the UI and not on the Rover computer.
 * @author allan
 */
public class SocketClient implements Runnable {
    private String mHost;
    private int mPort;
    private Socket mSocket;

    private final ComPipe mComPipe;
    private final ExecutorService mExecutorService = Executors.newFixedThreadPool(2);

    public SocketClient(String host, int port, ComPipe comPipe) {
        mHost = host;
        mPort= port;
        mComPipe = comPipe;
    }

    @Override
    public void run() {
        boolean spawned = false;
        while(true) {
            if (mSocket == null) {
                try {
                    mSocket = new Socket(mHost, mPort);
                    
                } 
                catch (java.net.ConnectException ce) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    System.err.println("UI tried to connect to Rover at " + mHost + " " + mPort + "but refused:  "+ce+" "+timestamp);
                } 
                catch (IOException ex) {
                    // Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (mSocket != null && !spawned) {
                System.out.println("Spawning new socket from UI");
                mComPipe.start();
                mExecutorService.execute(new SocketReadHandler(mSocket, mComPipe));
                mExecutorService.execute(new SocketWriteHandler(mSocket, mComPipe));
                spawned = true;
            }
            if (mSocket != null && mSocket.isClosed()){
                mSocket = null;
                mComPipe.stop();
                System.out.println("Closing new socket from UI");
                spawned = false;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                //Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }
}
