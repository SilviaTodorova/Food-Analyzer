package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.ImageNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.GlobalConstants.GET_DATA_FROM_API;
import static bg.sofia.uni.fmi.food.analyzer.server.GlobalConstants.GET_DATA_FROM_FILE_SYSTEM;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;
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
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithLessArguments()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        testCommand.execute(Collections.emptyList());
    }

    @Test
    public void testExecuteCommandWithDataFromFileSystem()
            throws FoodNotFoundException,
            FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistByName(NAME))
                .thenReturn(true);

        when(repositoryMock.getFoodByName(NAME))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(Collections.singletonList(NAME));

        // Assert
        Assert.assertEquals(GET_DATA_FROM_FILE_SYSTEM, RESPONSE, result);
    }

    @Test(expected = FoodNotFoundException.class)
    public void testExecuteCommandThrowsFoodNotFoundException()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistByName(NAME))
                .thenReturn(false);

        when(clientMock.getFoodByName(NAME))
                .thenReturn(new ArrayList<>());

        // Act & Assert
        String result = testCommand.execute(Collections.singletonList(NAME));
    }

    @Test
    public void testExecuteCommandWithDataFromApi()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistByName(NAME))
                .thenReturn(false);

        Food food = new Food(FDC_ID, GTIN_UPC, DESCRIPTION, EMPTY_STRING, null);
        when(clientMock.getFoodByName(NAME))
                .thenReturn(List.of(food));

        // Act
        String result = testCommand.execute(Collections.singletonList(NAME));

        // Assert
        Assert.assertEquals(GET_DATA_FROM_API, food.toString().trim(), result);
    }
}
