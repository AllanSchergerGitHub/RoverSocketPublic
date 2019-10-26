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

    public String getOut() {
        if (!mIsOpen) {
            //System.out.println("null issue");
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

    public void putOut(String message) {
        if (!mIsOpen || message == null) return;        
        mOutput.offer(message);
    }

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
