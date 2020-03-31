# InfectStatistic-221701412
疫情统计

描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

***

|这个作业属于哪个课程|<[2020春W班 (福州大学)](https://edu.cnblogs.com/campus/fzu/2020SpringW)>|
|-- |-- |
|这个作业要求在哪里|<[作业要求](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)>|
|这个作业的目标|<建立博客、回顾、简历、绘制学习路线>|
|作业正文|<[作业正文](https://www.cnblogs.com/theTuring/p/12267528.html)>|
|其他参考文献|<csdn,github,《码出高效_阿里巴巴Java开发手册》,《W3CSchool Junit使用教程》>|

# Part.01 我的Github仓库地址

> **在文章开头给出Github仓库地址**

+ *Github* *“https://github.com/theTuring/InfectStatistic-main”* <[点击进入](https://github.com/theTuring/InfectStatistic-main)>

***

# Part.02 PSP表格

> **阅读《构建之法》第一章至第三章的内容，并在下方作业里体现出阅读后的成果。特别是第2章中的效能分析及个人软件开发流程（PSP）。之后给出此次作业的PSP表格**

PSP2.1 | Personal Software Process Stages | 预估耗时（分钟）|   实际耗时（分钟）
--|:--:|--:|--:
Planning|计划|30|40
Estimate|估计这个任务需要多少时间|30|40       
Development|开发|960|1415
Analysis|需求分析 (包括学习新技术)|60|60     
Design Spec|生成设计文档|30|45      
Design Review|设计复审|30|60        
Coding Standard|代码规范 (为目前的开发制定合适的规范)|60|90      
Design|具体设计|240|300     
Coding|具体编码|480|720     
Code Review|代码复审|60|60     
Test|测试（自我测试，修改代码，提交修改）|80|80       
Reporting|报告|120|165      
Test Repor|测试报告|60|60       
Size Measurement|计算工作量|30|45        
Postmortem & Process Improvement Plan|事后总结, 并提出过程改进计划|30|60        
|合计||1110|1620

***

# Part.03 思路描述

> **解题思路描述。即刚开始拿到题目后，如何思考，如何找资料的过程**

+ 关于思考

  其实一拿到本次作业基本思路就有了，脑袋里第一浮现的思路就是这是个命令行（因为以前有用向jvm这么玩过，大一学的c也对命令行有将过一些），要解析它，文件读写，然后有八种类型的文本，因为自己也写过好几个爬虫了，所以第一反应便是用正则表达式匹配+提取参数，于是在作业当天就fork了仓库，后来定睛一看，“这次作业重点考察需求的分析”、“请在正式编写程序之前先理清需求文档”、“请在理清需求文档后先设计好程序模块、类图、数据结构、算法流程等部分”，原来这次不是仅仅要求只实现功能什么的，把首先要做的代码规范制定也看漏了，ok，乖乖关了编译器，github启动乖乖读文档。在后来的开发阶段，我觉得把本次作业分为五个内部类，在对需求文档进行分析后分为五个内部类完成本次作业（在后续实际开发中分为六个内部类）：
  
```  
        class CommandLine//命令行对应的实体类、  
        class CommandLineAnalytic//命令行解析类、  
        class RegexUtil//正则表达式工具类、    
        class LogUtil//Log文件工具类、   
        class LogDao//Log文件控制类、   
        class CommandLineApplication//启动类，
```

  毕竟咱java程序员还是要面向对象一点点的，这样分模块开发自己觉得也更适合维护和后续拓展，然后对于怎么开发，自己是按class CommandLine->class CommandLineAnalytic->class RegexUtil //正则工具类(在后续开发中独立封装出来的)->class LogUti->class LogDao->class CommandLineApplication的顺序开发，包括GitHub的commit也是按照这个来的。

+ 关于找资料

    + 资料的话首先参考了《码出高效_阿里巴巴Java开发手册》，学到了不少东西还是，在一边学习大厂的代码规范的同时，也慢慢的把自己代码的coolstyle制定了，学着在每个方法和类添加了自动的注解方便阅读，在开发中重新看了正则表达式的教程。

***

# Part.04 设计实现过程

> **设计实现过程。设计包括代码如何组织，关键函数的流程图**

+ 在设计本次疫情统计系统时，为了提高程序的可维护性与拓展性，在程序的内部划分了六个内部类：

    +  class CommandLine 命令行对应的实体类：因命令行组成为 命令 + 命令行参数，具体分为了两部分，class Command 命令对应的实体类、class Arguments 命令行参数对应的实体类（其中type类型因固定四种，故使用枚举类型），有对应的get、set方法，成员变量使用布尔型，表示命令行的激活状态，如命令$ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt对应的命令行对象即为new Commandline{list: true; date: true; date_content: 2020-01-22; log: true; log_content：D:/log/; out: true; out_content: D:/output.txt; type: false; province: false}.

    + class CommandLineAnalytic 命令行解析类: 获得main函数的命令行args拼成的List<String>进行解析，构造对应解析结果的命令行的对象。

    + class RegexUtil 正则表达式工具类：存入了对应八种类型的正则表达式，eg：String type1 = "\\W+ 新增 感染患者 \\d+人";先匹配对应类型，再提取参数。

    + class LogUtil Log文件工具类：首先构建了文件结构对应的实体类class InfectResult，有对应的get、set方法，覆写了toString方法 -> 以* province + " " + ip +  " " + sp  + " "  + cure + " " + dead的格式方便按行存入结果文件；有一个处理list的class ListUtil list工具类，在解析文件时采用先列后合并的方法如文件2020-01-22.log.txt中“福建 新增 感染患者 2人”与“湖北 感染患者 流入 福建 2人“这两行就构造为“福建 2 0 0”、“湖北 -2 0 0”、“福建 2 0 0”最后再合并同类项，这样效率更高；class ResultUtil 结果工具类：封装了正则匹配后的结果。

    + class LogDao 文件控制类：initLog初始化方法为本类所有方法的起点,读入文件解析返回List<LogUtil.InfectResult>，当路径为文件时进行解析，当路径为一个目录时递归调用自身读入整个目录；outLog写入结果文件outLog(String fileName, String out_path, String commandline, List<String> province, List<String> type, String date)传入多个参数控制写入；sortResultByProvince方法调用Comparator接口进行中文排序。

    + class CommandLineApplication 启动类：程序的起点，Application方法实例化前几个类，进行命令行解析、文件正则匹配读写。

***
