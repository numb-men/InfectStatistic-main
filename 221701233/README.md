# InfectStatistic-221701233

[TOC]



## 功能简介

用java实现的疫情统计程序，能够通过日志读取疫情记录，并根据命令行参数格式化输出疫情的统计结果到指定的文件中。

## 运行

#### 编译环境

- java 8
- Windows

#### 方法一：控制台

 定位到`InfectStatistic.java`和`Lib.java`的目录

输入以下指令进行编译：

```
javac -encoding UTF-8 Lib.java InfectStatistic.java
```

输入指令运行：

```
java InfectStatistic <命令> <参数> <值>... <参数> <值>... ..
```

例如，从统计`D:\log\`下的日志中截止2020年1月22日福建省和河北省的所有数据并导出到`D:\ListOut2.txt`：

```
java InfectStatistic list -log D:\log\ -out D:\ListOut2.txt -date 2020-01-22 -province 福建 河北
```



#### 方法二：IDE

导入项目文件到IDE中；

在IDE的运行配置下输入命令行参数，运行。

推荐IDE为 IDEA、Eclipse。

具体运行方式请参考：

[Intellij IDEA 输入命令行参数运行的方法](https://blog.csdn.net/HeatDeath/article/details/79102408)

[Eclipse中命令行参数的输入方法](https://blog.csdn.net/weixin_44256803/article/details/91517241)



当前可用命令和参数如下：

**`list`命令 ：**

- `-log` 指定日志目录的位置，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径

- `-out` 指定输出文件路径和文件名，该项**必会附带**，请直接使用传入的路径，而不是自己设置路径

- `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件

- `-type` 可选择的值

    - `ip`： infection patients 感染患者
    - `sp`： suspected patients 疑似患者
    - `cure`：治愈 
    - `dead`：死亡患者

    使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。

- `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江

## 了解更多

作业链接：https://github.com/WallofWonder/InfectStatistic-main/tree/zyf/221701233 <br/>
博客链接：

