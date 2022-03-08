# Java程序常见错误分析

## Broke pipe

## SocketException: Socket closed

Example

```log
2021-09-17 10:58:29 org.apache.thrift.transport.TIOStreamTransport - Error closing output stream.
java.net.SocketException: Socket closed
        at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:118)
        at java.net.SocketOutputStream.write(SocketOutputStream.java:155)
        at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)
        at java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)
        at java.io.FilterOutputStream.close(FilterOutputStream.java:158)
        at org.apache.thrift.transport.TIOStreamTransport.close(TIOStreamTransport.java:108)
        at org.apache.thrift.transport.TSocket.close(TSocket.java:235)
        at org.apache.thrift.transport.TFramedTransport.close(TFramedTransport.java:99)
```

`java.net.SocketException: Socket closed`. This exception means that you closed the socket, and then continue try to use it.

