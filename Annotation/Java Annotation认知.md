# Java Annotation认知

From：<https://www.cnblogs.com/skywang12345/p/3344137.html>

## 摘要

Java Anntation是 JDK1.5 引入的一种注释机制。

## 1. Annotation架构

先来看看 Anntation 的架构图：

![img](/Users/xavier/GitHub/JavaSE/Annotation/images/28123151-d471f82eb2bc4812b46cc5ff3e9e6b82-20200313231117028.jpg)

从中我门可以看出：

1. 1个 Annotation 和1个 RetentionPolicy 关联。可以理解为每个 Annotation 都会有唯一的 RetentionPolicy 属性。
2. 1个 Annotation 和 1～n 个 ElementType 关联，可以理解为每个 Annotation 可以有若干个 ElementType 属性。
3. Annotation 有许多实现类，包括：Deprecated, Documented, Inherited, Override等等。

下面，我们先介绍 Annotation 架构的左半部分，即：Annotation，RetentionPolicy，ElementType。

## 2. Annotation组成部分

Java Annotation 的组成中，有三个非常重要的主干类，它们分别是：

（1）java.lang.annotation.Annotation

```java
package java.lang.annotation;
public interface Annotation {

    boolean equals(Object obj);

    int hashCode();

    String toString();

    Class<? extends Annotation> annotationType();
}
```

（2）java.lang.annotation.ElementType

```java
package java.lang.annotation;

public enum ElementType {
    /** Class, interface (including annotation type), or enum declaration */
    TYPE,

    /** Field declaration (includes enum constants) */
    FIELD,

    /** Method declaration */
    METHOD,

    /** Formal parameter declaration */
    PARAMETER,

    /** Constructor declaration */
    CONSTRUCTOR,

    /** Local variable declaration */
    LOCAL_VARIABLE,

    /** Annotation type declaration */
    ANNOTATION_TYPE,

    /** Package declaration */
    PACKAGE,

    /**
     * Type parameter declaration
     *
     * @since 1.8
     */
    TYPE_PARAMETER,

    /**
     * Use of a type
     *
     * @since 1.8
     */
    TYPE_USE
}

```

- TYPE：类、接口（包括注释类型）或枚举声明
- FIELD：字段声明（包括枚举常量）
- METHOD：方法声明
- PATAMETER：参数声明
- CONSTRUCTOR：构造方法声明
- LOCAL_VARIABLE：局部变量声明
- ANNOTATION_TYPE：注释类型声明
- PACKAGE：包声明
- TYPE_PARAMETER：类型参数声明（1.8开始）
- TYPE_USER：类型使用（1.8开始）

（3）java.lang.annotation.RetentionPolicy

```java
package java.lang.annotation;

public enum RetentionPolicy {
    /**
     * Annotations are to be discarded by the compiler.
     */
    SOURCE,

    /**
     * Annotations are to be recorded in the class file by the compiler
     * but need not be retained by the VM at run time.  This is the default
     * behavior.
     */
    CLASS,

    /**
     * Annotations are to be recorded in the class file by the compiler and
     * retained by the VM at run time, so they may be read reflectively.
     *
     * @see java.lang.reflect.AnnotatedElement
     */
    RUNTIME
}
```

关于上面三个类的一些说明：

- Annotation 就是一个接口，每个 Annotation 对象都会有唯一一个 RetentionPolicy属性，会有若干个 ElementType 属性。

- ElementType 是枚举类型，它用来指定 Annotation 的类型。

  当 Annotation 与某个 ElementType 关联时，就意味着 Annotation 有了某种用途，例如，若一个 Annotation 对象是 METHOD 类型，则该 Annotation 只能用来修饰方法。

