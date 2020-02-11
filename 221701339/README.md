# InfectStatistic-221701339

# 疫情统计

- ## 介绍

  ​		通过读取疫情日志数据，通过各省份和全国的感染、未确诊、治愈和死亡人数数据。

- ## 运行

  ​		进入到src目录下，编译src目录下的所有源文件：

  ```bash
  $ javac * -encoding utf-8
  ```

  ​		**注意 指定`-encoding`选项为`utf-8`，否则可能出现编译错误。**

  ​	之后运行命令:

  ```bash
  $ java InfectStatistic list -date 2020-01-22 -log E:/test/log/ -out E:/test/output.txt
  ```

- ## 功能

  - `-log` 指定日志目录的位置，该项**必需附带**，请直接使用传入的路径。
  - `-out` 指定输出文件路径和文件名，该项**必需附带**，请直接使用传入的路径。
  - `-date` 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
  - `-type` 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 `-type ip` 表示只列出感染患者的情况，`-type sp cure`则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
  - `-province` 指定列出的省，如`-province 福建`，则只列出福建，`-province 全国 浙江`则只会列出全国、浙江

- ## 作业链接

  ​	[https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)

- ## 博客链接

  ​	[https://www.cnblogs.com/Zhifeng-Shen/p/12258768.html](https://www.cnblogs.com/Zhifeng-Shen/p/12258768.html)

