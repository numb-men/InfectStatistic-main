##代码风格
### 缩进
- 1.用四个半角空格进行缩进，或者使用四个空格长度的TAB键
- 2.运算符左右各有一个空格，如： int a = 0;
- 3.关键词与小括号间有一个空格，如：while (flag == 0);
- 4.如果是大括号内为空，则简洁地写成{}即可
- 5.左大括号后跟一个空格并且不换行，else if和else要重起一行写，右大括号必须换行
    > 正例：
    public class text (int flag) {
    &ensp;&ensp;&ensp;&ensp;if (flag == 1) {
    &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;System.out.println("hello!");
    &ensp;&ensp;&ensp;&ensp;}
    &ensp;&ensp;&ensp;&ensp;else {
    &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;System.out.println("error!");
    &ensp;&ensp;&ensp;&ensp;}
    }

###变量命名
- 1.代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
    > 反例： _name / __name / $Object / name_ / name$ / Object$
- 2.代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
    > 反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。
- 3.成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    > 正例：localValue / getHttpMessage() /  inputUserId
- 4.常量命名全部大写，单词间用下划线隔开，命名尽量表达出语义
    > 正例： MAX_STOCK_COUNT
      反例： MAX_COUNT
- 5.杜绝完全不规范的缩写，避免望文不知义。
    > 反例： AbstractClass“缩写”命名成AbsClass；condition“缩写”命名成 condi，此类随意缩写严重降低了代码的可阅读性。

###每行最多字符数
- 1.单行字符不超过100个，超出的需要换行，且有以下规定：
    - 运算符与下文一起换行
    - 参数过多导致需要换行时，应将逗号后文换行
    - 换行后，新一行相对原来那行缩进4个空格
    > 正例：
      int func (int a, int b, int c,...,
&ensp;&ensp;&ensp;&ensp;int d, int f);
      if ((a == b) && ... &&(c
&ensp;&ensp;&ensp;&ensp;== d));

###函数最大行数
- 以100行为界限，超过100行的函数需要拆分为多个函数

###函数、类命名
- 1.类名使用UpperCamelCase风格，必须遵从驼峰形式，除了领域模型的相关命名DO / BO / DTO / VO等。
    > 正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
- 2.方法名、参数名都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    > 正例： localValue / getHttpMessage() /  inputUserId
- 3.抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。
- 4.杜绝完全不规范的缩写，避免望文不知义。
    > 反例： AbstractClass“缩写”命名成AbsClass；condition“缩写”命名成 condi，此类随意缩写严重降低了代码的可阅读性。
- 5.如果使用到了设计模式，建议在类名中体现出具体模式。
    > 正例：public class OrderFactory;
public class LoginProxy;
public class ResourceObserver;
- 6.接口类中的方法和属性不要加任何修饰符号（public也不要加），保持代码的简洁性，并加上有效的Javadoc注释。尽量不要在接口里定义变量，如果一定要定义变量，肯定是与接口方法相关，并且是整个应用的基础常量。
    > 正例：接口方法签名：void f();
接口基础常量表示：String COMPANY = "alibaba";
反例：接口方法定义：public abstract void f();
说明：JDK8中接口允许有默认实现，那么这个default方法，是对所有实现类都有价值的默认实现。
- 7.枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。
    > 正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKNOWN_REASON。
- 8.对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别。
    > 正例：CacheServiceImpl实现CacheService接口。
- 9.如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able的形式）。
    > 正例：AbstractTranslator实现 Translatable。

###常量
- 1.不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。
    > 反例： String key="Id#taobao_"+tradeId；
cache.put(key,  value);
- 2.long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解。
    > 说明：Long a = 2l;写的是数字的21，还是Long型的2?
- 3.不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护。如：缓存相关的常量放在类：CacheConsts下；系统配置相关的常量放在类：ConfigConsts下。
    >说明：大而全的常量类，非得使用查找功能才能定位到修改的常量，不利于理解和维护。
- 4.常量的复用层次有五层：跨应用共享常量、应用内共享常量、子工程内共享常量、包内共享常量、类内共享常量。
    - 跨应用共享常量：放置在二方库中，通常是client.jar中的constant目录下。
    - 应用内共享常量：放置在一方库的modules中的constant目录下。
    > 反例：易懂变量也要统一定义成应用内共享常量，两位攻城师在两个类中分别定义了表示“是”的变量：
类A中：public  static final String YES = "yes";
类B中：public  static final String YES = "y";
A.YES.equals(B.YES)，预期是true，但实际返回为false，导致产生线上问题。

    - 子工程内部共享常量：即在当前子工程的constant目录下。
    - 包内共享常量：即在当前包下单独的constant目录下。
    - 类内共享常量：直接在类内部private static final定义。
- 5.如果变量值仅在一个范围内变化用Enum类。如果还带有名称之外的延伸属性，必须使用Enum类，下面正例中的数字就是延伸信息，表示星期几。
    > 正例：publicEnum {MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);}

