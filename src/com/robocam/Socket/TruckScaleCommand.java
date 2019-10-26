package com.robocam.Socket;


public class TruckScaleCommand extends Command {
    private static final String COMMAND_NAME = "ts";
    private double mTruckScale;

    public TruckScaleCommand() {
        super(COMMAND_NAME);
    }

    public void setTruckScale(double value) {
        mTruckScale = value;
    }

    public double getTruckScale() {
        return mTruckScale;
    }

    @Override
    public String buildCommand() {
        return String.format(
                "%s%s%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mTruckScale);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mTruckScale = Double.parseDouble(arguments[0]);
        }
    }
}
