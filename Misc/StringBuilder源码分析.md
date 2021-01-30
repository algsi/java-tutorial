# <font color="red">StringBuilder源码分析</font>

## <font color="red">包</font>

	package java.lang;

## <font color="red">类特点</font>

```
public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
```

- 该类被final修饰，不能被继承。

- 继承了AbstractStringBuilder类

- 实现了java.io.Serializable和CharSequence接口

## <font color="red">构造方法</font>

```
 public StringBuilder() {
        super(16);
    }

    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity specified by the {@code capacity} argument.
     *
     * @param      capacity  the initial capacity.
     * @throws     NegativeArraySizeException  if the {@code capacity}
     *               argument is less than {@code 0}.
     */
    public StringBuilder(int capacity) {
        super(capacity);
    }

    /**
     * Constructs a string builder initialized to the contents of the
     * specified string. The initial capacity of the string builder is
     * {@code 16} plus the length of the string argument.
     *
     * @param   str   the initial contents of the buffer.
     */
    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }

    /**
     * Constructs a string builder that contains the same characters
     * as the specified {@code CharSequence}. The initial capacity of
     * the string builder is {@code 16} plus the length of the
     * {@code CharSequence} argument.
     *
     * @param      seq   the sequence to copy.
     */
    public StringBuilder(CharSequence seq) {
        this(seq.length() + 16);
        append(seq);
    }
```

不适用任何参数实例化一个对象时，初始容量为16（也可以指定初始化的长度）。使用序列或String类初始化会有16的长度增量。

## <font color="red">append()方法</font>

```
@Override
public StringBuilder append(Object obj) {
    return append(String.valueOf(obj));
}
    
@Override
public StringBuilder append(String str) {
    super.append(str);
    return this;
}
 
public StringBuilder append(StringBuffer sb) {
    super.append(sb);
    return this;
}

@Override
public StringBuilder append(CharSequence s) {
    super.append(s);
    return this;
}

@Override
public StringBuilder append(CharSequence s, int start, int end) {
    super.append(s, start, end);
    return this;
}

@Override
public StringBuilder append(char[] str) {
    super.append(str);
    return this;
}

```

## <font color="red">insert()方法</font>

```
@Override
public StringBuilder insert(int offset, Object obj) {
        super.insert(offset, obj);
        return this;
}

/**
 * @throws StringIndexOutOfBoundsException {@inheritDoc}
 */
@Override
public StringBuilder insert(int offset, String str) {
    super.insert(offset, str);
    return this;
}

 @Override
public StringBuilder insert(int offset, char[] str) {
    super.insert(offset, str);
    return this;
}
```

其中offset表示字符串生成器的位置，该参数必须大于等于0，且小于等于此序列的长度（当等于此序列的长度时，则添加的末尾）。

## <font color="red">delete()方法</font>

```
/**
 * @throws StringIndexOutOfBoundsException {@inheritDoc}
 */
@Override
public StringBuilder delete(int start, int end) {
    super.delete(start, end);
    return this;
}

/**
 * @throws StringIndexOutOfBoundsException {@inheritDoc}
 */
@Override
public StringBuilder deleteCharAt(int index) {
    super.deleteCharAt(index);
    return this;
}
```

start：将要删除的字符串的起点位置（此位置包含在删除之内）
end：将要删除的字符数的终点位置（此位置不删除）
index：将要删除的字符的位置

## <font color="red">replace()方法</font>

```
@Override
public StringBuilder replace(int start, int end, String str) {
    super.replace(start, end, str);
    return this;
}
```