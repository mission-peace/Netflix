package com.netflix.command;

public class NoOpCommand implements Command {
    @Override
    public void execute() {}

    @Override
    public void printCommmand() {}
}
