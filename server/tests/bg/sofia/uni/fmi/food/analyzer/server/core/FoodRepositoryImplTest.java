package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.core.repositories.FoodRepositoryImpl;
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
    private static final long EXISTING_ID = 415269;
    private static final long NOT_EXISTING_ID = 555555;
    private static final String EXISTING_BARCODE = "009800146130";
    private static final String NOT_EXISTING_BARCODE = "0098001";
    private static final String EXISTING_NAME = "beef noodle soup";
    private static final String NOT_EXISTING_NAME = "beef";
    private static final String SOME_TEXT = "some text";

    private static final Path PATH_HOME_DIR = FileSystems.getDefault().getPath(REPOSITORY_TESTS_DIR).toAbsolutePath();

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
    public void testSaveFoodReportByIdWithValidArguments() {
        // Arrange
        repository.saveFoodReportById(EXISTING_ID, SOME_TEXT);

        //  Act
        String actual = repository.getFoodById(EXISTING_ID);

        // Assert
        Assert.assertEquals(SOME_TEXT, actual);
    }

    @Test
    public void testSaveFoodByIdWithValidArguments() {
        // Arrange
        repository.saveFoodByName(EXISTING_NAME, SOME_TEXT);

        //  Act
        String actual = repository.getFoodByName(EXISTING_NAME);

        // Assert
        Assert.assertEquals(SOME_TEXT, actual);
    }

    @Test
    public void testSaveFoodByBarcodeWithValidArguments() {
        // Arrange
        repository.saveFoodByBarcode(EXISTING_BARCODE, SOME_TEXT);

        //  Act
        String actual = repository.getFoodByBarcode(EXISTING_BARCODE);

        // Assert
        Assert.assertEquals(SOME_TEXT, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFoodByNotExistingIdThrowsIllegalArgumentException() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodById(NOT_EXISTING_ID).length());
    }

    @Test
    public void testGetFoodByExistingId() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodIdDir, String.valueOf(EXISTING_ID));
        File file = new File(path.toString());
        writeInFile(file, SOME_TEXT);

        // Act and Assert
        Assert.assertNotEquals(0, repository.getFoodById(EXISTING_ID).length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFoodByNotExistingNameThrowsIllegalArgumentException() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByName(NOT_EXISTING_NAME).length());
    }

    @Test
    public void testGetFoodByExistingName() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodNameDir, EXISTING_NAME);
        File file = new File(path.toString());
        writeInFile(file, SOME_TEXT);

        // Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByName(EXISTING_NAME).length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFoodByNotExistingBarcodeThrowsIllegalArgumentException() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByBarcode(NOT_EXISTING_BARCODE).length());
    }

    @Test
    public void testGetFoodByExistingBarcode() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodBarcodeDir, EXISTING_BARCODE);
        File file = new File(path.toString());
        writeInFile(file, SOME_TEXT);

        // Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByBarcode(EXISTING_BARCODE).length());
    }

    @Test
    public void testCheckFoodExistByExistingBarcode() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodBarcodeDir, EXISTING_BARCODE);
        File file = new File(path.toString());
        file.createNewFile();

        // Act and Assert
        Assert.assertTrue(repository.checkFoodExistByBarcode(EXISTING_BARCODE));
    }

    @Test
    public void testCheckFoodExistByNotExistingBarcode() {
        // Arrange, Act and Assert
        Assert.assertFalse(repository.checkFoodExistByBarcode(NOT_EXISTING_BARCODE));
    }

    @Test
    public void testCheckFoodExistByExistingId() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodIdDir, String.valueOf(EXISTING_ID));
        File file = new File(path.toString());
        file.createNewFile();

        // Act and Assert
        Assert.assertTrue(repository.checkFoodExistById(Long.parseLong(String.valueOf(EXISTING_ID))));
    }

    @Test
    public void testCheckFoodExistByNotExistingId() {
        // Arrange, Act and Assert
        Assert.assertFalse(repository.checkFoodExistById(NOT_EXISTING_ID));
    }

    @Test
    public void testCheckFoodExistByExistingName() throws IOException {
        // Arrange
        Path path = Paths.get(pathFoodNameDir, EXISTING_NAME);
        File file = new File(path.toString());
        file.createNewFile();

        // Act and Assert
        Assert.assertTrue(repository.checkFoodExistByName(EXISTING_NAME));
    }

    @Test
    public void testCheckFoodExistByNotExistingName() {
        // Arrange, Act and Assert
        Assert.assertFalse(repository.checkFoodExistByName(NOT_EXISTING_NAME));
    }

    private void writeInFile(File file, String text) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(text);
        }
    }
}
