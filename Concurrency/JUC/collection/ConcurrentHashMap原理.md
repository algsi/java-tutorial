# ConcurrentHashMap 原理

ConcurrentHashMap 在 JDK1.7 中是采用 Segment + HashEntry + ReentrantLock 的方式进行实现的，而 1.8 中放弃了 Segment 臃肿的设计，取而代之的是采用 Node + CAS + Synchronized 来保证并发安全进行实现。

> The primary design goal of this hash table is to maintain concurrent readability (typically method get(), but also iterators and related methods) while minizing update contention. Secondary goals are to keep space consumption about the same or better than java.util.HashMap, and to support high initial insertion rates on an empty table by many threads.

表：table
桶：bin
槽：数组中的每个结点称之为槽，也就是说“槽”存储了链表的头结点或者红黑树的根结点

链表节点的 hash 值 >= 0
ForwardingNode 的 hash 值为 -1
红黑树的根节点的 hash 值为 -2

```java
/*
 * Encodings for Node hash fields. See above for explanation.
 */
static final int MOVED     = -1; // hash for forwarding nodes
static final int TREEBIN   = -2; // hash for roots of trees
static final int RESERVED  = -3; // hash for transient reservations
static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
```

## 内部类

- Node
- TreeBin
    
    > TreeBins hold the roots of set TreeNodes. TreeBin do not hold user keys or values, but instead point to list of TreeNodes and their root. ForwardingNodes are placed at the heads of bins during resizing.
- TreeNode
    
    > Nodes for use in TreeBins
- ForwardingNode
    
    > A node inserted at head of bins during transfer operations.
- CounterCell

> The types TreeBin, ForwardingNode, and  ReservationNode do not hold normal user keys, values, or hashes, and are readily distinguishable during search etc because they have negative hash fields and null key and value fields.
> Upon transfer, the old table bin contains only a special forwarding node (with the hash field "MOVED") that contains the next table as its key. On encountering a forwarding node, access and update operations restart, using the new table.

## get

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

通过源码发现，整个 get 操作都没有加锁，也没有用 CAS 操作，那么 get 方法是怎么保证线程安全的呢？现在先不回答这个问题，不过我们应该注意 get 方法中头结点 hash 值小于 0 的情况（即eh < 0）的情况，结合后面的扩容操作进行解释。

## put

```java
/** Implementation for put and putIfAbsent */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;  // binCount 为相应桶的节点数量统计
    // 没有终止条件，只有循环内部 break
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            // 需要初始化 table
            tab = initTable();
        // 每次循环都会重新计算槽的位置，因为可能刚好完成扩容操作，扩容完成后会使用新 table，槽的位置可能会发生改变
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // 槽为空，先尝试用CAS方式进行添加结点，如果添加成功则直接结束循环
            if (casTabAt(tab, i, null,
                            new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        // 当前线程先帮助迁移，迁移完成后更新 tab，在新表中进行 put
        else if ((fh = f.hash) == MOVED)
            // help transfer if a resize is in progress
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            // 当前桶并没有迁移操作
            // 加锁，防止其他线程对此桶同时进行put，remove，transfer操作
            // 加锁的对象为 f，即数组中的槽，为链表的头节点或者红黑树的根节点
            synchronized (f) {
                // 头节点发生变化，就说明当前链表（或红黑树）的头节点已经不是 f 了，
                // 可能被前面的线程 remove 掉了或者迁移到新表上了
                // 如果被 remove 掉了，需要重新对链表新的头节点加锁
                if (tabAt(tab, i) == f) {
                    // ForwordingNode 的 hash 值为 -1
                    // 链表节点的 hash 值 >= 0
                    if (fh >= 0) {
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                    (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            // 如果遍历到了链表最后一个节点，就把新节点插入到链表尾部
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                            value, null);
                                break;
                            }
                        }
                    }
                    // 红黑树的根结点 root 的 hash 值为 -2
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        // 调用红黑树 root 节点（TreeBin类型）的 putTreeVal 方法，将 key 和 value 插入进去
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                        value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    // 比较 binCount 和 TREEIFY_THRESHOLD，判断是否需要将链表树化
                    treeifyBin(tab, i);
                if (oldVal != null)
                    // 如果 oldVal 不为 null，则说明该 key 在 map 中有旧值，
                    // put 只更新值，不需要扩容，因此直接返回
                    return oldVal;
                break;
            }
        }
    }

    // 由 addCount 判断是否需要扩容
    addCount(1L, binCount);
    return null;
}
```

## ForwardingNode

> A node inserted at head of bins during transfer operations.

Forwarding Node 是一种在节点转移操作过程中插在桶头部的节点。

```java
/**
 * A node inserted at head of bins during transfer operations.
 */
static final class ForwardingNode<K,V> extends Node<K,V> {
    final Node<K,V>[] nextTable;
    ForwardingNode(Node<K,V>[] tab) {
        super(MOVED, null, null, null);
        this.nextTable = tab;
    }

    Node<K,V> find(int h, Object k) {
        // loop to avoid arbitrarily deep recursion on forwarding nodes
        outer: for (Node<K,V>[] tab = nextTable;;) {
            Node<K,V> e; int n;
            if (k == null || tab == null || (n = tab.length) == 0 ||
                (e = tabAt(tab, (n - 1) & h)) == null)
                return null;
            for (;;) {
                int eh; K ek;
                if ((eh = e.hash) == h &&
                    ((ek = e.key) == k || (ek != null && k.equals(ek))))
                    return e;
                if (eh < 0) {
                    if (e instanceof ForwardingNode) {
                        tab = ((ForwardingNode<K,V>)e).nextTable;
                        continue outer;
                    }
                    else
                        return e.find(h, k);
                }
                if ((e = e.next) == null)
                    return null;
            }
        }
    }
}
```

ForwardingNode类继承类 Node 类，所以ForwardingNode对象也是Node类型对象，所以它也可以放到表中。

ForwardingNode 只在扩容中使用，每个 ForwardingNode 对象都包含了一个扩容后的表的引用（新表保存在 nextTable 引用中）， ForwardingNode 对象的key，value，next属性值全部为 null，因此它不会存储实际的数据。

由于 ForwardingNode 的构造方法中调用 super 构造方法传入的 hash 一直都是 MOVED 变量（-1），因此它的 hash 值始终为 -1（注意小于是 0 哦，可以去关联 get 方法中对应的部分了）。

ForwardingNode 中重写了 Node 的 find 方法，它的逻辑是从扩容后的新表中查询结点，而不是以自身为头结点进行查找。

## 临时笔记

原表中不同桶上的结点，在新表上一定不会分配到相同位置的槽上。我们可以让不同线程负责原表不同位置的桶中所有结点的迁移，这样两个线程的迁移操作是不会相互干扰的。

四个关于扩容的方法

- addCount
- helpTransfer
- resizeStamp
- transfer

## 参考资料

- <https://www.cnblogs.com/nullzx/p/8647220.html>

