package bg.sofia.uni.fmi.food.analyzer.server.core.providers;

import bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.DELIMITER;

public class CommandParserImplTest {
    private static final String NAME = "beef noodle soup";
    private static final String FDC_ID = "415269";

    private CommandParser parser;

    @Before
    public void before() {
        parser = new CommandParserImpl();
    }

    @Test
    public void testParseCommandWithValidArguments() {
        // Arrange & Act
        String command = parser.parseCommand(CommandConstants.GET_FOOD_COMMAND + DELIMITER + NAME);

        // Assert
        Assert.assertEquals(CommandConstants.GET_FOOD_COMMAND, command);
    }

    @Test
    public void testParseParametersWithValidArguments() {
        // Arrange & Act
        List<String> params = parser.parseParameters(CommandConstants.GET_FOOD_REPORT_COMMAND + DELIMITER + FDC_ID);

        // Assert
        Assert.assertEquals(1, params.size());
        Assert.assertEquals(params.get(0), FDC_ID);
    }
}
