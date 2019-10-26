package com.robocam.Socket;

public class ConfigDBParamCommand extends Command {
    private static final String COMMAND_NAME = "dbpm";

    private String mName;
    private String mValue;
    private String mValueType;

    public ConfigDBParamCommand() {
        super(COMMAND_NAME);
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    public String getValueType() {
        return mValueType;
    }

    @Override
    public String buildCommand() {
        return String.format(
                "%s%s%s,%s,%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mName, mValue, mValueType);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 2) {
            mName = arguments[0];
            mValue = arguments[1];
            mValueType = arguments[2];
        };
    }
}
