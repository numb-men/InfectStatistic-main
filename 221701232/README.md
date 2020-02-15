# InfectStatistic-221701232
疫情统计

**项目概述**
>**需求分析**
1.读取指定路径下所有格式为YYYY-MM-dd.log.txt的日志文件，统计日志文件的信息并输出到指定的文件
2.支持处理命令行参数.
````
日志文件可能出现的几种情况
1、<省> 新增 感染患者 n人
2、<省> 新增 疑似患者 n人
3、<省1> 感染患者 流入 <省2> n人
4、<省1> 疑似患者 流入 <省2> n人
5、<省> 死亡 n人
6、<省> 治愈 n人
7、<省> 疑似患者 确诊感染 n人
8、<省> 排除 疑似患者 n人
PS：日志中各种情况的出现顺序不定，省出现的顺序不定，出现哪些省不定，省出现几次不定。
````
````
输出文件样例
全国 感染患者22人 疑似患者25人 治愈10人 死亡2人
福建 感染患者2人 疑似患者5人 治愈0人 死亡0人
浙江 感染患者3人 疑似患者5人 治愈2人 死亡1人
// 该文档并非真实数据，仅供测试使用
PS : 每一行记录的输出顺序规则如下
1.先输出全国,其他省份按汉字拼音排序,且只输出日志文件中出现过的省份
2.如果命令行指定了-province,则应该输出所有指定的项没有指定就不必输出,顺序同规则1
````
**如何运行**
1.源文件在src/目录下可通过JAVA编译器编译,开发过程使用JDK1.8
2.命令行参数格式 : java InfectStatistic list
                -log (logFilePath, 必须指定)
		-out (outFilePath, 必须指定)
		-date (yyyy-MM-dd, 可选)
		-type (ip, sp, cure, dead, 可选)
		-province (武汉, 福建, 北京, ..., 可选)
		-show (print args, 可选)
**功能简介**
````
命令行样例:java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
list命令 支持以下命令参数:
 -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
 -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
 -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
 -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，
 如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
 -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
 PS：java InfectStatistic表示执行主类InfectStatistic，list为命令，-date代表该命令附带的参数，-date后边跟着具体的参数值，
 如2020-01-22。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数
````
作业链接
[https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281) 
博客链接
[https://www.cnblogs.com/konananan/p/12310490.html](https://www.cnblogs.com/konananan/p/12310490.html) 

