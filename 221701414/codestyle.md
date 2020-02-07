# 代码风格

[TOC]

## 缩进

<table><tr><td bgcolor=#D1EEEE>

1. 缩进采用 4 个空格。使用 tab 缩进，通过 IDEA 设置 tab 为 4 个空格，不勾选 Use tab character 。

</td></tr></table>

## 变量命名

<table><tr><td bgcolor=#D1EEEE>

1. 代码中的命名均不以下划线或美元符号开始，也不以下划线或美元符号结束。

<br />

2. 代码中的命名使用英文的方式。

<br />

3. 中括号是数组类型的一部分，数组定义如下：String[] args;

<br />

4. 杜绝完全不规范的缩写，避免望文不知义。

</td></tr></table>

## 每行最多字符数

<table><tr><td bgcolor=#D1EEEE>

1. 单行字符数限制不超过 120 个，超出需要换行，换行时遵循如下原则：

- 第二行相对第一行缩进 4 个空格，从第三行开始，不再继续缩进，参考示例。
- 运算符与下文一起换行。
- 方法调用的点符号与下文一起换行。
- 在多个参数超长，逗号后进行换行。
- 在括号前不要换行，见反例。

```
正例：
StringBuffer sb = new StringBuffer();
//超过120个字符的情况下，换行缩进4个空格，并且方法前的点符号一起换行
sb.append("zi").append("xin")...
	.append("huang")...
	.append("huang")...
	.append("huang");
反例：
StringBuffer sb = new StringBuffer();
//超过120个字符的情况下，不要在括号前换行
sb.append("zi").append("xin")...append
	("huang");
//参数很多的方法调用可能超过120个字符，不要在逗号前换行
method(args1, args2, args3, ...
	, argsX);
```

</td></tr></table>

## 函数最大行数

<table><tr><td bgcolor=#D1EEEE>

1. 函数最大行数原则上不超过 80 行

- 当发现函数中有重复代码的时候，将它封装成一个新的函数

</td></tr></table>

## 函数、类命名

<table><tr><td bgcolor=#D1EEEE>

1. 函数名、参数名、成员变量、局部变量都统一使用 lowerCamelCase 风格，必须遵从驼峰形式。

```
正例： localValue / getHttpMessage() /  inputUserId
```

<br />

2. 类名使用 UpperCamelCase 风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO 等。

```
正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal / TaPromotion
```

<br />

3. 抽象类命名使用 Abstract 或 Base 开头；异常类命名使用 Exception 结尾；测试类命名以它要测试的类的名称开始，以 Test 结尾。

<br />

4. POJO 类中布尔类型的变量，都不要加 is，否则部分框架解析会引起序列化错误。

```
反例：定义为基本数据类型boolean isSuccess；的属性，它的方法也是isSuccess()，RPC
框架在反向解析的时候，“以为”对应的属性名称是success，
导致属性获取不到，进而抛出异常。
```

<br />

5. 包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形式。

<br />

6. 如果使用到了设计模式，建议在类名中体现出具体模式。

```
正例：public class OrderFactory;
public class LoginProxy;
public class ResourceObserver;
```

<br />

7. 接口类中的方法和属性不要加任何修饰符号（public 也不要加），保持代码的简洁性，并加上有效的 Javadoc 注释。尽量不要在接口里定义变量，如果一定要定义变量，肯定是与接口方法相关，并且是整个应用的基础常量。

```
正例：接口方法签名：void f();
接口基础常量表示：String COMPANY = "alibaba";
反例：接口方法定义：public abstract void f();
说明：JDK8中接口允许有默认实现，那么这个default方法，是对所有实现类都有价值的默认实现。
```

<br />

8. 对于 Service 和 DAO 类，基于 SOA 的理念，暴露出来的服务一定是接口，内部的实现类用 Impl 的后缀与接口区别。

```
正例：CacheServiceImpl实现CacheService接口。
```

<br />

9. 枚举类名建议带上 Enum 后缀，枚举成员名称需要全大写，单词间用下划线隔开。

```
正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKNOWN_REASON。
```

<br />

10. 各层命名归约

