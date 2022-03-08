# How to read file from end to start (in reverse order) in Java

## 1. ReversedLinesFileReader

Apache Common IO has the [ReversedLinesFileReader](https://commons.apache.org/proper/commons-io/javadocs/api-2.4/org/apache/commons/io/input/ReversedLinesFileReader.html) class for this now (well, since version 2.2).

So your code could be:

```java
String path = "";
try (ReversedLinesFileReader fr = new ReversedLinesFileReader(new File(path), StandardCharsets.UTF_8)) {
    String line;
    while ((line = fr.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

depencency

```xml
<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
</dependency>
```

## Reference

- [How to read file from end to start (in reverse order) in Java?](https://stackoverflow.com/questions/8664705/how-to-read-file-from-end-to-start-in-reverse-order-in-java)

