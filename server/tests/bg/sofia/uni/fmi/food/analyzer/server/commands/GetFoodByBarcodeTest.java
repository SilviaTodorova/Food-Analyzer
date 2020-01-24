package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClientImpl;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetFoodByBarcodeTest {
    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClientImpl clientMock;

    private Command testCommand;

    @Before
    public void before() {
        testCommand = new GetFoodByBarcode(repositoryMock, clientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_should_throwException_when_passedLessArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_should_throwException_when_passedMoreArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList(new String[3]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_should_x() {
        when(repositoryMock.checkFoodExistByBarcode("10"))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode("10"))
                .thenReturn("apple");

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("10"));
    }

    @Test
    public void execute_should_xx() {
        when(repositoryMock.checkFoodExistByBarcode("10"))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode("10"))
                .thenReturn("apple");

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("--code=10"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for barcode: 10 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("apple");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_y() {
        when(repositoryMock.checkFoodExistByBarcode("887434010245"))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode("887434010245"))
                .thenReturn("apple");

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("--img="+"D:\\JavaProjects\\ModernJavaTechnology\\food-analyzer\\server\\resources\\apple.png"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for barcode: 887434010245 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("apple");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_xy() {
        when(repositoryMock.checkFoodExistByBarcode("10"))
                .thenReturn(false);

        when(clientMock.getFoodByBarcode("10"))
                .thenReturn(new ArrayList<>());

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("--code=10"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for barcode: 10 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("No foods were found!");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_yy() {
        when(repositoryMock.checkFoodExistByBarcode("10"))
                .thenReturn(false);

        List<Food> foods = List.of(new Food(10,"desc", "123"));

        when(clientMock.getFoodByBarcode("10"))
                .thenReturn(foods);

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("--code=10"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for barcode: 10 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("fdcId: 10")
                .append(System.lineSeparator())
                .append("gtinUpc: 123")
                .append(System.lineSeparator())
                .append("description: desc")
                .append(System.lineSeparator());
        Assert.assertEquals(builder.toString(), result);
    }
}
