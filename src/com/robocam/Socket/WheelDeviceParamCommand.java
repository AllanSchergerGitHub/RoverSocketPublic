package com.robocam.Socket;

public class WheelDeviceParamCommand extends Command {
    private static final String COMMAND_NAME = "wdp";

    private static final String WHEEL_INDEX = "n";
    private static final String BLDC_MOTOR_POS = "bmp";
    private static final String BLDC_MOTOR_1_POS = "bm1p";
    private static final String BLDC_MOTOR_2_POS = "bm2p";
    private static final String BLDC_MOTOR_1_DUTY_CYCLE = "bm1d";
    private static final String BLDC_MOTOR_2_DUTY_CYCLE = "bm2d";

    private final int mWheelIndex;
    private double mBLDCMotorPosDepreciated;
    private double[] mBLDCMotorPosList = {0, 0};
    private final double[] mBLDCMotorDutyCycleList = {0, 0};
    
    private String mLastBuiltCommand;

    public WheelDeviceParamCommand(int wheelIndex ) {
        super(String.format("%s_%d", COMMAND_NAME, wheelIndex));
        mWheelIndex = wheelIndex;
    }

    public int getWheelIndex() {
        return mWheelIndex;
    }
    
    public double getBLDCMotorPosDepreciated() {
        System.err.println("THIS SHOULD BE DEPRECIATED - Aug 18 2019");
        return mBLDCMotorPosDepreciated;
    }
    
    public void setBLDCMotorPosDepreciated(double value) {
        mBLDCMotorPosDepreciated = value;
    }
    
    public void setBLDCMotorPos(int index,double value) {
        mBLDCMotorPosList[index] = value;
    }
    
    public double getBLDCMotorPos(int index) {
        return mBLDCMotorPosList[index];
    }

    public double getBLDCMotorDutyCycle(int index) {
        return mBLDCMotorDutyCycleList[index];
    }

    public void setBLDCMotorDutyCycle(int index, double value) {
        mBLDCMotorDutyCycleList[index] = value;
    }
    
    public String buildCommandIfChanged() {
        String command = buildCommand();
        if (mLastBuiltCommand != null && command.equals(mLastBuiltCommand)) {
            return null;
        }
        mLastBuiltCommand = command;
        return command;
    }

    @Override
    public String buildCommand() {
        /*For testing
        mBLDCMotorDutyCycleList[0] +=Math.random();
        mBLDCMotorDutyCycleList[1] += Math.random();
        mBLDCMotorPosList[0] += Math.random();
        mBLDCMotorPosList[1] += Math.random();
        */
        return String.format("%s%s%s=%.2f,%s=%.2f,%s=%.2f,%s=%.2f,%s=%.2f%s",
                mName, Command.COMMAND_ARG_SEPARATOR,
                BLDC_MOTOR_POS, mBLDCMotorPosDepreciated,
                BLDC_MOTOR_1_DUTY_CYCLE, mBLDCMotorDutyCycleList[0],
                BLDC_MOTOR_2_DUTY_CYCLE, mBLDCMotorDutyCycleList[1],
                BLDC_MOTOR_1_POS, mBLDCMotorPosList[0],
                BLDC_MOTOR_2_POS, mBLDCMotorPosList[1],
                PRIORITY_COMMAND_FLAG);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].startsWith(BLDC_MOTOR_POS)) {
                mBLDCMotorPosDepreciated = Double.parseDouble(
                        arguments[i].substring(BLDC_MOTOR_POS.length()+1));
            } else if (arguments[i].startsWith(BLDC_MOTOR_1_DUTY_CYCLE)) {
                mBLDCMotorDutyCycleList[0] = Double.parseDouble(
                        arguments[i].substring(BLDC_MOTOR_1_DUTY_CYCLE.length()+1));
            } else if (arguments[i].startsWith(BLDC_MOTOR_2_DUTY_CYCLE)) {
                mBLDCMotorDutyCycleList[1] = Double.parseDouble(
                        arguments[i].substring(BLDC_MOTOR_2_DUTY_CYCLE.length()+1));
            } else if (arguments[i].startsWith(BLDC_MOTOR_1_POS)) {
                mBLDCMotorPosList[0] = Double.parseDouble(
                        arguments[i].substring(BLDC_MOTOR_1_POS.length()+1));
            } else if (arguments[i].startsWith(BLDC_MOTOR_2_POS)) {
                mBLDCMotorPosList[1] = Double.parseDouble(
                        arguments[i].substring(BLDC_MOTOR_2_POS.length()+1));
            }
        }
    }
}
