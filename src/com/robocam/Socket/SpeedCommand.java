package com.robocam.Socket;

public class SpeedCommand extends Command {
    private static final String COMMAND_NAME = "spd";
    private double mSpeed = 1;
    private double mVelocityLimit = 100;
    private double mVelocityLimitIncrement = 5;
    
    public SpeedCommand(){
        super(COMMAND_NAME);
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public double getSpeed() {
        return mSpeed;
    }
    
    public void setVelocityLimit(double velocityLimit) {
        mVelocityLimit = velocityLimit;
    }

    public double getVelocityLimit() {
        return mVelocityLimit;
    }
    
    public void setVelocityLimitIncrement(double velocityIcrement) {
        mVelocityLimitIncrement = velocityIcrement;
    }

    public double getVelocityLimitIncrement() {
        return mVelocityLimitIncrement;
    }

    @Override
    public String buildCommand() {
        return String.format("%s%s%.5f,%.5f,%.5f%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mSpeed, mVelocityLimit, mVelocityLimitIncrement,
                PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mSpeed = Double.parseDouble(arguments[0]);
        }
        if (arguments.length > 1) {
            mVelocityLimit = Double.parseDouble(arguments[1]);
        }
        if (arguments.length > 2) {
            mVelocityLimitIncrement = Double.parseDouble(arguments[2]);
        }
    }
}
