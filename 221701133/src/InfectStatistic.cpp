/*
	疫情统计代码
	编辑者：lhf
*/
#include <iostream>
#include <string>

using namespace std;

const string REGIONNAME[] = { "全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西",
"陕西","上海","四川","天津","西藏","新疆","云南","浙江" };//用于记录时关于地区的顺序模板
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
	friend ostream &operator<<(ostream &out,const Regions &r);//用以输出该地区的疫情信息
};

class StatisticList //疫情统计表
{
	Regions regionsList[REGIONNUM];//地区情况统计表
public:
	StatisticList();
	friend bool readLogMessage(StatisticList &slist);//读取日志文件并存储
	friend ostream &operator<<(ostream &out, const StatisticList &s);//用以输出地区列表的疫情信息
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

StatisticList::StatisticList()
{
	for (int i = 0; i < REGIONNUM; i++) //默认初始化为各个省份的所有人数参数为零
	{
		regionsList[i] = Regions(REGIONNAME[i]);
	}
}

ostream &operator<<(ostream &out, const Regions &r)//用以输出该地区的疫情信息
{
	out << r.name<<" 感染患者"<<r.confirmed<<"人 疑似患者"<<r.suspect<<"人 治"
		"愈"<<r.cure<<"人 死亡"<<r.death<<"人\n";
	return out;
}

ostream &operator<<(ostream &out, const StatisticList &s)//用以输出地区列表的疫情信息
{
	for (int i = 0; i < REGIONNUM; i++) //默认初始化为各个省份的所有人数参数为零
	{
		out << s.regionsList[i];
	}
	return out;
}

bool readLogMessage(StatisticList &slist)//读取日志文件并存储
{
	return true;
}

int main(int argc, char* argv[])
{

	std::cout << "Hello World!\n";
	return 0;
}