- RetentionPolicy 是枚举类型，它用来指定 Annotation 的保留策略。通俗点说，就是不同 RetentionPolicy 类型的 Annotation 的作用域不同。每个 Annotation 对象都只有一个 RetentionPolicy 关联。

  - 若 Annotation 对象的类型是 SOURCE，则意味着：Annotation仅存在于编译器处理期间，编译器处理完之后，该Annotation就没用了。例如，`@Override` 标志就是一个Annotation。当它修饰一个方法的时候，就意味着该方法覆盖父类的方法，并且在编译期间会进行语法检查！编译器处理完后，`@Override` 就没有任何作用了。
  - 若 Annotation 的类型为 CLASS，则意味着：编译器将 Annotation 存储于类对应的 .class 文件中，它是 Annotation 的默认行为。
  - 若 Annotation 的类型为 RUNTIME，则意味着：编译器将 Annotation 存储于 .class 文件中，并且将会在运行时被 JVM 读入，所以它们可通过反射来获取。

## 3. Annotation的通用定义

理解了上面的3个类的作用之后，我们接下来可以讲解Annotation实现类的语法定义了。

通用定义：

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
  
}
```

上面的作用是定义一个Annotation，它的名字是MyAnnotation。定义了MyAnnotation之后，我们可以在代码中通过 `@MyAnnotation` 来使用它。
其它的，`@Documented`，`@Target` ，`@Retention`，`@interface` 都是来修饰MyAnnotation的。下面分别说说它们的含义：

1. @interface

   使用 `@interface` 定义注解时，意味着它实现了 java.lang.annotation.Annotation 接口，即该注解就是一个Annotation。
   *定义Annotation时，@interface是必须的。*
   注意：它和我们通常的implemented实现接口的方法不同。Annotation接口的实现细节都由编译器完成。通过 `@interface` 定义注解后，该注解不能继承其他的注解或接口。

2. @Documented
   类和方法的Annotation在缺省情况下是不出现在javadoc中的。如果使用@Documented修饰该Annotation，则表示它可以出现在javadoc中。
   *定义Annotation时，@Documented可有可无；若没有定义，则Annotation不会出现在javadoc中。*

3. @Target(ElementType.TYPE)

   前面我们说过，ElementType 是 Annotation 的类型属性。而 `@Target` 的作用，就是来指定 Annotation 的类型属性。
   `@Target(ElementType.TYPE)`  的意思就是指定该Annotation的类型是 `ElementType.TYPE`。这就意味着，MyAnnotation 是来修饰“类、接口（包括注释类型）或枚举声明”的注解。
   *定义Annotation时，@Target可有可无。若有@Target，则该Annotation只能用于它所指定的地方；若没有@Target，则该Annotation可以用于任何地方。*

4. @Retention(RetentionPolicy.RUNTIME)
   前面我们说过，RetentionPolicy 是 Annotation 的保留策略属性，而 `@Retention` 的作用，就是指定Annotation的保留策略属性。
   `@Retention(RetentionPolicy.RUNTIME)` 的意思就是指定该 Annotation 的策略是 `RetentionPolicy.RUNTIME`。这就意味着，编译器会将该 Annotation 信息保留在 .class 文件中，并且能在运行时被 JVM 读取。
   *定义Annotation时，@Retention可有可无。若没有@Retention，则默认是RetentionPolicy.CLASS。*

## 4. Java自带的Annotation

通过上面的示例，我们了解到了如何来定义一个 Annotation。而 Java 中自带来许多常用的 Annotation，这些常用的 Annotation 是非常好的学习例子，下面就看看 Java 中自带的 Annotation 实现累，即 Annotation 架构图的右半部分。

**Java 常用的Annotation：**

- @Deprecated  --  @Deprecated 所标注内容，不再被建议使用。
- @Override    --  @Override 只能标注方法，表示该方法覆盖父类中的方法。
- @Documented  --  @Documented 所标注内容，可以出现在javadoc中。
- @Inherited   --  @Inherited只能被用来标注“Annotation类型”，它所标注的Annotation具有继承性。
- @Retention   --  @Retention只能被用来标注“Annotation类型”，它被用来指定Annotation的RetentionPolicy属性。
- @Target      --  @Target只能被用来标注“Annotation类型”，而且它被用来指定Annotation的ElementType属性。
- @SuppressWarnings  --  @SuppressWarnings 所标注内容产生的警告，编译器会对这些警告保持静默。

由于 `@Deprecated` 和 `@Override` 类似，`@Documented`，` @Inherited`，` @Retention`，`@Target`类似。下面，我们只对`@Deprecated`，`@Inherited`，`@SuppressWarnings` 这3个Annotation进行说明。

### 4.1. @Deprecated

@Deprecated 的定义如下：

```java
package java.lang;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface Deprecated {
}

