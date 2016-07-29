package com.netflix.command;

import com.netflix.title.TitleFileIterator;
import com.netflix.title.TitleInfo;
import com.netflix.trie.PrefixMatcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessFileCommand implements Command {

    private final String arg;
    private final FileInputStream fileInputStream;
    private final Executor executor;
    private final ConcurrentHashMap<Long, TitleInfo> idToTitleMap;
    private final ConcurrentHashMap<String, Boolean> uniqueTitles;
    private final PrefixMatcher prefixMatcher;
    private final AtomicLong titleCounter;
    public ProcessFileCommand(final String arg, final String[] params, final Executor executor,
                              final ConcurrentHashMap<Long, TitleInfo> idToTitleMap, final ConcurrentHashMap<String, Boolean> uniqueTitles,
                              final PrefixMatcher prefixMatcher, final AtomicLong titleIdCounter) throws IOException{
        if (params.length != 2) {
            throw new IllegalArgumentException("Not sufficient arguments");
        }
        this.arg = arg;
        this.fileInputStream = new FileInputStream(params[1]);
        this.executor = executor;
        this.idToTitleMap = idToTitleMap;
        this.prefixMatcher = prefixMatcher;
        this.titleCounter = titleIdCounter;
        this.uniqueTitles = uniqueTitles;
    }

    @Override
    public void execute() {
        TitleFileIterator titleFileIterator = new TitleFileIterator(fileInputStream);
        executor.execute(() -> {
            try {
                while (titleFileIterator.hasNext()) {
                    TitleInfo titleInfo = titleFileIterator.next();
                    Boolean exists = uniqueTitles.putIfAbsent(titleInfo.getFullTitle(), true);
                    if (exists != null) {
                        continue;
                    }
                    long id = titleCounter.getAndIncrement();
                    idToTitleMap.put(id, titleInfo);
                    for (String token : titleInfo.getTokens()) {
                        prefixMatcher.insert(token, id);
                    }
                }
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    System.err.print("Exception when closing stream " + e);
                }
            }
        });
    }

    @Override
    public void printCommmand() {
        System.out.println(arg);
    }
}
