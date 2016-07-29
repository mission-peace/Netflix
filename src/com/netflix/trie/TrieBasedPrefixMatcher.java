package com.netflix.trie;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;             

/**
 * Threadsafe implementation of PrefixMatcher using trie.
 */
public class TrieBasedPrefixMatcher implements PrefixMatcher {

    private class TrieNode {
        private final ConcurrentMap<Character, TrieNode> children;
        //this set is backed up by concurrentHashMap so it is threadsafe to put/iterate through this set.
        private final Set<Long> ids;
        public TrieNode() {
            children = new ConcurrentHashMap<>();
            ids = Collections.newSetFromMap(new ConcurrentHashMap<>());
        }

        public void add(Long id) {
            ids.add(id);
        }
    }

    private final TrieNode root;
    public TrieBasedPrefixMatcher() {
        root = new TrieNode();
    }

    @Override
    public void insert(String token, Long id) {
        TrieNode current = root;
        for (int i = 0; i < token.length(); i++) {
            TrieNode node = current.children.compute(token.charAt((i)), (key, value) -> {
                if (value != null) {
                    return value;
                } else {
                    return new TrieNode();
                }
            });
            current = node;
        }
        current.add(id);
    }

    @Override
    public Collection<Long> search(String word, int limit) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.children.get(ch);
            //if node does not exist for given char then return false
            if (node == null) {
                return Collections.EMPTY_LIST;
            }
            current = node;
        }
        Set<Long> result = new HashSet<>();
        addResult(current, limit, result);
        return result;
    }

    private void addResult(TrieNode current, int limit, Set<Long> result)  {
        if (limit == result.size() || current == null) {
            return;
        }

        Iterator<Long> iterator = current.ids.iterator();
        while (limit > result.size() && iterator.hasNext())  {
            result.add(iterator.next());
        }

        for (TrieNode child : current.children.values()) {
            addResult(child, limit, result);
        }
    }
}