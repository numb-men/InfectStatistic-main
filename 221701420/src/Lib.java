import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//链表节点
class Node {
    public int data;//记录在数组中的位置
    public int heavy;//记录权重
    public Node next;
    public Node(){
        data=-1;
    }
    public Node(int e){
        this.data = e;
    }
}
public class Lib {
    //储存省份患者信息
    class province_info{
        int heavy;//
        String name;//省份
        int infected;//感染患者
        int suspected;//疑似患者
        int cure;//治愈
        int dead;//死亡
        boolean out;//是否输出
        public province_info(String temp){
            name=temp;
            heavy=0;
            infected=0;//感染患者
            suspected=0;//疑似患者
            cure=0;//治愈
            dead=0;//死亡
            out=false;
        }
    }

    //初始化数据数组
    ArrayList<province_info> infos = new ArrayList<province_info>();
    String log=new String();//指定日志目录的位置
    String out=new String();//指定输出文件路径和文件名
    String date=new String();//指定日期
    Node list=new Node();

    ArrayList<String> type = new ArrayList<String>();//指定显示类型

    ArrayList<String> province = new ArrayList<String>();//指定显示地区

    public Lib(String[] arg) throws IOException {
        //for(String a:args) System.out.println(a);

        Scanner sc = new Scanner(System.in);
        System.out.println("输入");
        String in = sc.nextLine();
        String[] args=in.split(" ");
        for (int i=0;i<args.length;i++){
            if(args[i].startsWith("-")){
                if(args[i].equals("-log")) log=args[i+1];
                else if(args[i].equals("-out")) out=args[i+1];
                else if(args[i].equals("-date")) date=args[i+1];
                else if(args[i].equals("-type")){
                    for (int j=i+1;!args[j].startsWith("-");j++){
                        type.add(args[j]);//获取要显示的类型
                    }
                }
                else if(args[i].equals("-province")){
                    for (int j=i+1;j<args.length&&!args[j].startsWith("-");j++){
                        System.out.println(args[j]);
                        province.add(args[j]);//获取要显示的地区
                    }
                }
            }
        }


        //读取排序好的文件对象并进行处理
        File[] sort= sort_file(log);//获得排序好的文件对象
        if(date.compareTo(sort[sort.length-1].getName())>0) System.out.println("日期超出范围");
        else {
            System.out.println(sort[0].getName().split("[.]")[0]);
            for (int i=0;i<sort.length;i++) {
                String temp=(sort[i].getName().split("[.]"))[0];//获取文件对应的日期
                if(temp.compareTo(date)<=0)//如果文件日期小于等于指定日期
                    read_file(sort[i]);//读取指定的文件
                else break;
            }
            summary();
            Arrangement();
            output(out);
        }

    }
    //对指定目录下的文件按照日期进行排序
    private File[] sort_file(String log) throws IOException {
        File file=new File(log);//创建指定目录的File对象
        File[] files = file.listFiles();
        List fileList = Arrays.asList(files);

        //按照日期升序对file对象进行排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        return files;
    }

