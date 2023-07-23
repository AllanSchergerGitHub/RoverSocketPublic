package com.robocam.Socket;

public class CommonSensorCommand extends Command {
    private static final String COMMAND_NAME = "cs";
    private double mElectricalCurrent;
    private double mDistanceRemainingRover = 0.0;

    public CommonSensorCommand() {
        super(COMMAND_NAME);
    }

    public double getElectricalCurrent() {
        return mElectricalCurrent;
    }
    
    public void setElectricalCurrent(double value) {
        mElectricalCurrent = value;
    }
    
    public double getDistanceRemainingRover() {
        return mDistanceRemainingRover;
    }
    
    public void setDistanceRemainingRover(double value) {
        mDistanceRemainingRover = value;
    }
    
    @Override
    public String buildCommand() {
        return String.format("%s%s%f%s%f%s",
            COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
            mElectricalCurrent, Command.COMMAND_ARG_SEPARATOR,
            mDistanceRemainingRover,
            PRIORITY_COMMAND_FLAG);
    }

    @Override
    /**
     * An example command looks like this: cs:-0.645920:120.750000!
     * In this example the electricalcurrent is -0.645 and the distanceremaining is 120.75
     * This data is used on the UI side.
     */
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            String[] numbers = arguments[0].trim().split(":");
            if (numbers.length == 2) {
                mElectricalCurrent = Double.parseDouble(numbers[0]);
                mDistanceRemainingRover = Double.parseDouble(numbers[1]);
            }
        }
    }
}
