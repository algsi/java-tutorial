# <font color="red">java.util.regex包下Pattern、Matcher类学习</font>

java.util.regex包主要包括以下三个类：

- Pattern 类

- Matcher 类

- PatternSyntaxException 类

	PatternSyntaxException 是一个非强制异常类，它表示一个正则表达式模式中的语法错误。

## <font color="red">Pattern类</font>

Pattern 对象是一个正则表达式的编译表示。Pattern 类没有公共构造方法。要创建一个 Pattern 对象，你必须首先调用其公共静态编译方法，它返回一个 Pattern 对象。该方法接受一个正则表达式作为它的第一个参数。

Pattern 实现类ljava.io中的Serializable序列化接口。

### <font color="orange">常用方法</font>

- compile()方法：

	这是Pattern类的静态编译方法，该方法就相当于一个构造方法，因为它返回一个类的示例。它的String类型参数是一个正则表达式。

    ```
    /**
     * Compiles the given regular expression into a pattern.
     *
     * @param  regex
     *         The expression to be compiled
     * @return the given regular expression compiled into a pattern
     * @throws  PatternSyntaxException
     *          If the expression's syntax is invalid
     */
    public static Pattern compile(String regex) {
    	return new Pattern(regex, 0);
    }

    public static Pattern compile(String regex, int flags) {
    	return new Pattern(regex, flags);
    }
    ```
    
    其中参数flags是表明匹配模式，下面是取值说明，这些都是 Pattern 类的静态常量（final类型）
    
    ```
     * @param  flags
     *         Match flags, a bit mask that may include
     *         {@link #CASE_INSENSITIVE}, {@link #MULTILINE}, {@link #DOTALL},
     *         {@link #UNICODE_CASE}, {@link #CANON_EQ}, {@link #UNIX_LINES},
     *         {@link #LITERAL}, {@link #UNICODE_CHARACTER_CLASS}
     *         and {@link #COMMENTS}
	```

- toString()方法：返回模板的字符串形式

	```
    /**
     * <p>Returns the string representation of this pattern. This
     * is the regular expression from which this pattern was
     * compiled.</p>
     *
     * @return  The string representation of this pattern
     * @since 1.5
     */
    public String toString() {
        return pattern;
    }
    ```

- matcher()：用于获得Matcher对象的一个方法，该方法接收一个被判定的序列作为参数。其中compiled是一个boolean类型成员变量，初始值为false，以记录该pattern是否被编译。

	```
    /**
     * Creates a matcher that will match the given input against this pattern.
     *
     * @param  input
     *         The character sequence to be matched
     *
     * @return  A new matcher for this pattern
     */
    public Matcher matcher(CharSequence input) {
        if (!compiled) {
            synchronized(this) {
                if (!compiled)
                    compile();
            }
        }
        Matcher m = new Matcher(this, input);
        return m;
    }
	```

- matches()：匹配搜索，返回boolean值，实际上 String 类中的 matches 方法正是调用的此方法。

	```
    public static boolean matches(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }
    ```

## <font color="red">Matcher类</font>

该类实现了 MatchResult 接口。

Matcher类没有提供什么静态方法，通过调用 Pattern 对象的 matcher 方法来获得一个 Matcher 对象，如：

Pattern pattern = Pattern.compile("regExp");
Matcher matcher = pattern.matcher("string");

此时我们就得到了一个Matcher对象，通过此对象就可以对字符串进行操作了。


#### <font color="orange">构造方法</font>

友好的而并非public，所以，并没有公共默认的构造方法，需要 Pattern 的 matcher() 方法。在第二个方法中我们可以看到匹配完成之后，此对象又将返回初始化状态。