```
Service/DAO层方法命名规约

1. 获取单个对象的方法用get做前缀。
2. 获取多个对象的方法用list做前缀。
3. 获取统计值的方法用count做前缀。
4. 插入的方法用save（推荐）或insert做前缀。
5. 删除的方法用remove（推荐）或delete做前缀。
6. 修改的方法用update做前缀。

//-------------------------------------------------------//

领域模型命名规约

1. 数据对象：xxxDO，xxx即为数据表名。
2. 数据传输对象：xxxDTO，xxx为业务领域相关的名称。
3. 展示对象：xxxVO，xxx一般为网页名称。
4. POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。
```

</td></tr></table>

## 常量

<table><tr><td bgcolor=#D1EEEE>

1. 常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。

<br />

2. 不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。

<br />

3. long 或者 Long 初始赋值时，必须使用大写的 L，不能是小写的 l，小写容易跟数字 1 混淆，造成误解。

<br />

4. 不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护。如：缓存相关的常量放在类：CacheConsts 下；系统配置相关的常量放在类：ConfigConsts 下。

<br />

5. 如果变量值仅在一个范围内变化用 Enum 类。如果还带有名称之外的延伸属性，必须使用 Enum 类，下面正例中的数字就是延伸信息，表示星期几。

```
正例：publicEnum{MONDAY(1),TUESDAY(2),WEDNESDAY(3),THURSDAY(4),FRIDAY(5),SATURDAY(6), SUNDAY(7);}
```

</td></tr></table>

## 空行规则

<table><tr><td bgcolor=#D1EEEE>

1. 大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：

- 左大括号前不换行。
- 左大括号后换行。
- 右大括号前换行。
- 右大括号后还有 else 等代码则不换行；表示终止右大括号后必须换行。
  <br />

2. 每个类的第一行留空

<br />

3. 方法的第一行留空

<br />

4. 每个类属性的声明用一行空行隔开

<br />

5. 方法体内的执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义之间插入一个空行。相同业务逻辑和语义之间不需要插入空行。

</td></tr></table>

## 注释规则

<table><tr><td bgcolor=#D1EEEE>

1. 类、类属性、类方法的注释必须使用 Javadoc 规范，使用/\*\*内容\*/格式，不得使用//xxx 方式。

<br />

2. 所有的抽象方法（包括接口中的方法）必须要用 Javadoc 注释、除了返回值、参数、异常说明外，还必须指出该方法做什么事情，实现什么功能。

<br />

3. 所有的类都必须添加创建者信息。

<br />

4. 方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用
   /\* \*/注释，注意与代码对齐。

<br />

5. 所有的枚举类型字段必须要有注释，说明每个数据项的用途。

<br />

6. 代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑等的修改。

<br />

7. 注释掉的代码尽量要配合说明，而不是简单的注释掉。

<br />

8. 对于注释的要求：

- ① 能够准确反应设计思想和代码逻辑；
- ② 能够描述业务含义，使别的程序员能够迅速了解到代码背后的信息。完全没有注释的大段代码对于阅读者形同天书，注释是给自己看的，即使隔很长时间，也能清晰理解当时的思路；注释也是给继任者看的，使其能够快速接替自己的工作。

</td></tr></table>

## 操作符前后空格

<table><tr><td bgcolor=#D1EEEE>

- 任何运算符左右必须加一个空格

<br />

- 左括号和后一个字符之间不出现空格；
  同样，右括号和前一个字符之间也不出现空格。

<br />

- if/for/while/switch/do 等保留字与左右括号之间都必须加空格。

<br />

- 方法参数在定义和传入时，多个参数逗号后边必须加空格。

```
method("a", "b", "c");
```

</td></tr></table>

## 其他规则

### 控制语句

<table><tr><td bgcolor=#D1EEEE>

1. 在一个 switch 块内，每个 case 要么通过 break/return 等来终止，要么注释说明程序将继续执行到哪一个 case 为止；在一个 switch 块内，都必须包含一个 default 语句并且放在最后，即使它什么代码也没有。

<br />

2. 在 if/else/for/while/do 语句中必须使用大括号，即使只有一行代码，避免使用下面的形式：if (condition) statements;

<br />

3. 推荐尽量少用 else， if-else 的方式可以改写成：

```
if(condition){
	...
	return obj;
}
//接着写else的业务逻辑代码;
说明：如果非得使用if()...else if()...else...方式表达逻辑，【强制】请勿超过 3层，
超过请使用状态设计模式。
正例：逻辑上超过 3层的if-else代码可以使用卫语句，或者状态模式来实现。
```

