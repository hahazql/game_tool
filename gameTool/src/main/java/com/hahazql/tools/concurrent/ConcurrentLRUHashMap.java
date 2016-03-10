package com.hahazql.tools.concurrent;

import com.hahazql.tools.cache.LRUHashMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Concurrent LRU map
 *
 * @param <K>
 * @param <V>
 */
public class ConcurrentLRUHashMap<K, V> {
    private final LRUHashMap<K, V> innerMap;
    private final ReentrantLock lock;


    public ConcurrentLRUHashMap() {
        this(1024);
    }


    public int size() {
        this.lock.lock();
        try {
            return this.innerMap.size();
        } finally {
            this.lock.unlock();
        }
    }


    public ConcurrentLRUHashMap(int capacity) {
        this.innerMap = new LRUHashMap<K, V>(capacity, true);
        this.lock = new ReentrantLock();
    }


    public void put(K k, V v) {
        this.lock.lock();
        try {
            this.innerMap.put(k, v);
        } finally {
            this.lock.unlock();
        }
    }


    public V get(K k) {
        this.lock.lock();
        try {
            return this.innerMap.get(k);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean containsKey(Object key) {
        HashMap<K, V> map = (HashMap<K, V>) this.innerMap.clone();
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        HashMap<K, V> map = (HashMap<K, V>) this.innerMap.clone();
        return map.containsValue(value);
    }

    public Collection<V> values() {
        HashMap<K, V> map = (HashMap<K, V>) this.innerMap.clone();
        return map.values();
    }

    public Set<K> keySet() {
        HashMap<K, V> map = (HashMap<K, V>) this.innerMap.clone();
        return map.keySet();
    }


}