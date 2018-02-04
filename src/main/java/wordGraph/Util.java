package wordGraph;

import java.util.List;
import java.util.StringJoiner;

public class Util {
    public static String listOfListToStr(List<List<String>> res) {
        StringJoiner sj = new StringJoiner("\n");
        for (List<String> list : res) {
            String path = String.join("->", list);
            sj.add(path);
        }
        return sj.toString();
    }
}
