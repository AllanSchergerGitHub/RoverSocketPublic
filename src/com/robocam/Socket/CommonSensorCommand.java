package com.robocam.Socket;

public class CommonSensorCommand extends Command {
    private static final String COMMAND_NAME = "cs";
    private double mElectricalCurrent;
    
    public CommonSensorCommand() {
        super(COMMAND_NAME);
    }

    public double getElectricalCurrent() {
        return mElectricalCurrent;
    }
    
    public void setElectricalCurrent(double value) {
        mElectricalCurrent = value;
    }
    
    @Override
    public String buildCommand() {
        return String.format(
            "%s%s%f%s",
            COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
            mElectricalCurrent,
            PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mElectricalCurrent = Double.parseDouble(arguments[0].trim());
        }
    }
}