```
/**
 * No default constructor.
 */
Matcher() {
}

/**
 * All matchers have the state used by Pattern during a match.
 */
Matcher(Pattern parent, CharSequence text) {
    this.parentPattern = parent;
    this.text = text;

    // Allocate state storage
    int parentGroupCount = Math.max(parent.capturingGroupCount, 10);
    groups = new int[parentGroupCount * 2];
    locals = new int[parent.localCount];

    // Put fields into initial states
    reset();
}

```
    
#### <font color="orange">reset ( )</font>

重新设置方法。这个方法被重载，可以用一个新的序列来重设这个对象，但最后还是调用了无参的方法。

```
/**
 * Resets this matcher.
 *
 * <p> Resetting a matcher discards all of its explicit state information
 * and sets its append position to zero. The matcher's region is set to the
 * default region, which is its entire character sequence. The anchoring
 * and transparency of this matcher's region boundaries are unaffected.
 *
 * @return  This matcher
 */
public Matcher reset() {
    first = -1;
    last = 0;
    oldLast = -1;
    for(int i=0; i<groups.length; i++)
        groups[i] = -1;
    for(int i=0; i<locals.length; i++)
        locals[i] = -1;
    lastAppendPosition = 0;
    from = 0;
    to = getTextLength();
    return this;
}

/**
 * Resets this matcher with a new input sequence.
 *
 * <p> Resetting a matcher discards all of its explicit state information
 * and sets its append position to zero.  The matcher's region is set to
 * the default region, which is its entire character sequence.  The
 * anchoring and transparency of this matcher's region boundaries are
 * unaffected.
 *
 * @param  input
 *         The new input character sequence
 *
 * @return  This matcher
 */
public Matcher reset(CharSequence input) {
    text = input;
    return reset();
}
```



#### <font color="orange">主要的匹配查找方法</font>

Matcher有以下可以用来匹配查找的方法：

- boolean matches()

- boolean lookingAt()

- boolean find()

- boolean find(int start)

matches()的前提是Pattern匹配整个字符串，而lookingAt()的意思是Pattern匹配字符串的开头。

#### <font color="orange">find ( )</font>

查找。此方法返回值为布尔类型。用来搜索与正则表达式相匹配的任何目标字符串。即发现CharSequence里的，与pattern相匹配的多个字符序列，并尝试去查找下一个与模板匹配的序列。find()像一个迭代器，从头到尾扫描一遍字符串。第二个find()方法是带int参数的，正如你所看到的，它会告诉方法从哪里开始找——即从参数位置开始查找。一旦找到，便可以通过其他方法（注释里面有提及）获取更多信息。

以下是源码以及注释

```
    /**
     * Attempts to find the next subsequence of the input sequence that matches
     * the pattern.
     *
     * <p> This method starts at the beginning of this matcher's region, or, if
     * a previous invocation of the method was successful and the matcher has
     * not since been reset, at the first character not matched by the previous
     * match.
     *
     * <p> If the match succeeds then more information can be obtained via the
     * <tt>start</tt>, <tt>end</tt>, and <tt>group</tt> methods.  </p>
     *
     * @return  <tt>true</tt> if, and only if, a subsequence of the input
     *          sequence matches this matcher's pattern
     */
    public boolean find() {
        int nextSearchIndex = last;
        if (nextSearchIndex == first)
            nextSearchIndex++;

        // If next search starts before region, start it at region
        if (nextSearchIndex < from)
            nextSearchIndex = from;

        // If next search starts beyond region then it fails
        if (nextSearchIndex > to) {
            for (int i = 0; i < groups.length; i++)
                groups[i] = -1;
            return false;
        }
        return search(nextSearchIndex);
    }

    /**
     * Resets this matcher and then attempts to find the next subsequence of
     * the input sequence that matches the pattern, starting at the specified
     * index.
     *
     * <p> If the match succeeds then more information can be obtained via the
     * <tt>start</tt>, <tt>end</tt>, and <tt>group</tt> methods, and subsequent
     * invocations of the {@link #find()} method will start at the first
     * character not matched by this match.  </p>
     *
     * @param start the index to start searching for a match
     * @throws  IndexOutOfBoundsException
     *          If start is less than zero or if start is greater than the
     *          length of the input sequence.
     *
     * @return  <tt>true</tt> if, and only if, a subsequence of the input
     *          sequence starting at the given index matches this matcher's
     *          pattern
     */
    public boolean find(int start) {
        int limit = getTextLength();
        if ((start < 0) || (start > limit))
            throw new IndexOutOfBoundsException("Illegal start index");
        reset();
        return search(start);
    }
```

