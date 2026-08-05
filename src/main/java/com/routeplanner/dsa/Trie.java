package com.routeplanner.dsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;
        String fullWord;

        public TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.fullWord = word; // Store original case
    }

    public List<String> searchAutocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;
        for (char ch : prefix.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return results; // No match found
            }
            current = current.children.get(ch);
        }
        findAllWords(current, results);
        return results;
    }

    private void findAllWords(TrieNode node, List<String> results) {
        if (node.isEndOfWord) {
            results.add(node.fullWord);
        }
        for (TrieNode child : node.children.values()) {
            findAllWords(child, results);
        }
    }
}
