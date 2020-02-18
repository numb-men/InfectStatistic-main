# InfectStatistic-221701438
疫情统计

##1.解题思路描述
作业发布前几天还是不知从何做起，在查阅了资料和询问同学后有如下思路：
- 提取命令行args来解析出各个参数：log和date参数读取txt文件内容，type参数确定输出，province选择省份，最后输出到out参数指定文件
- 用hashmap来存储命令中的参数与参数值(这里认定只有list命令)
- 用正则表达式来匹配以及提取匹配和提取“省份”和“感染患者”、“疑似患者”、“治愈”、“死亡”的人数，将其存放到类RegularMatch里。

##2.设计实现过程

1. statement类对输出日志中每一行语句进行拆分：provinceName,ip,sp,cure,dead，并且有五种参数的print和set方法，provinceName是String类型，其余四个是指确诊、疑似、治愈和死亡人数，为int类型。
2. HashMap commandLine 将args中的参数和参数值以键值对形式存储，方便后面进行正则匹配和提取。
3. RegularMatch正則匹配工具类：输入日志有以下8种语句格式
    > <省> 新增 感染患者 n人
      <省> 新增 疑似患者 n人
      <省1> 感染患者 流入 <省2> n人
      <省1> 疑似患者 流入 <省2> n人
      <省> 死亡 n人
      <省> 治愈 n人
      <省> 疑似患者 确诊感染 n人
    > <省> 排除 疑似患者 n人

    在这个类中用八个正则语句去匹配，并且有8个相应函数处理每种语句在provinceName,ip,sp,cure,dead五个参数的变化，最后统计全国情况。
4. readFile函数 读取目录下的所有日志内容，一次读一行，忽略最后的注释行
5. commandLinePrasing是对命令行解析后功能实现,检索到三个参数log province out并用三个相关函数实现相应的功能