使用例子

```
package com.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {

		//数字模板：连续的三个数字
		Pattern pattern = Pattern.compile("\\d{3}");
		
		String s="123 456 789 4d56";
		
		Matcher matcher = pattern.matcher(s);
		int count=0;

		while(matcher.find()) {
			 count++;
	         System.out.println("Match number: "+count);
	         //此次匹配到的起点索引值
	         System.out.print("start(): "+matcher.start());
	         //此次匹配到的终点索引值
	         System.out.println(", end(): "+matcher.end());
	         //匹配到的内容，里面可以指定int类型的参数
	         System.out.println(matcher.group(0));
	         System.out.println();
		}
	}
}
```

输出结果为：
```
Match number: 1
start(): 0. end(): 3
123

Match number: 2
start(): 4. end(): 7
456

Match number: 3
start(): 8. end(): 11
789
```

#### <font color="orange">group ( )</font>

group是用括号括起来的，能被后面的表达式调用的<font color="red">正则表达式</font>。group 0 表示整个表达式，group 1表示第一个（从左往右数）被括起来的group，以此类推。所以

	A((BC)(D))E

里面有四个group：group 0是ABCDE， group 1是BCD，group 2是BC， group 3是D。

你可以用下述Matcher方法来使用group：

public int groupCount( )返回matcher对象中的group的数目。不包括group 0。

public String group( ) 返回上次匹配操作(比方说find( ))的group 0(整个匹配)

public String group(int i)返回上次匹配操作的某个group。如果匹配成功，但是没能找到group，则返回null。

public int start(int group)返回上次匹配所找到的group的开始位置（参数即为group索引）。

public int end(int group)返回上次匹配所找到的group的结束位置，最后一个字符的下标加一（参数即为group索引）。

示例：

```
package com.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static final String poem =
			"Twas brillig, and the slithy toves/n" +
			"Did gyre and gimble in the wabe./n" +
			"All mimsy were the borogoves,/n" +
			"And the mome raths outgrabe./n/n" +
			"Beware the Jabberwock, my son,/n" +
			"The jaws that bite, the claws that catch./n" +
			"Beware the Jubjub bird, and shun/n" +
			"The frumious Bandersnatch.";
	
	public static void main(String[] args) {
		/*注意：正则表达式中包含了括号*/
		Matcher m = Pattern.compile("(\\S+)\\s+((\\S+)\\s+(\\S+))").matcher(poem);
		while(m.find()) {
			for (int j = 0; j <= m.groupCount(); j++) {
				System.out.println("group "+j+" [" + m.group(j) + "]");
				
				/*测试有参数的start和end方法，找出某个group的起始终结位置*/
				if(j==1) {
					System.out.println("group1' start: "+m.start(1)+" , group1' end: "+m.end(1));
				}
			}
			System.out.println();
		}
	}
}
```

运行结果：

