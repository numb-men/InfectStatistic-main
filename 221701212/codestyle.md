## 代码风格

- 缩进：
	每次缩进以两个空格为单位
- 变量命名：
	*变量由单词全拼、单词缩写、单词与数字表示
	*多个单词组合时第二个单词首字母大写以区分
		如：provinceArray
	*缩写取前三或前四个字母(不包括tmp、flg、sum等常用特殊含义缩写)
	*数字“2”、“4”出现在单词与单词之间有特殊含义：2表示to、4表示for
		如：cmd4type、flg4province等
	*静态变量大写
		如：static int PROVINCE_NUM = 32;
- 每行最多字符数：
	每行最多80字符
- 函数最大行数
	函数最大120行
- 函数、类命名
	*变量由单词全拼、单词缩写、单词与数字表示
	*多个单词组合时以下划线区分
		如：init_argument()
	*缩写取前三或前四个字母(不包括tmp、flg、sum等常用特殊含义缩写)
- 常量
	常量大写，最后一个字母后加下划线
- 空行规则
	*变量声明与其他语句之间空一行，如：
		FileInputStream fis = new FileInputStream(path);   
  		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");   
  		BufferedReader br = new BufferedReader(isr);  
  		String dataLine = br.readLine();
  		
  		while(dataLine != null) {
  			if(!dataLine.startsWith("//")) {
  				handle_data(dataLine);
  			}
  			dataLine = br.readLine();
  		}
	*函数体与函数体之间空一行
- 注释规则
	行注释以//形式直接加在语句后，超出行80字符限制另起一行注释
	块注释在函数体前或类前按以下方式添加
	/*************************
		块注释
	**************************/			
- 操作符前后空格
	*所有操作符前后均空一格，如：a = b + c;
- 其他规则
	*花括号括起语句块时，左花括号不独占一行，而是空一格紧跟于语句后，如：
    		for(int j = 0; j < cmd4type.size(); j ++) {
    			if(cmd4type.get(j).equals("ip")) {
    				out += " 感染患者" + ip[i] + "人";
    			} else if(cmd4type.get(j).equals("sp")) {
    				out += " 疑似患者" + sp[i] + "人";
    			} else if(cmd4type.get(j).equals("cure")) {
    				out += " 治愈" + cure[i] + "人";
    			} else if(cmd4type.get(j).equals("dead")) {
    				out += " 死亡" + dead[i] + "人";
    			}     		 
    		}
	*若循环语句、条件语句后仅有一条代码，也必须由花括号括起
	*空代码块的花括号空一格后直接接在循环语句、条件语句后，不额外占一行，如：
		if(a == 5) {};
