import com.flink.unit.PropertiesUnit;

/**
 * @Author LT-0024
 * @Date 2020/7/13 16:53
 * @Version 1.0
 */
public class ContentTest {
    public static void main(String[] args) {
        String name = PropertiesUnit.getStringByKey("name", "config.properties");
        System.out.println(name);
    }
}
