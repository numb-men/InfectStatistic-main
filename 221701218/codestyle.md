## 缩进：
>缩进采用4个空格，禁止使用tab字符。
说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert spaces for tabs。

>正例：（涉及1-5点）
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

## 变量命名
- 代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
>反例： _name / __name / $Object / name_ / name$ / Object$

- 代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
说明：正确的英文拼写和语法可以让阅读者易于理解，避免歧义。 注意，即使纯拼音命名方式也要避免采用。
>反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。

- 方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
>正例： localValue / getHttpMessage() /  inputUserId

## 每行最多字符数：
>单行字符数限制不超过 120个，超出需要换行，换行时遵循如下原则：
-    第二行相对第一行缩进 4个空格，从第三行开始，不再继续缩进，参考示例。
-    运算符与下文一起换行。
-    方法调用的点符号与下文一起换行。
-    在多个参数超长，逗号后进行换行。
-    在括号前不要换行，见反例。

>正例：
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

## 函数最大行数：
>单个方法的总行数不超过100行。

## 函数、类命名：
方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
>正例： localValue / getHttpMessage() /  inputUserId

类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。
>正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion

## 常量
>常量命名全部大写，单词间用下划线隔开

## 空行规则
大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
>-    左大括号前不换行。
-    左大括号后换行。
-    右大括号前换行。
-    右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。

## 注释规则
>类、类属性、类方法的注释必须使用Javadoc规范，使用/*内容/格式，不得使用//xxx方式。
方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/ */注释，注意与代码对齐。

## 操作符前后空格
>任何运算符左右必须加一个空格。
说明：运算符包括赋值运算符=、逻辑运算符&&、加减乘除符号、三目运行符等。

## 其他规则
>-    1.在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
说明：不要在方法体内定义：Pattern pattern = Pattern.compile(规则);
-    2.velocity调用POJO类的属性时，建议直接使用属性名取值即可，模板引擎会自动按规范调用POJO的getXxx()，如果是boolean基本数据类型变量（boolean命名不需要加is前缀），会自动调用isXxx()方法。
说明：注意如果是Boolean包装类对象，优先调用getXxx()的方法。
-    3.后台输送给页面的变量必须加$!{var}——中间的感叹号。
说明：如果var=null或者不存在，那么${var}会直接显示在页面上。
-    4.注意 Math.random()这个方法返回是double类型，注意取值的范围 0≤x<1（能够取到零值，注意除零异常），如果想获取整数类型的随机数，不要将x放大10的若干倍然后取整，直接使用Random对象的nextInt或者nextLong方法。
-    5.获取当前毫秒数System.currentTimeMillis();而不是new Date().getTime();
说明：如果想获取更加精确的纳秒级时间值，用System.nanoTime()。在JDK8中，针对统计时间等场景，推荐使用Instant类。
-    6.尽量不要在vm中加入变量声明、逻辑运算符，更不要在vm模板中加入任何复杂的逻辑。
-    7.任何数据结构的构造或初始化，都应指定大小，避免数据结构无限增长吃光内存。
-    8.对于“明确停止使用的代码和配置”，如方法、变量、类、配置文件、动态配置属性等要坚决从程序中清理出去，避免造成过多垃圾。