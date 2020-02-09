## 代码规范
* 缩进 
每次缩进3个空格 
```
data.forEach((line) => {
   var res = []
   function match(reg, hasTwoPvovinces) { //匹配正则，清洗数据以及初始化
      if (/\/\//.test(line)) return false
```
Object类型的初始化可以并排
```
const CmdParam = {  //存放命令参数
   log: [], out: [], date: [], type: [], province: [],
}
```
* 变量命名    
驼峰命名  
```
var reading = '',
   article = '',
   argv = process.argv.slice(2),
   cmd = argv.join(' ')
```
* 每行最多字符数  
100个字符，以确保显示屏能在宽度上容纳两个文本框为准 

* 函数最大行数  
以实现一个粒度合适的基准功能区分，限定数量的话一般不超过40 

* 函数、类命名  
驼峰命名
```
function appendData(date) {
   var fileName = CmdParam.log[0] + date + '.log.txt' 
```
* 常量  
外部引入(require)的变量全部小写  
数据型常量全部大写   
引用型常量首字母大写  
```
const fs = require('fs')
const readline = require('readline')  //读写文件的自带库
const prior = require('./Lib.js')
const MONTH = [0, 31, 28, 30, 51, 30, 31, 31, 30, 31, 30, 31]  //月份常量
const Provinces = new Set()  //存放省份的集合
const Total = {  //存放统计数据
   ip: {}, sp: {}, dead: {}, cure: {},
}
const CmdParam = {  //存放命令参数
   log: [], out: [], date: [], type: [], province: [],
}
```
* 空行规则  
无  
* 注释规则  
行内，空格加斜杠
```
   if (!fs.existsSync(fileName)) return  //如果没有对应日期的文件，就忽略 
   var data = fs.readFileSync(fileName, 'utf-8')  //效率非常低的同步读取
```
块级注释
```
/*
... ...
*/
```
* 操作符前后空格 
逗号后只有一个空格  
```
const CmdParam = {  //存放命令参数
   log: [], out: [], date: [], type: [], province: [],
}
```
操作符间均有1个空格
```
var fileName = CmdParam.log[0] + date + '.log.txt'
```
* 其他规则  
匿名函数  
```
process.on('uncaughtException', (e) => {
   console.error('错误：', e.message)
})
```
