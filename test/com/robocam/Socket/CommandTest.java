package com.robocam.Socket;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sujoy
 */
public class CommandTest {
    
    @Test
    public void testBasicFeatures() {
        Command commandObject = new Command("TestCommand") {
            @Override
            public String buildCommand() { return null; }
            
            @Override
            public void parseCommand(String command) {}
        };
        
        assertTrue(commandObject.canServeCommand("TestCommand:"));
        assertFalse(commandObject.canServeCommand("TestCommand"));
        assertArrayEquals(
                new String[] {"p1=va", "p2=vb"}, 
                commandObject.getArguments("TestCommand:p1=va,p2=vb")
        );
    }
}
