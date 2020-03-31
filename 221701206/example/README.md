# InfectStatistic-221701206

### 如何运行

> * 切换目录到src文件夹下
> * 执行javac -encoding utf-8 InfectStatistic.java进行编译
> * 执行java InfectStatistic list...
> * 必带参数-log指定日志文件目录、-out指定输出文件
> * 可选参数-province、-type、-date

### 功能介绍

>程序能够列出全国和各省在某日的感染情况
>
>命令行（win+r cmd）cd到项目src下，之后输入命令：
>
>```
>$ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
>```
>
>会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）
>
>list命令 支持以下命令行参数：
>
>- `-log` 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
>- `-out` 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径
>- `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
>- `-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
>- `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江
>
>注：java InfectStatistic表示执行主类InfectStatistic，list为**命令**，-date代表该命令附带的**参数**，-date后边跟着具体的**参数值**，如`2020-01-22`。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数

### 作业连接

[寒假作业2/2疫情统计](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)

### 博客连接

[是九啊](https://www.cnblogs.com/FZU-mjy/)

