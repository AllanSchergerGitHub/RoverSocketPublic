package com.robocam.Socket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ComPipe {
    public static final String HEART_BEAT = "hb";
    public static final String CLOSE_COMMAND = "cc";
    private static final int HEART_BEAT_INTERVAL = 1000; //millisecond

    private final ConcurrentLinkedQueue<String> mInput;
    private final ConcurrentLinkedQueue<String> mOutput;

    private long mLastMessageReceivedAt = 0;
    private long mLastMessageSentAt = 0;

    private long mLagTime  = 1;
    private long newtimestamp = System.currentTimeMillis();
    private long oldtimestamp = System.currentTimeMillis();
    
    private boolean mIsOpen  = true;
    
    private static final int MAX_QUEUE_SIZE = 1000;
    
    public ComPipe() {
        mInput = new ConcurrentLinkedQueue<>();
        mOutput = new ConcurrentLinkedQueue<>();
    }
    
    public void start() {
        mLastMessageReceivedAt = 0;
        mLastMessageSentAt = 0;
        mIsOpen = true;
        mInput.clear();
        mOutput.clear();
    }
    
    public void stop() {
        mIsOpen = false;
        mInput.clear();
        mOutput.clear();
    }
    
    public void sendCloseMessage() {
        putOut(CLOSE_COMMAND);
    }

    public String getIn() {
        return mInput.poll();
    }

    /**
     * The getOut() method is used to retrieve a message from the mOutput queue. 
     * If the queue is empty and a certain amount of time has passed since the 
     * last message was sent, a heartbeat message is returned instead.
     * The getOut() method also updates the mLastMessageSentAt timestamp 
     * each time a message is retrieved.
     * The actual sending of the message is likely handled by the part of your 
     * application that calls getOut(). This could be a separate thread or a 
     * network-related class that's responsible for sending messages over a network connection.
     * It appears like it could be either the SocketHandler or SocketWriteHandler classes.
     * @return 
     */
    public String getOut() {
        if (!mIsOpen) {
            System.out.println("null issue? ComPipe isn't open?");
            return null;
        }
        String message = mOutput.poll();
        if(message == null &&
                (System.currentTimeMillis() - mLastMessageSentAt) > HEART_BEAT_INTERVAL) {
            message = HEART_BEAT;
        }
        if (message != null) {
            //if (message != HEART_BEAT) System.out.println("getOut message: " + message);
            mLastMessageSentAt = System.currentTimeMillis();
        }
        return message;
    }

    /**
     * The putOut method is used to add a message to the mOutput queue. 
     * This queue is used to store messages that are waiting to be sent.
     * the offer() method in Java's Queue interface is used to insert a specific 
     * element into the queue. This method is generally preferable to add(), 
     * because offer() will not throw an exception if the queue is full or 
     * capacity-restricted. Instead, it will return false.
     * In the context of your code, mOutput.offer(message) is adding the message 
     * to the mOutput queue. If the queue is full and cannot accept any more elements, 
     * this operation will fail silently (i.e., it will not add the element, but 
     * it also will not throw an exception).
     * @param message 
     */
    public void putOut(String message) {
        if (!mIsOpen || message == null) return;        
        mOutput.offer(message);
    }

    /**
     * The putIn method is used to add a message to the mInput queue. 
     * This queue is used to store messages that have been received and are waiting to be processed.
     * @param message 
     */
    public void putIn(String message) {
        if(!message.equals(HEART_BEAT)) {
            //System.out.println("putIn message" + message);
        } else {
            //System.out.println("Got heartbeat message");
        }
        //System.out.println("putIn message" + message);
        mLastMessageReceivedAt = System.currentTimeMillis();
        if(!message.equals(HEART_BEAT)) {
            if (mInput.size() > MAX_QUEUE_SIZE &&
                !message.endsWith(Command.PRIORITY_COMMAND_FLAG)) {
                return;
            } else {
                //System.out.println("putIn message2 " + message);
            }
            if(mInput.size()>100){
                System.out.println("MAX_QUEUE_SIZE has exceeded 100. This is in ComPipe.java in the RoverSocketLib code: " + mInput.size() + " most recent message: " + message);
            }
            
            mInput.offer(message);
        }
    }

    public long getLagTime() {
        return mLagTime;
    }
    
    public boolean isReceivedDelayed() {
        //return false;
        ///*
        if (mLastMessageReceivedAt == 0) {
            mLastMessageReceivedAt = System.currentTimeMillis();
            return false;
        }
        newtimestamp = System.currentTimeMillis() ;
        mLagTime = newtimestamp-oldtimestamp;
        oldtimestamp = newtimestamp ;
        return ((newtimestamp - mLastMessageReceivedAt) > 30*HEART_BEAT_INTERVAL);///*/
    }
    
    /**
+     * Returns the milliseconds from last heartbeat received.
+    */
    public long getLastReceviedMessageAge() {
        return (System.currentTimeMillis() - mLastMessageReceivedAt);
    }
}
