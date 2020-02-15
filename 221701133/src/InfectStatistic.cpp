/*
	疫情统计代码
	编辑者：lhf
*/
#include <iostream>
#include <string>
#include <fstream>

using namespace std;

const string REGIONNAME[] = { "全国","安徽","北京","重庆","福建","甘肃","广东",
"广西","贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西",
"辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏",
"新疆","云南","浙江"};//用于记录时关于地区的顺序模板
const int REGIONNUM = sizeof(REGIONNAME) / sizeof(REGIONNAME[0]);//地区数

class Regions //地区类
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

class StatisticList //疫情统计表
{
	Regions regionsList[REGIONNUM];//地区情况统计表
public:
	StatisticList();
	bool selectiveOutput(const string *s);//选择性输出 
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

bool Regions::confirmedPeopleAdd(const int &n)//确诊人数增加
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

bool Regions::suspectPeopleAdd(const int &n)//增加疑似人数
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

bool Regions::curePeopleAdd(const int &n)//增加治愈人数
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

bool Regions::deathPeopleAdd(const int &n)//增加死亡人数
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

bool Regions::selectiveOutput(const string *s)//选择性输出 
{
	
} 
	
StatisticList::StatisticList()
{
	for (int i = 0; i < REGIONNUM; i++) //默认初始化为各个省份的所有人数参数为零
	{
		regionsList[i] = Regions(REGIONNAME[i]);
	}
}

bool StatisticList::selectiveOutput(const string *s)//选择性输出 
{
	
}

bool isSuportComand(string c){//判断使用的命令是否合理 
	if(c=="list") 
	{
		return true;
	}
	else
	{
		cout<<"暂时只支持list命令\n";
		return false;
	}
}

bool dealParameters(int ac, char* arv[], ofstream &out)//处理传入的命令行参数 
{
	int statistic[]={0,0,0,0,0};//为零表示传入的命令行参数没有涉及对应参数设置 
	for(int i=2;i<ac-1;i+=2)
	{
		if(arv[i]=="-log")
		{
			statistic[0]=1;
			dealLogParameters(argv[i+1]);
		}
		else if(arv[i]=="-date")
		{
			statistic[1]=1;
			dealDateParameters(argv[i+1]);
		}
		else if(arv[i]=="-type")
		{
			statistic[2]=1;
			dealTypeParameters(argv[i+1]);
		}
		else if(arv[i]=="-out")
		{
			statistic[3]=1;
			
		}
		else if(arv[i]=="-province")
		{
			statistic[4]=1;
			
		}
		else
		{
			cout<<"传入命令有误！\n"
			return false;
		}
	}
}

bool dealLogParameters(string p)//处理log的参数
{
	
}

bool dealDateParameters(string p)//处理date的参数
{
	
}

int dealTypeParameters(string p)//处理type的参数
{
	
}

bool dealProvinceParameters(string p)//处理province的参数
{
	
}

bool isRightTimeLog(const string &s)//判断日志是否为要读取的
{

}

bool readLogMessage(StatisticList &slist)//读取日志文件并存储
{
	
	return true;
}

int main(int argc, char* argv[])
{
	if(argc>1)//有输入参数时才检查 
	{
		if(isSuportComand(argv[1]))//只有支持的命令才会执行操作 
		{
			dealParameters(string p);
		}
	}
	else
	{
		cout<<"未输入参数！\n";	
	}
	cout<<"over\n"; 
	return 0;
}
