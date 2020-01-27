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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static bg.sofia.uni.fmi.food.analyzer.server.GlobalConstants.GET_DATA_FROM_API;
import static bg.sofia.uni.fmi.food.analyzer.server.GlobalConstants.GET_DATA_FROM_FILE_SYSTEM;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetFoodByBarcodeTest {
    private static final int FDC_ID = 10;
    private static final String DESCRIPTION = "some description";
    private static final String GTIN_UPC = "1020202505";
    private static final String BARCODE = "887434010245";
    private static final String RESPONSE = "some text";
    private static final String CODE_FLAG = "--code=";
    private static final String CODE_IMG = "--img=";
    private static final String IMG_APPLE = "apple.png";
    private static final String IMG_BANANA = "banana.jpg";

    private static String imgPath;
    private static String fakeImagePath;
    private static String fakePath;

    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClient clientMock;

    private Command testCommand;

    @BeforeClass
    public static void beforeClass() {
        Path path = FileSystems.getDefault().getPath(RESOURCES_DIR).toAbsolutePath();
        imgPath = Paths.get(path.toString(), IMG_APPLE).toString();
        fakeImagePath = Paths.get(path.toString(), IMG_BANANA).toString();
        fakePath = Paths.get(path.toString(), IMG_BANANA + IMG_BANANA).toString();
    }

    @Before
    public void before() {
        testCommand = new GetFoodByBarcode(repositoryMock, clientMock);
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
        testCommand.execute(asList(new String[3]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithInvalidArgument()
            throws FoodBarcodeNotFoundException,
            FoodIdNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        String result = testCommand.execute(asList(BARCODE));
    }

    @Test
    public void testExecuteCommandWithDataFromFileSystemFlagCode()
            throws FoodBarcodeNotFoundException,
            FoodIdNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode(BARCODE))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(CODE_FLAG + BARCODE));

        // Assert
        Assert.assertEquals(GET_DATA_FROM_FILE_SYSTEM, RESPONSE, result);
    }

    @Test
    public void testExecuteCommandWithDataFromFileSystemFlagImg()
            throws FoodBarcodeNotFoundException,
            FoodIdNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode(BARCODE))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(CODE_IMG + imgPath));

        // Assert
        Assert.assertEquals(GET_DATA_FROM_FILE_SYSTEM, RESPONSE, result);
    }

    @Test(expected = FoodBarcodeNotFoundException.class)
    public void testExecuteCommandThrowsFoodBarcodeNotFoundException()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        String result = testCommand.execute(asList(CODE_FLAG + BARCODE));
    }

    @Test(expected = ImageNotFoundException.class)
    public void testExecuteCommandThrowsImageNotFoundException()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        String result = testCommand.execute(asList(CODE_IMG + fakeImagePath));
    }

    @Test(expected = ImageNotFoundException.class)
    public void testExecuteCommandThrowsImageNotFoundExceptionInvalidPathImage()
            throws FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange, Act & Assert
        String result = testCommand.execute(asList(CODE_IMG + fakePath));
    }

    @Test
    public void testExecuteCommandWithDataFromApi() throws
            FoodIdNotFoundException,
            FoodBarcodeNotFoundException,
            FoodNotFoundException,
            ImageNotFoundException {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(false);

        Food food = new Food(FDC_ID, GTIN_UPC, DESCRIPTION, EMPTY_STRING, null);
        when(clientMock.getFoodByBarcode(BARCODE))
                .thenReturn(List.of(food));

        // Act
        String result = testCommand.execute(asList(CODE_FLAG + BARCODE));

        // Assert
        Assert.assertEquals(GET_DATA_FROM_API,
                food.toString().trim(),
                result);
    }
}
