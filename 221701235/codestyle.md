## 代码风格

* **【强制】**缩进采用4个空格，禁止使用tab字符。
>说明：如果使用tab缩进，必须设置1个tab为4个空格。IDEA设置tab为4个空格时，请勿勾选Use tab character；而在eclipse中，必须勾选insert spaces for tabs。

* **【强制】**方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式。
>正例： localValue / getHttpMessage() /  inputUserId

* **【强制】**单行字符数限制不超过120个，超出需要换行，换行时遵循如下原则：
1. 第二行相对第一行缩进4个空格，从第三行开始，不再继续缩进，参考示例。
2. 运算符与下文一起换行。
3. 方法调用的点符号与下文一起换行。
4. 在多个参数超长，逗号后进行换行。
5. 在括号前不要换行，见反例。
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
* **【强制】**函数最大行数不超过200行

* **【强制】**类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO / DTO / VO等。
>正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion

* **【强制】**常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
>正例： MAX_STOCK_COUNT
反例： MAX_COUNT

* **【强制】**两个方法之间使用一个空行

* **【强制】**方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用/* */注释，注意与代码对齐。

* **【强制】**任何运算符左右必须加一个空格。
>说明：运算符包括赋值运算符=、逻辑运算符&&、加减乘除符号、三目运行符等。

* 其他规则
**【强制】** if/for/while/switch/do等保留字与左右括号之间都必须加空格。
**【强制】**左括号和后一个字符之间不出现空格；同样，右括号和前一个字符之间也不出现空格。
**【强制】**大括号的使用约定。如果是大括号内为空，则简洁地写成{}即可，不需要换行；如果是非空代码块则：
1. 左大括号前不换行。
2. 左大括号后换行。
3. 右大括号前换行。
4. 右大括号后还有else等代码则不换行；表示终止右大括号后必须换行。