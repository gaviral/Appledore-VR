package com.vroneinc.vrone;

import android.util.Log;

public class My_Plugin
{
    public static final String COMMAND_FORWARD = "F";
    public static final String COMMAND_BACKWARD = "B";
    public static final String COMMAND_RIGHT = "R";
    public static final String COMMAND_LEFT = "L";

    int counter = 0;
    static String message = "Not changed";
    volatile private static char currentBTCommand = 'N';

    public static void parseCommand(String command) {
        if(command.equals(COMMAND_FORWARD)) {
            currentBTCommand = 'F';
        }
        else if(command.equals(COMMAND_BACKWARD)) {
            currentBTCommand = 'B';
        }
        else if(command.equals(COMMAND_LEFT)) {
            currentBTCommand = 'L';
        }
        else if(command.equals(COMMAND_RIGHT)) {
            currentBTCommand = 'R';
        }
        else {
            currentBTCommand = 'X';
        }
    }

    public static char getBTCommand() {
        Log.d("BTCommand","Command is: " + currentBTCommand);
        return currentBTCommand;
    }

    public static void setBTCommand(char command) {
        currentBTCommand = command;
    }

    public static String getMessage() {
        return message;
    }

}
