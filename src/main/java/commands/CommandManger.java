package commands;

import wordGraph.WordGraph;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import result.Result;

public class CommandManger {
    Map<String, Command> commandMap;
    WordGraph wg;

    public CommandManger(WordGraph wg) throws ParseException {
        commandMap = new HashMap<>();
        this.wg = wg;
        commandMap.put("query", new QueryRelationCmd()); //Search the information of the given phrase.
        commandMap.put("search", new SearchRelationCmd()); //Search the path from first phrase to second phrase.
    }

    public Result execut(String cmd) {
        String[] args = getArgs(cmd);
        if (args.length == 0)
            return null;
        String op = args[0];
        if (commandMap.containsKey(op)) {
            Command cmdObj = commandMap.get(op);
            return cmdObj.execution(wg, args);
        } else {
            System.out.println(String.format("Operation %s is not supported.", op));
        }
        return null;
    }

    private String[] getArgs(String cmd) {
        String[] args = cmd.split(" ");
        return Arrays.stream(args).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    public void run() {
        System.out.println("SE Bash v0.1");
        while (true) {
            System.out.print(">");
            Scanner input = new Scanner(System.in);
            String line = input.nextLine();
            Result res = execut(line);
            if (res != null) {
                System.out.println(res.toString());
            }
        }
    }
}
