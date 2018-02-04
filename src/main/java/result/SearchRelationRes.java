package result;

import wordGraph.Util;

import java.util.List;

public class SearchRelationRes extends Result{
    List<List<String>> paths;

    public SearchRelationRes(List<List<String>> paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return Util.listOfListToStr(paths);
    }
}
