package com.robocam.Socket;

public abstract class Command {
    public static final String COMMAND_ARG_SEPARATOR = ":";
    public static final String PRIORITY_COMMAND_FLAG = "!";
    protected String mName;

    public Command(String name) {
        mName = name;
    }

    public boolean canServeCommand(String command) {
        return command.startsWith(mName + COMMAND_ARG_SEPARATOR);
    }

    public String getRestOfCommand(String command) {
        return command.substring(mName.length() + COMMAND_ARG_SEPARATOR.length());
    }

    public String[] getArguments(String command) {
        String argumentsString = getRestOfCommand(command);
        if (argumentsString.endsWith(PRIORITY_COMMAND_FLAG)) {
            argumentsString = argumentsString.substring(
                    0, argumentsString.length()-PRIORITY_COMMAND_FLAG.length());
        }
        String[] argumentsArray = argumentsString.split(",");
        return argumentsArray;
    }

    public abstract String buildCommand();
    public abstract void parseCommand(String command);
}
