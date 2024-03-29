package com.robocam.Socket;

public class WheelSteeringAngleCommand extends Command {
    private static final String COMMAND_NAME = "wsa";
    
    private static final String PARAM_INDICES = "i";
    private static final String PARAM_MODE = "m";
    private static final String PARAM_ANGLE = "a";
    
    public static final String MODE_INCREMENTAL = "i";
    public static final String MODE_ABSOLUTE = "a";
    private String mMode = MODE_ABSOLUTE;
    private String[] mWheelIndices = new String[]{};

    private double mAngle = 0;
    
    public WheelSteeringAngleCommand() {
        super(COMMAND_NAME);
    }
    
    public int[] getWheelIndices() {
        int[] indices = new int[mWheelIndices.length];
        for (int i = 0; i < mWheelIndices.length; i++) {
            indices[i] = Integer.parseInt(mWheelIndices[i]);
        }
        return indices;
    }

    public String getMode() {
        return mMode;
    }

    public Double getAngle() {
        return mAngle;
    }
    
    @Override
    public String buildCommand() {
        if (mWheelIndices.length == 0) return "";
        return String.format(
                "%s%s%s=%s,%s=%s,%s=%.2f",
                mName, Command.COMMAND_ARG_SEPARATOR,
                PARAM_INDICES,
                String.join("+", mWheelIndices),
                PARAM_MODE,
                mMode,
                PARAM_ANGLE,
                mAngle);
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
            } else if (argument.startsWith(PARAM_ANGLE)) {
                mAngle = Double.parseDouble(
                        argument.substring(PARAM_MODE.length()+1));
            }
        }
    }
}
