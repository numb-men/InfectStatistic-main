# InfectStatistic-221701429
疫情统计

描述你的项目，包括如何运行、功能简介、作业链接、博客链接等

|这个作业属于哪个课程|<center>https://edu.cnblogs.com/campus/fzu/2020SpringW/</center>|
|-- |-- |
|这个作业要求在哪里|<center>https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281</center>|
|这个作业的目标|<center>完成疫情统计的程序以及博客</center>|
|作业正文|<center>....</center>|
|其他参考文献|<center>....</center>|

##作业和博客链接
[仓库地址](https://github.com/theguiltyperson/InfectStatistic-main)
[博客链接](https://www.cnblogs.com/hxd1017/p/12296316.html)

##解题思路描述
看完需求之后，我想到要用提取命令行的args来解析出各个参数，然后根据log参数和date参数来读取相应txt文件的内容，type参数确定输出的语句，province参数选择需要输出的省份，最后输出到out参数指定的位置。其中读取txt文件内容还需要用正则表达式来匹配和提取“省份”和“感染患者”、“疑似患者”、“治愈”、“死亡”的人数，将其存放到一个内部类里，方便后续的输出。
至于查找资料的部分，因为对HashMap使用的不熟练，我还去找寻HashMap的原理和使用方法的资料，以及同学发的这次作业相关的博文，还有一些细节上的思路问题没法查找的，就去问同学。在这里也非常感谢帮助我的几位同学。

##设计实现过程
<img src="https://images.cnblogs.com/cnblogs_com/hxd1017/1646187/o_200212110009%E6%A8%A1%E5%9D%97%E7%BB%93%E6%9E%84%E5%9B%BE.png" width="850" height="200"/>
[查看原图](https://images.cnblogs.com/cnblogs_com/hxd1017/1646187/o_200212110009%E6%A8%A1%E5%9D%97%E7%BB%93%E6%9E%84%E5%9B%BE.png)
* 首先命令行的输入，把args提取到hashmap里存放，把参数和参数值以键值对形式存储，后面把输入的参数（值）和提供的参数（值）进行正则表达式匹配和提取，进入对应的行为中。
* 因为考虑到文档中的关键数据：省份、ip、sp、cure、dead人数会反复用到，所以将这些内容放入一个province类中存储，省份为String类型，其余四种为int型，province类中含有多种返回参数的方法和构造函数。
* 接下来是对命令参数的分析，-log和-date都是输入的，-province是对province类的数据选择输出的，-type是改变输出格式的，-out是选择输出文件的，故将date涵盖在log里，完成输入，province是中间过程，自为一体，type和out化为一体，控制输出的格式。
* -date是要求选择输入指定日期前的txt文件，把txt文件的日期部分提取出来（这部分应该用正则表达式匹配，当时没想到），转化为日期格式，与输入的-date参数值用isBefore方法比较先后，然后通过输入输出流提取文档中的关键字，将8种行为分别计算结果后，将结果存放回province类中，不同省份的类用ArrayList链接起来，至此完成了输入部分。
* -province比较简单，只需要选取name属性符合要求的province类即可，将选取后的ArrayList返回，这个参数的功能也就实现了。
* -type将选择类的指定属性以一定格式输出，从输入的命令中提取-type参数的键值，对比属性名，配对则输出属性加上格式。
* -out则是接-type，将字符串写到指定文件中。
**流程如下：**
<img src="https://images.cnblogs.com/cnblogs_com/hxd1017/1646187/o_200212115636%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg" width="700" height="70"/>
[查看原图](https://images.cnblogs.com/cnblogs_com/hxd1017/1646187/o_200212115636%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg)