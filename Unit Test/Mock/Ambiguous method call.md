# Ambiguous method call

当你开始打桩的时候：

```java
doNothing().when(upgradeClusterRepo).updateUpgradeClusterStatus(any(), anyString(), any());
```

会告诉你匹配到多个方法。

Ambiguous method call. Both `updateUpgradeClusterStatus(List<UpgradeCluster>, String, Date) in UpgradeClusterRepo` and `updateUpgradeClusterStatus(UpgradeCluster, String, Date)` in UpgradeClusterRepo match.

这两个方法都被匹配到，因为 `any()` 是不包含类型的。

`any()` Matches anything, including nulls and varargs.  
See examples in javadoc for ArgumentMatchers class This is an alias of: anyObject() and any(Class)

Notes :  
For primitive types use anyChar() family or isA(Class) or any(Class).
Since mockito 2.1.0 any(Class) is not anymore an alias of this method.

Since Mockito 2.1.0, only allow non-null String. As this is a nullable reference, the suggested API to match null wrapper would be isNull(). 
We felt this change would make tests harness much safer that it was with Mockito 1.x. See examples in javadoc for ArgumentMatchers class.

## 解决方案

基于此，我们可以提供两种方案：

方案一：强转

```java
doNothing().when(upgradeClusterRepo).updateUpgradeClusterStatus((UpgradeCluster) any(), anyString(), any());
```

方案二：指定 class

```java
doNothing().when(upgradeClusterRepo).updateUpgradeClusterStatus(any(UpgradeCluster.class), anyString(), any());
```
