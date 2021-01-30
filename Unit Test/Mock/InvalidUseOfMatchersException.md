# Mockito:org.mockito.exceptions.misusing.InvalidUseOfMatchersException

这个错误是新手常犯的错误，先看测试代码：

```java
Mockito.when(callerService.checkCallerAuth(Mockito.anyString(),Const.EDITFILE_OUTER_CALLER_API)).thenReturn(true);//错误发生的代码区
Mockito.when(dtagOuterService.saveTagLock(Matchers.same(tagLock))).thenReturn("correct");
```

错误分析：

从异常的位置来看，该异常发生在打桩阶段，还未执行到真正的测试。

从异常的信息来看，显然违反了一个Mockito框架中的Matchers匹配参数的规则。根据Matchers文档如下，在打桩阶段有一个原则，一个mock对象的方法，如果其若干个参数中，有一个是通过Matchers提供的，则该方法的所有参数都必须通过Matchers提供。而不能是有的参数通过Matchers提供，有的参数直接给出真实的具体值。

解决方法：就是修改两个都用具体值或者两个都用匹配。