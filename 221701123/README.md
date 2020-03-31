# 对新冠病毒的本地文件读写统计

## 运行 
cd至InfectStatistic.js文件目录，然后在node环境下直接 
```
node InfectStatistic list -log ../log/ -out ../result/output.txt 
// 可附加参数：
-type sp ip dead cure  //限定只输出疑似、感染、死亡、治愈数据。
-province 西藏 香港  //限定只输出这些省份。若存在限定省份，则默认不输出“全国”。
-date 2020-02-05    //只统计文件至该日期。
```
# 功能简介 

统计每天的日志文件，并汇总统计结果

# 作业链接 
[作业链接 ](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)

# 博客链接 
[博客链接](https://www.cnblogs.com/viridianfairy/p/12236360.html)
