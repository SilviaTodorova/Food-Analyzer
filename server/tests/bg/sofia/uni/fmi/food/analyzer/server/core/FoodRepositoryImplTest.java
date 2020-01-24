package bg.sofia.uni.fmi.food.analyzer.server.core;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodRepository;
import bg.sofia.uni.fmi.food.analyzer.server.core.repositories.FoodRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FoodRepositoryImplTest {
    private FoodRepository repository;

    @Before
    public void before() {
        repository = new FoodRepositoryImpl();
    }

    @Test
    public void saveFoodReportByIdShouldSaveFoodReportWhenPassedValidArguments() {
        // Arrange, Act
        repository.saveFoodReportById(1, "test");

        // Assert
        String actual = repository.getFoodById(Long.parseLong("1"));
        Assert.assertEquals("test", actual);
    }

    @Test
    public void saveFoodReportByNameShouldSaveFoodReportByNameWhenPassedValidArguments() {
        // Arrange, Act
        repository.saveFoodReportByName("name", "test");

        // Assert
        String actual = repository.getFoodByName("name");
        Assert.assertEquals("test", actual);
    }

    @Test
    public void saveFoodReportByBarcodeShouldSaveFoodReportByBarcodeWhenPassedValidArguments() {
        // Arrange, Act
        repository.saveFoodReportByBarcode("code", "test");

        // Assert
        String actual = repository.getFoodByBarcode("code");
        Assert.assertEquals("test", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFoodByIdShouldThrowExceptionWhenPassedInvalidId() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodById(Long.parseLong("0")).length());
    }

    @Test
    public void getFoodByIdShouldReturnContent() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodById(Long.parseLong("415269")).length());
    }

    @Test
    public void getFoodByNameShouldReturnContent() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByName("beef noodle soup").length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFoodByNameShouldThrowExceptionWhenPassedInvalidName() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByName("beef").length());
    }

    @Test
    public void getFoodByBarcodeShouldReturnContent() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByBarcode("009800146130").length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFoodByBarcodeShouldThrowExceptionWhenPassedInvalidBarcode() {
        // Arrange, Act and Assert
        Assert.assertNotEquals(0, repository.getFoodByBarcode("0").length());
    }

    @Test
    public void checkFoodExistByBarcodeShouldReturnTrue() {
        // Arrange, Act and Assert
        Assert.assertTrue(repository.checkFoodExistByBarcode("009800146130"));
    }

    @Test
    public void checkFoodExistByBarcodeShouldReturnFalse() {
        // Arrange, Act and Assert
        Assert.assertFalse(repository.checkFoodExistByBarcode("0098001"));
    }

    @Test
    public void checkFoodExistByIdShouldReturnTrue() {
        // Arrange, Act and Assert
        Assert.assertTrue(repository.checkFoodExistById(Long.parseLong("415269")));
    }

    @Test
    public void checkFoodExistByIdShouldReturnFalse() {
        // Arrange, Act and Assert
        Assert.assertFalse(repository.checkFoodExistById(Long.parseLong("555555")));
    }

    @Test
    public void checkFoodExistByNameShouldReturnTrue() {
        // Arrange, Act and Assert
        Assert.assertTrue(repository.checkFoodExistByName("beef noodle soup"));
    }

    @Test
    public void checkFoodExistByNameShouldReturnFalse() {
        // Arrange, Act and Assert
        Assert.assertFalse(repository.checkFoodExistByName("beef"));
    }
}
