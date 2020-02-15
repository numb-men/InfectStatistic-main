import java.util.Comparator;

public class kind implements Comparator<kind> {
    int num;
    String name;
    int quantify=0;
    public int compare(kind o1,kind o2) {//自定义排序逻辑
       return -(o1.quantify-o2.quantify);//以传入的Cell的横坐标由小到大进行排序
    }
}
