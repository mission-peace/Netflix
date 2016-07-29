package com.netflix.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Iterator for input file consisting of titles and returns {@link TitleInfo} for each title.
 */
public class TitleFileIterator implements Iterator<TitleInfo> {

    private final BufferedReader bufferedReader;
    private String next;
    public TitleFileIterator(InputStream stream) {
        bufferedReader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        try {
            next = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed in processing input stream.");
        }
    }

    @Override
    public boolean hasNext(){
        return next != null;
    }

    @Override
    public TitleInfo next() {
        String data = next;
        try {
            next = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed in processing input stream.");
        }
        String[] input = data.split("\t");
        if (input.length != 3) {
            System.err.println("Looks like incorrect data. Returning empty tokens" + data);
            return new TitleInfo("", "", Collections.EMPTY_LIST);
        }
        return new TitleInfo(input[0] + " " + input[1], input[2], createTokens(input[2]));
    }

    private static Collection<String> createTokens(String data) {
        String[] inputTokens = data.split("\\s+");
        Set<String> tokens = new HashSet<>();
        tokens.add(data.toLowerCase());
        for (int i = 0; i < inputTokens.length; i++) {
            tokens.add(inputTokens[i].toLowerCase());
        }
        return tokens;
    }
}
