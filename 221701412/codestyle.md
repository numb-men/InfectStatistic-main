## 代码风格

- ###缩进

    - 缩进采用4个空格，禁止使用tab字符。
    > 说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert spaces for tabs。
    
    ```
    正例：
    public static void main(String args[]) {
	    //缩进4个空格
	    String say = "hello";
	    //运算符的左右必须有一个空格
	    int flag = 0;
	    //关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
	    if (flag == 0) {
		    System.out.println(say);
	    }
	    //左大括号前加空格且不换行；左大括号后换行
	    if (flag == 1) {
		    System.out.println("world");
		    //右大括号前换行，右大括号后有else，不用换行
	    } else {
		    System.out.println("ok");
		    //在右大括号后直接结束，则必须换行
	    }
    }
    ```
    
- ###变量命名

    - 1.代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
    
    ```
    反例： _name / __ name / $Object / name _ / name$ / Object$
    ```
 
    - 2.代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
    > 说明：正确的英文拼写和语法可以让阅读者易于理解，避免歧义。 注意，即使纯拼音命名方式也要避免采用。
    
    ```    
    反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
        
    正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。
    ```
        
    - 3.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    
    ``` 
    正例： localValue / getHttpMessage() /  inputUserId
    ``` 
        
    - 4.中括号是数组类型的一部分，数组定义如下：String[] args;
    
    ``` 
    反例：请勿使用String  args[]的方式来定义。
    ``` 
    
    - 5.POJO类中布尔类型的变量，都不要加is，否则部分框架解析会引起序列化错误。
    
    ``` 
    反例：定义为基本数据类型boolean isSuccess；的属性，它的方法也是isSuccess()，RPC框架在反向解析的时候，“以为”对应的属性名称是success，导致属性获取不到，进而抛出异常。
    ```
  
    - 6.杜绝完全不规范的缩写，避免望文不知义。
    
    ```     
    反例： AbstractClass“缩写”命名成AbsClass；condition“缩写”命名成 condi，此类随意缩写严重降低了代码的可阅读性。
    ``` 
  
- ###每行最多字符数

    - 单行字符数限制不超过 120个，超出需要换行，换行时遵循如下原则：
    
        - 第二行相对第一行缩进 4个空格，从第三行开始，不再继续缩进，参考示例。
        
        - 运算符与下文一起换行。
        
        - 在多个参数超长，逗号后进行换行。
        
        - 在多个参数超长，逗号后进行换行。
        
        ```
         //正例：
          StringBuffer sb = new StringBuffer();
          //超过120个字符的情况下，换行缩进4个空格，并且方法前的点符号一起换行
          sb.append("zi").append("xin")...
          	.append("huang")...
          	.append("huang")...
          	.append("huang");
         //反例：
          StringBuffer sb = new StringBuffer();
          //超过120个字符的情况下，不要在括号前换行
          sb.append("zi").append("xin")...append
          	("huang");
          //参数很多的方法调用可能超过120个字符，不要在逗号前换行
          method(args1, args2, args3, ...
          	, argsX);
       ```

- ###函数最大行数

    - 以80、200、500为规定界限，超过80行的函数即为超大函数，即进行拆分，规定最大行数为80-200行，拆分时遵循如下原则：
    
        - 每一个代码块都可以封装为一个函数
        
        - 每一个循环体都可以封装为一个函数
        
        - 每一个条件体都可以封装为一个函数

- ###函数、类命名

    - 1.类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。
    
    ```
    正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
    反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
    ```
  
    - 2.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
    
    ```
    正例： localValue / getHttpMessage() /  inputUserId
    ```

    - 3.抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。
    
    - 4.杜绝完全不规范的缩写，避免望文不知义。
    
    ```
    反例： AbstractClass“缩写”命名成AbsClass；condition“缩写”命名成 condi，此类随意缩写严重降低了代码的可阅读性。
    ```
  
    - 5.如果使用到了设计模式，建议在类名中体现出具体模式。
    > 说明：将设计模式体现在名字中，有利于阅读者快速理解架构设计思想。

    ```
    正例：public class OrderFactory;
    public class LoginProxy;
    public class ResourceObserver;
    ```
    
    - 6.接口类中的方法和属性不要加任何修饰符号（public也不要加），保持代码的简洁性，并加上有效的Javadoc注释。尽量不要在接口里定义变量，如果一定要定义变量，肯定是与接口方法相关，并且是整个应用的基础常量。
    
    ```
    正例：接口方法签名：void f();
    接口基础常量表示：String COMPANY = "alibaba";
    反例：接口方法定义：public abstract void f();
    说明：JDK8中接口允许有默认实现，那么这个default方法，是对所有实现类都有价值的默认实现。
    ```
  
    - 7.接口和实现类的命名有两套规则：
    
    - 8.对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别。
    
    ```
    正例：CacheServiceImpl实现CacheService接口。
    ```
    - 9.如果是形容能力的接口名称，取对应的形容词做接口名（通常是–able的形式）。
    ```
    正例：AbstractTranslator实现 Translatable。
    ```
    
    - 10.枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。
    > 说明：枚举其实就是特殊的常量类，且构造方法被默认强制是私有。
    
    ```
    正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKNOWN_REASON。
    ```
    