```
group 0 [Twas brillig, and]
group 1 [Twas]
group1' start: 0 , group1' end: 4
group 2 [brillig, and]
group 3 [brillig,]
group 4 [and]

group 0 [the slithy toves/nDid]
group 1 [the]
group1' start: 18 , group1' end: 21
group 2 [slithy toves/nDid]
group 3 [slithy]
group 4 [toves/nDid]

group 0 [gyre and gimble]
group 1 [gyre]
group1' start: 40 , group1' end: 44
group 2 [and gimble]
group 3 [and]
group 4 [gimble]

group 0 [in the wabe./nAll]
group 1 [in]
group1' start: 56 , group1' end: 58
group 2 [the wabe./nAll]
group 3 [the]
group 4 [wabe./nAll]

group 0 [mimsy were the]
group 1 [mimsy]
group1' start: 74 , group1' end: 79
group 2 [were the]
group 3 [were]
group 4 [the]

group 0 [borogoves,/nAnd the mome]
group 1 [borogoves,/nAnd]
group1' start: 89 , group1' end: 104
group 2 [the mome]
group 3 [the]
group 4 [mome]

group 0 [raths outgrabe./n/nBeware the]
group 1 [raths]
group1' start: 114 , group1' end: 119
group 2 [outgrabe./n/nBeware the]
group 3 [outgrabe./n/nBeware]
group 4 [the]

group 0 [Jabberwock, my son,/nThe]
group 1 [Jabberwock,]
group1' start: 144 , group1' end: 155
group 2 [my son,/nThe]
group 3 [my]
group 4 [son,/nThe]

group 0 [jaws that bite,]
group 1 [jaws]
group1' start: 169 , group1' end: 173
group 2 [that bite,]
group 3 [that]
group 4 [bite,]

group 0 [the claws that]
group 1 [the]
group1' start: 185 , group1' end: 188
group 2 [claws that]
group 3 [claws]
group 4 [that]

group 0 [catch./nBeware the Jubjub]
group 1 [catch./nBeware]
group1' start: 200 , group1' end: 214
group 2 [the Jubjub]
group 3 [the]
group 4 [Jubjub]

group 0 [bird, and shun/nThe]
group 1 [bird,]
group1' start: 226 , group1' end: 231
group 2 [and shun/nThe]
group 3 [and]
group 4 [shun/nThe]

```

#### <font color="orange">start( ) and end( )</font>

如果匹配成功，start( )会返回此次匹配的开始位置，end( )会返回此次匹配的结束位置，即最后一个字符的下标加一。如果之前的匹配不成功(或者没匹配)，那么无论是调用start( )还是end( )，都会引发一个IllegalStateException。下面这段程序还演示了matches( )和lookingAt( )：

```
package com.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {

		 String[] input = new String[] {
				 "Java has regular expressions in 1.4",
				 "regular expressions now expressing in Java",
				 "Java represses oracular expressions"
		 		 };
	     Pattern p1 = Pattern.compile("re\\w*");
	     Pattern p2 = Pattern.compile("Java.*");
	     for(int i = 0; i < input.length; i++) {
	    	 System.out.println("input[" + i + "]: " + input[i]);
	    	 
	    	 Matcher m1 = p1.matcher(input[i]);
	    	 Matcher m2 = p2.matcher(input[i]);
	    	 
	    	 while(m1.find())
	    		 System.out.println("m1.find() '" + m1.group() + "' start = "+ m1.start() + " end = " + m1.end());
	       
	    	 while(m2.find())
	    		 System.out.println("m2.find() '" + m2.group() + "' start = "+ m2.start() + " end = " + m2.end());
	    	 
	    	 if(m1.lookingAt())
	    		 System.out.println("m1.lookingAt() start = " + m1.start() + " end = " + m1.end());
	    	 if(m2.lookingAt())
	    		 System.out.println("m2.lookingAt() start = " + m2.start() + " end = " + m2.end());
	    	 
	    	 if(m1.matches())
	    		 System.out.println("m1.matches() start = " + m1.start() + " end = " + m1.end());
	    	 if(m2.matches())
	    		 System.out.println("m2.matches() start = " + m2.start() + " end = " + m2.end());
	    	
	    	 System.out.println();
	     }
	}
}
```

输出结果为：