<br />

5. 除常用方法（如 getXxx/isXxx）等外，不要在条件判断中执行其它复杂的语句，将复杂逻辑判断的结果赋值给一个有意义的布尔变量名，以提高可读性。

```
正例：
//伪代码如下
boolean existed = (file.open(fileName, "w") != null) && (...) || (...);
if (existed) {
...
}
反例：
if ((file.open(fileName, "w") != null) && (...) || (...)) {
...
}
```

</td></tr></table>

### OPP 规约

<table><tr><td bgcolor=#D1EEEE>

1. 避免通过一个类的对象引用访问此类的静态变量或静态方法，无谓增加编译器解析成本，直接用类名来访问即可。

<br />

2. 所有的覆写方法，必须加@Override 注解。

<br />

3. 相同参数类型，相同业务含义，才可以使用 Java 的可变参数，避免使用 Object。

```
//说明：可变参数必须放置在参数列表的最后。（提倡同学们尽量不用可变参数编程）

正例：public User getUsers(String type, Integer... ids)
```

<br />

4. 对外暴露的接口签名，原则上不允许修改方法签名，避免对接口调用方产生影响。接口过时必须加@Deprecated 注解，并清晰地说明采用的新接口或者新服务是什么。

<br />

5. 不能使用过时的类或方法。
   > 说明：java.net.URLDecoder 中的方法 decode(StringencodeStr)这个方法已经过时，应该使用双参数 decode(String source, String encode)。接口提供方既然明确是过时接口，那么有义务同时提供新的接口；作为调用方来说，有义务去考证过时方法的新实现是什么。

<br />

6. Object 的 equals 方法容易抛空指针异常，应使用常量或确定有值的对象来调用 equals。

<br />

7. 所有的相同类型的包装类对象之间值的比较，全部使用 equals 方法比较。
   > 说明：对于 Integer var=?在-128 至 127 之间的赋值，Integer 对象是在 IntegerCache.cache 产生，会复用已有对象，这个区间内的 Integer 值可以直接使用==进行判断，但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑，推荐使用 equals 方法进行判断。

<br />

8. 关于基本数据类型与包装数据类型的使用标准如下：

- 所有的 POJO 类属性必须使用包装数据类型。
- RPC 方法的返回值和参数必须使用包装数据类型。
- 所有的局部变量【推荐】使用基本数据类型。
- **POJO 类属性没有初值是提醒使用者在需要使用时，必须自己显式地进行赋值，任何 NPE 问题，或者入库检查，都由使用者来保证。**

<br />

9. 构造方法里面禁止加入任何业务逻辑，如果有初始化逻辑，请放在 init 方法中。

<br />

10. POJO 类必须写 toString 方法。使用 IDE 的中工具：source> generate toString 时，如果继承了另一个 POJO 类，注意在前面加一下 super.toString。

<br />

11. 使用索引访问用 String 的 split 方法得到的数组时，需做最后一个分隔符后有无内容的检查，否则会有抛 IndexOutOfBoundsException 的风险。

<br />

12. 当一个类有多个构造方法，或者多个同名方法，这些方法应该按顺序放置在一起，便于阅读。

<br />

13. 类内方法定义顺序依次是：公有方法或保护方法 >私有方法 > getter/setter 方法。

<br />

14. setter 方法中，参数名称与类成员变量名称一致，this.成员名=参数名。在 getter/setter 方法中，尽量不要增加业务逻辑，增加排查问题的难度。

<br />

15. 循环体内，字符串的联接方式，使用 StringBuilder 的 append 方法进行扩展。

<br />

16. final 可提高程序响应效率，声明成 final 的情况：

- 不需要重新赋值的变量，包括类属性、局部变量。
- 对象参数前加 final，表示不允许修改引用的指向。
- 类方法确定不允许被重写。

<br />

17. 成员与方法访问控制从严：

- 如果不允许外部直接通过 new 来创建对象，那么构造方法必须是 private。
- 工具类不允许有 public 或 default 构造方法。
- 类非 static 成员变量并且与子类共享，必须是 protected。
- 类非 static 成员变量并且仅在本类使用，必须是 private。
- 类 static 成员变量如果仅在本类使用，必须是 private。
- 若是 static 成员变量，必须考虑是否为 final。
- 类成员方法只供类内部调用，必须是 private。
- 类成员方法只对继承类公开，那么限制为 protected。

