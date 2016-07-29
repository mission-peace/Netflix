package com.netflix.command;

public class QuitCommand implements Command {

    @Override
    public void execute() {
        System.exit(0);
    }

    @Override
    public void printCommmand() {
        System.out.println("quit");
    }
}