    //读取指定的文件(传入File对象)
    private void read_file(File file) throws IOException {
        InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"UTF-8");
        BufferedReader bfreader=new BufferedReader(reader);
        String line;
        while((line=bfreader.readLine())!=null) {//包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
           // System.out.println(line);
            adjust_arry(line);
            //Arrangement();
            //System.out.println();
        }
    }

    //对某行文字进行判断并对数据队列进行调整
    private void adjust_arry(String line){
            String[] lines=line.split(" ");
            if(lines[0].equals("//")) return;//省略注释部分

            //判断该条信息所对应的省份是否被创建
            boolean success=false;
            int where=0;
            for( int i=0;i<infos.size();i++){
                if(infos.get(i).name.equals(lines[0])){
                    success=true;
                    where=i;
                }
            }

            if(success==false){
                where=infos.size();
                province_info e=new province_info(lines[0]);
                infos.add(e);
            }

            //死亡或者治愈
            if (lines.length==3){
                if (lines[1].equals("死亡")){
                    int temp=trans_num(lines[2]);
                    infos.get(where).dead+=temp;
                    infos.get(where).infected-=temp;
                }
                else if (lines[1].equals("治愈")){
                    int temp=trans_num(lines[2]);
                    infos.get(where).cure+=temp;
                    infos.get(where).infected-=temp;
                }
            }
            //其他情况
            else if(lines.length==4){
                if (lines[1].equals("新增")){
                    int temp=trans_num(lines[3]);
                    if (lines[2].equals("感染患者")){//新增感染患者
                        infos.get(where).infected+=temp;
                    }
                    else {//新增疑似患者
                        infos.get(where).suspected+=temp;
                    }
                }
                else if(lines[1].equals("疑似患者")){//确认感染
                    int temp=trans_num(lines[3]);
                    infos.get(where).suspected-=temp;
                    infos.get(where).infected+=temp;
                }
                else if(lines[1].equals("排除")){
                    int temp=trans_num(lines[3]);
                    infos.get(where).suspected-=temp;
                }
            }

            //人口流动情况
            else if(lines.length==5){
                int temp=trans_num(lines[4]);//流动人数
                int to=search_province(lines[3]);//患者流向的省份
                if (lines[1].equals("感染患者")){//感染患者流动
                    infos.get(where).infected-=temp;
                    infos.get(to).infected+=temp;
                }
                else {//疑似患者流动
                    infos.get(where).suspected-=temp;
                    infos.get(to).suspected+=temp;
                }
            }
    }

    //对表示人数的字符串由String转为int
    private int trans_num(String line){
        //正则表达式提取数字
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(line);
        int num=Integer.parseInt(m.replaceAll("").trim());
        return num;
    }

    //搜寻省份对应的
    private int search_province(String line){
        int num=-1;
        for (int i=0;i<infos.size();i++){
            if (line.equals(infos.get(i).name)){
                num=i;
                break;
            }
        }
        if (num==-1){
            num=infos.size();
            province_info e=new province_info(line);
            infos.add(e);
        }
        return num;
    }

    //对现有的对象数组进行整理
    private void Arrangement(){
        set_heavy();
        Comparator<province_info> comparator = new Comparator<province_info>(){
            public int compare(province_info s1, province_info s2) {
                    return s1.heavy-s2.heavy;
            };
        };
        //对输出队列排序
        Collections.sort(infos,comparator);


    }
    //设置省份对应的权重
    private void set_heavy(){
        String names[]={
                "全国",
                "安徽",
                "北京",
                "重庆",
                "福建",
                "甘肃",
                "广东",
                "广西",
                "贵州",
                "海南",
                "河北",
                "河南",
                "黑龙江",
                "湖北",
                "湖南",
                "吉林",
                "江苏",
                "江西",
                "辽宁",
                "内蒙古",
                "宁夏",
                "青海",
                "山东",
                "山西",
                "陕西",
                "上海",
                "四川",
                "天津",
                "西藏",
                "新疆",
                "云南",
                "浙江",
        };
        for(int i=0;i<infos.size();i++){
            for (int j=0;j<names.length;j++){
                if(infos.get(i).name.equals(names[j])){
                    infos.get(i).heavy=j;
                    break;
                }
            }
        }
    }


    //统计全国的情况
    private void summary(){
        province_info all=new province_info("全国");
        for (int i=0;i<infos.size();i++){
            all.dead+= infos.get(i).dead;
            all.suspected+=infos.get(i).suspected;
            all.infected+=infos.get(i).infected;
            all.cure+=infos.get(i).cure;
        }

        infos.add(all);
    }

    //写入文件
    private void output(String out) throws IOException {
        String[] temp=out.split("\\\\");
        String filename=temp[temp.length-1];//获取地址中的文件名
        //System.out.println(filename);
        String path=out.split(filename)[0];//将地址中的文件名剔除
        File f1=new File(path);//传入文件/目录的路径
        File f2=new File(f1,filename);//第一个参数为一个目录文件，第二个参数为要在当前f1目录下要创建的文件
        PrintWriter printWriter =new PrintWriter(new FileWriter(f2,true),true);//第二个参数为true，从文件末尾写入 为false则从开头写入


        if(province.size()==0){//不指定显示地区
            if(type.size()==0){//不指定显示类别
                for (int i=0;i<infos.size();i++){
                    printWriter.print(infos.get(i).name+" ");
                    printWriter.print("感染患者"+infos.get(i).infected+"人 ");
                    printWriter.print("疑似患者"+infos.get(i).suspected+"人 ");
                    printWriter.print("治愈"+infos.get(i).cure+"人 ");
                    printWriter.print("死亡"+infos.get(i).dead+"人 \n");
                }
            }
            else{//指定显示类别
                for(int i=0;i<infos.size();i++){
                    printWriter.print(infos.get(i).name+" ");
                    for (int j=0;j<type.size();j++){
                        char od=type.get(j).charAt(0);
                        switch (od){
                            case 'i':{
                                printWriter.print("感染患者"+infos.get(i).infected+"人 ");
                                break;
                            }
                            case 's':{
                                printWriter.print("疑似患者"+infos.get(i).suspected+"人 ");
                                break;
                            }
                            case 'c':{
                                printWriter.print("治愈"+infos.get(i).cure+"人 ");
                                break;
                            }
                            case 'd':{
                                printWriter.print("死亡"+infos.get(i).dead+"人 ");
                                break;
                            }
                        }
                    }
                    printWriter.println();//行末换号
                }
            }
        }
        else {//指定显示地区
            for(int i=0;i<province.size();i++){
                boolean get=false;
                for (int j = 0; j < infos.size(); j++) {
                    if (province.get(i).equals(infos.get(j).name)) {
                            get=true;//该省份的信息存在
                            break;
                    }
                }
                if(get==false){//该省份的信息不存在
                    province_info tenp = new province_info(province.get(i));
                    infos.add(tenp);
                }
            }
            Arrangement();//增加新的项目后重新排序

            //记录需要输出的地区的数据项
            for (int i = 0; i < province.size(); i++) {
                for (int j = 0; j < infos.size(); j++) {
                    if (province.get(i).equals(infos.get(j).name)) {
                        infos.get(j).out=true;
                        break;
                    }
                }
            }
          /*  //如果想要显示的地区信息不存在 创建一个空数据项
            if (t.size() < province.size()) {
                for (int i = 0; i < province.size(); i++) {
                    if (!province.get(i).equals("none")) {
                        province_info tenp = new province_info(province.get(i));
                        infos.add(tenp);
                        t.add(infos.size() - 1);
                    }
                }
            }*/
            if(type.size()==0) {//不指定显示类别
                //输出需要输出的地区情况
                for (int i = 0; i < infos.size(); i++) {
                    if(infos.get(i).out==true){
                        printWriter.print(infos.get(i).name + " ");
                        printWriter.print("感染患者" + infos.get(i).infected + "人 ");
                        printWriter.print("疑似患者" + infos.get(i).suspected + "人 ");
                        printWriter.print("治愈" + infos.get(i).cure + "人 ");
                        printWriter.print("死亡" + infos.get(i).dead + "人 \n");
                    }

                }
            }
            else{//指定显示类别
                for(int i=0;i<infos.size();i++){
                    if(infos.get(i).out==true){
                        printWriter.print(infos.get(i).name + " ");
                        for (int j=0;j<type.size();j++){

                            char od=type.get(j).charAt(0);
                            switch (od){
                                case 'i':{
                                    printWriter.print("感染患者" + infos.get(i).infected + "人 ");
                                    break;
                                }
                                case 's':{
                                    printWriter.print("疑似患者" + infos.get(i).suspected + "人 ");
                                    break;
                                }
                                case 'c':{
                                    printWriter.print("治愈" + infos.get(i).cure + "人 ");
                                    break;
                                }
                                case 'd':{
                                    printWriter.print("死亡" + infos.get(i).dead + "人 ");
                                    break;
                                }
                            }
                        }
                        printWriter.println();//行末换号
                    }
                }
            }
        }


        printWriter.close();//记得关闭输入流
    }
}