</td></tr></table>

### 集合处理

<table><tr><td bgcolor=#D1EEEE>

1. **【强制】**关于 hashCode 和 equals 的处理，遵循如下规则：

- 只要重写 equals，就必须重写 hashCode。
- 因为 Set 存储的是不重复的对象，依据 hashCode 和 equals 进行判断，所以 Set 存储的对象必须重写这两个方法。
- 如果自定义对象做为 Map 的键，那么必须重写 hashCode 和 equals。

```
正例：String重写了hashCode和equals方法，所以我们可以非常愉快地使用String对象作为key来使用。
```

2. **【强制】**ArrayList 的 subList 结果不可强转成 ArrayList，否则会抛出 ClassCastException 异常：java.util.RandomAccessSubList cannot be cast to java.util.ArrayList ;

> 说明：subList 返回的是 ArrayList 的内部类 SubList，并不是 ArrayList，而是 ArrayList 的一个视图，对于 SubList 子列表的所有操作最终会反映到原列表上。

3. **【强制】**在 subList 场景中，高度注意对原集合元素个数的修改，会导致子列表的遍历、增加、删除均产生 ConcurrentModificationException 异常。

4. **【强制】**使用集合转数组的方法，必须使用集合的 toArray(T[] array)，传入的是类型完全一样的数组，大小就是 list.size()。

```
反例：直接使用toArray无参方法存在问题，此方法返回值只能是Object[]类，若强转其它类型数组将出现ClassCastException错误。
正例：
List<String> list = new ArrayList<String>(2);
list.add("guan");
list.add("bao");
String[] array = new String[list.size()];
array = list.toArray(array);
说明：使用toArray带参方法，入参分配的数组空间不够大时，toArray方法内部将重新分配
内存空间，并返回新数组地址；如果数组元素大于实际所需，下标为[ list.size() ]的数组
元素将被置为null，其它数组元素保持原值，因此最好将方法入参数组大小定义与集合元素
个数一致。
```

5. **【强制】**使用工具类 Arrays.asList()把数组转换成集合时，不能使用其修改集合相关的方法，它的 add/remove/clear 方法会抛出 UnsupportedOperationException 异常。

> 说明：asList 的返回对象是一个 Arrays 内部类，并没有实现集合的修改方法。Arrays.asList 体现的是适配器模式，只是转换接口，后台的数据仍是数组。

```
String[] str = new String[] { "a", "b" };
List list = Arrays.asList(str);
第一种情况：list.add("c");运行时异常。
第二种情况：str[0]= "gujin";那么list.get(0)也会随之修改。
```

6. **【强制】**泛型通配符<? extends T>来接收返回的数据，此写法的泛型集合不能使用 add 方法。

> 说明：苹果装箱后返回一个<? extends Fruits>对象，此对象就不能往里加任何水果，包括苹果。

7. **【强制】**不要在 foreach 循环里进行元素的 remove/add 操作。remove 元素请使用 Iterator 方式，如果并发操作，需要对 Iterator 对象加锁。

```
反例：
List<String> a = new ArrayList<String>();
	a.add("1");
	a.add("2");
	for (String temp : a) {
		if("1".equals(temp)){
		a.remove(temp);
		}
	}
	说明：以上代码的执行结果肯定会出乎大家的意料，那么试一下把“1”换成“2”，会是同样的
	结果吗？
	正例：
	Iterator<String> it = a.iterator();
	while(it.hasNext()){
		String temp = it.next();
		if(删除元素的条件){
		it.remove();
	}
}
```

8. **【强制】**在 JDK7 版本以上，Comparator 要满足自反性，传递性，对称性，不然 Arrays.sort，Collections.sort 会报 IllegalArgumentException 异常。

> 说明：

> 1）自反性：x，y 的比较结果和 y，x 的比较结果相反。

> 2）传递性：x>y,y>z,则 x>z。

> 3）对称性：x=y,则 x,z 比较结果和 y，z 比较结果相同。