###空行规则
- 1.大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
    - 左大括号前不换行。
    - 左大括号后换行。
    - 右大括号前换行。
    - 右大括号后还有else等代码则换行。
    - 表示终止右大括号后必须换行。
- 2.类中不同方法间或者方法中不同逻辑块间可插入一个空行

###注释规则
- 1.类、类属性、类方法的注释必须使用Javadoc规范，使用/*内容/格式，不得使用//xxx方式。
    > 说明：在IDE编辑窗口中，Javadoc方式会提示相关注释，生成Javadoc可以正确输出相应注释；在IDE中，工程调用方法时，不进入方法即可悬浮提示方法、参数、返回值的意义，提高阅读效率。
- 2.所有的抽象方法（包括接口中的方法）必须要用Javadoc注释、除了返回值、参数、异常说明外，还必须指出该方法做什么事情，实现什么功能。
    > 说明：对子类的实现要求，或者调用注意事项，请一并说明。
- 3.方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/ */注释，注意与代码对齐。
- 4.所有的枚举类型字段必须要有注释，说明每个数据项的用途。
- 5.用中文注释把问题说清楚。专有名词与关键字保持英文原文即可。
    > 反例：“TCP连接超时”解释成“传输控制协议连接超时”，理解反而费脑筋。
- 6.代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑等的修改。
    > 说明：代码与注释更新不同步，就像路网与导航软件更新不同步一样，如果导航软件严重滞后，就失去了导航的意义。
- 7.注释掉的代码尽量要配合说明，而不是简单的注释掉。
    > 说明：代码被注释掉有两种可能性：
    1）后续会恢复此段代码逻辑。
    2）永久不用。
    前者如果没有备注信息，难以知晓注释动机。后者建议直接删掉（代码仓库保存了历史代码）。
- 8.对于注释的要求：
    - 第一、能够准确反应设计思想和代码逻辑；
    - 第二、能够描述业务含义，使别的程序员能够迅速了解到代码背后的信息。                    完全没有注释的大段代码对于阅读者形同天书，注释是给自己看的，即使隔很长时间，也能清晰理解当时的思路；注释也是给继任者看的，使其能够快速接替自己的工作。
- 9.好的命名、代码结构是自解释的，注释力求精简准确、表达到位。避免出现注释的一个极端：过多过滥的注释，代码的逻辑一旦修改，修改注释是相当大的负担。
    > 反例：
// put elephant into fridge
put(elephant, fridge);
方法名put，加上两个有意义的变量名elephant和fridge，已经说明了这是在干什么，语义清晰的代码不需要额外的注释。
- 10.特殊注释标记，请注明标记人与标记时间。注意及时处理这些标记，通过标记扫描，经常清理此类标记。线上故障有时候就是来源于这些标记处的代码。
    - 待办事宜（TODO）:（标记人，标记时间，[预计处理时间]）表示需要实现，但目前还未实现的功能。这实际上是一个Javadoc的标签，目前的Javadoc还没有实现，但已经被广泛使用。只能应用于类，接口和方法（因为它是一个Javadoc标签）。
    - 错误，不能工作（FIXME）:（标记人，标记时间，[预计处理时间]）在注释中用FIXME标记某代码是错误的，而且不能工作，需要及时纠正的情况。

###操作符前后空格
- 1.if/for/while/switch/do等保留字与左右括号之间都必须加空格。
- 2.运算符左右加一个空格。
- 3.左右括号与相邻字符之间均不加空格。
- 4.多个参数的定义与传入时，逗号后必须加空格。

###其他规则
- 1.在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
    > 说明：不要在方法体内定义：Pattern pattern = Pattern.compile(规则);
- 2.velocity调用POJO类的属性时，建议直接使用属性名取值即可，模板引擎会自动按规范调用POJO的getXxx()，如果是boolean基本数据类型变量（boolean命名不需要加is前缀），会自动调用isXxx()方法。
    > 说明：注意如果是Boolean包装类对象，优先调用getXxx()的方法。
- 3.后台输送给页面的变量必须加$!{var}——中间的感叹号。
    > 说明：如果var=null或者不存在，那么${var}会直接显示在页面上。
- 4.获取当前毫秒数System.currentTimeMillis();而不是new Date().getTime();
    > 说明：如果想获取更加精确的纳秒级时间值，用System.nanoTime()。在JDK8中，针对统计时间等场景，推荐使用Instant类。
- 5.任何数据结构的构造或初始化，都应指定大小，避免数据结构无限增长吃光内存。
- 6.尽量不要在vm中加入变量声明、逻辑运算符，更不要在vm模板中加入任何复杂的逻辑。
- 7.对于“明确停止使用的代码和配置”，如方法、变量、类、配置文件、动态配置属性等要坚决从程序中清理出去，避免造成过多垃圾。
