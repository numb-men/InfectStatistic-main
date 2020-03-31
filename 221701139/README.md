# 项目描述
最近新型冠状病毒疫情严重，全国人民都感到担忧，迫切希望能够及时了解到病毒最新的情况，作为IT学子，大家请你帮忙开发一个疫情统计程序。
该项目只需一条简单的命令即可统计疫情信息,
# 运行方法
命令行（win+r cmd）cd到项目src下，之后输入命令：可能会有编码问题
``` javac -encoding UTF-8 InfectStatistic.java Lib.java ```
``` java -cp .; -Dfile.encoding=utf-8 InfectStatistic list -log ..\log -out D:/output.txt ```
目前支持的命令为list
其相关参数为:
* `-log` 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
* `-out` 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
* `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
* `-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
* `-province` 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
# 功能简介
该程序可以根据疫情信息,以文本形式给出统计信息,之后将统计的信息输出到文件上
<br>
1. 根据参数条件输出内容
# 作业连接
[17级软件工程寒假第二次作业](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)
# 博客链接
[寒假作业（2/2）——疫情统计](https://www.cnblogs.com/cybersa/p/12323482.html)