- ###常量

    - 1.不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。
    
    ```
    反例： String key="Id#taobao_"+tradeId；
    cache.put(key,  value);
    ```
  
    - 2.long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解。
    > 说明：Long a = 2l;写的是数字的21，还是Long型的2?
    
    - 3.不要使用一个常量类维护所有常量，应该按常量功能进行归类，分开维护。如：缓存相关的常量放在类：CacheConsts下；系统配置相关的常量放在类：ConfigConsts下。
    > 说明：大而全的常量类，非得使用查找功能才能定位到修改的常量，不利于理解和维护。
    
    - 4.常量的复用层次有五层：跨应用共享常量、应用内共享常量、子工程内共享常量、包内共享常量、类内共享常量。
    
        - 跨应用共享常量：放置在二方库中，通常是client.jar中的constant目录下。
        
        - 应用内共享常量：放置在一方库的modules中的constant目录下。
    
        ```
        反例：易懂变量也要统一定义成应用内共享常量，两位攻城师在两个类中分别定义了表示“是”的变量：
        类A中：public  static final String YES = "yes";
        类B中：public  static final String YES = "y";
        A.YES.equals(B.YES)，预期是true，但实际返回为false，导致产生线上问题。
        ```
    
        - 子工程内部共享常量：即在当前子工程的constant目录下。
        
        - 包内共享常量：即在当前包下单独的constant目录下。
       
        - 类内共享常量：直接在类内部private  static  final定义。
    
    - 5.如果变量值仅在一个范围内变化用Enum类。如果还带有名称之外的延伸属性，必须使用Enum类，下面正例中的数字就是延伸信息，表示星期几。
    
    ```
    正例：publicEnum{MONDAY(1),TUESDAY(2),WEDNESDAY(3),THURSDAY(4),FRIDAY(5),SATURDAY(6), SUNDAY(7);}
    ```

- ###空行规则

    - 1.大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
    
        - 左大括号前不换行。
      
        - 左大括号后换行。
      
        - 右大括号前换行。
      
        - 右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。
    
    ```
        正例：
        public static void main(String args[]) {
        	//缩进4个空格
        	String say = "hello";
        	//运算符的左右必须有一个空格
        	int flag = 0;
        	//关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
        	if (flag == 0) {
        		System.out.println(say);
        	}
        	//左大括号前加空格且不换行；左大括号后换行
        	if (flag == 1) {
        		System.out.println("world");
        		//右大括号前换行，右大括号后有else，不用换行
        	} else {
        		System.out.println("ok");
        		//在右大括号后直接结束，则必须换行
        	}
        }
     ```
    
    - 2.方法体内的执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义之间插入一个空行。相同业务逻辑和语义之间不需要插入空行。
    > 说明：没有必要插入多行空格进行隔开。

- ###注释规则

    - 1.类、类属性、类方法的注释必须使用Javadoc规范，使用/**内容*/格式，不得使用//xxx方式。
    > 说明：在IDE编辑窗口中，Javadoc方式会提示相关注释，生成Javadoc可以正确输出相应注释；在IDE中，工程调用方法时，不进入方法即可悬浮提示方法、参数、返回值的意义，提高阅读效率。
    
    - 2.所有的抽象方法（包括接口中的方法）必须要用Javadoc注释、除了返回值、参数、异常说明外，还必须指出该方法做什么事情，实现什么功能。
    > 说明：对子类的实现要求，或者调用注意事项，请一并说明。
    
    - 3.所有的类都必须添加创建者信息。
    
    - 4.方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/* */注释，注意与代码对齐。
    
    - 5.所有的枚举类型字段必须要有注释，说明每个数据项的用途。
    
    - 6.与其“半吊子”英文来注释，不如用中文注释把问题说清楚。专有名词与关键字保持英文原文即可。
    ```
    反例：“TCP连接超时”解释成“传输控制协议连接超时”，理解反而费脑筋。
    ```
    - 7.代码修改的同时，注释也要进行相应的修改，尤其是参数、返回值、异常、核心逻辑等的修改。
    > 说明：代码与注释更新不同步，就像路网与导航软件更新不同步一样，如果导航软件严重滞后，就失去了导航的意义。
    
    - 8.注释掉的代码尽量要配合说明，而不是简单的注释掉。
    > 说明：代码被注释掉有两种可能性：
    
    > 1）后续会恢复此段代码逻辑。
    
    > 2）永久不用。
    
    > 前者如果没有备注信息，难以知晓注释动机。后者建议直接删掉（代码仓库保存了历史代码）。
    
    - 9.对于注释的要求：
    
        - 第一、能够准确反应设计思想和代码逻辑；
    
        - 第二、能够描述业务含义，使别的程序员能够迅速了解到代码背后的信息。完全没有注释的大段代码对于阅读者形同天书，注释是给自己看的，即使隔很长时间，也能清晰理解当时的思路；注释也是给继任者看的，使其能够快速接替自己的工作。
    
    - 10.好的命名、代码结构是自解释的，注释力求精简准确、表达到位。避免出现注释的一个极端：过多过滥的注释，代码的逻辑一旦修改，修改注释是相当大的负担。
    
    ```
    反例：
    // put elephant into fridge
    put(elephant, fridge);
    方法名put，加上两个有意义的变量名elephant和fridge，已经说明了这是在干什么，语义清晰的代码不需要额外的注释。
    ```
  
    - 11.特殊注释标记，请注明标记人与标记时间。注意及时处理这些标记，通过标记扫描，经常清理此类标记。线上故障有时候就是来源于这些标记处的代码。
    
        - 待办事宜（TODO）:（标记人，标记时间，[预计处理时间]）表示需要实现，但目前还未实现的功能。这实际上是一个Javadoc的标签，目前的Javadoc还没有实现，但已经被广泛使用。只能应用于类，接口和方法（因为它是一个Javadoc标签）。

