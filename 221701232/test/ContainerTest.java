import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    @Test
    void sortByProvince() {
        Container container = new Container();
        String[] province = { "北京", "重庆", "广州", "哈尔滨", "合肥", "南京", "厦门", "上海",
         "乌鲁木齐", "武汉", "长沙", "郑州"};
        for (String pro : province) {
            Record record = new Record();
            record.setProvinceName(pro);
            container.addRecord(record);
        }
        container.sortByProvince();
        Map<String, Record> stringRecordMap = container.getRecordMap();
        for (Map.Entry<String, Record> entry : stringRecordMap.entrySet()) {
            System.out.print(entry.getKey() + ", ");
        }
    }
}