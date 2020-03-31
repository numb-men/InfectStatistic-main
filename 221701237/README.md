# InfectStatistic-221701237
疫情统计<br/>
## 如何运行：<br/>
> * 1.命令行下切换到src目录下<br/>
> * 2.使用javac 命名编译 Infectstatistic.java<br/>
> * 3.使用java Infectstatistic list -各命令 参数<br/>
> * 注：txt文件应为UTF-8无BOM格式，java文件为UTF-8格式，输出文件也为UTF-8格式<br/>
## 功能简介：<br/>
> 根据-date命令后带的日期参数，读取-log命令后带路径参数下的日志文件，统计各个省份和全国的疫情情况，按照-province和-typy命令指定的参数输出数据到-out命令指定的输出路径的文件中。<br/>
> -date命令可缺省，缺省时截止止日志文件的最新时间<br/>
> -log命令不可缺省，后带参数不可缺省<br/>
> -out命令不可缺省，后带参数不可缺省<br/>
> -province命令可缺省，若输入-province命令则后带参数不可缺省<br/>
> -province 福建 则只列出福建的信息<br/>
> -type命令可缺省，若输入-type命令则后带参数不可缺省<br/>
> -type命名后带参数仅能为：ip(确诊)、sp(疑似)、cure(治愈)、dead(死亡)<br/>
> 如：java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt<br/>
> 会读取D:/log/下的所有日志，然后处理日志和命令，将结果生成ouput.txt文件中<br/>
## 作业链接<br/>
<a href="https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281">寒假作业（2/2）——疫情统计</a><br/>
## 博客链接<br/>
<a herf="#">我的博客</a>
