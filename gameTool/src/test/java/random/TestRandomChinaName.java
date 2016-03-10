package random;/**
 * Created by zql on 15/12/17.
 */

import com.hahazql.tools.random.name.china.Name;
import com.hahazql.tools.random.name.china.RandChinaName;
import com.hahazql.tools.random.name.china.RandomChinaEntity;
import org.junit.Test;

/**
 * Created by zql on 15/12/17.
 *
 * @className TestRandomChinaName
 * @classUse
 */
public class TestRandomChinaName {
    @Test
    public void testRandomChinaName() {
        RandChinaName.load();
        for (int i = 0; i < 10000; i++) {
            Name n = RandomChinaEntity.getName();
            String name = "邱" + n.getName();
            System.out.println(name);
            System.out.println(n.getDes());
        }
    }
}
