package bg.sofia.uni.fmi.food.analyzer.server.core.clients;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FoodClientTest {
    private static final int FDC_ID_BEEF = 338105;
    private static final int FDC_ID_RAFFAELLO = 415269;
    private static final String NAME = "beef noodle soup";
    private static final String BARCODE = "009800146130";

    private static final String FOOD_REPORT_VALID_JSON = "{\"description\":\"RAFFAELLO\",\"labelNutrients\":" +
            "{\"fat\":{\"value\":15.00},\"carbohydrates\":{\"value\":12.00},\"fiber\":{\"value\":0.99}," +
            "\"protein\":{\"value\":2.00},\"calories\":{\"value\":189.90}},\"fdcId\":415269, " +
            "\"ingredients\":\"ENRICHED WHEAT FLOUR\"}";

    private static final String FOOD_REPORT_INVALID_JSON = "{\"description\":\"RAFFAELLO\",\"labelNutrients\":" +
            "{\"fats\":{\"value\":15.00},\"carbohydrates\":{\"value\":12.00},\"fiber\":{\"value\":0.99}," +
            "\"protein\":{\"value\":2.00},\"calories\":{\"value\":189.90}},\"fdcId\":415269, " +
            "\"ingredients\":\"ENRICHED WHEAT FLOUR\"}";

    private static final String FOODS_VALID_JSON = "{\"foods\":[{\"fdcId\":338105,\"description\":" +
            "\"Beef and noodles\",\"gtinUpc\":\"009800146130\"}]}";

    private static final String FOODS_INVALID_JSON = "{\"food\":[{\"fdcId\":338105,\"description\":" +
            "\"Beef and noodles\",\"gtinUpc\":\"009800146130\"}]}";

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private FoodClient client;

    @Before
    public void setUp() {
        client = new FoodClientImpl(httpClientMock, null);
    }

    @Test
    public void testGetFoodByBarcodeWithValidJSON() throws Exception {
        // Arrange
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.body()).thenReturn(FOODS_VALID_JSON);

        // Act
        List<Food> actual = new ArrayList<>(client.getFoodByBarcode(BARCODE));

        // Assert
        assertEquals(1, actual.size());
        assertEquals(FDC_ID_BEEF, actual.get(0).getFdcId());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFoodByBarcodeThrowsExceptionWithInvalidJSON() throws Exception {
        // Arrange
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.body()).thenReturn(FOODS_INVALID_JSON);

        // Act and Assert
        List<Food> actual = new ArrayList<>(client.getFoodByBarcode(BARCODE));
    }

    @Test
    public void testGetFoodByNameWithValidJSON() throws Exception {
        // Arrange
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.body()).thenReturn(FOODS_VALID_JSON);

        // Act
        List<Food> actual = new ArrayList<>(client.getFoodByName(NAME));

        // Assert
        assertEquals(1, actual.size());
        assertEquals(FDC_ID_BEEF, actual.get(0).getFdcId());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFoodByNameThrowsExceptionWithInvalidJSON() throws Exception {
        // Arrange
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.body()).thenReturn(FOODS_INVALID_JSON);

        // Act and Assert
        List<Food> actual = new ArrayList<>(client.getFoodByName(NAME));
    }

    @Test
    public void testGetFoodByIdWithValidJSON() throws Exception {
        // Arrange
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.body()).thenReturn(FOOD_REPORT_VALID_JSON);

        // Act
        FoodReport actual = client.getFoodReportById(FDC_ID_RAFFAELLO);

        // Assert
        assertEquals(actual.getFdcId(), FDC_ID_RAFFAELLO);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFoodByIdThrowsExceptionWithInvalidJSON() throws Exception {
        // Arrange
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.body()).thenReturn(FOOD_REPORT_INVALID_JSON);

        // Act and Assert
        FoodReport actual = client.getFoodReportById(FDC_ID_BEEF);
    }
}
