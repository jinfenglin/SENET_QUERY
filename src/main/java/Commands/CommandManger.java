package Commands;

import WordGraph.WordGraph;
import org.apache.commons.cli.ParseException;

import java.util.HashMap;
import java.util.Map;

public class CommandManger {
    Map<String, Command> commandMap;
    WordGraph wg;

    public CommandManger(WordGraph wg) throws ParseException {
        commandMap = new HashMap<String, Command>();
        this.wg = wg;
        commandMap.put("query", new QueryRelationCmd());
    }

    public void execut(String cmd) {
        String op = getOperation(cmd);
        String args = getOptions(cmd);
        if (commandMap.containsKey(op)) {
            Command cmdObj = commandMap.get(op);
            cmdObj.execution(wg, args);
        } else {
            System.out.println(String.format("Operation %s is not supported.", op));
        }
    }

    private String getOperation(String cmd) {
        return cmd.split(" ")[0];
    }

    private String getOptions(String cmd) {
        String op = getOperation(cmd);
        return cmd.substring(op.length());
    }
}