```
input[0]: Java has regular expressions in 1.4
m1.find() 'regular' start = 9 end = 16
m1.find() 'ressions' start = 20 end = 28
m2.find() 'Java has regular expressions in 1.4' start = 0 end = 35
m2.lookingAt() start = 0 end = 35
m2.matches() start = 0 end = 35

input[1]: regular expressions now expressing in Java
m1.find() 'regular' start = 0 end = 7
m1.find() 'ressions' start = 11 end = 19
m1.find() 'ressing' start = 27 end = 34
m2.find() 'Java' start = 38 end = 42
m1.lookingAt() start = 0 end = 7

input[2]: Java represses oracular expressions
m1.find() 'represses' start = 5 end = 14
m1.find() 'ressions' start = 27 end = 35
m2.find() 'Java represses oracular expressions' start = 0 end = 35
m2.lookingAt() start = 0 end = 35
m2.matches() start = 0 end = 35
```

#### <font color="orange">split( )</font>

所谓分割是指将以正则表达式为界，将字符串分割成String数组。

	String[] split(CharSequence charseq)
	String[] split(CharSequence charseq, int limit)

这是一种既快又方便地将文本根据一些常见的边界标志分割开来的方法。

示例：

```
package com.general;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		String input = "This!!unusual use!!of exclamation!!points";
			System.out.println(Arrays.asList(Pattern.compile("!!").split(input)));
				
			/*Only do the first three*/
			System.out.println(Arrays.asList(Pattern.compile("!!").split(input, 3)));
			
			/*String's function split*/
			System.out.println(Arrays.asList("Aha! String has a split() built in!".split(" ")));
	}
}
```

运行结果

```
[This, unusual use, of exclamation, points]
[This, unusual use, of exclamation!!points]
[Aha!, String, has, a, split(), built, in!]
```

#### <font color="orange">匹配模式</font>

compile( )方法还有另一种重载，它可以传入一个控制正则表达式的匹配行为的参数.

```
	/* @param  flags
     *         Match flags, a bit mask that may include
     *         {@link #CASE_INSENSITIVE}, {@link #MULTILINE}, {@link #DOTALL},
     *         {@link #UNICODE_CASE}, {@link #CANON_EQ}, {@link #UNIX_LINES},
     *         {@link #LITERAL}, {@link #UNICODE_CHARACTER_CLASS}
     *         and {@link #COMMENTS}
     */

public static Pattern compile(String regex, int flags) {
    	return new Pattern(regex, flags);
    }
```

当调用另一个只有一个String类型的参数时，实际上就是调用了flags=0的有两个参数的compile()方法，即默认匹配模式参数为0。

那么，参数flags所代表的模式究竟是什么意义呢？如下表格为flags的取值范围（参数都是Pattern类里的静态int类型常量，为十六进制，可以直接调用）：

| 编译标志 | 描述 |
|--------|--------|
| CANON_EQ | 当且仅当两个字符的"正规分解(canonical decomposition)"都完全相同的情况下，才认定匹配。比如用了这个标志之后，表达式"a\u030A"会匹配"?"。默认情况下，不考虑"规范相等性(canonical equivalence)"。 |
| CASE_INSENSITIVE | 默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。这个标志能让表达式忽略大小写进行匹配。要想对Unicode字符进行大小不明感的匹配，只要将UNICODE_CASE与这个标志合起来就行了。 |
| COMMENTS | 在这种模式下，匹配时会忽略(正则表达式里的)空格字符(注：不是指表达式里的"\\s"，而是指表达式里的空格，tab，回车之类)。注释从#开始，一直到这行结束。可以通过嵌入式的标志来启用Unix行模式。|
| DOTALL | 在这种模式下，表达式'.'可以匹配任意字符，包括表示一行的结束符。默认情况下，表达式'.'不匹配行的结束符。 |
| MULTILINE | 在这种模式下，' ^ '和' $ '分别匹配一行的开始和结束。此外，' ^ '仍然匹配字符串的开始，'$'也匹配字符串的结束。默认情况下，这两个表达式仅仅匹配字符串的开始和结束。 |
| UNICODE_CASE | 在这个模式下，如果你还启用了CASE_INSENSITIVE标志，那么它会对Unicode字符进行大小写不明感的匹配。默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。 |
| UNIX_LINES | 在这个模式下，只有 '\n' 才被认作一行的中止，并且与 ' . '，' ^ '，以及' $ '进行匹配。 |

