# InfectStatistic-181700141
疫情统计

* list命令处理类说明  
&emsp;&emsp;成员变量ipMap，spMap，cureMap以及deadMap以键值对形式存储各省份不同类型的人数，利用已经排好序的数组provincesArray（全国在第一个），顺序插入最开始各省份各类型数据均为0。  
inDirectory及OutDirectory分别存储-log和-out参数值；date，types和provinces分别存储-date，-type和-province参数值；-dateIsExist，-typeIsExist和-provinceIsExist分别用来记录用户  
是否输入-date，-type和-province参数。 s1-s8为合法日志文件中每一行所需遵循的正则表达式。  
&emsp;&emsp;成员方法dealParameter(String[])负责判断用户输入的list命令，非法则抛出异常IllegalException，合法则将初始化与参数处理有关的成员变量。sort()方法负责对-provinces的参数  
值排序，利用前面以及排好序的数组可降低设计算法的难度；carryOutActions()方法负责执行参数所要求的操作，它将调用handleFiles(File)方法，该方法负责读取目录日志下符合命名要求XXXX-XX-XX.log.txt  
的日志文件以及将处理结果输出到指定文件中。handleFiles(String)负责处理指定日志文件中内容。getAmount(String)方法负责提取出满足s1-s8正则表达式所要求的字符串中的数字（即人数）；  
out(BufferedWriter,List)负责具体输出。  

* 作业链接  
[寒假作业（2/2）](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)

*博客链接  
[软件工程实践2020第二次作业](https://www.cnblogs.com/duolaam/p/12323385.html)
