package com.netflix.command;

/**
 * Directives from command line
 */
public interface Command {
    void execute();
    void printCommmand();
}
