package bg.sofia.uni.fmi.food.analyzer.server.core.factories;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.EMPTY_STRING;

public class CommandFactoryImplTest {
    private CommandFactory factory;

    @Before
    public void before() {
        factory = new CommandFactoryImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCommandWithInvalidArgumentsThrowsIllegalArgumentException() {
        // Arrange & Act
        Command command = factory.createCommand(EMPTY_STRING, null, null);

        // Assert
        Assert.assertNotNull(command);
    }

    @Test
    public void testCreateCommandGetFoodWithValidArguments() {
        // Arrange & Act
        Command command = factory.createCommand(GET_FOOD_COMMAND, null, null);

        // Assert
        Assert.assertNotNull(command);
    }

    @Test
    public void testCreateCommandGetFoodReportWithValidArguments() {
        // Arrange & Act
        Command command = factory.createCommand(GET_FOOD_REPORT_COMMAND, null, null);

        // Assert
        Assert.assertNotNull(command);
    }

    @Test
    public void testCreateCommandGetFoodReportByBarcodeWithValidArguments() {
        // Arrange & Act
        Command command = factory.createCommand(GET_FOOD_BY_BARCODE_COMMAND, null, null);

        // Assert
        Assert.assertNotNull(command);
    }
}
