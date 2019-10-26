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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sujoy
 */
public class SocketHandler implements Runnable {
    private final Socket mSocket;
    private PrintWriter mOutStream;
    private BufferedReader mInStream;
    private final ComPipe mComPipe;

    public SocketHandler(Socket socket, ComPipe comPipe) {
        this.mSocket = socket;
        mComPipe = comPipe;
    }

    @Override
    public void run() {
        try {
            mOutStream = new PrintWriter(mSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            mInStream = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            mOutStream.close();
            return;
        }

        while(true) {
            String line = null;
            try {
                if(mInStream.ready()) {
                    line = mInStream.readLine();
                }
            } catch (IOException ex) {}
            if (line != null) {
                mComPipe.putIn(line);
            }
            line = mComPipe.getOut();
            if (line != null) {
                System.out.println("getOut:" + line);
                mOutStream.println(line);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                break;
            }
            if (mComPipe.isReceivedDelayed()) {
                System.out.println("System reset");
                break;
            }
        }
        mOutStream.close();
        try {
            mInStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
