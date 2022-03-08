# Why use buffers to read/write Streams

Following reading various questions on reading and writing Streams, all the various answers define something like this as the correct way to do it:

```java
private void CopyStream(Stream input, Stream output)
{
   byte[] buffer = new byte[16 * 1024];
   int read;
   while ((read = input.Read(buffer, 0, buffer.Length)) > 0)
   {
      output.Write(buffer, 0, read);
   } 
}
```

数据缓冲就是因为数据被输入后在处理的时候需要一定的时间，为了输入接着输出，零时差，就需要缓冲了，先预读并处理一部分信息，然后开始输出，在输出的同时进行后面的输入和处理，然后等缓冲的部分输出完后，另一部分的数据也处理完毕了，就可以接着输出了。根据处理速度的不同，需要的缓冲区大小也是不同的。

因为 CPU 的速度比内存快，内存速度比硬盘快，缓冲是为了缓解速度跟不上的问题。倘若没有这个缓冲，那么就会很慢了，断断续续的数据流，因为处理不完。实际上缓存、内存、显存都是这种东西。

## When do we have to use buffered reader and buffered writer

There are two main reasons why you would use buffered I/O:

- Performance. Unbuffered IO requests work on one character at a time down to the lowest OS level (well, not real, but for most practical purposes it does). The buffering of IO requests lets the OS read/write more data in a single request, saving a bunch of individual calls. On some operating systems, for some types of IO, this can even allow the IO be done by a dedicated IO processing system or something like a DMA channel. In general, it is much more efficient to read the contents of a 1MiB file using a 1KiB buffer and making 1024 calls than it is to read it one byte at a time. The Buffered* classes simply hide this optimization.
- Look ahead. With a buffered read, it is possible to “put back” a character if you changed your mind. This is especially useful when scanning input. For example, say you want to read an integer from an input stream. With buffered IO you can simply start reading characters and process them. Once you get a non numeric character you can put it back and let the rest of the code deal with that character and what follows.

## Reference

- [Why use buffers to read/write Streams](https://stackoverflow.com/questions/2818509/why-use-buffers-to-read-write-streams)
- [Why use BufferedReader and BufferedWriter Classses in Java](https://medium.com/@isaacjumba/why-use-bufferedreader-and-bufferedwriter-classses-in-java-39074ee1a966)
- [When do we have to use buffered reader and buffered writer in Java?](https://www.quora.com/When-do-we-have-to-use-buffered-reader-and-buffered-writer-in-Java)
