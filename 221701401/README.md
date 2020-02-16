# InfectStatistic-221701401

# 运行方式

1.通过命令行进入到src目录

2.输入下方代码编译

```
javac -encoding UTF-8 InfectStatistic.java
```

3.输入命令编译

```
例：java InfectStatistic list -log D:/log/ -out D:/output.txt
```

# 项目功能简介

指令格式: java InfectStatistic list 后接相关参数
list命令 支持以下命令行参数：

- -log 指定日志目录的位置，该项必会附带
- -out 指定输出文件路径和文件名，该项必会附带
- -date 指定日期，不设置则默认为所提供日志最新的一天
- -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择， 如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
- -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
  exp:输入指令:java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
  会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）



# 博客链接

[链接在这](https://www.cnblogs.com/kokodayo/p/12318844.html)

# 作业链接

[链接在这](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)