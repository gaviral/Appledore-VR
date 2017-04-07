package com.vroneinc.vrone;

import android.util.Log;

public class CommandParser
{
    // The length of each command (for x and y)
    // Each command looks like this: x**** or y****, where each * is a number 0-9, ex. x0511
    public static final int COMMAND_LENGTH = 5;

    // values to look for in the command signifying different buttons
    public static final String COMMAND_STICK_BUTTON = "b";
    public static final String COMMAND_FORWARD = "F";
    public static final String COMMAND_RIGHT = "R";
    public static final String COMMAND_LEFT = "L";

    // Store empty strings and default values for x and y (so that the parser doesn't flip out).
    // The reason there are 9 is since we need 9 commands to be sure we'll get a single full command
    // (of length 5) in the worst case (when each command is one character long).
    private static String previousCommands[] =  new String[] {"", "", "", "", "", "", "", "y0511", "x0511"};

    // fields that contain the current x and y values read from bluetooth
    // (initialized to "center" values, since x and y range form 0 - 1023)
    private static int currentX = 511, currentY = 511;

    // fields that contain the values of buttons (true if pressed, false otherwise)
    private static boolean stickButtonPressed = false,
            forwardButtonPressed = false,
            leftButtonPressed = false,
            rightButtonPressed = false;


    // Use the stored previous bluetooth data and the newly received data to find valid commands
    public static void parseCommand(String command) {
        // check the new command for buttons
        if(command.contains(COMMAND_STICK_BUTTON)) {
            stickButtonPressed = true;
            command.replace(COMMAND_STICK_BUTTON, "");
        }
        if(command.contains(COMMAND_LEFT)) {
            leftButtonPressed = true;
            command.replace(COMMAND_LEFT, "");
        }
        if(command.contains(COMMAND_FORWARD)) {
            forwardButtonPressed = true;
            command.replace(COMMAND_FORWARD, "");
        }
        if(command.contains(COMMAND_RIGHT)) {
            rightButtonPressed = true;
            command.replace(COMMAND_RIGHT, "");
        }

        StringBuilder sb = new StringBuilder("");

        // concatenate all previous commands with the new one
        for(String prevCmd : previousCommands) {
            sb.append(prevCmd);
        }
        sb.append(command);

        // parse for x's from the back of the string
        int lastXIndex = sb.toString().lastIndexOf('x');
        String sub = sb.toString();
        while(lastXIndex != -1) {
            // make sure this command isn't cut off at the end of the string
            if(lastXIndex <= sub.length() - COMMAND_LENGTH) {
                try {
                    // the commands look like this: x****, where each * is a number, so grab the star part of it
                    currentX = Integer.parseInt(sub.substring(lastXIndex + 1, lastXIndex + COMMAND_LENGTH));
                    Log.d("BTCommand", "New X: " + currentX);
                    break;
                }
                catch(NumberFormatException e) {
                    // As I'm pretty sure this code works properly, this seems to only happen (and we've tested this quite a bit) when
                    // the commands received were not contiguous, meaning the bluetooth receiver missed a command.
                    // Nothing we can do, so just continue through the loop and look for an older value.
                    Log.i("BTCommand", "NumberFormatException when parsing bluetooth command");
                }
            }
            sub = sub.substring(0, lastXIndex);
            lastXIndex = sub.lastIndexOf('y');
        }

        // same as for x, but for y
        int lastYIndex = sb.toString().lastIndexOf('y');
        sub = sb.toString();
        while(lastYIndex != -1) {
            if(lastYIndex <= sub.length() - COMMAND_LENGTH) {
                try {
                    currentY = Integer.parseInt(sub.substring(lastYIndex + 1, lastYIndex + COMMAND_LENGTH));
                    Log.d("BTCommand", "New Y: " + currentY);
                    break;
                }
                catch(NumberFormatException e) {
                }
            }
            sub = sub.substring(0, lastYIndex);
            lastYIndex = sub.lastIndexOf('y');
        }

        // update the stored previous commands with the newly received one
        updatePreviousCommands(command);
    }

    private static void updatePreviousCommands(String command) {
        // shift the previous commands by 1 (the oldest gets kicked out)
        for(int i=0; i<8; i++) {
            previousCommands[i] = previousCommands[i+1];
        }
        // assign the latest command to the one passed in
        previousCommands[8] = command;
    }

    // gets the current stored x value from the analog stick
    public static int getCurrentX() {
        return currentX;
    }

    // gets the current stored y value from the analog stick
    public static int getCurrentY() {
        return currentY;
    }

    public static boolean getStickButton() {
        // If the stick button was pressed, return true once, and set it to false
        // This is since Unity will call this every frame, and so we only want it to get true once
        if(stickButtonPressed) {
            stickButtonPressed = false;
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean getForwardButton() {
        if(forwardButtonPressed) {
            forwardButtonPressed = false;
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean getLeftButton() {
        if(leftButtonPressed) {
            leftButtonPressed = false;
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean getRightButton() {
        if(rightButtonPressed) {
            rightButtonPressed = false;
            return true;
        }
        else {
            return false;
        }
    }
}
