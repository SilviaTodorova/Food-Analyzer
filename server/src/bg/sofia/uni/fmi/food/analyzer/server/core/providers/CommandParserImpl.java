package bg.sofia.uni.fmi.food.analyzer.server.core.providers;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.DELIMITER;

public class CommandParserImpl implements CommandParser {
    @Override
    public String parseCommand(String fullCommand) {
        return fullCommand.split(DELIMITER)[0];
    }

    public List<String> parseParameters(String fullCommand) {
        String[] commandParts = fullCommand.split(DELIMITER);
        List<String> parameters = new ArrayList<>(Arrays.asList(commandParts).subList(1, commandParts.length));
        return parameters;
    }
}
