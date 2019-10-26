package com.robocam.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketWriteHandler implements Runnable {
    private final Socket mSocket;
    private PrintWriter mOutStream;
    private final ComPipe mComPipe;
    
    public SocketWriteHandler(Socket socket, ComPipe comPipe) {
        mSocket = socket;
        mComPipe = comPipe;
    }
    
    @Override
    public void run() {
        //System.out.println("writing to socket");
        try {
            mOutStream = new PrintWriter(mSocket.getOutputStream(), true);
        } catch (IOException ex) {
            //Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        while(true) {
            String line = mComPipe.getOut();
            if (line != null) {
                //System.out.println("SocketWriteHandler:" + line);
                mOutStream.println(line);
                mOutStream.flush();
            } else {
                //System.out.println("SocketWriteHandler: blank====");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                break;
            }
            if (mSocket.isClosed()) {
                //System.out.println("System reset; Breaking Socket write handler");
                break;
            }
        }
        mOutStream.close();
    }
    
}
