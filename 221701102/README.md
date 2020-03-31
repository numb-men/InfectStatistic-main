
# InfectStatistic-221701102
# 疫情统计

## 项目简介
统计日志文本中记录的国内各省每天的感染情况，并可指定输出类别与省份，按照省份首字母拼音排序输出。

## 运行方式
 - 命令行（win+r cmd）cd到项目src下，输入命令：
```
javac encoding UTF-8 InfectStatistic.java
```
 - 输入命令：
```
java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
```
 - 会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）
> 全国 感染患者22人 疑似患者25人 治愈10人 死亡2人
福建 感染患者2人 疑似患者5人 治愈0人 死亡0人
浙江 感染患者3人 疑似患者5人 治愈2人 死亡1人
// 该文档并非真实数据，仅供测试使用
   
## 功能简介
list命令支持以下命令行参数：
- **-log** 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径。
- **-out** 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径。
- **-date** 指定日期，不设置则默认为所提供日志最新的一天。程序将会处理指定日期之前的所有log文件。
- **-type** 可选择 **[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈，dead：死亡患者]**，使用缩写选择，如 **-type ip** 表示只列出感染患者的情况，**-type sp cure**则会按顺序**sp, cure**列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
- **-province** 指定列出的省，如 **-province 福建**，则只列出福建，**-province 全国 浙江**则只会列出全国、浙江。

## 作业链接 [寒假作业（2/2）——疫情统计][1]

## 博客链接 [软件工程实践 - 寒假作业（2/2）—— 疫情统计][2]

  [1]: https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281
  [2]: https://www.cnblogs.com/langogo/p/12258974.html