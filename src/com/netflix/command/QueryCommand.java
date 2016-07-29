package com.netflix.command;

import com.netflix.title.TitleInfo;
import com.netflix.trie.PrefixMatcher;

import java.awt.image.TileObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCommand implements Command {

    private static final int LIMIT = 10;
    private final String arg;
    private final String[] params;
    private final ConcurrentHashMap<Long, TitleInfo> idToTitleMap;
    private final PrefixMatcher prefixMatcher;
    public QueryCommand(final String arg, final String[] params, final ConcurrentHashMap<Long, TitleInfo> idToTitleMap,
                        final PrefixMatcher prefixMatcher) {
        if (params.length < 2) {
            throw new IllegalArgumentException("Number of arguments should be 2" + params);
        }
        this.arg = arg;
        this.params = params;
        this.idToTitleMap = idToTitleMap;
        this.prefixMatcher = prefixMatcher;

    }
    @Override
    public void execute() {
        int firstSpaceIndex = arg.indexOf(" ");
        String query = arg.substring(firstSpaceIndex + 1).trim().toLowerCase();
        Collection<Long> ids = prefixMatcher.search(query, LIMIT);
        List<TitleInfo> result = new ArrayList<>();
        for (long id : ids) {
            TitleInfo titleInfo = idToTitleMap.get(id);
            result.add(titleInfo);
        }
        //sort the result by their titles and print them.
        result.sort(QueryCommand::compare);
        result.stream().map(title -> title.getFullTitle()).forEach(System.out::println);
    }

    private static int compare(TitleInfo titleInfo1, TitleInfo titleInfo2) {
        return titleInfo1.getTitle().compareTo(titleInfo2.getTitle());
    }

    @Override
    public void printCommmand() {
        System.out.println(arg);
    }
}
