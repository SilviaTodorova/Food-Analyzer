package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.core.repositories.FoodRepositoryImpl;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;

public class FoodRepositoryImplTest {
    private static final String SAVE_FOOD_REPORT_BY_ID = "Save food report by id";
    private static final String SAVE_FOODS_BY_NAME = "Save foods by name";
    private static final String SAVE_FOODS_BY_BARCODE = "Save foods by barcode";

    private static final String GET_FOOD_REPORT_BY_ID = "Get food report by id";
    private static final String GET_FOODS_BY_NAME = "Get foods by name";
    private static final String GET_FOODS_BY_BARCODE = "Get foods by barcode";

    private static final String CHECK_FOOD_REPORT_EXIST_BY_ID = "Check food report exist by id";
    private static final String CHECK_FOODS_EXIST_BY_NAME = "Check foods exist by name";
    private static final String CHECK_FOODS_EXIST_BY_BARCODE = "Check foods exist by barcode";

    private static final long EXISTING_ID = 415269;
    private static final long NOT_EXISTING_ID = 555555;
    private static final String EXISTING_BARCODE = "009800146130";
    private static final String NOT_EXISTING_BARCODE = "0098001";
    private static final String EXISTING_NAME = "beef noodle soup";
    private static final String NOT_EXISTING_NAME = "beef";
    private static final String SOME_TEXT = "some text";

    private static final Path PATH_HOME_DIR = FileSystems.getDefault()
            .getPath(REPOSITORY_TESTS_DIR)
            .toAbsolutePath();

    private static String pathFoodNameDir;
    private static String pathFoodIdDir;
    private static String pathFoodBarcodeDir;

    private FoodRepository repository;

    @BeforeClass
    public static void beforeClass() {
        pathFoodNameDir = Paths.get(PATH_HOME_DIR.toString(), FOOD_NAME_DIR).toString();
        pathFoodIdDir = Paths.get(PATH_HOME_DIR.toString(), FOOD_ID_DIR).toString();
        pathFoodBarcodeDir = Paths.get(PATH_HOME_DIR.toString(), FOOD_BARCODE_DIR).toString();
    }

    @Before
    public void before() {
        repository = new FoodRepositoryImpl(PATH_HOME_DIR);
    }

    @Test
    public void testSaveFoodReportByIdWithValidArguments() throws FoodIdNotFoundException {
        // Arrange
        repository.saveFoodReportById(EXISTING_ID, SOME_TEXT);

        //  Act
        String actual = repository.getFoodById(EXISTING_ID);

        // Assert
        Assert.assertEquals(SAVE_FOOD_REPORT_BY_ID, SOME_TEXT, actual);
    }

    @Test
    public void testSaveFoodByNameWithValidArguments() throws FoodNotFoundException {
        // Arrange
        repository.saveFoodByName(EXISTING_NAME, SOME_TEXT);

        //  Act
        String actual = repository.getFoodByName(EXISTING_NAME);

        // Assert
        Assert.assertEquals(SAVE_FOODS_BY_NAME, SOME_TEXT, actual);
    }

    @Test
    public void testSaveFoodByBarcodeWithValidArguments() throws FoodBarcodeNotFoundException {
        // Arrange
        repository.saveFoodByBarcode(EXISTING_BARCODE, SOME_TEXT);

        //  Act
        String actual = repository.getFoodByBarcode(EXISTING_BARCODE);

        // Assert
        Assert.assertEquals(SAVE_FOODS_BY_BARCODE, SOME_TEXT, actual);
    }

    @Test(expected = FoodIdNotFoundException.class)
    public void testGetFoodByNotExistingIdThrowsIllegalArgumentException() throws FoodIdNotFoundException {
        // Arrange, Act and Assert
        Assert.assertNotEquals(GET_FOOD_REPORT_BY_ID,
                0,
                repository.getFoodById(NOT_EXISTING_ID).length());
    }

