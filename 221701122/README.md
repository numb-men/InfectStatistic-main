# InfectStatistic-221701122
疫情统计

描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

### 项目介绍

> 读取指定目录下的所有日志，处理日志和命令，在指定目录下生成指定文件名的文件，列出指定日期之前（包括当日）的各地区感染情况

### 功能

**目前仅支持list命令 支持以下命令行参数：**

- `-log`  指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径  
- `-out`  指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径  
- `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件  
- `-type`  可选择[`ip`： infection patients 感染患者，`sp`： suspected patients 疑似患者，`cure`：治愈 ，`dead`：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。  
- `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江  

注：java InfectStatistic表示执行主类InfectStatistic，list为**命令**，-date代表该命令附带的**参数**，-date后边跟着具体的**参数值**，如`2020-01-22`。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数

**示例：**  
>`java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt`  

会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）

**PS：** 对于windows下的DOS界面与eclipse自带的javaIDE的不同，会有运行结果不同的问题。针对上述问题，其实是DOS界面下使用java命令对于处理文件时采用默认的GBK编码，而我的eclipse使用UTF-8编码进行文件处理。
话不多说，解决方案如下：
1. 配置环境变量JAVA_TOOL_OPTIONS：
![image](https://images.cnblogs.com/cnblogs_com/hylog/1647349/o_200214075649b.png)
2. 在DOS窗口下运行命令（写了两次，可以但是没必要）：
![image](https://images.cnblogs.com/cnblogs_com/hylog/1647349/o_200214075638a.png)

### 其它关于项目的链接：
- 项目链接: [模拟疫情统计]https://github.com/Just-hy/InfectStatistic-main
- 博客链接：[软件工程实践寒假作业（2/2）](https://www.cnblogs.com/hylog/p/12321795.html)
- 作业链接：[寒假作业（2/2）——疫情统计 ](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)