- ###操作符前后空格

    - 1.左括号和后一个字符之间不出现空格；同样，右括号和前一个字符之间也不出现空格。详见第5条下方正例提示。
    
    - 2.if/for/while/switch/do等保留字与左右括号之间都必须加空格。
    
    - 3.任何运算符左右必须加一个空格。
    > 说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert   spaces for tabs。

    c
    正例：（涉及1-3点）
    public static void main(String args[]) {
    	//缩进4个空格
    	String say = "hello";
    	//运算符的左右必须有一个空格
    	int flag = 0;
    	//关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
    	if (flag == 0) {
    		System.out.println(say);
    	}
    	//左大括号前加空格且不换行；左大括号后换行
    	if (flag == 1) {
    		System.out.println("world");
    		//右大括号前换行，右大括号后有else，不用换行
    	} else {
    		System.out.println("ok");
    		//在右大括号后直接结束，则必须换行
    	}
    }
    ```

    - 4.方法参数在定义和传入时，多个参数逗号后边必须加空格。
    
    ```
    正例：下例中实参的"a",后边必须要有一个空格。
    method("a", "b", "c");
    ```

- ###其他规则

    - ####Service/DAO层方法命名规约
    
        1. 获取单个对象的方法用get做前缀。
        2. 获取多个对象的方法用list做前缀。
        3. 获取统计值的方法用count做前缀。
        4. 插入的方法用save（推荐）或insert做前缀。
        5. 删除的方法用remove（推荐）或delete做前缀。
        6. 修改的方法用update做前缀。
    
    - ####领域模型命名规约
    
        1. 数据对象：xxxDO，xxx即为数据表名。
        2. 数据传输对象：xxxDTO，xxx为业务领域相关的名称。
        3. 展示对象：xxxVO，xxx一般为网页名称。
        4. POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。

    - 1.IDE的text file encoding设置为UTF-8; IDE中文件的换行符使用Unix格式，不要使用windows格式。 9.*【推荐】*没有必要增加若干空格来使某一行的字符与上一行的相应字符对齐。
    
    ```
    正例：
    int a = 3;
    long b = 4L;
    float c = 5F;
    StringBuffer sb = new StringBuffer();
    说明：增加sb这个变量，如果需要对齐，则给a、b、c都要增加几个空格，在变量比较多的
    ```
    - 2.在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
    > 说明：不要在方法体内定义：Pattern pattern = Pattern.compile(规则);
    
    - 3.velocity调用POJO类的属性时，建议直接使用属性名取值即可，模板引擎会自动按规范调用POJO的getXxx()，如果是boolean基本数据类型变量（boolean命名不需要加is前缀），会自动调用isXxx()方法。
    > 说明：注意如果是Boolean包装类对象，优先调用getXxx()的方法。
    
    - 4.后台输送给页面的变量必须加$!{var}——中间的感叹号。
    > 说明：如果var=null或者不存在，那么${var}会直接显示在页面上。
    
    - 5.注意 Math.random()这个方法返回是double类型，注意取值的范围   0≤x<1（能够取到零值，注意除零异常），如果想获取整数类型的随机数，不要将x放大10的若干倍然后取整，直接使用Random对象的nextInt或者nextLong方法。
    
    - 6.获取当前毫秒数System.currentTimeMillis();而不是new    Date().getTime();
    > 说明：如果想获取更加精确的纳秒级时间值，用System.nanoTime()。在JDK8中，针对统计时间等场景，推荐使用Instant类。
    
    - 7.尽量不要在vm中加入变量声明、逻辑运算符，更不要在vm模板中加入任何复杂的逻辑。
    
    - 8.任何数据结构的构造或初始化，都应指定大小，避免数据结构无限增长吃光内存。
    
    - 9.对于“明确停止使用的代码和配置”，如方法、变量、类、配置文件、动态配置属性等要坚决从程序中清理出去，避免造成过多垃圾。
    
    
    
    
    
    
    
    
    
    
    
    
