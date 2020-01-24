package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.clients.FoodClientImpl;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetFoodReportTest {
    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClientImpl clientMock;

    private Command testCommand;

    @Before
    public void before() {
        testCommand = new GetFoodReport(repositoryMock, clientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_should_throwException_when_passedLessArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_should_throwException_when_passedMoreArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList(new String[2]));
    }

    @Test
    public void execute_should_x() {
        when(repositoryMock.checkFoodExistById(Long.parseLong("10")))
                .thenReturn(true);

        when(repositoryMock.getFoodById(Long.parseLong("10")))
                .thenReturn("apple");

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("10"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for fdcId: 10 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("apple");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_y() {
        when(repositoryMock.checkFoodExistById(Long.parseLong("10")))
                .thenReturn(false);

        when(clientMock.getFoodReportById(10))
                .thenReturn(null);

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("10"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for fdcId: 10 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("No foods were found!");
        Assert.assertEquals(builder.toString(), result);
    }

    @Test
    public void execute_should_yy() {
        when(repositoryMock.checkFoodExistById(Long.parseLong("10")))
                .thenReturn(false);

        FoodReport report = new FoodReport(10, "desc", "xxx");
        report.addNutrient(new Nutrient("xx", 10));
        when(clientMock.getFoodReportById(10))
                .thenReturn(report);

        // Arrange, Act & Assert
        String result = testCommand.execute(asList("10"));
        StringBuilder builder = new StringBuilder()
                .append("-------- Search Results for fdcId: 10 --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("description: desc")
                .append(System.lineSeparator())
                .append("ingredients: xxx")
                .append(System.lineSeparator())
                .append("xx: 10,00")
                .append(System.lineSeparator());
        Assert.assertEquals(builder.toString(), result);
    }
}