```

说明：

1. @interface -- 它的用来修饰Deprecated，意味着Deprecated实现了java.lang.annotation.Annotation接口；即Deprecated就是一个注解。
2. @Documented -- 它的作用是说明该注解能出现在javadoc中。
3. @Retention(RetentionPolicy.RUNTIME) -- 它的作用是指定Deprecated的策略是RetentionPolicy.RUNTIME。这就意味着，编译器会将Deprecated的信息保留在.class文件中，并且能被 JVM 读取。
4. @Deprecated 所标注内容，不再被建议使用。
   例如，若某个方法被 @Deprecated 标注，则该方法不再被建议使用。如果有开发人员试图使用或重写被@Deprecated标示的方法，编译器会给相应的提示信息，如果你用了IDE开发，那么IDE会在调用该方法的地方画出一条横线。

### 4.2. @Inherited

@Inherited 的定义如下：

```java
package java.lang.annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Inherited {
}
```

说明：

1. @Target(ElementType.ANNOTATION_TYPE) -- 它的作用是指定Inherited的类型是ANNOTATION_TYPE。这就意味着，@Inherited只能被用来标注“Annotation类型”。
2. @Inherited 的含义是，它所标注的Annotation将具有继承性。
   假设，我们定义了某个Annotaion，它的名称是MyAnnotation，并且MyAnnotation被标注为@Inherited。现在，某个类Base使用了MyAnnotation，则Base具有了“具有了注解MyAnnotation”；现在，Sub继承了Base，由于MyAnnotation是@Inherited的(具有继承性)，所以，Sub也“具有了注解MyAnnotation”。

@Inherited注解的使用示例：

InheritableSon.java

```java
package com.xavier.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@interface Inheritable {

}

@Inheritable
class InheritableFather {
    public InheritableFather() {
        // InheritableFather 是否具有 @Inheritable 注解
        System.out.println("InheritableFather: " + InheritableFather.class.isAnnotationPresent(Inheritable.class));
    }
}

public class InheritableSon extends InheritableFather {

    public InheritableSon() {
        // 调用父类构造函数
        super();
        // InheritableSon是否具有 @Inheritable 注解
        System.out.println("InheritableSon: " + InheritableSon.class.isAnnotationPresent(Inheritable.class));
    }

    public static void main(String[] args) {
        InheritableSon son = new InheritableSon();
    }
}

```

Output

```
InheritableFather: true
InheritableSon: true
```

现在，我们对 InheritableSon.java 进行修改，注释掉 @Inheritable 的 @Inherited 注解。

InheritableSon.java

```java
package com.xavier.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// @Inherited
@interface Inheritable {

}

@Inheritable
class InheritableFather {
    public InheritableFather() {
        // InheritableFather 是否具有 @Inheritable 注解
        System.out.println("InheritableFather: " + InheritableFather.class.isAnnotationPresent(Inheritable.class));
    }
}

public class InheritableSon extends InheritableFather {

    public InheritableSon() {
        // 调用父类构造函数
        super();
        // InheritableSon是否具有 @Inheritable 注解
        System.out.println("InheritableSon: " + InheritableSon.class.isAnnotationPresent(Inheritable.class));
    }

