package com.robocam.Socket;

import java.awt.Point;

public class MousePosCommand extends Command {
    private static final String COMMAND_NAME = "ms";
    private final Point.Double mPos= new Point.Double(0, 0);

    public MousePosCommand() {
        super(COMMAND_NAME);
    }

    public void setPos(Point.Double pos) {
        mPos.setLocation(pos);
    }

    public void setPos(Double xPos, Double yPos) {
        mPos.setLocation(xPos, yPos);
    }

    public Point.Double getPos() {
        Point.Double pos = new Point.Double();
        pos.setLocation(mPos);
        return pos;
    }

    @Override
    public String buildCommand() {
        return String.format(
                "%s%s%.5f,%.5f",
                COMMAND_NAME, Command.COMMAND_ARG_SEPARATOR,
                mPos.x, mPos.y);
    }

    @Override
    public void parseCommand(String command) {
        String[] arguments = getArguments(command);
        if (arguments.length > 1) {
            mPos.x = Double.parseDouble(arguments[0]);
            mPos.y = Double.parseDouble(arguments[1]);
        }
    }
}
