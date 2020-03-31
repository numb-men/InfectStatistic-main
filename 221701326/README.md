# InfectStatistic-221701326
疫情统计

## 作业链接
[作业链接](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)
## 博客链接
[博客链接](https://www.cnblogs.com/puzb/)
## 项目概述
*    功能
根据日志文本列出全国和各省在某日的感染情况。命令行（win+rcmd）cd到项目src下，之后输入命令：$ java InfectStatistic list -date 2020-01-22 -log D:/log/-out D:/output.txt
会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）
*    参数实现
实现了-log 和-out
## 如何运行
*    类：主要有4个类 分别为infectstatistic类 state类 fileopreate类 commandline类。其中infectstatistic类为主类，接受命令行参数。并执行调用其他类中相关的方法 实现功能
*    state类:
1.创建字符串数组provinces[]保存全国和各个省份名。
2.创建pattern字符串用于各个文本行类型的判断。
3.judgetxtline(Stringtextstr)方法传入readline读取的str，通过textstr与pattern的比较，返回一个整形的文本类型，用于conditions方法的switch分支。
4.conditions(String textstr,int conditions[][])方法，首先用split()方法，对readline读入的文本行切割，并用choice接受judgetextline()返回的文本行格式类型。switch分支，对各种类型的文本行进行各省的相关人数操作。
*    fileoperate类
1.findFile(File dir,int conditions[][])方法，创建一个文件数组dirfiles[]，将路径dir下的文件赋值给dirfiles[]，然后便利dirfiles数组，对符合条件的文件，用字符流的方式每行读取文本，并调用state类中的conditions方法
2.writetoFile(File file,int conditions[][])方法，传入存储了所有数据的conditions[][]，并以一定的格式输出到文本
*    commandline类
1.analysisCommand(String args[])方法，分析并存储命令行传入的参数

