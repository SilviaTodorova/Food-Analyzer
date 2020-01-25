package bg.sofia.uni.fmi.food.analyzer.server.commands;

import bg.sofia.uni.fmi.food.analyzer.server.commands.contracts.Command;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
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

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.NO_FOODS_WERE_FOUND_MESSAGE;
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
    private static final String IMG = "apple.png";

    private static String imgPath;

    @Mock
    private FoodRepository repositoryMock;

    @Mock
    private FoodClient clientMock;

    private Command testCommand;

    @BeforeClass
    public static void beforeClass() {
        Path path = FileSystems.getDefault().getPath(RESOURCES_DIR).toAbsolutePath();
        imgPath = Paths.get(path.toString(), IMG).toString();
    }

    @Before
    public void before() {
        testCommand = new GetFoodByBarcode(repositoryMock, clientMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithLessArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithMoreArguments() {
        // Arrange, Act & Assert
        testCommand.execute(asList(new String[3]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteCommandThrowsIllegalArgumentExceptionWithInvalidArgument() {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode(BARCODE))
                .thenReturn(RESPONSE);

        // Act & Assert
        String result = testCommand.execute(asList(BARCODE));
    }

    @Test
    public void testExecuteCommandWithDataFromCacheFlagCode() {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode(BARCODE))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(CODE_FLAG + BARCODE));

        // Assert
        Assert.assertEquals(formatResponse(BARCODE, RESPONSE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromCacheFlagImg() {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(true);

        when(repositoryMock.getFoodByBarcode(BARCODE))
                .thenReturn(RESPONSE);

        // Act
        String result = testCommand.execute(asList(CODE_IMG + imgPath));

        // Assert
        Assert.assertEquals(formatResponse(BARCODE, RESPONSE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromApiNoResult() {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(false);

        when(clientMock.getFoodByBarcode(BARCODE))
                .thenReturn(asList());

        // Act
        String result = testCommand.execute(asList(CODE_FLAG + BARCODE));

        // Assert
        Assert.assertEquals(formatResponse(BARCODE, NO_FOODS_WERE_FOUND_MESSAGE), result);
    }

    @Test
    public void testExecuteCommandWithDataFromApi() {
        // Arrange
        when(repositoryMock.checkFoodExistByBarcode(BARCODE))
                .thenReturn(false);

        when(clientMock.getFoodByBarcode(BARCODE))
                .thenReturn(List.of(new Food(FDC_ID, DESCRIPTION, GTIN_UPC)));

        // Act
        String result = testCommand.execute(asList(CODE_FLAG + BARCODE));

        // Assert
        StringBuilder builder = new StringBuilder();
        append(builder, FDC_ID_FIELD, String.valueOf(FDC_ID));
        append(builder, GTIN_UPC_FIELD, GTIN_UPC);
        append(builder, DESC_FIELD, DESCRIPTION);
        Assert.assertEquals(formatResponse(BARCODE, builder.toString()), result);
    }

    private String formatResponse(String param, String response) {
        StringBuilder builder = new StringBuilder();
        builder.append("-------- Search Results for barcode: ")
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
