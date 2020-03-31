# Java代码规范（参照阿里巴巴Java开发手册）
## 缩进
1.缩进采用4个空格，禁止使用tab字符。
> 说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert   spaces for tabs。
## 变量命名
1.代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束。
```
反例： _name / __name / $Object / name_ / name$ / Object$
```
2.代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式。
```
反例： DaZhePromotion [打折] / getPingfenByName()  [评分] / int某变量 = 3
正例： alibaba / taobao / youku / hangzhou等国际通用的名称，可视同英文。
```
3.成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
```
正例： localValue / getHttpMessage() /  inputUserId
```
4.中括号是数组类型的一部分，数组定义如下：String[]   args;
```
反例：请勿使用String  args[]的方式来定义。
```
5.枚举类名建议带上Enum后缀，枚举成员名称需要全大写，单词间用下划线隔开。
> 说明：枚举其实就是特殊的常量类，且构造方法被默认强制是私有。

```
正例：枚举名字：DealStatusEnum，成员名称：SUCCESS / UNKNOWN_REASON。
```
## 每行最多字符数
1.单行字符数限制不超过  120个，超出需要换行，换行时遵循如下原则：
* 第二行相对第一行缩进   4个空格，从第三行开始，不再继续缩进，参考示例。
* 运算符与下文一起换行。
* 方法调用的点符号与下文一起换行。
* 在多个参数超长，逗号后进行换行。
* 在括号前不要换行，见反例。
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
## 函数最大行数
1.单个方法的总行数不超过80行。
## 函数、类命名
1.类名使用UpperCamelCase风格，必须遵从驼峰形式
```
正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
```
2.方法名、参数名都统一使用lowerCamelCase风格，必须遵从驼峰形式。
```
正例： localValue / getHttpMessage() /  inputUserId
```
3.抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。

4.包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形式。
```
正例：应用工具类包名为com.alibaba.open.util、类名为MessageUtils（此规则参考spring的框架结构）
```
## 常量
1.常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
```
正例： MAX_STOCK_COUNT
反例： MAX_COUNT
```
2.不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。
```
反例： String key="Id#taobao_"+tradeId；
cache.put(key,  value);
```
## 空行规则
1.大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
* 左大括号前不换行。
* 左大括号后换行。
* 右大括号前换行。
* 右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。

2.没有必要增加若干空格来使某一行的字符与上一行的相应字符对齐。
```
正例：
int a = 3;
long b = 4L;
float c = 5F;
StringBuffer sb = new StringBuffer();
说明：增加sb这个变量，如果需要对齐，则给a、b、c都要增加几个空格，在变量比较多的
```
## 注释规则
1.类、类属性、类方法的注释必须使用Javadoc规范，使用/**内容*/格式，不得使用//xxx方式。
> 说明：在IDE编辑窗口中，Javadoc方式会提示相关注释，生成Javadoc可以正确输出相应注释；在IDE中，工程调用方法时，不进入方法即可悬浮提示方法、参数、返回值的意义，提高阅读效率。

2.所有的类都必须添加创建者信息。

3.方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/* */注释，注意与代码对齐。

4.与其“半吊子”英文来注释，不如用中文注释把问题说清楚。专有名词与关键字保持英文原文即可。
```
反例：“TCP连接超时”解释成“传输控制协议连接超时”，理解反而费脑筋。
```
## 操作符前后空格
1.左括号和后一个字符之间不出现空格；同样，右括号和前一个字符之间也不出现空格。

2.if/for/while/switch/do等保留字与左右括号之间都必须加空格。

3.任何运算符左右必须加一个空格。
> 说明：运算符包括赋值运算符=、逻辑运算符&&、加减乘除符号、三目运行符等。

4.方法参数在定义和传入时，多个参数逗号后边必须加空格。
```
正例：下例中实参的"a",后边必须要有一个空格。
method("a", "b", "c");
```
## 其他规则
1.IDE的text   file encoding设置为UTF-8;  IDE中文件的换行符使用Unix格式，不要使用windows格式。

2.在if/else/for/while/do语句中必须使用大括号，即使只有一行代码，避免使用下面的形式：if (condition) statements;