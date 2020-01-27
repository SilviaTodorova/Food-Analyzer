package bg.sofia.uni.fmi.food.analyzer.server.core.providers;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.GET_FOOD_COMMAND;
import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.GET_FOOD_REPORT_COMMAND;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.DELIMITER;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.EMPTY_STRING;

public class CommandParserImplTest {
    private static final String SIZE_OF_PARAMETERS = "Size of parameters";
    private static final String PARSE_PARAMETERS = "Parse parameters";
    private static final String PARSE_COMMAND = "Parse command";

    private static final String NAME = "beef noodle soup";
    private static final String FDC_ID = "415269";

    private CommandParser parser;

    @Before
    public void before() {
        parser = new CommandParserImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCommandWithInValidArgumentsThrowsIllegalArgumentException() {
        // Arrange, Act & Assert
        String command = parser.parseCommand(EMPTY_STRING);
    }

    @Test
    public void testParseCommandWithValidArguments() {
        // Arrange & Act
        String command = parser.parseCommand(GET_FOOD_COMMAND + DELIMITER + NAME);

        // Assert
        Assert.assertEquals(PARSE_COMMAND, GET_FOOD_COMMAND, command);
    }

    @Test
    public void testParseParametersWithValidArguments() {
        // Arrange & Act
        List<String> params = parser.parseParameters(
                GET_FOOD_REPORT_COMMAND
                        + DELIMITER
                        + FDC_ID);

        // Assert
        Assert.assertEquals(SIZE_OF_PARAMETERS,1, params.size());
        Assert.assertEquals(PARSE_PARAMETERS, params.get(0), FDC_ID);
    }
}
