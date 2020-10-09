# transient关键字

其实 transient 关键字非常好理解，简单的一句话：将不需要序列化的属性用关键字 transient 来修饰，序列化对象的时候，就不会序列化该对象。

在 java.util.HashMap 中，有使用示例：`transient Node<K,V>[] table`。

## 静态变量能被序列化吗

静态变量不管是否有 transient 关键字修饰，都不会被序列化。为什么呢？其实原因很简单，因为静态变量属于 Class 实例的信息，位于方法区（永久代），它是全局共享的，因此完全没有必要被序列化。
