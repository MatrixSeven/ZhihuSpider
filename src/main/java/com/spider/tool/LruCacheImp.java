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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
 * Created by seven on 2016/12/21.
 */
public class LruCacheImp<K> implements LruCacheInterface<K> {

    volatile LinkedList<K> temp;
    int maxsize;
    AtomicLong hits=new AtomicLong(0);
    AtomicLong trytimes=new AtomicLong(0);
    public LruCacheImp(int size) {
        this.temp = new LinkedList<K>();
        maxsize=size;
    }
    private void removeLast(){
        synchronized (this.temp) {
            System.out.println("移除数据");
            int removeIndex = maxsize / 3;
            while (removeIndex-- > 0) {
                temp.removeLast();
            }
        }
    }

    @Override
    public void add(K k) {
        synchronized (temp) {
            temp.addLast(k);
            if (temp.size() > maxsize) {
                removeLast();
            }
        }
    }

    @Override
    public K remove(K k) {
        temp.remove(k);
        return null;
    }

    @Override
    public Boolean containsKey(K k) {
        trytimes.incrementAndGet();
        if(temp.contains(k)){
            temp.remove(k);
            temp.addFirst(k);
            hits.incrementAndGet();
            return true;
        }
        add(k);
        return false;
    }

    @Override
    public void removeAll() {
        temp.clear();
    }

    @Override
    public void resetSize(int size) {
        this.maxsize=size;
    }

    @Override
    public int size() {
        return temp.size();
    }

    @Override
    public void addAll(List<K> list) {
        temp.addAll(list);
    }

    @Override
    public String toString() {
        return String.format("LruCache[maxSize=%s,hits=%s,misses=%s,hitRate=%s]",
                maxsize, hits, trytimes, (hits.intValue()/1.00)/(trytimes.intValue()/1.00));
    }
}