    public static void main(String[] args) {
        InheritableSon son = new InheritableSon();
    }
}
```

Output

```
InheritableFather: true
InheritableSon: false
```

对比上面的两个结果，我们发现：当注解 Inheritable 被 @Inherited 标注时，它具有继承性；否则，没有继承性。

### 4.3. @SuppressWarnings

@SuppressWarngings 的定义如下：

```java
package java.lang;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
    String[] value();
}
```

说明：

1. @Retention(RetentionPolicy.SOURCE) -- 它的作用是指定SuppressWarnings的策略是RetentionPolicy.SOURCE。这就意味着，SuppressWarnings信息仅存在于编译器处理期间，编译器处理完之后SuppressWarnings就没有作用了。
2. @Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE}) -- 它的作用是指定SuppressWarnings的类型同时包括TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE。
3. String[] value() -- 意味着，SuppressWarnings能指定参数
4. SuppressWarnings 的作用是，让编译器对“它所标注的内容”的某些警告保持静默。例如，`@SuppressWarnings(value={"deprecation", "unchecked"})`  表示对“它所标注的内容”中的 “SuppressWarnings不再建议使用警告”和“未检查的转换时的警告”保持沉默。

补充：**SuppressWarnings 常用的关键字**

```
deprecation  -- 使用了不赞成使用的类或方法时的警告
unchecked    -- 执行了未检查的转换时的警告，例如当使用集合时没有用泛型 (Generics) 来指定集合保存的类型。
fallthrough  -- 当 Switch 程序块直接通往下一种情况而没有 Break 时的警告。
path         -- 在类路径、源文件路径等中有不存在的路径时的警告。
serial       -- 当在可序列化的类上缺少 serialVersionUID 定义时的警告。
finally      -- 任何 finally 子句不能正常完成时的警告。
all          -- 关于以上所有情况的警告。
```

## 5. Annotation的作用

Annotation 是一个辅助类，它在Junit、Struts、Spring等工具框架中被广泛使用。我们在编程中经常会使用到的Annotation作用有：

### 5.1 编译检查

Annotation具有“让编译器进行编译检查的作用”。

例如，@SuppressWarnings, @Deprecated 和 @Override 都具有编译检查作用。
- 关于@SuppressWarnings和@Deprecated，已经详细介绍过了，这里就不再举例说明了。

- 若某个方法被 @Override的 标注，则意味着该方法会覆盖父类中的同名方法。如果有方法被@Override标示，但父类中却没有被@Override标注的同名方法，则编译器会报错。

### 5.2 在反射中使用 Annotation

在反射的Class, Method, Field等函数中，有许多于Annotation相关的接口。这也意味着，我们可以在反射中解析并使用Annotation。

AnnotationTest.java

```java
package com.xavier.spring.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * @author Xavier
 */
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    String[] value() default "unknow";
}

class Person {

    @MyAnnotation
    @Deprecated
    public void empty() {
        System.out.println("\nempty");
    }

    @MyAnnotation(value = {"girl", "boy"})
    public void someBody(String name, Integer age) {
        System.out.println("\nsomebody: "+name+", "+age);
    }
}

public class AnnotationTest {

    public static void main(String[] args) throws Exception {
        Person person = new Person();
        Class<Person> clazz = Person.class;

        Method mSomebody = clazz.getDeclaredMethod("someBody", String.class, Integer.class);
        mSomebody.invoke(person, "Lily", 19);
        iteratorAnnotations(mSomebody);

        Method nEmpty = clazz.getDeclaredMethod("empty");
        nEmpty.invoke(person);
        iteratorAnnotations(nEmpty);
    }

    public static void iteratorAnnotations(Method method) {
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
            String[] values = annotation.value();
            for (String s : values) {
                // 获取该注解上的指定属性的值
                System.out.print(s + ", ");
            }
            System.out.println();
        }

        Annotation[] annotations = method.getAnnotations();
        for (Annotation a : annotations) {
            System.out.println(a);
        }
    }
}
```

Output

```java
somebody: Lily, 19
girl, boy, 
@com.xavier.spring.annotation.MyAnnotation(value=[girl, boy])

empty
unknow, 
@com.xavier.spring.annotation.MyAnnotation(value=[unknow])
@java.lang.Deprecated()
```

因为 MyAnnotation 注解的保留策略是 RUNTIME，所以我们可以通过反射来在运行时获取类或者方法的 MyAnnotation，否则，则运行时通过反射是获取不到这个注解信息的，读者可以自己试试。

### 6. 框架中常用的Annotation

其他框架中的Annotation示例也非常值得我们借鉴，通过这些优秀示例，我们能更加灵活的使用Annotation：

Spring：

- @Autowired
- @RequestBody
- @RequestParam

MyBatis：

- @Param
- @Mapper