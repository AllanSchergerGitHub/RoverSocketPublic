/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robocam.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sujoy
 */
public class SocketServer implements Runnable {
    private final ServerSocket mServerSocket;
    private final ExecutorService mExecutorService;
    private final ComPipe mComPipe;
    
    private final ArrayList<Socket> mConnectedSockets = new ArrayList<>();

    public SocketServer(int port, ComPipe comPipe) throws IOException {
        mServerSocket = new ServerSocket(port);
        System.out.println(String.format("%d", port));
        //At present only two thead is needed to
        //execute read from and write to UI socket
        //Additional 2 threads is for safe operation
        mExecutorService = Executors.newFixedThreadPool(5);
        mComPipe = comPipe;
    }
    
    public Socket[] getConnectedSockets() {
        return mConnectedSockets.toArray(new Socket[mConnectedSockets.size()]);
    }

    @Override
    public void run() {
        while(true) {
            Socket socket = null;
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(
                    "Client cannot be connected - Error", e);
            }
            try {
                socket.setSoTimeout(0);//no timeout, hearbeat will handle it
            } catch (SocketException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (socket != null) {
                mConnectedSockets.add(socket);
                System.out.println("New connection!" + socket.getRemoteSocketAddress().toString());
                mExecutorService.execute(new SocketReadHandler(socket, mComPipe));
                mExecutorService.execute(new SocketWriteHandler(socket, mComPipe));
            }
            
            // Remove closed sockets
            int i = 0;
            while (i < mConnectedSockets.size()) {
                if (mConnectedSockets.get(i).isClosed()) {
                    mConnectedSockets.remove(i);
                    i--;
                }
                i++;
            }
        }
    }
}
