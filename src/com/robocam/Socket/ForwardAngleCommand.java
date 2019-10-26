package com.robocam.Socket;

import com.robocam.Socket.Command;

/**
 *
 * @author sujoy
 */
public class ForwardAngleCommand extends Command{
    private static final String COMMAND_NAME = "fa";
    private double mForwardAngle;
    private double mForwardAngleMultiplier;

    public ForwardAngleCommand() {
        super(COMMAND_NAME);
    }

    public void setForwardAngle(double angle) {
        mForwardAngle = angle;
    }

    public double getForwardAngle() {
        return mForwardAngle;
    }
    
    public void setForwardAngleMultiplier(double angleMultiplier) {
        mForwardAngleMultiplier = angleMultiplier;
    }

    public double getForwardAngleMultiplier() {
        return mForwardAngleMultiplier;
    }

    @Override
    public String buildCommand() {
        return String.format(
                "%s%s%s,%s%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mForwardAngle, mForwardAngleMultiplier, PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mForwardAngle = Double.parseDouble(arguments[0]);
        }
        if (arguments.length > 1) {
            mForwardAngleMultiplier = Double.parseDouble(arguments[1]);
        }
    }
}
