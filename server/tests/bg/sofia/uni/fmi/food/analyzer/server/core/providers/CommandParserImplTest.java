package bg.sofia.uni.fmi.food.analyzer.server.core.providers;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CommandParserImplTest {
    private CommandParser parser;

    @Before
    public void before() {
        parser = new CommandParserImpl();
    }

    @Test
    public void parseCommandShouldParseCommandWhenPassedInvalidCommand() {
        // Arrange & Act
        String command = parser.parseCommand("get-food beef noodle soup");

        // Assert
        Assert.assertEquals("get-food", command);
    }

    @Test
    public void parseParametersShouldParseParametersWhenPassedInvalidCommand() {
        // Arrange & Act
        List<String> params = parser.parseParameters("get-food-report 415269");

        // Assert
        Assert.assertEquals(1, params.size());
        Assert.assertEquals(params.get(0), "415269");
    }
}
