import java.util.Comparator;

public class province implements Comparator<province> {
    int num;
    String name;
    int quantify=0;
    public int compare(province o1,province o2){
        return -(o1.quantify-o2.quantify);
    }
}
