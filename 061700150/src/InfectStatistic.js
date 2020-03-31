const fs = require('fs')  //读写文件的自带库
const prior = require('./Lib.js')
const MONTH = [0, 31, 28, 30, 51, 30, 31, 31, 30, 31, 30, 31]  //月份常量
const Provinces = new Set()  //存放省份的集合
const Total = {  //存放统计数据
   ip: {}, sp: {}, dead: {}, cure: {},
}
const CmdParam = {  //存放命令参数
   log: [], out: [], date: [], type: [], province: [],
}
const REG = [
   /(\S{2,3})\s新增 感染患者 (\d+)人/g,
   /(\S{2,3})\s新增 疑似患者 (\d+)人/g,
   /(\S{2,3})\s疑似患者 流入 (\S{2,3})\s(\d+)人/g,
   /(\S{2,3})\s感染患者 流入 (\S{2,3})\s(\d+)人/g,
   /(\S{2,3})\s死亡 (\d+)人/g,
   /(\S{2,3})\s治愈 (\d+)人/g,
   /(\S{2,3})\s疑似患者 确诊感染 (\d+)人/g,
   /(\S{2,3})\s排除 疑似患者 (\d+)人/g,
]
var reading = '',
   article = '',
   argv = process.argv.slice(2),
   cmd = argv.join(' ')
function appendData(date) {
   var fileName = CmdParam.log[0] + date + '.log.txt' //组合文件
   if (!fs.existsSync(fileName)) return  //如果没有对应日期的文件，就忽略 
   var data = fs.readFileSync(fileName, 'utf-8')  //效率非常低的同步读取
   data = data.split('\n')   //分行
   data.forEach((line) => {
      var res = []
      function match(reg, hasTwoPvovinces) { //匹配正则，清洗数据以及初始化
         if (/\/\//.test(line)) return false //如果是注释，忽略掉
         var res = reg.exec(line)
         if (!res) return
         for (key of Object.keys(Total)) { //初始化数据数字为Number类型，防止NaN
            if (!Total[key][res[1]]) { //非undefined类型代表已经初始化过
               Provinces.add(res[1])  //顺便收集唯一省份
               Total[key][res[1]] = 0
            }
            if (hasTwoPvovinces && !Total[key][res[2]]) { //同上，并且判断要收集第三项数据
               Provinces.add(res[2])  //顺便收集唯一省份
               Total[key][res[2]] = 0
            }
         }
         return res.slice(1, 4) //返回正则匹配的组内容
      }
      if (res = match(REG[0])) //正则
         Total.ip[res[0]] += Number(res[1])  
      else if (res = match(REG[1]))
         Total.sp[res[0]] += Number(res[1])
      else if (res = match(REG[2], true)) {
         Total.sp[res[0]] -= Number(res[2])
         Total.sp[res[1]] += Number(res[2])
      }
      else if (res = match(REG[3], true)) {
         Total.ip[res[0]] -= Number(res[2])
         Total.ip[res[1]] += Number(res[2])
      }
      else if (res = match(REG[4])) {
         Total.ip[res[0]] -= Number(res[1])
         Total.dead[res[0]] += Number(res[1])
      }
      else if (res = match(REG[5])) {
         Total.cure[res[0]] += Number(res[1])
         Total.ip[res[0]] -= Number(res[1])
      }
      else if (res = match(REG[6])) {
         Total.ip[res[0]] += Number(res[1])
         Total.sp[res[0]] -= Number(res[1])
      }
      else if (res = match(REG[7]))
         Total.sp[res[0]] -= Number(res[1])
   })
}
function washCmdParam(){
   if(argv[0]!='list') throw new Error('仅能接受list命令！')
   argv.slice(1).forEach(v => {
      var checked = false  //checked为true，说明该参数是一个以-为前缀的key
      for (item of Object.keys(CmdParam))
         if (v == '-' + item) {
            reading = item
            checked = true
            break
         }
      !checked && CmdParam[reading].push(v) //统计键值对
   })
   if(!fs.existsSync(CmdParam.log[0])) throw new Error('输入的日志目录不存在！')
   if (fs.readdirSync(CmdParam.log[0]).sort().reverse()[0].split('.')[0] < CmdParam.date[0])
      throw new Error('日期超出范围！（-date不会提供在日志最晚一天后的日期）');
   if (!CmdParam.date[0]) 
      return fs.readdirSync(CmdParam.log[0]).sort().reverse()[0].split('.')[0].split('-') //最大日期
   else //指定了日期
      return CmdParam.date[0].split('-')
}
function print(){
   var provincesSorted = []
   for (let item of Provinces.keys()) { //提取集合里的省份
      provincesSorted.push(item)
   }
   provincesSorted = provincesSorted.sort((a, b) => { //进行汉字拼音排序
      return prior.indexOf(a) - prior.indexOf(b)
   })
   for (let item of Object.keys(Total)) {  //统计'全国'的数据
      let sum = 0
      for (let num of Object.values(Total[item]))
         sum += num
      Total[item]['全国'] = sum
   }
   provincesSorted.unshift('全国') //确保‘全国’一定在其他省份的前面
   if (!CmdParam.type.length) CmdParam.type = ['ip', 'sp', 'cure', 'dead'] // 不指定-type即为输出全部四项
   provincesSorted.forEach((v) => {
      if (!(CmdParam.province.length == 0 || CmdParam.province.includes(v))) return //筛选-province省份
      article += `${v}`   //开始处理输出数据的附加项-type
      if (CmdParam.type.includes('ip')) article += ` 感染患者${Total.ip[v]}人`
      if (CmdParam.type.includes('sp')) article += ` 疑似患者${Total.sp[v]}人`
      if (CmdParam.type.includes('cure')) article += ` 治愈${Total.cure[v]}人`
      if (CmdParam.type.includes('dead')) article += ` 死亡${Total.dead[v]}人`
      article += `\n`
   })
   article += `// 该文档并非真实数据，仅供测试使用\n// 命令：node InfectStatistic ${cmd}`
   fs.writeFileSync(CmdParam.out[0], article, 'utf-8')  //最后写入文件
}
(module.exports = function main() {  //若要开始单元测试。。。改变arguments以及washCmdParam()
   var [y,m,d] = washCmdParam()
   for (var i = 1; i <= m; i++){ //不循环年份，因为老爷说了，19年没有叫这个名的病毒
      for (var j = 1; j <= (i != m ? MONTH[i] : d); j++) {
         appendData(`2020-${i <= 9 ? '0' + i : i}-${j <= 9 ? '0' + j : j}`)
      }
   }
   print()
})()
process.on('uncaughtException', (e) => {
   console.error('错误：', e.message)   //若要开始单元测试。。。改为article拼接
})