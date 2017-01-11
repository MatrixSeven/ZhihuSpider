package com.spider.tool;
//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/!\
//		 .' @          /!!!\
//		 :         ,    !!!!
//		  `-..__.-' _.-\!!!/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //狗神保佑\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LRUCache<K> {
    private final LinkedHashMap<K, Object> map;
    private volatile int size;
    private int maxSize;
    private volatile AtomicInteger putCount=new AtomicInteger(0);
    private volatile AtomicInteger evictionCount=new AtomicInteger(0);;
    private volatile AtomicInteger hitCount=new AtomicInteger(0);;
    private volatile AtomicInteger missCount=new AtomicInteger(0);;
    private final Object value = new Object();

    public LRUCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap(maxSize, 0.75f, true);
    }

    public void init(List<K> list) {
        list.stream().forEach(a -> this.map.put(a, value));
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

    public final Boolean get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        Object mapValue;
        synchronized (this) {
            mapValue = map.get(key);
            if (mapValue != null) {
                hitCount.incrementAndGet();
                return true;
            }
            this.put(key);
            missCount.incrementAndGet();
        }
        return false;
    }


    public final Object put(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        Object previous;
        synchronized (this) {
            putCount.incrementAndGet();
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

    public void trimToSize(int maxSize) {
        while (true) {
            K key;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<K, Object> toEvict = map.entrySet().iterator().next();
                key = toEvict.getKey();
                map.remove(key);
                size -= safeSizeOf(key, value);
                evictionCount.intValue();
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
        remove(key);
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
        trimToSize(-1);
    }

    public synchronized final int mapSize() {
        return this.map.size();
    }

    public synchronized final int size() {
        return size;
    }

    public synchronized final int maxSize() {
        return maxSize;
    }

    public synchronized final int hitCount() {
        return hitCount.intValue();
    }

    public synchronized final int missCount() {
        return missCount.intValue();
    }

    public synchronized final int putCount() {
        return putCount.intValue();
    }

    public synchronized final int evictionCount() {
        return evictionCount.intValue();
    }

    public synchronized final Map<K, Object> snapshot() {
        return new LinkedHashMap<K, Object>(map);
    }

    @Override
    public final String toString() {
        int accesses = hitCount.intValue() + missCount.intValue();
        int hitPercent = accesses != 0 ? (100 * hitCount.intValue() / accesses) : 0;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
                maxSize, hitCount.intValue(), missCount.intValue(), hitPercent);
    }
}
