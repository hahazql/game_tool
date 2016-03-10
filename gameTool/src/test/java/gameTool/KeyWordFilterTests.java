package gameTool;

import com.hahazql.tools.wordsfilter.KeyWordsACFilter;
import org.junit.Test;

public class KeyWordFilterTests {
    @Test
    public void testFilter() {
        KeyWordsACFilter filter = KeyWordsACFilter.getInstance('*', "cccc");
        String out = filter.filt("ccccc");
        System.out.println(out);
    }
}
