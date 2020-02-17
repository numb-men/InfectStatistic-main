 描述你的项目，包括如何运行、功能简介、作业链接、博客链接等
1.项目简介

  该项目读取用户所输入的文件路径，获得日志，根据其他的-type,-province,-date来做筛选，最后输出到用户指定的文件中
2.运行
    命令行（win+r cmd）cd到项目src下，之后输入命令：
    使用`javac InfectStatistic.java` 进行编译
    使用`$ java InfectStatistic list -date 日期 -log 日志所在目录 -out 输出目录/文件名.txt`运行

3.功能简介
    list命令 支持以下命令行参数：

    -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
    -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
    -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

4.作业链接

    https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281

5.博客链接

    https://www.cnblogs.com/rcwmdbb/p/12264674.html