package bg.sofia.uni.fmi.food.analyzer.server.core.factories;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.CommandFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandFactoryImplTest {
    private CommandFactory factory;

    @Before
    public void before() {
        factory = new CommandFactoryImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCommandShouldThrowExceptionWhenPassedInvalidCommand() {
        // Arrange & Act
        Command command = factory.createCommand("", null, null);

        // Assert
        Assert.assertTrue(command instanceof Command);
    }

    @Test
    public void createCommandGetFoodShouldCreateNewCommandWhenInputIsValid() {
        // Arrange & Act
        Command command = factory.createCommand("get-food", null, null);

        // Assert
        Assert.assertTrue(command instanceof Command);
    }

    @Test
    public void createCommandGetFoodReportShouldCreateNewCommandWhenInputIsValid() {
        // Arrange & Act
        Command command = factory.createCommand("get-food-report", null, null);

        // Assert
        Assert.assertTrue(command instanceof Command);
    }

    @Test
    public void createCommandGetFoodByBarcodeShouldCreateNewCommandWhenInputIsValid() {
        // Arrange & Act
        Command command = factory.createCommand("get-food-by-barcode", null, null);

        // Assert
        Assert.assertTrue(command instanceof Command);
    }
}
