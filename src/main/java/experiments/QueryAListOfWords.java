package experiments;

import commands.CommandManger;
import result.Result;
import wordGraph.WordGraph;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryAListOfWords {
    public static void main(String[] args) throws Exception {
        String inputPath = "src/main/resources/ExpData/queryList.txt";
        String output = "src/main/resources/ExpData/queryResult.txt";
        List<String> finalRes = new ArrayList<>();
        WordGraph wg = new WordGraph();
        CommandManger cmdManger = new CommandManger(wg);
        Stream<String> inputStream = Files.lines(Paths.get(inputPath));
        Set<String> phrases = inputStream.filter(line -> line.contains("[REQ]"))
                .map(p -> p.substring(p.indexOf(']') + 1).trim()).collect(Collectors.toSet());
        for (String phrase : phrases) {
            String command = "query " + phrase;
            Result res = cmdManger.execut(command);
            finalRes.add(res.toString());
        }
        Files.write(Paths.get(output), finalRes);

    }
}
