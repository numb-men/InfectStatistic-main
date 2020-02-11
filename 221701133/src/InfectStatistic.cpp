/*
	疫情统计代码
	编辑者：lhf
*/
#include <iostream>
#include <string>

using namespace std;

//用于记录时关于地区的顺序模板
const string REGIONNAME[] = { "全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西",
"陕西","上海","四川","天津","西藏","新疆","云南","浙江" };
const int REGIONNUM = sizeof(REGIONNAME) / sizeof(REGIONNAME[0]);//地区数

int main(int argc, char* argv[])
{

    std::cout << "Hello World!\n"; 
	return 0;
}

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
};

class StatisticList //疫情统计表
{
	Regions regionsList[REGIONNUM];//地区情况统计表
public:
	StatisticList();
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

StatisticList::StatisticList()
{
	for (int i = 0; i < REGIONNUM; i++) {
		regionsList[i] = Regions(REGIONNAME[i]);
	}
}
