package com.robocam.Socket;

import org.junit.Test;
import static org.junit.Assert.*;

public class WheelSteeringAngleCommandTest {
    
    @Test
    public void testBuildCommand() {
        WheelSteeringAngleCommand command = new WheelSteeringAngleCommand();
        command.setMode(WheelSteeringAngleCommand.MODE_ABSOLUTE);
        command.setWheelIndices(1, 2);
        assertEquals("wsa:i=1+2,m=a", command.buildCommand());
    }

    @Test
    public void testParseCommand() {
        WheelSteeringAngleCommand command = new WheelSteeringAngleCommand();
        command.parseCommand("wsa:i=3+4,m=i");
        assertArrayEquals(new int[] {3, 4}, command.getWheelIndices());
        assertEquals(
                WheelSteeringAngleCommand.MODE_INCREMENTAL, command.getMode());
    }
    
}
