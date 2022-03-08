# Proper way to close an AutoCloseable

What is the most reliable pattern to follow when closing an [`OutputStream`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/OutputStream.html), [`ServerSocket`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/ServerSocket.html), or other object that implements the [`AutoCloseable`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/AutoCloseable.html) interface?

Should I use `try`-`catch`-`finally`? Or a shutdown hook.

## Answer

The correct way to use an `AutoCloseable` instance is with a `try-with-resources` hook, so the resource is reliably closed even if an exception is thrown.

like this:

```java
try (OutputStream stream = new ...) {
    ... // use the resource
} catch (IOException e) {
    ... // exception handling code
}
```

You can also [control multiple resources](https://stackoverflow.com/a/12372940/545127) using one block (rather than nested blocks):

```java
try (
     OutputStream out1 = ...;
     OutputStream out2 = ...;
     InputStream in1 = ...;
     InputStream in2 = ...;
) {
     ...
}
```

Don't use `try...finally` block, that will misbehave for some edge cases (the cases that require a suppressed exception).

Don't use a shutdown-hook: resources are rarely truely gloabl, and that approach will be prone to race hazards. `try`-with-resources is the recommended manner of properly closing **all** `AutoCloseable` resources: the two were introduced to Java at the same time so they can work together.

Doing this ensures that the resource is closed precisely once. Beware that in general it is unsafe to close an `AutoCloseable` more than once: the `close()` operation is **not** guaranteed to be idempotent.

## Reference

- <https://stackoverflow.com/questions/56112598/proper-way-to-close-an-autocloseable>

