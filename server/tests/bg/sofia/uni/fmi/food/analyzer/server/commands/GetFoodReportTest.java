package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.ImageNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;
import bg.sofia.uni.fmi.food.analyzer.server.models.Value;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static bg.sofia.uni.fmi.food.analyzer.server.GlobalConstants.GET_DATA_FROM_API;
import static bg.sofia.uni.fmi.food.analyzer.server.GlobalConstants.GET_DATA_FROM_FILE_SYSTEM;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetFoodReportTest {
    private static final long FDC_ID = 105025;
    private static final String RESPONSE = "some text";
    private static final String DESCRIPTION = "some description";
    private static final String INGREDIENTS = "some ingredients";
    private static final double NUTRIENT_VALUE = 10.56;

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
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithLessArguments()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        testCommand.execute(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithMoreArguments()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        testCommand.execute(asList(new String[2]));
    }

    @Test
    public void testExecuteCommandWithDataFromFileSystem()
            throws FoodIdNotFoundException,
            FoodNotFoundException,
            FoodBarcodeNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistById(FDC_ID))
                .thenReturn(true);

        when(repositoryMock.getFoodById(FDC_ID))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(String.valueOf(FDC_ID)));

        // Assert
        Assert.assertEquals(GET_DATA_FROM_FILE_SYSTEM, RESPONSE, result);
    }

    @Test(expected = FoodIdNotFoundException.class)
    public void testExecuteCommandThrowsFoodIdNotFoundException()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        String result = testCommand.execute(asList(String.valueOf(FDC_ID)));
    }

    @Test
    public void testExecuteCommandWithDataFromApi()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistById(FDC_ID))
                .thenReturn(false);

        Nutrient nutrient = new Nutrient();
        nutrient.setFat(new Value(NUTRIENT_VALUE));
        nutrient.setCalories(new Value(NUTRIENT_VALUE));
        nutrient.setCarbohydrates(new Value(NUTRIENT_VALUE));
        nutrient.setFiber(new Value(NUTRIENT_VALUE));
        nutrient.setProtein(new Value(NUTRIENT_VALUE));
        Food food = new Food(FDC_ID, EMPTY_STRING, DESCRIPTION, INGREDIENTS, nutrient);

        when(clientMock.getFoodReportById(FDC_ID))
                .thenReturn(food);

        // Act
        String result = testCommand.execute(asList(String.valueOf(FDC_ID)));

        // Assert
        StringBuilder builder = new StringBuilder();
        builder.append(food);
        builder.append(System.lineSeparator());
        builder.append(nutrient);
        Assert.assertEquals(GET_DATA_FROM_API, builder.toString(), result);
    }
}