```
反例：下例中没有处理相等的情况，实际使用中可能会出现异常：
new Comparator<Student>() {
	@Override
	public int compare(Student o1, Student o2) {
		return o1.getId() > o2.getId() ? 1 : -1;
	}
}
```

9. *【推荐】*集合初始化时，尽量指定集合初始值大小。

> 说明：ArrayList 尽量使用 ArrayList(int initialCapacity)初始化。

10. *【推荐】*使用 entrySet 遍历 Map 类集合 KV，而不是 keySet 方式进行遍历。

> 说明：keySet 其实是遍历了 2 次，一次是转为 Iterator 对象，另一次是从 hashMap 中取出 key 所对应的 value。而 entrySet 只是遍历了一次就把 key 和 value 都放到了 entry 中，效率更高。如果是 JDK8，使用 Map.foreach 方法。

```
正例：values()返回的是V值集合，是一个list集合对象；keySet()返回的是K值集合，是一个Set集合对象；entrySet()返回的是K-V值组合集合。
```

11. *【推荐】*高度注意 Map 类集合 K/V 能不能存储 null 值的情况，如下表格：

| 集合类            | Key           | Value         | Super       | 说明       |
| :---------------- | :------------ | :------------ | :---------- | :--------- |
| Hashtable         | 不允许为 null | 不允许为 null | Dictionary  | 线程安全   |
| ConcurrentHashMap | 不允许为 null | 不允许为 null | AbstractMap | 分段锁技术 |
| TreeMap           | 不允许为 null | 允许为 null   | AbstractMap | 线程不安全 |
| HashMap           | 允许为 null   | 允许为 null   | AbstractMap | 线程不安全 |

```
反例：由于HashMap的干扰，很多人认为ConcurrentHashMap是可以置入null值，注意存储null值时会抛出NPE异常。
```

12. 【参考】合理利用好集合的有序性(sort)和稳定性(order)，避免集合的无序性(unsort)和不稳定性(unorder)带来的负面影响。

> 说明：稳定性指集合每次遍历的元素次序是一定的。有序性是指遍历的结果是按某种比较规则依次排列的。如：ArrayList 是 order/unsort；HashMap 是 unorder/unsort；TreeSet 是 order/sort。

13. 【参考】利用 Set 元素唯一的特性，可以快速对一个集合进行去重操作，避免使用 List 的 contains 方法进行遍历、对比、去重操作。

</td></tr></table>

### 并发处理

<table><tr><td bgcolor=#D1EEEE>

1. **【强制】**获取单例对象需要保证线程安全，其中的方法也要保证线程安全。

> 说明：资源驱动类、工具类、单例工厂类都需要注意。

2. **【强制】**创建线程或线程池时请指定有意义的线程名称，方便出错时回溯。

```
正例：
public class TimerTaskThread extends Thread {
	public TimerTaskThread(){
		super.setName("TimerTaskThread");  ...
}
```

3. **【强制】**线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。

> 说明：使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统资源的开销，解决资源不足的问题。如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。

4. **【强制】**线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。

> 说明：Executors 返回的线程池对象的弊端如下：

> 1）FixedThreadPool 和 SingleThreadPool:

> 允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。

> 2）CachedThreadPool 和 ScheduledThreadPool:

> 允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。

5. **【强制】**SimpleDateFormat 是线程不安全的类，一般不要定义为 static 变量，如果定义为 static，必须加锁，或者使用 DateUtils 工具类。

```
正例：注意线程安全，使用DateUtils。亦推荐如下处理：
private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
	@Override
	protected DateFormat initialValue() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
};
说明：如果是JDK8的应用，可以使用Instant代替Date，LocalDateTime代替Calendar，DateTimeFormatter代替Simpledateformatter，
官方给出的解释：simple beautiful strong immutable thread-safe。
```

6. **【强制】**高并发时，同步调用应该去考量锁的性能损耗。能用无锁数据结构，就不要用锁；能锁区块，就不要锁整个方法体；能用对象锁，就不要用类锁。

7. **【强制】**对多个资源、数据库表、对象同时加锁时，需要保持一致的加锁顺序，否则可能会造成死锁。

> 说明：线程一需要对表 A、B、C 依次全部加锁后才可以进行更新操作，那么线程二的加锁顺序也必须是 A、B、C，否则可能出现死锁。

