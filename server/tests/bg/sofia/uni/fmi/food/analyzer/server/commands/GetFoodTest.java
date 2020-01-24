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
public class GetFoodTest {
    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClientImpl clientMock;

    private Command testCommand;

    @Before
    public void before() {
        testCommand = new GetFood(repositoryMock, clientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_should_throwException_when_passedLessArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList());
    }

    @Test
    public void execute_should_x() {
        when(repositoryMock.checkFoodExistByName("apple"))
                .thenReturn(true);

        when(repositoryMock.getFoodByName("apple"))
                .thenReturn("apple");

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("apple"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for apple --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("apple");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_y() {
        when(repositoryMock.checkFoodExistByName("apple"))
                .thenReturn(false);

        when(clientMock.getFoodByName("apple"))
                .thenReturn(new ArrayList<>());

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("apple"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for apple --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("No foods were found!");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_yy() {
        when(repositoryMock.checkFoodExistByName("apple"))
                .thenReturn(false);

        List<Food> foods = List.of(new Food(10,"desc", "123"));

        when(clientMock.getFoodByName("apple"))
                .thenReturn(foods);

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("apple"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for apple --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("fdcId: 10")
                .append(System.lineSeparator())
                .append("gtinUpc: 123")
                .append(System.lineSeparator())
                .append("description: desc");

        Assert.assertEquals(builder.toString(), result);
    }


}
