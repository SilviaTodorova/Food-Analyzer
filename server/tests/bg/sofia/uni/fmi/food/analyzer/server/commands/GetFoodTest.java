package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
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

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.NO_FOODS_WERE_FOUND_MESSAGE;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetFoodTest {
    private static final String NAME = "beef noodle soup";
    private static final String RESPONSE = "some text";
    private static final int FDC_ID = 10;
    private static final String DESCRIPTION = "some description";
    private static final String GTIN_UPC = "1020202505";

    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClient clientMock;

    private Command testCommand;

    @Before
    public void before() {
        testCommand = new GetFood(repositoryMock, clientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithLessArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList());
    }

    @Test
    public void testExecuteCommandWithDataFromCache() {
        // Arrange
        when(repositoryMock.checkFoodExistByName(NAME))
                .thenReturn(true);

        when(repositoryMock.getFoodByName(NAME))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(NAME));

        // Assert
        Assert.assertEquals(formatResponse(NAME, RESPONSE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromApiNoResult() {
        // Arrange
        when(repositoryMock.checkFoodExistByName(NAME))
                .thenReturn(false);

        when(clientMock.getFoodByName(NAME))
                .thenReturn(new ArrayList<>());

        // Act
        String result = testCommand.execute(asList(NAME));
        Assert.assertEquals(formatResponse(NAME, NO_FOODS_WERE_FOUND_MESSAGE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromApi() {
        // Arrange
        when(repositoryMock.checkFoodExistByName(NAME))
                .thenReturn(false);

        when(clientMock.getFoodByName(NAME))
                .thenReturn(List.of(new Food(FDC_ID, DESCRIPTION, GTIN_UPC)));

        // Act
        String result = testCommand.execute(asList(NAME));

        // Assert
        StringBuilder builder = new StringBuilder();
        append(builder, FDC_ID_FIELD, String.valueOf(FDC_ID));
        append(builder, GTIN_UPC_FIELD, GTIN_UPC);
        append(builder, DESC_FIELD, DESCRIPTION);
        Assert.assertEquals(formatResponse(NAME, builder.toString().trim()), result);
    }

    private String formatResponse(String param, String response) {
        StringBuilder builder = new StringBuilder();
        builder.append("-------- Search Results for ")
                .append(param)
                .append(" --------")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(response);

        return builder.toString();
    }

    private void append(StringBuilder builder, String field, String value) {
        if (!IS_NULL_OR_EMPTY_FIELD_PREDICATE.test(value)) {
            builder.append(field).append(": ").append(value).append(System.lineSeparator());
        }
    }
}
