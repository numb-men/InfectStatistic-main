/*
	疫情统计代码
	编辑者：lhf
*/

#include <iostream>
#include <string>
#include <fstream>
#include <vector>


using namespace std;


const string REGIONNAME[] = { "全国","安徽","北京","重庆","福建","甘肃","广东",
"广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西",
"辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏",
"新疆","云南","浙江"};//用于记录时关于地区的顺序模板
const int REGIONNUM = sizeof(REGIONNAME) / sizeof(REGIONNAME[0]);//地区数

const string LIST="list";//支持的命令 
const string LOG="-log";//命令行参数,关于指定日志目录的位置
const string DATE="-date";//命令行参数,关于处理指定日期之前的所有log文件
const string TYPE="-type";//命令行参数,关于要输出的患者的情况的方式 
const string OUT="-out";//命令行参数,关于指定输出文件路径和文件名
const string PROVINCE="-province";//命令行参数,关于指定输出时列出的省
const string POSIBLEP[]={LOG,DATE,TYPE,OUT,PROVINCE};//可能的命令行参数，用于检测匹配 
const inr POSIBLEPNUM=sizeof(POSIBLEP) / sizeof(POSIBLEP[0]);//支持的命令行参数数 



//地区类
class Regions 
{
	string name;//地区名
	int confirmed;//确诊人数
	int suspect;//疑似人数
	int cure;//治愈人数
	int death;//死亡人数
public:
	Regions(void);
	Regions(string n);
	Regions(string n, int c, int s, int cue, int d);
	bool confirmedPeopleAdd(const int &n);//增加确诊人数
	bool suspectPeopleAdd(const int &n);//增加疑似人数
	bool curePeopleAdd(const int &n);//增加治愈人数
	bool deathPeopleAdd(const int &n);//增加死亡人数
	bool selectiveOutput(const string *s);//选择性输出 
};

//疫情统计表
class StatisticList 
{
	Regions regionsList[REGIONNUM];//地区情况统计表
public:
	StatisticList();
	bool selectiveOutput(const string *ts,const string *rs);//选择性输出 
	friend bool readLogMessage(StatisticList &slist);//读取日志文件并存储
};




Regions::Regions(string n, int c, int s, int cue, int d)
{
	name = n;
	confirmed = c;
	suspect = s;
	cure = cue;
	death = d;
}


Regions::Regions(string n)
{
	name = n;
	confirmed = 0;
	suspect = 0;
	cure = 0;
	death = 0;
}


Regions::Regions(void)
{
	name = "error";
	confirmed = 0;
	suspect = 0;
	cure = 0;
	death = 0;
}


//确诊人数增加
bool Regions::confirmedPeopleAdd(const int &n)
{
	if (confirmed += n)
	{
		return true;
	}
	else 
	{
		return false;
	}
}


//增加疑似人数
bool Regions::suspectPeopleAdd(const int &n)
{
	if (suspect += n) 
	{
		return true;
	}
	else 
	{
		return false;
	}
}


//增加治愈人数
bool Regions::curePeopleAdd(const int &n)
{
	if (cure += n) 
	{
		return true;
	}
	else 
	{
		return false;
	}
}


//增加死亡人数
bool Regions::deathPeopleAdd(const int &n)
{
	if (death += n) 
	{
		return true;
	}
	else 
	{
		return false;
	}
}


//选择性输出 
//将只输出s所指向的字符串数组相关类型信息内容 
bool Regions::selectiveOutput(const string *s)
{
	
} 

	
StatisticList::StatisticList()
{
	for (int i = 0; i < REGIONNUM; i++) //默认初始化为各个省份的所有人数参数为零
	{
		regionsList[i] = Regions(REGIONNAME[i]);
	}
}


//选择性输出 
//ts: 将只输出ts所指向的字符串数组相关的各个地区的疫情情况的各类型信息内容 
//rs: 将只输出rs所指向的字符串数组相关的地区的疫情情况
bool StatisticList::selectiveOutput(const string *ts,const string *rs)
{
	
	
}


