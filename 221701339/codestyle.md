

## 代码风格

- 缩进与换行

  1. 缩进采用4个空格，禁用tab字符。

  2. 换行时缩进规则

     - 第二行相对第一行缩进 4个空格，此后断行和第二行缩进相同，例如：

       ```java
       sb.append("zi").append("xin")...
       	.append("huang")
       	.append("huang")
       	.append("huang");
       ```

     - 换行字符：LF(Unix格式)

     - 换行字符位置规则：

       1. 方法调用符"."或者其他运算符位于行首。
       2. 参数分隔符","位于行末。
       3. 括号出现于行尾附近。

       示例：

       ```java
       sb.append("zi").append("xin")...
       	.append("huang")
       	.append("huang")
       	.append("huang");
       Person person = new Person("Mike",
       	"USA",
       	"120xxxxx",
           ...
       	18);
       String str="HHHHHHHHHHHHH"
       	+ "EEEEEEEEEEEEE"
       	+ "LLLLLLLLLLLLL"
       	+ "LLLLLLLLLLLLL"
       	+ "OOOOOOOOOOOOO";
       ```

- 变量命名

  1. 变量名不以下划线或美元符号开始，也不以下划线或美元符号结束。
  2. 成员变量、局部变量使用lowerCamelCase风格。

  

- 每行最多字符数

  1. ​	单行字符数限制不超过 120个(非空白符)，换行规则参见缩进与换行。

- 函数最大行数

  1. 单个方法的总行数不超过80行。

  

- 函数、类命名

  1. 函数命名

     1. 函数命名使用lowerCamelCase风格。
     2. Service/DAO层方法命名规约：
        1. 获取单个对象的方法用get做前缀。
        2. 获取多个对象的方法用list做前缀。
        3. 获取统计值的方法用count做前缀。
        4. 插入的方法推荐用save或insert做前缀。
        5. 删除的方法推荐用remove或delete做前缀。
        6. 修改的方法用update做前缀。

  2. 类命名

     1. 类命名使用lowerCamelCase风格，领域模型的相关命名除外，例如：

        ```java
        class EmployeeService {
            ...
        }
        
        class XmlService {
            ...
        }
        ```

     2. 抽象类命名使用Abstract或Base开头；异常类命名使用Exception结尾；测试类命名以它要测试的类的名称开始，以Test结尾。

     3. 对于Service和DAO类，内部的实现类用接口名+Impl的后缀与接口区别。

     

- 常量

  1. 常量命名全部大写，单词间用下划线隔开

- 空行规则

  1. 方法体内的根据变量定义，逻辑部分进行适当的插入一行空行，进行逻辑部分划分。
  2. 执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义之间插入一个空行。
  3. 不以分隔为目的空行插入多行（2行以上）空行。

  

- 注释规则

  1. 类、类属性、类方法的注释时使用Javadoc规范
  2. 方法内部单行注释使用“//”位于要说明内容上方；多行注释使用“/ * */”位于要说明内容上方。
  3. 所有的类都必须添加创建者信息

- 操作符前后空格

  1. 多元运算符左右必须加一个空格来分隔操作数

     

- 其他规则

     1. 左大括号前不换行，左大括号后换行。

     2. 右大括号前换行，右大括号后还有else等关键字不换行；表示终止右大括号后换行。

     3. 如果大括号内没有内容可以不用换行，把左右大括号放在一行即可

     4. if/for/while/switch/do等保留字与左右括号之间加一个空格。

     5. 左括号和后一个字符之间不出现空格；右括号和前一个字符之间也不出现空格。

     6. 运算符和操作数之间用一个空格分隔，除一元运算符之外。

        示例：

        ```java
        public static void main(String args[]) {
        	String say = "hello";
        	int flag = 0;
        	boolean ready=false;
        	if (flag == 0 && !ready) {
        		System.out.println(say);
        	}
        	if (flag == 1) {
        		System.out.println("world");
        	} else {
        		System.out.println("ok");
        	}
        }
        ```

        

