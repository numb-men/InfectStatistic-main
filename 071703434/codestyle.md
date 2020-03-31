## 代码风格



- 缩进

   1.大括号的开始在代码块开始的行尾，闭合在和代码块同一缩进的行首，同一层次的代码要保持整齐

   2.使用tab缩进源代码。 使用alt+shift+f （eclipse）来格式化代码,注：格式化代码后还需手动来调下。

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

- 变量命名

   1.代码中的命名均不能以下划线或美元符号开始，也不能以下划线或美元符号结束

   2.代码中的命名严禁使用拼音与英文混合的方式，更不允许直接使用中文的方式

   3.方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase风格，必须遵从驼峰形式，比如: supplierName, addNewContract，而不是 supplier_name, add_new_contract

   4.数组定义如下：String[] args，不可使用String  args[]的方式来定义

- 每行最多字符数

   单行字符数限制不超过 100个，超出需要换行，换行时遵循如下原则

   - 第二行相对第一行缩进 4个空格，从第三行开始，不再继续缩进

   - 运算符与下文一起换行

   - 在多个参数超长，逗号后进行换行

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

- 函数最大行数

  超过80行的函数即为超大函数，即进行拆分，规定最大行数为80-200行

- 函数、类命名

   - 类名使用UpperCamelCase风格，必须遵从驼峰形式

   ```
       正例：MarcoPolo / UserDO / XmlService / TcpUdpDeal /   TaPromotion
       反例：macroPolo / UserDo / XMLService / TCPUDPDeal /   TAPromotion
   ```

   | 后缀名   | 意义                                                       | 举例                       |
   | -------- | ---------------------------------------------------------- | -------------------------- |
   | EJB      | 表示这个类为EJB类                                          | LCIssueInfoManagerEJB      |
   | Service  | 表明这个类是个服务类，里面包含了给其他类提同业务服务的方法 | PaymentOrderService        |
   | Impl     | 这个类是一个实现类，而不是接口                             | PaymentOrderServiceImpl    |
   | Inter    | 这个类是一个接口                                           | LifeCycleInter             |
   | Dao      | 这个类封装了数据访问方法                                   | PaymentOrderDao            |
   | Action   | 直接处理页面请求，管理页面逻辑了类                         | UpdateOrderListAction      |
   | Listener | 响应某种事件的类                                           | PaymentSuccessListener     |
   | Event    | 这个类代表了某种事件                                       | PaymentSuccessEvent        |
   | Servlet  | 一个Servlet                                                | PaymentCallbackServlet     |
   | Factory  | 生成某种对象工厂的类                                       | PaymentOrderFactory        |
   | Adapter  | 用来连接某种以前不被支持的对象的类                         | DatabaseLogAdapter         |
   | Job      | 某种按时间运行的任务                                       | PaymentOrderCancelJob      |
   | Wrapper  | 这是一个包装类，为了给某个类提供没有的能力                 | SelectableOrderListWrapper |
   | Bean     | 这是一个POJO                                               | MenuStateBean              |

   - 函数名

     首字母小写，如 addOrder() 不要 AddOrder()；动词在前，如 addOrder()，不要orderAdd()

     查询方法要查询的内容在前，条件在后。 如getXxByXx（）

     动词前缀往往表达特定的含义，如下表：

     | 前缀名             | 意义                                     | 举例                           |
     | ------------------ | ---------------------------------------- | :----------------------------- |
     | create             | 创建                                     | createOrder()                  |
     | delete             | 删除                                     | deleteOrder()                  |
     | add                | 创建，暗示新创建的对象属于某个集合       | addPaidOrder()                 |
     | remove             | 删除                                     | removeOrder()                  |
     | init或则initialize | 初始化，暗示会做些诸如获取资源等特殊动作 | initializeObjectPool           |
     | destroy            | 销毁，暗示会做些诸如释放资源的特殊动作   | destroyObjectPool              |
     | open               | 打开                                     | openConnection()               |
     | close              | 关闭                                     | closeConnection()<             |
     | read               | 读取                                     | readUserName()                 |
     | write              | 写入                                     | writeUserName()                |
     | get                | 获得                                     | getName()                      |
     | set                | 设置                                     | setName()                      |
     | prepare            | 准备                                     | prepareOrderList()             |
     | copy               | 复制                                     | copyCustomerList()             |
     | modity             | 修改                                     | modifyActualTotalAmount()      |
     | calculate          | 数值计算                                 | calculateCommission()          |
     | do                 | 执行某个过程或流程                       | doOrderCancelJob()             |
     | dispatch           | 判断程序流程转向                         | dispatchUserRequest()          |
     | start              | 开始                                     | startOrderProcessing()         |
     | stop               | 结束                                     | stopOrderProcessing()          |
     | send               | 发送某个消息或事件                       | sendOrderPaidMessage()         |
     | receive            | 接受消息或时间                           | receiveOrderPaidMessgae()      |
     | respond            | 响应用户动作                             | responseOrderListItemClicked() |
     | find               | 查找对象                                 | findNewSupplier()              |
     | update             | 更新对象                                 | updateCommission()             |

- 常量

   全大写用下划线分割，如

   public static find String ORDER_PAID_EVENT = “ORDER_PAID_EVENT”;

- 空行规则

   连续两行的空行代表更大的语义分割。

   方法之间用空行分割（尽量用一行空行）

   域之间用空行分割

- 注释规则

     1. 表明类、域和方法等的意义和用法等的注释，要以javadoc的方式来写

     2. 方法内部单行注释使用“//”位于要说明内容上方；多行注释使用“/ * */”位于要说明内容上方。

     3. 块级别注释，单行时用 //, 多行时用 /\* .. \*/

     4. 较短的代码块用空行表示注释作用域

     5. 行内注释行内注释用 // 写在行尾

     6. 较长的代码块要用

        **/\*------ start: ------\*/**

        和

        **/\*-------- end: -------\*/**

        包围

     7. 注释要和代码同步，过多的注释会成为开发的负担

        

- 操作符前后空格

  多元运算符左右必须加一个空格来分隔操作数

- 其他规则

    1. 标识符的命名力求做到统一、达意和简洁
    2. 程序职责单一，从而降低难度，减少出错
    3. 变量的声明，初始化和被使用尽量放到一起