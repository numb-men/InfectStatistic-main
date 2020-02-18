# InfectStatistic-221701218
疫情统计

## 如何运行
>1、命令行（win+r cmd）cd到项目src下
2、编译：javac Infectstatistic.java
3、输入参数运行：java Infectstatistic list （+各个参数 其中-log，-out为必选项，-date,-type,-province为可选项)

## 功能简介
>程序能够列出全国和各省在某日的感染情况
list命令 支持以下命令行参数：
-log 指定日志目录的位置
-out 指定输出文件路径和文件名
-date 指定日期，不设置则默认为所提供日志最新的一天。处理指定日期之前的所有log文件
-type 可选择[ip,sp,cure,dead]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

## 作业链接
>[作业正文](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)

## 博客链接

>[我的博客](https://www.cnblogs.com/spike218/p/12324502.html)