package com.robocam.Socket;

public class WheelSteeringAngleCommand extends Command {
    private static final String COMMAND_NAME = "wsa";
    private static final String PARAM_INDICES = "i";
    private static final String PARAM_MODE = "m";
    
    public static final String MODE_INCREMENTAL = "i";
    public static final String MODE_ABSOLUTE = "a";

    private String[] mWheelIndices = new String[]{};
    private String mMode = MODE_ABSOLUTE;
    
    public WheelSteeringAngleCommand() {
        super(COMMAND_NAME);
    }
    
    public void setWheelIndices(int ...indices) {
        mWheelIndices = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            mWheelIndices[i] = String.valueOf(indices[i]);
        }
    }
    
    public int[] getWheelIndices() {
        int[] indices = new int[mWheelIndices.length];
        for (int i = 0; i < mWheelIndices.length; i++) {
            indices[i] = Integer.parseInt(mWheelIndices[i]);
        }
        return indices;
    }
    
        
    public void setMode(String mode) {
        mMode = mode;
    }

    public String getMode() {
        return mMode;
    }

    @Override
    public String buildCommand() {
        if (mWheelIndices.length == 0) return "";
        return String.format(
                "%s%s%s=%s,%s=%s",
                mName, Command.COMMAND_ARG_SEPARATOR,
                PARAM_INDICES,
                String.join("+", mWheelIndices),
                PARAM_MODE,
                mMode);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        for (String argument : arguments) {
            if (argument.startsWith(PARAM_INDICES)) {
                mWheelIndices = argument.substring(
                        PARAM_INDICES.length()+1).split("\\+");
            } else if (argument.startsWith(PARAM_MODE)) {
                mMode = argument.substring(PARAM_MODE.length()+1);
            }
        }
    }
}
