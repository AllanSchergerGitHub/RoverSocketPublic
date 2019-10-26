package com.robocam.Socket;


public class DirectionCommand extends Command {
    private static final String COMMAND_NAME = "dir"; // dir = direction
    private String mDir = "str";

    public DirectionCommand(){
        super(COMMAND_NAME);
    }

    public void setDirectionMode(String mode) {
        mDir = mode;
    }

    public String getDirectionMode() {
        return mDir;
    }

    @Override
    public String buildCommand() {
        System.out.println(" mDir "+mDir);
        return String.format(
                "%s%s%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mDir);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mDir = arguments[0].trim();
        }
    }
}
