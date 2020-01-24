package bg.sofia.uni.fmi.food.analyzer.server.commands.contracts;

import java.util.List;

public interface Command {
    String execute(List<String> parameters);
}
