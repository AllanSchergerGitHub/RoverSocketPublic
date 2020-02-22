package com.robocam.Socket;

public class FloatSubCommand extends Command {
    private static final String COMMAND_NAME = "flt";
    
    public static final Character SUB_COMMAND_NONE = 'n';
    public static final Character STEER_STOP = 'a';
    public static final Character STEER_FRONT = 'b';
    public static final Character ROTATE_LEFT = 'c';
    public static final Character ROTATE_STRAIGHT = 'd';
    public static final Character ROTATE_RIGHT = 'e';
    public static final Character ROTATE_FORWARD = 'f';
    public static final Character ROTATE_BACKWARD = 'g';
    public static final Character SPEED_FASTER = 'h';
    public static final Character SPEED_SLOWER = 'i';
    public static final Character VEHICLE_POINTER_HALT = 'j';
    public static final Character VEHICLE_POINTER_TRACK = 'k';
    public static final Character STEER_STRAIGHT= 'l';
    public static final Character SPEED_FASTER_TRACK = 'm';

    private Character mSubCommand; 
    private Double mValue = null;

    public FloatSubCommand(){
        super(COMMAND_NAME);
        this.mSubCommand = SUB_COMMAND_NONE;
    }
    
    public void setSubCommand(Character subCommand) {
        mSubCommand = subCommand;
    }
    
    public boolean setValue(double value) {
        if (mValue != null && mValue == value) {
            return false;
        }
        mValue = value;
        return true;
    }

    public Character getSubCommand() {
        return mSubCommand;
    }
    
    public double getValue() {
        return mValue;
    }
    
    @Override
    public String buildCommand() {
        return String.format("%s%s%c,%.5f%s",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mSubCommand, mValue,
                PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 0) {
            mSubCommand = arguments[0].charAt(0);
        }
        if (arguments.length > 1) {
            mValue = Double.parseDouble(arguments[1]);
        }
    }
    
}
