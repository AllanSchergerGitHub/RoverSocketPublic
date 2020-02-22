package com.robocam.Socket;

import org.junit.Test;
import static org.junit.Assert.*;

public class FloatSubCommandTest {
    
    @Test
    public void testBuildCommand() {
        FloatSubCommand command = new FloatSubCommand();
        command.setSubCommand(FloatSubCommand.SPEED_FASTER);
        command.setValue(.5);
        assertEquals("flt:h,0.50000!", command.buildCommand());
    }

    @Test
    public void testParseCommand() {
        FloatSubCommand command = new FloatSubCommand();
        command.parseCommand("flt:a,.6!");
        assertEquals(
                FloatSubCommand.STEER_STOP, command.getSubCommand());
        assertEquals(0.6f, command.getValue(), 0.01);
    }
}
