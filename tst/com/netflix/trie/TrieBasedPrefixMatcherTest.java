package com.netflix.trie;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TrieBasedPrefixMatcherTest {

    @Test
    public void testInsertAndSearch() {
        PrefixMatcher prefixMatcher = new TrieBasedPrefixMatcher();
        prefixMatcher.insert("Game of thrones", 1l);
        prefixMatcher.insert("Game", 1l);
        prefixMatcher.insert("of", 1l);
        prefixMatcher.insert("thrones", 1l);

        Collection<Long> ids = prefixMatcher.search("of", 1);
        Assert.assertEquals(1, ids.size());
        Assert.assertEquals(1l, (long)ids.iterator().next());

        prefixMatcher.insert("House of cards", 2l);
        prefixMatcher.insert("House", 2l);
        prefixMatcher.insert("of", 2l);
        prefixMatcher.insert("cards", 2l);

        ids = prefixMatcher.search("of", 2);
        Assert.assertEquals(2, ids.size());
        Set<Long> expectedResult = new HashSet<>();
        expectedResult.add(1l);
        expectedResult.add(2l);

        Assert.assertEquals(expectedResult, ids);

        ids = prefixMatcher.search("of", 1);
        Assert.assertEquals(1, ids.size());

    }
}
