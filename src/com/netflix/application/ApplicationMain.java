package com.netflix.application;

import com.netflix.command.Command;
import com.netflix.command.NoOpCommand;
import com.netflix.command.ProcessFileCommand;
import com.netflix.command.QueryCommand;
import com.netflix.command.QuitCommand;
import com.netflix.title.TitleInfo;
import com.netflix.trie.PrefixMatcher;
import com.netflix.trie.TrieBasedPrefixMatcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main application start point.
 */
public class ApplicationMain {

    private static final Integer THREAD_POOL_SIZE = 5;
    private final Executor executor;
    private final ConcurrentHashMap<Long, TitleInfo> idToTitleMap;
    private final ConcurrentHashMap<String, Boolean> uniqueTitles;
    private final PrefixMatcher prefixMatcher;
    private final AtomicLong titleIdCounter = new AtomicLong(0);

    ApplicationMain() {
        executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        idToTitleMap = new ConcurrentHashMap<>();
        uniqueTitles = new ConcurrentHashMap<>();
        prefixMatcher = new TrieBasedPrefixMatcher();
    }

    public void start() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String arg = br.readLine();
            if (arg == null) {
                System.err.println("Quit command not received but end of stdin reached. Exiting");
                System.exit(-1);
            }
            //split on one or more spaces.
            String[] params = arg.split("\\s+");
            if (params.length == 0) {
                System.err.println("Incorrect arg. Skipping " + arg);
                continue;
            }

            String directive = params[0];
            try {
                Command command;
                switch (directive) {
                    case "process-file":
                        command = new ProcessFileCommand(arg, params, executor, idToTitleMap, uniqueTitles, prefixMatcher, titleIdCounter);
                        break;
                    case "query":
                        command = new QueryCommand(arg, params, idToTitleMap, prefixMatcher);
                        break;
                    case "quit":
                        command = new QuitCommand();
                        break;
                    default:
                        System.err.println("Unexpected command encountered. Ignoring " + arg);
                        command = new NoOpCommand();
                }
                command.printCommmand();
                command.execute();
            } catch (Exception e) {
                System.err.println("Exception swallowed. Moving on to next directive " + e);
            }
        }
    }

    public static void main(String args[]) throws Exception{
        ApplicationMain applicationMain = new ApplicationMain();
        applicationMain.start();
    }
}
