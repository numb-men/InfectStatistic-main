## 代码风格
Python的代码风格基本按照PEP8的要求进行编写

- 缩进

    每一级缩进使用4个空格。
    
    ```
    def func():
          if 1 != 2:
              pass
    ```
    续行若第一行有参数则应该与其第一行参数对齐
    ```
    s = func(var_one, var_two,
             var_three, var_four)
    ```
    若无应该则增加缩进以区别其他行
    ```
        s = func(
             var_one, var_two,
             var_three, var_four)
        
        
        def func2(
             var_1, var_2,
             var_3, var_4):
            print(var_1, var_2, var_3, var_4)
    ```
    
- 变量命名
    
    小写字母加下划线分隔， 可用单个小写字母。
    ```python
        num_list = [2, 3, 4 , 5, 6]
        max_num = num_list[0]
        max_index = 0
        for i in range(len(num_list)):
            if num_list[i] > max_num:
                max_num = num_list[i]
                max_index = i
        print(max_index, max_num)
    ```
    

- 每行最多字符数

    一般情况下，每行字符数不超过80个字符，以屏幕大小可做一些调整。

- 函数最大行数

    除一些定义外，函数的具体执行语句一般不超过20行。

- 函数、类命名

    类名为大写字母开头的驼峰命名法， 一般情况下只使用一个单词
    
    函数名则与变量命名方法相同

- 常量
  
    全大写字母加下划线分隔
    
    `MAX_TIMES = 20`

- 空行规则

    函数和类的定义:前后用两个空行隔开
    
    类的方法:前后用一个空行隔开
    
    相关的功能组可以用额外的一个空行隔开
    
    ```
    import re
    
    
    def func1():
        pass
        
    
    def func2():
        pass
        
     
    class Class1():
        def __init__(self):
            pass
        
        def func3(self):
            pass
    
    
    def func4():
        pass
        
        
    if __name__ == "__main__":
        pass
    ```

- 注释规则

    1. 行内注释
        
        对某一行的内容注释。
        
        注释与代码之间至少两个空格, 注释以#和一个空格开始。
        
        ```python
        s = s + 1 if not isinstance(s, int) else 0   # 若s不是数字则设为0
        ```
    2. 块注释
        
        对某一块内容(如函数定义，或一段无空行的代码)注释
        
        注释缩进与内容相同，注释与内容之间无空行, 注释以#和一个空格开始。
        
        ```python
        # 处理字符串s的函数
        def fun_s(s):
           pass
        ```
    
    3.

- 操作符前后空格

    各种括号后， 逗号、冒号前， 函数参数括号前， 索引括号前不接空格
    
    逗号后接一个空格
    
    ```
    list_1 = ['a', 'b', 'c', 'd', 'e']
    print(list_1[1:3])
    ```
    二元运算符及赋值语句的符号前后空格数相同，一般为一个空格。
    
    参数及函数默认值'='前后不加空格
    ```
    def sum(num1, num2, num3=0, num4=4):
        result = num1 + num2 + num3 + num4
        print(result)
    
    
    func(1, 2, num4=2)
    ```

- 其他规则
    - 导入
        不同模块应当分开导入
        ```python
        import os
        import sys
        ```
        而不是
        ```python
        import os, sys
        ```
        同一模块下的不同部分可一起导入
        ```python
        from flask import Flask, request
        ```
        
        
- ...
