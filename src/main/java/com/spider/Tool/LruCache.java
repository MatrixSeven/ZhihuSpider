
package com.spider.Tool;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K> {
    private final LinkedHashMap<K, Object> map;
    private int size;
    private int maxSize;
    private int putCount;
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private int missCount;
    private final Object object=new Object();

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K,Object>(maxSize, 0.75f, true);
    }
    public void resize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }

        synchronized (this) {
            this.maxSize = maxSize;
        }
        trimToSize(maxSize);
    }
    public final Object get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        Object mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            if (mapValue != null) {
                hitCount++;
                return mapValue;
            }
            missCount++;
        }
        Object createdValue = create(key);
        if (createdValue == null) {
            return null;
        }

        synchronized (this) {
            createCount++;
            mapValue = map.put(key, createdValue);

            if (mapValue != null) {
                map.put(key, mapValue);
            } else {
                size += safeSizeOf(key, createdValue);
            }
        }

        if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            trimToSize(maxSize);
            return createdValue;
        }
    }
    //containsKey
    public final boolean containsKey(K key){
        if( get(key)==null){
            put(key,object);
            return false;
        }
        return  true;
    }
    public final Object put(K key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        Object previous;
        synchronized (this) {
            putCount++;
            size += safeSizeOf(key, value);
            previous = map.put(key, value);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }

        trimToSize(maxSize);
        return previous;
    }
    private void trimToSize(int maxSize) {
        while (true) {
            K key;
            Object value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize) {
                    break;
                }

                Map.Entry<K, Object> toEvict = null;
                for (Map.Entry<K, Object> entry : map.entrySet()) {
                    toEvict = entry;
                }
                if (toEvict == null) {
                    break;
                }

                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= safeSizeOf(key, value);
                evictionCount++;
            }

            entryRemoved(true, key, value, null);
        }
    }

    public final Object remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        Object previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }

        return previous;
    }
    protected void entryRemoved(boolean evicted, K key, Object oldValue, Object newValue) {

    }

    protected Object create(K key) {
        return null;
    }

    private int safeSizeOf(K key, Object value) {
        int result = sizeOf(key, value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }

    protected int sizeOf(K key, Object value) {
        return 1;
    }


    public final void evictAll() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    public synchronized final int size() {
        return size;
    }


    public synchronized final int maxSize() {
        return maxSize;
    }


    public synchronized final int hitCount() {
        return hitCount;
    }

    public synchronized final int missCount() {
        return missCount;
    }

    public synchronized final int createCount() {
        return createCount;
    }

    public synchronized final int putCount() {
        return putCount;
    }


    public synchronized final int evictionCount() {
        return evictionCount;
    }

    public synchronized final Map<K, Object> snapshot() {
        return new LinkedHashMap<K, Object>(map);
    }

    @Override
    public synchronized final String toString() {
        int accesses = hitCount + missCount;
        int hitPercent = accesses != 0 ? (100 * hitCount / accesses) : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
                maxSize, hitCount, missCount, hitPercent);
    }
}