可以用"OR" ( ' | ' )运算符把这些标志合使用。

示例：

```
package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFlags {

	public static void main(String[] args) {

		//同时启用多个匹配模式：对大小写不敏感以及倒三角号表示一行的开始
		Pattern p = Pattern.compile("^java", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher("java has regex\nJava has regex\n" + "JAVA has pretty good regular expressions\n"
				+ "Regular expressions are in Java");
		while (m.find())
			System.out.println(m.group());
	}

}

```

运行结果：

```
java
Java
JAVA
```

#### <font color="orange">替换方法</font>

Matcher类同时提供了四个将匹配子串替换成指定字符串的方法：

- replaceAll(String replacement)：将目标字符串里与既有模式相匹配的子串全部替换为指定的字符串。

- replaceFirst(String replacement)：将目标字符串里第一个与既有模式相匹配的子串替换为指定的字符串。

- appendReplacement(StringBuffer sb, String replacement)：将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。

- appendTail(StringBuffer sb)：将最后一次匹配工作后剩余的字符串添加到一个StringBuffer对象里。

程序示例：

```
package com.li.regex.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*此程序将对Matcher类的四个将匹配子串替换成指定字符串的方法进行测试*/

public class ReplaceTest {

	/* 对replaceAll方法进行测试 */
	/*将目标字符串里与既有模式相匹配的子串全部替换为指定的字符串，并返回返回新的整个字符串*/
	public static void replaceAllTest() {
		Pattern pattern = Pattern.compile("cat");
		Matcher matcher = pattern.matcher("cat cat cat cat");
		boolean flag=matcher.find();
		if(flag) {
			System.out.println(matcher.replaceAll("hat"));
		}
	}
	
	/* 对replaceFirst方法进行测试 */
	/*将目标字符串里第一个与既有模式相匹配的子串替换为指定的字符串。*/
	public static void replaceFirstTest() {
		Pattern pattern = Pattern.compile("cat");
		Matcher matcher = pattern.matcher("cat cat cat cat");
		boolean flag=matcher.find();
		if(flag) {
			System.out.println(matcher.replaceFirst("hat"));
		}
	}
	
	/* 对appendReplacement(StringBuffer sb, String replacement)和appendTail(StringBuffer sb)方法进行测试 */
	/*appendReplacement将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。*/
	/*appendTail将最后一次匹配工作后剩余的字符串添加到一个StringBuffer对象里。*/
	public static void appendTest() {
		Pattern pattern = Pattern.compile("Kelvin");
		Matcher matcher = pattern.matcher("Kelvin Li and Kelvin Chan are both working in Kelvin Chen's KelvinSoftShop company");
		StringBuffer sb = new StringBuffer();
		int i=0;
        //使用find()方法查找第一个匹配的对象
        boolean result = matcher.find();
        //使用循环将句子里所有的kelvin找出并替换再将内容加到sb里
        while(result) {
            i++;
            matcher.appendReplacement(sb, "Sakila");
            System.out.println("第"+i+"次匹配后sb的内容是："+sb.toString());
            result = matcher.find();
        }
        //最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
        matcher.appendTail(sb);
        System.out.println("调用m.appendTail(sb)后sb的最终内容是:"+ sb.toString());
	}
	
	public static void main(String[] args) {
		//ReplaceTest.replaceAllTest();
		//ReplaceTest.replaceFirstTest();
		ReplaceTest.appendTest();
	}

}

```