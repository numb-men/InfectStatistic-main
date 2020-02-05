## 代码风格

- 缩进

  缩进采用4个空格，禁止使用tab字符，若使用tab，则应在编辑器中设置将tab用4个空格替换。

  

- 变量命名

  成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式，不能以 `_` 或者 `$` 开始，布尔类型的值以`is`开头

  ```
  正例：userName / contentType / xmlFileReader / isAnimal
  反例：Username / ContentType / xmlfilereader / _name / $age
  ```




- 每行最多字符数

    每行最多字符数限制不超过 120个，超出需要换行，换行时遵循的原则如下：

    - 第二行相对第一行缩进 4个空格，从第三行开始对齐第二行。
    - 运算符与下文一起换行。
    - 方法调用的点符号与下文一起换行。
    - 在多个参数超长，逗号后进行换行。
    - 在括号前不要换行。

    ```
    StringBuffer sb = new StringBuffer();
    // 第二行相对第一行缩进 4个空格，从第三行开始对齐第二行。
    // 方法调用的点符号与下文一起换行。
    sb.append("zi").append("xin")
    	.append("huang") 
    	.append("huang")
    	.append("huang");
    
    // 反例：在括号前不要换行
    sb.append("zi").append("xin").append
    	("huang");
    
    // 运算符与下文一起换行。
    String str = "hello" + ....
    	+ "world"
    
    // 参数列表过长，逗号后进行换行。
    method(args1, args2, args3, ... argN,
    	argsX);
    ```

    

- 方法最大行数

    最大不宜超过60行，确保能在一屏显示完整的方法实现，针对逻辑较为复杂的方法，重构建议如下：

    - 发现方法中有重复代码的时候，可以将它封装成一个新的方法

    - 若方法可以分成好多个子功能，尽量将其抽取出来作为单独的方法

      

- 方法、类命名

    方法命名采用驼峰命名法，与成员变量、局部变量相同，getter、setter方法严格以get、set开头

    ```
    正例：getHttpMessage() / setName(String nname) / showAllStates()
    反例：returnHttpMessage() / Setname(String name) / showallstates()
    ```

    一般类命名使用UpperCamelCase风格，（领域模型的相关命名）DO / BO / DTO / VO等除外。

    ```
    public class Article / public class PasswordValidator
    ```

    特殊类的命名规则如下：

    - 抽象类以Abstract开头

        ```
        public abstract class AbstractClass
        ```

    - 异常类以Exception结尾

        ```
        public class NoMappingException extends Exception
        ```

    - 枚举类以Enum结尾

        ```
        public enum CardEnum
        ```

    - 如果接口提供了某种能力，以对应能力的形容词（通常为-able）做类名

        ```
        public class Translator extends Translatable
        ```
    
	> **建议：**如果使用到了设计模式，在类名中体现出具体模式，利于阅读者快速理解架构设计思想
	
	```
	public class OrderFactory
	```
	
	
	
- 常量

    常量、枚举变量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。

      ```
      正例： MAX_STOCK_COUNT
      反例： MAX_COUNT
      ```

    

- 空行和换行规则

    - 大括号

        如果是大括号内为空，则简洁地写成`{}`即可，否则：

        - `{` 左侧不换行，右侧换行
        - `}` 左侧换行，右侧如果还有`else`、`while`等不换行，否则换行

        ```
        正例：
        // 大括号内为空，则简洁地写成{}即可
        public void emptyMethod() {}
        
        if (a == 1) {
        	...
        } else if {
        	...
        } else {
        	...
        }
        // 终止括号后换行
        do {
        	...
        } while (b != 1)
        
        反例：
        public void emptyMethod() {
        }
        
        if (a == 1) {
        	...
        } 
        else {
        	...
        }
        
        ```

    - 类之间，类内部的成员之间空一行提高可读性

        ```
        class User {
        
            public int id;
        
            public String name;
        
            public Date birthday;
        
            public void method() {
            	...
            	...
            }
            
            ...
        }
        
        class Article {
        	...
        }
        ```

    > 若某方法体内的代码量大且无规律，不建议所有代码粘在一起写，可以酌情添加空行将子步骤分离开，提高阅读体验

- 注释规则

    - 类、类属性、类方法的注释必须使用Javadoc规范，使用`/* */`格式，不得使用`// `方式。

        ```
        正例：
        /**
         * MyClass
         * 描述
         *
         * @author xxx
         * @version xxx
         * @since xxx
         */
        class MyClass {
            ...
        
            /**
             * 描述
             *
             * @param arg
             * @return
             * @throws Exception
             */
             public void method(int arg) throws Exception {
                ...
             }
        }
        
        反例:
        // MyClass
        // 描述
        class MyClass {
            ...
        
            // 描述
            public void method(int arg) throws Exception {
                ...
            }
        }
        ```

    - 方法内部单行注释，在被注释语句上方另起一行，使用`//`注释。方法内部多行注释使用`/* */`注释，注意与代码对齐。

        ```
        public void method() {
            // 单行注释
            int a = 1;

            /*
            多行
            注释
            */
            int b = 2;

            反例：
            int c = 3; // 单行注释

            // 多行
            // 注释
            int d = 4;
        }
        ```

    - 若语句本身就能够很好的表达其功能，就不必添加多余的注释来解释其功能。

        ```
        反例：
        // 设置name的值为"Michael"
        setName("Michael");
        
        本例中方法名已经很清楚地说明这条语句的作用，不需要添加额外的解释性注释。
        ```

    - 若是代码被注释掉，可以用`//`注释，且必须在被注释代码上方说明不使用这段代码的原因。

- 操作符前后空格


    - 任何运算符左右必须加一个空格；
    
        括号内的表达式和括号之间不能有空格；
    
        `if` `else` `while` `try` 等保留字与括号之间必须加空格；
    
        ```java
        正例：
        int a = 1 + 2 * 3;
        int b = (a == 7) ? 10 : 20;
        if (a != b) {
        	...
        }
        
        反例：
        int a=1+2,c=32;
        while((a!=100)&&(c==3)){
        	...
        }
        ```
    
    - 同一行声明多个同类变量、定义和传入方法参数时，逗号后必须加一个空格
    
        ```java
        正例：
        int a, b, c;
        public void method(int a, String t, Boolean isChecked) {
        	...
        }
        method(123, "Michael", false);
        
        反例:
        int a,b,c;
        public void method(int a,String t,Boolean isChecked) {
        	...
        }
        method(123,"Michael",false);
        ```


​        

