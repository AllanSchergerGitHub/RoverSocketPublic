package com.robocam.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketReadHandler implements Runnable {
    private final Socket mSocket;
    private final ComPipe mComPipe;
    private BufferedReader mInStream;
    private final ExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();

    public SocketReadHandler(Socket socket, ComPipe comPipe) {
        this.mSocket = socket;
        mComPipe = comPipe;
    }

    @Override
    public void run() {
        try {
            mInStream = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException ex) {
            //Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        Callable<String> readingTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (!mInStream.ready()) return null;
                return mInStream.readLine();
            }
        };
        //System.out.println("reading from socket");
        while(true) {
            String line = null;
            /*try {
                if (mInStream.ready()) {
                    line = mInStream.readLine();
                }
            } catch (IOException ex) {
                //Logger.getLogger(SocketReadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            Future<String> submittedTask = mExecutor.submit(readingTask);
            try {
                line = submittedTask.get(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                //Logger.getLogger(SocketReadHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                //Logger.getLogger(SocketReadHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                //Logger.getLogger(SocketReadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (line != null) {
                //System.out.println("SocketReadHandler: " + line);
                if (line.equals(ComPipe.CLOSE_COMMAND)) {
                    //System.out.println("Got closing signal");
                    try {
                        mSocket.close();
                    } catch (IOException ex) {
                        //Logger.getLogger(SocketReadHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                mComPipe.putIn(line);
            }
            if (mComPipe.isReceivedDelayed()) {
                //System.out.println("System reset; Breaking Socket read handler");
                try {
                    mSocket.close();
                } catch (IOException ex) {
                    //Logger.getLogger(SocketReadHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        try {
            mInStream.close();
        } catch (IOException ex) {
            //Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