//判断使用的命令是否合理 
//c: 需要判断的命令
//返回值：true命令无误，false命令有误 
bool isSuportComand(string c){
	if(c==LIST) 
	{
		return true;
	}
	else
	{
		cout<<"暂时只支持list命令\n";
		return false;
	}
}


//处理传入的命令行参数
//ac:需要处理的命令行参数的字符串数+2后的值（因为包含了文件本身和list占用两个位置） 
//arv:命令行参数字符串 （包含了文件本身和list占用两个位置,在0、1的位置）
//返回noErorOcured：false表示过程中出错，true表示无错执行 
bool dealParameters(int ac,const char* arv[]) 
{
	bool noErorOcured=true;//用以返回是否无问题处理命令行参数 
	int pnum;//用于记录多参数的命令的参数个数
	vector typeParas,proParas;//记录type和privince的相关参数 
	string timeP=null;//用于记录截止日期参数  
	string fileLocation=null;//记录日志文件所在位置 
	ofstream out;//输出重定向
	 
	for(int i=2;i<ac;i+=(pnum+2))
	{
		pnum=0;
		switch(argv[i]){
			case LOG:
				fileLocation=argv[i+1];
				break;
			case DATE:
				timeP=argv[i+1];
				break;
			case TYPE:
				noErorOcured=dealMulParameter(i,ac,arv,typeParas,pnum);
				break;
			case OUT:
				noErorOcured=dealOutParameters(out,argv[i+1]);
				break;	
			case PROVINCE:
				noErorOcured=dealMulParameter(i,ac,arv,proParas,pnum);
				break;
			default://与已设命令行参数不匹配，有错误存在 
				noErorOcured=false;
				break;
		}
		if(erorOcured)
		{
			return erorOcured;//出现问题立即终止
		}
	}
	
	return true;
}


//处理可能携带多参数的命令行参数 
//ini: 在字符串数组中起始开始统计的位置 
//max: 字符串数组大小 
//arv: 要进行分析的数组
//paras: 要进行存储参数的数组 
//forward: 记录前进的步数 
bool dealMulParameter(int ini,const int &max,const char* arv[],vector &paras,int &forward)
{
	int num=0;//用于记录循环次数，避免产生死循环 
	while(1)
	{
		num++;
		nowLocation=ini+forward;//当前在数组中所处位置 
		if(num>100)
		{
			cout<<"处理多参数时出现死循环\n";
			return false;
			break; 
		}
		if(nowLocation>=max)
		{
			break;
		}
		else{
			for(int j=0;j<POSIBLEPNUM;j++)
			{
				if(arv[nowLocation]==POSIBLEP[j])//说明本次参数记录结束了 
				{
					break;
				}
				else
				{
					paras.push_back(arv[nowLocation]);
					forward++;
				}
			} 
		}
	}
	return true;
}


//处理out的参数
//out：要重定向的流
//s: 指定的数据流向的文件地址
//返回为true表示文件流打开没问题，false表示出错 
bool dealOutParameters(ofstream &out,string s)
{
	out.open(s);
	if(!out)
	{
		cout<<"文件重定向失败！！！\n";
		return false;
	}
	else{
		return true;
	}
}


//判断日志是否为要读取的
bool isRightTimeLog(const string &s)
{

}


//读取日志文件并存储
bool readLogMessage(StatisticList &slist)
{
	
	
}


//开始读取文件并输出
//vector typeParas,proParas记录type和privince的相关参数 
//string timeP=null用于记录截止日期参数  
//string fileLocation=null记录日志文件所在位置 
//ofstream out输出重定向
bool PerformOptns(const vector &type)
{
	
} 




int main(int argc, char* argv[])
{
	if(argc>1)//有输入参数时才检查 
	{
		if(isSuportComand(argv[1]))//只有支持的命令才会执行操作 
		{
			if(!dealParameters(argc,argv))
			{
				cout<<"过程出错\n";	
			}
		}
	}
	else
	{
		cout<<"未输入参数！\n";	
	}
	cout<<"over\n"; 
	return 0;
}