8. **【强制】**并发修改同一记录时，避免更新丢失，要么在应用层加锁，要么在缓存加锁，要么在数据库层使用乐观锁，使用 version 作为更新依据。

> 说明：如果每次访问冲突概率小于 20%，推荐使用乐观锁，否则使用悲观锁。乐观锁的重试次数不得小于 3 次。

9. **【强制】**多线程并行处理定时任务时，Timer 运行多个 TimeTask 时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用 ScheduledExecutorService 则没有这个问题。

10. *【推荐】*使用 CountDownLatch 进行异步转同步操作，每个线程退出前必须调用 countDown 方法，线程执行代码注意 catch 异常，确保 countDown 方法可以执行，避免主线程无法执行至 countDown 方法，直到超时才返回结果。

> 说明：注意，子线程抛出异常堆栈，不能在主线程 try-catch 到。

11. *【推荐】*避免 Random 实例被多线程使用，虽然共享该实例是线程安全的，但会因竞争同一 seed 导致的性能下降。

> 说明：Random 实例包括 java.util.Random 的实例或者 Math.random()实例。

```
正例：在JDK7之后，可以直接使用API  ThreadLocalRandom，在 JDK7之前，可以做到每个线程一个实例。
```

12. *【推荐】*通过双重检查锁（double-checked locking）（在并发场景）实现延迟初始化的优化问题隐患(可参考 The "Double-Checked Locking is Broken" Declaration),推荐问题解决方案中较为简单一种（适用于 JDK5 及以上版本），将目标属性声明为 volatile 型。

```
反例：
class Foo {
private Helper helper = null;
	public Helper getHelper() {
	if (helper == null)  synchronized(this) {
		if (helper == null)
			helper = new Helper();
		}
		return helper;
	}
	// other functions and members...
}
```

13. 【参考】volatile 解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。如果是 count++操作，使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1);如果是 JDK8，推荐使用 LongAdder 对象，比 AtomicLong 性能更好（减少乐观锁的重试次数）。

14. 【参考】 HashMap 在容量不够进行 resize 时由于高并发可能出现死链，导致 CPU 飙升，在开发过程中注意规避此风险。

15. 【参考】ThreadLocal 无法解决共享对象的更新问题，ThreadLocal 对象建议使用 static 修饰。这个变量是针对一个线程内所有操作共有的，所以设置为静态变量，所有此类实例共享此静态变量，也就是说在类第一次被使用时装载，只分配一块存储空间，所有此类的对象(只要是这个线程内定义的)都可以操控这个变量。

</td></tr></table>

## ...

<table><tr><td bgcolor=#D1EEEE>

1. **【强制】**在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。

   > 说明：不要在方法体内定义：Pattern pattern = Pattern.compile(规则);

2. **【强制】**velocity 调用 POJO 类的属性时，建议直接使用属性名取值即可，模板引擎会自动按规范调用 POJO 的 getXxx()，如果是 boolean 基本数据类型变量（boolean 命名不需要加 is 前缀），会自动调用 isXxx()方法。

   > 说明：注意如果是 Boolean 包装类对象，优先调用 getXxx()的方法。

3. **【强制】**后台输送给页面的变量必须加\$!{var}——中间的感叹号。

   > 说明：如果 var=null 或者不存在，那么\${var}会直接显示在页面上。

4. **【强制】**注意 Math.random()这个方法返回是 double 类型，注意取值的范围 0≤x<1（能够取到零值，注意除零异常），如果想获取整数类型的随机数，不要将 x 放大 10 的若干倍然后取整，直接使用 Random 对象的 nextInt 或者 nextLong 方法。

5. **【强制】**获取当前毫秒数 System.currentTimeMillis();而不是 new Date().getTime();

   > 说明：如果想获取更加精确的纳秒级时间值，用 System.nanoTime()。在 JDK8 中，针对统计时间等场景，推荐使用 Instant 类。

6. *【推荐】*尽量不要在 vm 中加入变量声明、逻辑运算符，更不要在 vm 模板中加入任何复杂的逻辑。

7. *【推荐】*任何数据结构的构造或初始化，都应指定大小，避免数据结构无限增长吃光内存。

8. *【推荐】*对于“明确停止使用的代码和配置”，如方法、变量、类、配置文件、动态配置属性等要坚决从程序中清理出去，避免造成过多垃圾。

</td></tr></table>
