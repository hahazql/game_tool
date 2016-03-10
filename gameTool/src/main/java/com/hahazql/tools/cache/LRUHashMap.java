package com.hahazql.tools.cache;


import java.util.LinkedHashMap;
import java.util.Map;

public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxCapacity;

    static final long serialVersionUID = 438971390573954L;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private boolean enableLRU;

    public LRUHashMap(int maxSize) {
        this(maxSize, 16, 0.75f, false);
    }

    public LRUHashMap(int maxSize, int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxCapacity = maxSize;
    }

    public LRUHashMap(final int maxCapacity, final boolean enableLRU) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
        this.enableLRU = enableLRU;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }
}