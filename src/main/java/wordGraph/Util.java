package wordGraph;

import java.util.List;

public class Util {
    public static void printListOfList(List<List<String>> res) {
        for (List<String> list : res) {
            String path = String.join("->", list);
            System.out.println(path);
        }
    }
}
