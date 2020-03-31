/*
	疫情统计代码
	编辑者：lhf
	参数举例：list -date 2020-01-22 -log E:\log\ -out E:\log\output.txt 
*/

#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <io.h>
#include <regex>
#include <sstream>


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
const int POSIBLEPNUM=sizeof(POSIBLEP) / sizeof(POSIBLEP[0]);//支持的命令行参数数 

const string FILEFORMAT="*.log.txt";//要读取的日志文件的后缀格式 

const int TMNUM=8;//设置的正则表达式的个数 
//读取日志文件时用到的正则表达式 
const string typeMatch[TMNUM]={
"(.*) 新增 感染患者 (.*)人",
"(.*) 新增 疑似患者 (.*)人",
"(.*) 感染患者 流入 (.*) (.*)人",
"(.*) 疑似患者 流入 (.*) (.*)人",
"(.*) 死亡 (.*)人",
"(.*) 治愈 (.*)人",
"(.*) 疑似患者 确诊感染 (.*)人",
"(.*) 排除 疑似患者 (.*)人"
};

//地区类
class Regions;
//疫情统计表
class StatisticList;


//判断使用的命令是否合理 
bool isSuportComand(const string &c);
//处理传入的命令行参数
bool dealParameters(int ac,char* arv[]);
//处理可能携带多参数的命令行参数 
bool dealMulParameter(int ini,const int &max,char* arv[],vector<string> &paras,
int &forward);
//处理out的参数
bool dealOutParameters(ofstream &out,string s);
//判断日志是否为要读取的
bool isRightTimeLog(const string &s,const string &timeP);
//读取日志文件并存储
bool readLogMessage(StatisticList &slist,const vector<string> &files);
//读取一份日志文件并存储
bool readOneFile(StatisticList &slist,const string &file);
//进行读取而来的字符串的正则表达式匹配
bool doStringSmatch(const string &target,int &tyNum,vector<string> &strSon);
//获取指定的文件夹中的所有文件路径名
void getFilesName(const string &path,const string &timeP,vector<string> &files);
//开始读取文件
bool performOptns(const string &fLocation,const string &timeP,StatisticList &sList);
//看与哪个地区名匹配 
int matchRegion(const string &s);
//将int类型转换为字符串
string itos(int num); 
//查看是否是规定的命令行参数之一
bool isPosibleP(const string &s);


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
	bool selectiveOutput(const vector<string> &s,ofstream &out);//选择性输出 
};

