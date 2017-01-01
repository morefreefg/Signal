# Signal

 Signal是一个为Android打造的发布/订阅模式，改进自 [EventBus](https://github.com/greenrobot/EventBus).。

* 编译时注解，效率更高。
* 支持四种线程模式。
* 事件根据订阅者和函数名识别，提升代码可阅读性。
* 支持可变参数，支持java基本数据类型，支持事件继承。
* 支持延时事件。
* 后期将支持进程间通讯。

## 在你的工程中使用

Gradle:
```gradle
compile 'com.bwelco:signal:1.0.3'
```

Maven:
```xml
<dependency>
  <groupId>com.bwelco</groupId>
  <artifactId>Signal</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

### 1. 写订阅函数
```
@SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
public void onSignal(String s1, String s2) {}
```
函数参数可变，在发送事件的时候将对参数进行匹配，支持基本类型。

threadMode有四种，默认为POSTERTHREAD。

* POSTERTHREAD : 此函数将在发送线程执行。

* MAINTHREAD : 此函数在主线程中执行，注意不要写复杂的代码以免导致ANR。

* BACKGROUND : 此函数在Signal默认的后台线程执行。每个事件按照顺序（在不设置延时参数的情况下）依次执行。

* ASYNC : 此函数会从Signal提供的线程池中随机抽取线程执行。事件之间互不干扰，并发执行。

### 2. 注册Signal
```java
@Override
public void onStart() {
      super.onStart();
      Signal.getDefault().subScribe(this);
}

@Override
public void onStop() {
      super.onStop();
      Signal.getDefault().unSubScribe(this);
}
```

### 3.发送事件
```java

send(SubScriber subscriber, Object arg1, Object arg2...)

sendDelayed(SubScriber subscriber, long delayMillis ,Object arg1, Object arg2...)

```

```java
 Signal.getDefault().send(new SubScriber(MainActivity.class, "onSignal"),
                        "message", "message2");

 Signal.getDefault().sendDelayed(new SubScriber(MainActivity.class, "onSignal"), 1000,
                        "message", "message2");

```

SubScriber : 事件处理函数，参数一为订阅者的class对象，参数二为处理函数的函数名。

delayMillis : 延时时间（毫秒），注意POSTERTHREAD不支持延时函数，调用时会抛出异常。

Object... args : 可变参数，支持java基本数据类型，支持基本类型参数，支持父类、接口等java支持的传参方式（默认不做函数参数类型检查）。

## License

Copyright (C) 2016-2016 bwelco (http://bwelco.coding.me)