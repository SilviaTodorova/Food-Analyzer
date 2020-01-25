package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.NO_FOODS_WERE_FOUND_MESSAGE;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.DESC_FIELD;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetFoodReportTest {
    private static final long FDC_ID = 105025;
    private static final String RESPONSE = "some text";
    private static final String DESCRIPTION = "some description";
    private static final String INGREDIENTS = "some ingredients";
    private static final double FAT_VALUE = 10.56;

    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClient clientMock;

    private Command testCommand;

    @Before
    public void before() {
        testCommand = new GetFoodReport(repositoryMock, clientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithLessArguments() throws FoodIdNotFoundException, FoodBarcodeNotFoundException, FoodNotFoundException {
        // Arrange, Act & Assert
        testCommand.execute(asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithMoreArguments() throws FoodIdNotFoundException, FoodBarcodeNotFoundException, FoodNotFoundException {
        // Arrange, Act & Assert
        testCommand.execute(asList(new String[2]));
    }

    @Test
    public void testExecuteCommandWithDataFromCache() throws FoodIdNotFoundException, FoodNotFoundException, FoodBarcodeNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistById(FDC_ID))
                .thenReturn(true);

        when(repositoryMock.getFoodById(FDC_ID))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(String.valueOf(FDC_ID)));

        // Assert
        Assert.assertEquals(formatResponse(String.valueOf(FDC_ID), RESPONSE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromApiNoResult() throws FoodIdNotFoundException, FoodBarcodeNotFoundException, FoodNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistById(FDC_ID))
                .thenReturn(false);

        when(clientMock.getFoodReportById(10))
                .thenReturn(null);

        // Act
        String result = testCommand.execute(asList(String.valueOf(FDC_ID)));

        // Assert
        Assert.assertEquals(formatResponse(String.valueOf(FDC_ID), NO_FOODS_WERE_FOUND_MESSAGE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromApi() throws FoodIdNotFoundException, FoodBarcodeNotFoundException, FoodNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistById(FDC_ID))
                .thenReturn(false);

        FoodReport report = new FoodReport(FDC_ID, DESCRIPTION, INGREDIENTS);
        report.addNutrient(new Nutrient(FAT_FIELD, FAT_VALUE));

        when(clientMock.getFoodReportById(FDC_ID))
                .thenReturn(report);

        // Act
        String result = testCommand.execute(asList(String.valueOf(FDC_ID)));

        // Assert
        StringBuilder builder = new StringBuilder();
        append(builder, DESC_FIELD, DESCRIPTION);
        append(builder, INGREDIENTS_FIELD, INGREDIENTS);
        append(builder, FAT_FIELD, String.format("%.2f", FAT_VALUE));
        Assert.assertEquals(formatResponse(String.valueOf(FDC_ID), builder.toString()), result);
    }

    private String formatResponse(String param, String response) {
        StringBuilder builder = new StringBuilder();
        builder.append("-------- Search Results for fdcId: ")
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