//疫情统计表
class StatisticList 
{
	Regions regionsList[REGIONNUM];//地区情况统计表
public:
	StatisticList();
	bool selectiveOutput(const vector<string> &ts,const vector<string> &rs,ofstream &out);//选择性输出
	//将获取到的信息放入疫情地区列表中
	friend bool setMessage(StatisticList &slist,vector<string> strMesg,const int &tyNum);
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
/*
int confirmed;//确诊人数
int suspect;//疑似人数
int cure;//治愈人数
int death;//死亡人数
*/
//句式举例 "全国 感染患者22人 疑似患者25人 治愈10人 死亡2人"
bool Regions::selectiveOutput(const vector<string> &s,ofstream &out)
{
	string cp,sp,cup,dp;//用于整理句式 
	string isM;//用于存储int转变成string的内容 
	string outputPatern=name;//最后输出的句式 
	
	for(int i=0;i<4;i++)
	{
		isM=itos(confirmed);
		cp=" 感染患者"+isM+"人";
		isM=itos(suspect);
		sp=" 疑似患者"+isM+"人";
		isM=itos(cure);
		cup=" 治愈"+isM+"人";
		isM=itos(death);
		dp=" 死亡"+isM+"人";
	}
	if(s.size()==0)//默认输出为输出全部信息 
	{
		outputPatern=outputPatern+cp+sp+cup+dp;
	}
	else
	{
		for(int i=0;i<s.size();i++)
		{
			if(s[i]=="ip"){outputPatern+=cp;}
			else if(s[i]=="sp"){outputPatern+=sp;}
			else if(s[i]=="cure"){outputPatern+=cup;}
			else if(s[i]=="dead"){outputPatern+=dp;}
			else{
				cout<<"地区内容信息输出error!\n";
				break;
			}
		}
	}
	out<<outputPatern<<"\n";
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
bool StatisticList::selectiveOutput(const vector<string> &ts,
const vector<string> &rs,ofstream &out)
{
	int needOutputRegion[REGIONNUM]={0};//零表示屏蔽输出
	if(rs.size()==0)//默认情况 ，输出全部疫情情况 
	{
		for(int i=0;i<REGIONNUM;i++)
		{
			regionsList[i].selectiveOutput(ts,out);
		}
	}
	else
	{
		for(int i=0;i<rs.size();i++)
		{
			needOutputRegion[matchRegion(rs[i])]=1;
		} 
		for(int j=0;j<REGIONNUM;j++)
		{
			if(needOutputRegion[j])//只输出提到的地区 
			{
				regionsList[j].selectiveOutput(ts,out);
			}
		}
	}
	out<<"// 该文档并非真实数据，仅供测试使用\n";
}


//判断使用的命令是否合理 
//c: 需要判断的命令
//返回值：true命令无误，false命令有误 
bool isSuportComand(const string &c){
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
bool dealParameters(int ac,char* arv[]) 
{
	bool noErorOcured=true;//用以返回是否无问题处理命令行参数 
	int pnum;//用于记录多参数的命令的参数个数
	vector<string> typeParas,proParas;//记录type和privince的相关参数 
	string timeP="null";//用于记录截止日期参数  
	string fileLocation="null";//记录日志文件所在位置 
	StatisticList sList;//地区疫情统计列表 
	ofstream out;//输出重定向
	 
	for(int i=2;i<ac;i+=(pnum+2))
	{
		pnum=0;
		if(arv[i]==LOG){
			fileLocation=arv[i+1];
		}
		else if(arv[i]==DATE){
			timeP=arv[i+1];
		}
		else if(arv[i]==TYPE){
			noErorOcured=dealMulParameter(i+1,ac,arv,typeParas,pnum);
			//cout<<"typeParas的数据数："<<typeParas.size()<<endl;//测试排错用 
		}
		else if(arv[i]==OUT){
			noErorOcured=dealOutParameters(out,arv[i+1]);
		}
		else if(arv[i]==PROVINCE){
			noErorOcured=dealMulParameter(i+1,ac,arv,proParas,pnum);
			//cout<<"proParas的数据数："<<proParas.size()<<endl;//测试排错用
			//cout<<"pnum："<<pnum<<endl;//测试排错用
		}
		else{//与已设命令行参数不匹配，有错误存在
			cout<<"命令行参数有出错:";
			cout<<arv[i]<<endl;
			noErorOcured=false;
		}
		if(!noErorOcured)
		{
			return noErorOcured;//出现问题立即终止
		}
	}
	performOptns(fileLocation,timeP,sList);//读入操作 
	sList.selectiveOutput(typeParas,proParas,out);//输出操作 
	out.close();
	return true;
}


//查看是否是规定的命令行参数之一
bool isPosibleP(const string &s)
{
	for(int j=0;j<POSIBLEPNUM;j++)
	{
		if(s==POSIBLEP[j]) 
		{
			return true;
		}
	} 
	return false;
}


//处理可能携带多参数的命令行参数 
//ini: 在字符串数组中起始开始统计的位置 
//max: 字符串数组大小 
//arv: 要进行分析的数组
//paras: 要进行存储参数的数组 
//forward: 记录前进的步数 
bool dealMulParameter(int ini,const int &max,char* arv[],vector<string> &paras,
int &forward)
{
	int num=0;//用于记录循环次数，避免产生死循环 
	int nowLocation;//记录当前在数组中所处位置 
	while(1)
	{
		num++;
		nowLocation=ini+forward;//当前在数组中所处位置 
		if(num>100)
		{
			cout<<"处理多参数时出现死循环\n";
			return false;
		}
		if(nowLocation>=max){break;}
		else
		{
			if(!isPosibleP(arv[nowLocation]))
			{
				paras.push_back(arv[nowLocation]);
				forward++;
			}
			else//说明本次参数记录结束了
			{
				break;
			}
		}
	}
	forward--;
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
//s：需要判断正确与否的字符串
//timeP：需要满足的时间限制  
//返回：true可行，false不可行 
bool isRightTimeLog(const string &s,const string &timeP)
{
	string ss=s.substr(0,10);
	if(ss<=timeP)
	{
		return true;
	}
	else
	{
		return false;
	}
}


//读取日志文件并存储
//slist: 疫情统计表 
//files：存储要读取的文件的文件列表路径 
bool readLogMessage(StatisticList &slist,const vector<string> &files)
{
	bool noOcuredEror=true;//用于记录是否有错误出现 
	for(int i=0;i<files.size();i++)  
	{  
    	readOneFile(slist,files[i]);
	}
	return noOcuredEror;
}


//读取一份日志文件并存储
//slist: 疫情统计表 
//files：存储要读取的文件的文件路径 
bool readOneFile(StatisticList &slist,const string &file)
{
	vector<string> strSon;
	int tyNum;
	ifstream in;//输入流对象 
	char s[100];//临时存储获取到的一行语句
    string s1;//存储获取到的一行语句 
    
   // int times=0;//测试用，统计读取的行数 
	
	in.open(file);
	if(!in)
	{
		cout<<"文件打开失败！\n";
		return false;
	}
	while(in.getline(s,100))
	{
		if((s[0]==s[1])&&(s[0]=='/')) break;//读取到注释位置了 
   		s1=s;
    	doStringSmatch(s1,tyNum,strSon);
    	setMessage(slist,strSon,tyNum);
    	strSon.clear();
    	//times++; //测试用 
	}
//	cout<<"file: "<<file<<"行数："<<times<<endl;//测试用 
	in.close();
}


//看与哪个地区名匹配 
//返回地区对应的位置（以REGIONNAME[]为参考） 
int matchRegion(const string &s){
	for (int i=0;i<REGIONNUM;i++){
		if(s==REGIONNAME[i]){
			//cout<<"当前匹配到的地区为："<<s<<" 编号："<<i<<endl;
			return i;
		}
	}
	cout<<"s: "<<s<<endl;
	cout<<"匹配城市出错！\n";
	return -1;
}


//将获取到的信息放入疫情地区列表中
//strMesg：要存储的信息 
//tyNum： 信息是属于哪一类型 
//StatisticList友元 
/*
const string typeMatch[TMNUM]={
"(.*) 新增 感染患者 (.*)人",
"(.*) 新增 疑似患者 (.*)人",
"(.*) 感染患者 流入 (.*) (.*)人",
"(.*) 疑似患者 流入 (.*) (.*)人",
"(.*) 死亡 (.*)人",
"(.*) 治愈 (.*)人",
"(.*) 疑似患者 确诊感染 (.*)人",
"(.*) 排除 疑似患者 (.*)人"
};
*/
bool setMessage(StatisticList &slist,vector<string> strMesg,const int &tyNum) 
{
	int reNum=-1;//存储地区编号 （以在REGIONNAME[]中的位置为参考） 
	int breakNum=0;//等于1时跳出循环体 
	//测试排错用 
/*	cout<<"要存储的信息的内容：";
	for(int i=0;i<strMesg.size();i++){
		cout<<"内容"<<i<<": "<<strMesg[i]; 
	}
	cout<<endl;*/
	
	for (int i=1;i<REGIONNUM;i++)//0是全国
	{
		breakNum=0;
		switch(tyNum)
		{
			case 0:
				reNum=matchRegion(strMesg[0]);
				//cout<<"感染患者增加:"<<atoi(strMesg[1].c_str())<<endl;//测试排错用 
				slist.regionsList[0].confirmedPeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[reNum].confirmedPeopleAdd(atoi(strMesg[1].c_str()));
				breakNum=1;
				break;
			case 1:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[0].suspectPeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[reNum].suspectPeopleAdd(atoi(strMesg[1].c_str()));
				breakNum=1;
				break;
			case 2:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[reNum].confirmedPeopleAdd((0-atoi(strMesg[2].c_str())));
				reNum=matchRegion(strMesg[1]);
				slist.regionsList[reNum].confirmedPeopleAdd(atoi(strMesg[2].c_str()));
				breakNum=1;
				break;
			case 3:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[reNum].suspectPeopleAdd((0-atoi(strMesg[2].c_str())));
				reNum=matchRegion(strMesg[1]);
				slist.regionsList[reNum].suspectPeopleAdd(atoi(strMesg[2].c_str()));
				breakNum=1;
				break;
			case 4:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[0].deathPeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[0].confirmedPeopleAdd((0-atoi(strMesg[1].c_str())));
				slist.regionsList[reNum].deathPeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[reNum].confirmedPeopleAdd((0-atoi(strMesg[1].c_str())));
				breakNum=1;
				break;
			case 5:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[0].curePeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[0].confirmedPeopleAdd((0-atoi(strMesg[1].c_str())));
				slist.regionsList[reNum].curePeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[reNum].confirmedPeopleAdd((0-atoi(strMesg[1].c_str())));
				breakNum=1;
				break;
			case 6:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[0].confirmedPeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[0].suspectPeopleAdd((0-atoi(strMesg[1].c_str())));
				slist.regionsList[reNum].confirmedPeopleAdd(atoi(strMesg[1].c_str()));
				slist.regionsList[reNum].suspectPeopleAdd((0-atoi(strMesg[1].c_str())));
				breakNum=1;
				break;
			case 7:
				reNum=matchRegion(strMesg[0]);
				slist.regionsList[0].suspectPeopleAdd((0-atoi(strMesg[1].c_str())));
				slist.regionsList[reNum].suspectPeopleAdd((0-atoi(strMesg[1].c_str())));
				breakNum=1;
				break;
			default:
				cout<<"无匹配项，文件内容出错!\n";
				breakNum=1;
				break;
				
		}
		if(breakNum){
			break;
		}
	}
}



//进行读取而来的字符串的正则表达式匹配
//target：需要进行获取关键子串的字符串
//tyNum：记录匹配到的是哪一类正则表达式 
//strSon：记录收集的子串 
bool doStringSmatch(const string &target,int &tyNum,vector<string> &strSon)
{
	bool noOcuredEror=false;//用于记录是否有错误出现 
	
	regex e[TMNUM]=
			{
				regex(typeMatch[0]),regex(typeMatch[1]),regex(typeMatch[2]),
				regex(typeMatch[3]),regex(typeMatch[4]),
				regex(typeMatch[5]),regex(typeMatch[6]),regex(typeMatch[7])
			};
	
    smatch sm;
    
    for(int i=0;i<TMNUM;i++)
	{
		regex_search(target,sm,e[i]);
		if(sm.size()){
			for (int j=1;j<sm.size();j++)//收集匹配得到的关键子串,排除0位置的本身 
			{
       			strSon.push_back(sm[j].str());
    		}
    		tyNum=i;
			noOcuredEror=true;//只有出现匹配，才证明没问题 
			break;	
		}
	}
	
	return noOcuredEror; 
}


//获取指定的文件夹中的所有文件路径名
//path：为执行程序时提供的命令行参数关于日志文件所在文件夹
//timeP：提供时间限制 
//files：存储文件夹中所有可能要读取的文件的文件路径 
void getFilesName(const string &path,const string &timeP,vector<string> &files)
{
    intptr_t hFile=0;   //win10 //文件信息 
    struct _finddata_t fileinfo;  
    string p; 
    
// "*"是指读取文件夹下的所有类型的文件，若想读取特定类型的文件，以png为例，则用"*.png" 
    if((hFile=_findfirst((p.assign(path)).append(FILEFORMAT).c_str(),&fileinfo))!=-1)  
    {  
		if(isRightTimeLog(fileinfo.name,timeP))
		{
			files.push_back(path+fileinfo.name); 
        	while(_findnext(hFile,&fileinfo)==0) 
       		{
				if(isRightTimeLog(fileinfo.name,timeP))
				{
        			files.push_back(path+fileinfo.name); 
				}
				else
				{
					break;	
				}
        	}
		}
    }  
}


//开始读取文件
//timeP 截止日期参数  
//fLocation 日志文件所在位置 
//slist 疫情统计表 
bool performOptns(const string &fLocation,const string &timeP,StatisticList &sList)
{
	vector<string> fName;//用于记录文件夹中符合要求查找的文件名
	getFilesName(fLocation,timeP,fName); 
	readLogMessage(sList,fName);
} 


//将int类型转换为字符串
string itos(int num)
{
 	stringstream ss;
 	ss<<num; 
 	string s1 = ss.str();
 	return s1;
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
	return 0;
}
