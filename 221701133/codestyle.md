# 代码风格  
## 1 标识符命名  
（1）局部变量采用驼峰命名方法。  
（2）当只是声明普通的局部变量时，允许使用简单的a,b,c，此类单个字母。若用途一致，可以使用a1、a2此类命名法。  
（3）函数名采用驼峰命名方法。  
（4）全局变量采用驼峰命名方法，应突出用途，如sumPrime突出求素数总和。  
（5）结构(类型)，枚举(类型)采用首字母大写，然后使用驼峰表示法，如StuScore为学生成绩的结构。  
（6）常量采用全大写的表示法，应突出用途来命名，如YMAX表示年份最大值，YMIN年份最小值等等。    
（7）都允许使用拼音的名字（但是应该为极少使用）。  
（8）标识符缩写规则：除最后一个单词外截取单词的前三个字母，最后一个单词写全称，如finPrime由find和prime组成，意思是寻找素数，creList由creat和list组成，意思是创造一个链表。  
（9）当标识符缩写冲突时，可以选择在其中一个的标识符名称内多加一个单词缩写，以此区分。  
（10）标识符长度规定15个字符以内。  
## 2 常量定义
（1）一般情况下允许使用神秘的数值（如：3.14等），如：整个程序只是在一处地方，在这一处求圆面积时使用到3.14，则允许。但是应该为极少量如此使用。  
（2）若是多次出现同一个神秘数值，如整个程序使用了3次3.14，则必须进行常量定义。  
（3）用const定义常量。  
（4）#define和const定义常量不应同时混合使用来声明常量，特别是同一语句块里的混合使用。  
## 3 缩进格式  
（1）缩进的字符数都为4，用Tab缩进。  
（2）每行含缩进不允许超过85个字符。  
（3）分割原则为在逗号运算符或在逻辑运算符后分割至下一行，如在“，”后面截断到下一行。  
（4）对于函数内容与函数名、结构内容与结构名、if的内容和if、for的内容和for、while的内容和while等这些在编写内容的情况时，凡是有另起一行都应时刻保持与名称保持缩进状态。  
## 4 大括号的位置  
（1）左、右大括号放置位置的规定为绝大多数情况下左括号另起一行，右括号另起一行。  
（2）若括号内容极短则括号内容和右括号跟随不另起一行的左括号放置在同一行。  
（3）对if等语句和函数（定义）没有放置位置的区别，都保持上述两点的规定进行编程。  
## 5 函数  
（1）函数名非必须动词打头，但是对于一些确实有进行一些行为的函数，还是必须根据其用途来定义函数名。  
（2）函数应为先在主函数前声明，然后在主函数之后对函数定义，不能在主函数前面直接定义。  
（3）即使是对于没有返回值的函数，也必须加上void，如void priError (void)，不得没写。  
（4）即便是对于没有参数的函数，也必须加上void，如void priError (void)，不得随意留空。main函数无用到命令行参数时可以例外，有用到时必须加上argc和argv[]。  
（5）除非是极为简单的函数，例如可能只有三四行的函数；否则不允许使用超过一个的return。  
（6）对于一些使用多次（超过三次）的语句或者多行运算，除非特殊，否则应该转换成函数的形式。  
（7）除了输出选择菜单等特殊函数，其余的函数的有效语句行总和一律不超过25行。  
（8）带有is开头的函数名的函数，返回值一律以1为是，以0为否。不得混乱使用，以免混淆。  
（9）函数内部的缩进层次应少于五层。缩进多时应转变为小的函数来实现相同的功能。  
（10）函数内部的局部变量不得超过7个，否则必须分割为数个函数来实现功能。  
## 6 注释  
（1）程序的注释位置为首行，内容为简述程序目的或功能。  
（2）函数的注释位置为函数声明及定义的同一行后面，内容为简述功能，以及可能附带有各个参数的含义。  
（3）当函数的注释在函数声明或者变量声明的同一行无法完全书写时，允许直接在上一行书写。  
（4）对于一些可能会引起误解的没有使用常量定义的神秘数值，必须在使用的地方后面加上注释说明。  
（5）对于一些申明的变量，应该尽量说明用意。特别是一些在后续的语句中带有++、--之类运算符的变量，必须说明用途。但是对于简单明了的或者只是一次性使用的变量，可以放松要求。  
## 7 空格与空行  
（1）两个函数定义之间必须加空行。  
（2）头文件的警卫和宏定义等头文件里面书写的内容之间必须加一行的空行。  
（3）结构定义，枚举定义，函数声明，变量声明，常量声明这些不同语句块之间应加上空行。  
（4）除必须加空格的情况外（如：return和0之间），下列为使用空格的情况：  
1)#include和<之间要加空格；  
2)while/for和（之间加；  
3)if和（之间不加；  
4)函数名和（之间加；  
5)双目运算符等的两边不加。  
6)struct和{之间不加。  
7)=两边不加。  
8)else和{之间不加。  
9)>、<左右不加空格  
10）对于指针的声明统一使用int *p的形式，即在int后面加一个空格，*后面不加空格。  
11）!=、+、-均在符号左右不加空格。  
12）>=、<=均在符号左右不加空格。  
## 8 一些方面的要求  
（1）不能使用goto；  
（2）尽量少用或者不用continue  
（3）对于指针变量的定义，如果不是马上会用到或者是函数的参数，则应该令其=NULL。  
（4）多个结构的定义，多个函数的声明，同一函数内的变量声明，同一作用域的常量声明应分别集中放置于一块区域。  
（5）对于同一体系下的语句，应该对齐，例如同一个switch里的case和default应该对齐。  
（6）自己创建的头文件必须加上警卫，以免重复定义，不得出现没有警卫的头文件。  
（7）尽量少用递归函数，如果转化为循环迭代的形式书写时可行且不会复杂化，则不得使用递归函数。  
（8）使用枚举变量时不能赋予超出枚举常量代表的值的数，更不能对这样的超出范围的值进行枚举的强制转化。  
（9）强制转化的书写统一使用这样的格式：（类型说明符）（表达式或者值）  
（10）不能出现函数return的返回值与函数声明时定义的返回值两者类型不一致的情况。要么修改函数声明的定义，要么就必须对return后的返回值进行强制转化。  
（11）结构指针在使用时，统一使用如：p->num这样的格式书写，不用(*p).num的格式，更不能随意混用。  
（12）在对文件进行操作之前，必须先检查文件是否可以打开文件  
如果不行则先报错处理或者为避免出错而直接结束程序。不得不先判断而直接使用文件并且操作。然后也要进行文件的关闭处理，即fclose(fp)，不得在对文件的操作结束后不关闭文件。  
（13）有申请空间的地方，则必须也有释放空间的地方。  
（14）宏定义函数必须在所有地方都加上括号。  
## 9 类
（1）若有使用构造函数，则必须在后面一同使用析构函数。  
（2）在利用类的构造函数对对象进行初始化操作时，使用如：ClassMate s(5)；的格式书写。  
（3）在构建一个类时，统一先写私有的再写公有的或者保护的。不能在同一个程序里混乱使用。  
（4）友元函数统一在类的公有的语句块里声明。  
（5）运算符的重载应与原来的运算符作用是同一种，如：将实数的加号重载为复数的加号，而不是重载为复数的减号。即不得随意重载运算符。  