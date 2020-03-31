# InfectStatistic-221701133  

## 程序主题为“疫情统计”    

## 运行：
运行本程序需要使用命令行参数，如“list -date 2020-01-22 -log E:\log\ -out E:\log\output.txt”  
暂时只支持list命令，以及附带的-log、-out、-type、-province命令行参数  
list命令 支持以下命令行参数：  
-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径  
-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径  
-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件  
-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。  
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江  


## 功能简介  

本程序的功能很是简单，即读取提供的文件夹中所有符合条件的log.txt文件，获取其中按照规范填写的内容，并加以整理，以所需要的方式输出到指定位置文件中    

## 作业链接  

[点击前往](https://www.cnblogs.com/17lhf/p/12327747.html)  

## 博客链接  

[点击前往](https://www.cnblogs.com/17lhf/) 
