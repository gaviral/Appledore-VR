package com.vroneinc.vrone;


public class My_Plugin
{
    public static final String COMMAND_FORWARD = "F";
    public static final String COMMAND_BACKWARD = "B";
    public static final String COMMAND_RIGHT = "R";
    public static final String COMMAND_LEFT = "L";

    int counter = 0;
    static String message = "Not changed";
    private static char currentBTCommand = 0;

    public My_Plugin() {
        Thread t = new Thread(new TestThread());
        t.start();
    }

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
        return currentBTCommand;
    }

    public static void setBTCommand(char command) {
        currentBTCommand = command;
    }

    public static String getMessage() {
        return message;
    }

    public int getNextNumber() {
        counter = ++counter % 300;
        return counter;
    }

    public class TestThread implements Runnable {

        @Override
        public void run() {
            try {
                // sleep for 5 seconds, then change the value of the counter
                Thread.sleep(2000);
                message = "Changed";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
