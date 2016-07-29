package com.netflix.trie;

import java.util.Collection;

/**
 * Data structure to allow prefix based search.
 */
public interface PrefixMatcher {

    /**
     * Inserts given token and id for token into trie.
     */
    void insert(String token, Long id);

    /**
     * Returns upto limit number of ids for given token.
     */
    Collection<Long> search(String token, int limit);

}
