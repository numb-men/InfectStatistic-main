# InfectStatistic-221701103
## 疫情统计

### 我的项目

#### 1. 如何运行

使用java编写

直接在cmd下输入命令运行，也可在eclipse下输入args运行

只使用了InfectStatistic.java文件，Lib.java为空

#### 2.功能简介

读取类似于这样格式的日志文件

```
福建 新增 感染患者 23人
福建 新增 疑似患者 2人
浙江 感染患者 流入 福建 12人
湖北 疑似患者 流入 福建 2人
安徽 死亡 2人
新疆 治愈 3人
福建 疑似患者 确诊感染 2人
新疆 排除 疑似患者 5人
// 该文档并非真实数据，仅供测试使用
```

程序能够列出全国和各省在某日的感染情况

命令行（win+r cmd）cd到项目src下，之后输入命令：

```
$ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt
```

会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）

可以根据输入的命令行参数（-date、-province、-type）进行不同输出

有输入检验功能，包括命令行格式和输入文件路径检验

#### 3.其他

详情信息请查看：[疫情统计作业](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)

也可以查看我的[博客链接](https://www.cnblogs.com/tyheng/p/12307649.html)得到该作业详情