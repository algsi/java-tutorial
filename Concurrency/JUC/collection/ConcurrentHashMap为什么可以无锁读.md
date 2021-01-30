# ConcurrentHashMap 为什么可以无锁读

get 操作源码

```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    // 根据 key 的 hashCode 计算出对应节点的 hash
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0)
            // 找到的节点的 hash 小于 0，两种情况，该 bin 是一棵树，或者该 bin 的 head 为 ForwardingNode 类型，标志着该节点正在扩容，
            // ForwardingNode 节点存储了 nextTable 引用，调用 ForwardingNode 的 find 方法在 nextTable 中进行查找
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) {
            // 节点的 hash 大于等于 0，从链表结构中查找
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```

整个 get 操作都没有加锁，也没有用 CAS 操作。

## 参考资料

- <https://blog.csdn.net/chenssy/article/details/100589173>
