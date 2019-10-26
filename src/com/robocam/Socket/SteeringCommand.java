package com.robocam.Socket;

/**
 *
 * @author sujoy
 */
public class SteeringCommand extends Command {
    private static final String COMMAND_NAME = "sm";
    private String mSteeringMode;

    public SteeringCommand() {
        super(COMMAND_NAME);
    }

    public void setSteeringMode(String mode) {
        mSteeringMode = mode;
    }

    public String getSteeringMode() {
        return mSteeringMode;
    }

    @Override
    public String buildCommand() {
        return String.format(
                "%s%s%s%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mSteeringMode, PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mSteeringMode = arguments[0].trim();
        }
    }
}
