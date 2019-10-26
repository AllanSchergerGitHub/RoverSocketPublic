package com.robocam.Socket;

public class MouseHandednessCommand extends Command {
    private static final String COMMAND_NAME = "mhnd";
    private String mHandedness = "left";

    public MouseHandednessCommand() {
        super(COMMAND_NAME);
    }

    public void setHandedness(String handedness) {
        mHandedness = handedness;
    }

    public String getHandedness() {
        return mHandedness;
    }

    @Override
    public String buildCommand() {
        return String.format(
                "%s%s%s%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mHandedness, PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mHandedness = arguments[0].trim();
        }
    }
}
