/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robocam.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


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
                } catch (IOException ex) {
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
                System.out.println("Cloasing new socket from UI");
                spawned = false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                //Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }
}