    @Test
    public void testGetFoodByExistingId() throws IOException, FoodIdNotFoundException {
        // Arrange
        Path path = Paths.get(pathFoodIdDir, String.valueOf(EXISTING_ID));
        File file = new File(path.toString());
        writeInFile(file);

        // Act and Assert
        Assert.assertNotEquals(GET_FOOD_REPORT_BY_ID,
                0,
                repository.getFoodById(EXISTING_ID).length());
    }

    @Test(expected = FoodNotFoundException.class)
    public void testGetFoodByNotExistingNameThrowsIllegalArgumentException() throws FoodNotFoundException {
        // Arrange, Act and Assert
        Assert.assertNotEquals(GET_FOODS_BY_NAME,
                0,
                repository.getFoodByName(NOT_EXISTING_NAME).length());
    }

    @Test
    public void testGetFoodByExistingName() throws IOException, FoodNotFoundException {
        // Arrange
        Path path = Paths.get(pathFoodNameDir, EXISTING_NAME);
        File file = new File(path.toString());
        writeInFile(file);

        // Act and Assert
        Assert.assertNotEquals(GET_FOODS_BY_NAME,
                0,
                repository.getFoodByName(EXISTING_NAME).length());
    }

    @Test(expected = FoodBarcodeNotFoundException.class)
    public void testGetFoodByNotExistingBarcodeThrowsIllegalArgumentException() throws FoodBarcodeNotFoundException {
        // Arrange, Act and Assert
        Assert.assertNotEquals(GET_FOODS_BY_BARCODE,
                0,
                repository.getFoodByBarcode(NOT_EXISTING_BARCODE).length());
    }

    @Test
    public void testGetFoodByExistingBarcode() throws IOException, FoodBarcodeNotFoundException {
        // Arrange
        Path path = Paths.get(pathFoodBarcodeDir, EXISTING_BARCODE);
        File file = new File(path.toString());
        writeInFile(file);

        // Act and Assert
        Assert.assertNotEquals(GET_FOODS_BY_BARCODE,
                0,
                repository.getFoodByBarcode(EXISTING_BARCODE).length());
    }

    @Test
    public void testCheckFoodExistByExistingBarcode() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodBarcodeDir, EXISTING_BARCODE);
        File file = new File(path.toString());
        file.createNewFile();

        // Act and Assert
        Assert.assertTrue(CHECK_FOODS_EXIST_BY_BARCODE,
                repository.checkFoodExistByBarcode(EXISTING_BARCODE));
    }

    @Test
    public void testCheckFoodExistByNotExistingBarcode() {
        // Arrange, Act and Assert
        Assert.assertFalse(CHECK_FOODS_EXIST_BY_BARCODE,
                repository.checkFoodExistByBarcode(NOT_EXISTING_BARCODE));
    }

    @Test
    public void testCheckFoodExistByExistingId() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodIdDir, String.valueOf(EXISTING_ID));
        File file = new File(path.toString());
        file.createNewFile();

        // Act and Assert
        Assert.assertTrue(CHECK_FOOD_REPORT_EXIST_BY_ID,
                repository.checkFoodExistById(Long.parseLong(String.valueOf(EXISTING_ID))));
    }

    @Test
    public void testCheckFoodExistByNotExistingId() {
        // Arrange, Act and Assert
        Assert.assertFalse(CHECK_FOOD_REPORT_EXIST_BY_ID,
                repository.checkFoodExistById(NOT_EXISTING_ID));
    }

    @Test
    public void testCheckFoodExistByExistingName() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodNameDir, EXISTING_NAME);
        File file = new File(path.toString());
        file.createNewFile();

        // Act and Assert
        Assert.assertTrue(CHECK_FOODS_EXIST_BY_NAME,
                repository.checkFoodExistByName(EXISTING_NAME));
    }

    @Test
    public void testCheckFoodExistByNotExistingName() {
        // Arrange, Act and Assert
        Assert.assertFalse(CHECK_FOODS_EXIST_BY_NAME,
                repository.checkFoodExistByName(NOT_EXISTING_NAME));
    }

    private void writeInFile(File file) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(SOME_TEXT);
        }
    }
}
