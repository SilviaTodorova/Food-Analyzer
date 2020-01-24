package bg.sofia.uni.fmi.food.analyzer.server.core.clients;

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
    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private FoodClientImpl client;

    @Before
    public void setUp() {
        client = new FoodClientImpl(httpClientMock, null);
    }

    @Test
    public void getFoodByBarcodeShouldReturnFoods() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        String json = "{\"foods\":[{\"fdcId\":415269,\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"gtinUpc\":\"009800146130\"}]}";

        when(httpResponseMock.body()).thenReturn(json);

        // Assert
        List<Food> actual = new ArrayList<>(client.getFoodByBarcode("009800146130"));
        assertEquals(1, actual.size());
        assertEquals(415269, actual.get(0).getFdcId());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFoodByBarcodeShouldThrowExceptionWhenJsonIsInvalid() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        String json = "{\"food\":[{\"fdcId\":415269,\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"gtinUpc\":\"009800146130\"}]}";

        when(httpResponseMock.body()).thenReturn(json);

        // Assert
        List<Food> actual = new ArrayList<>(client.getFoodByBarcode("009800146130"));
    }

    @Test
    public void testGetFoodByNameShouldReturnFoods() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        String json = "{\"foods\":[{\"fdcId\":338105,\"description\":\"Beef and noodles with (mushroom) soup (mixture)\"}]}";

        when(httpResponseMock.body()).thenReturn(json);

        // Assert
        List<Food> actual = new ArrayList<>(client.getFoodByName("009800146130"));
        assertEquals(1, actual.size());
        assertEquals(338105, actual.get(0).getFdcId());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFoodByNameShouldThrowExceptionWhenJsonIsInvalid() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        String json = "{\"food\":[{\"fdcId\":338105,\"description\":\"Beef and noodles with (mushroom) soup (mixture)\"}]}";

        when(httpResponseMock.body()).thenReturn(json);

        // Assert
        List<Food> actual = new ArrayList<>(client.getFoodByName("009800146130"));
    }

    @Test
    public void testGetFoodByIdShouldReturnFoods() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        String json = "{\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"labelNutrients\":{\"fat\":{\"value\":15.0000000000000000},\"carbohydrates\":{\"value\":12.0000000000000000},\"fiber\":{\"value\":0.9900000000000000},\"protein\":{\"value\":2.0010000000000000},\"calories\":{\"value\":189.9000000000000000}},\"fdcId\":415269, \"ingredients\":\"ENRICHED WHEAT FLOUR\"}";

        when(httpResponseMock.body()).thenReturn(json);

        // Assert
        FoodReport actual = client.getFoodReportById(415269);
        assertEquals(actual.getFdcId(), 415269);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetFoodByIdShouldThrowExceptionWhenJsonIsInvalid() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        String json = "{\"description\":\"RAFFAELLO, ALMOND COCONUT TREAT\",\"labelNutrients\":{\"carbohydrates\":{\"value\":12.0000000000000000},\"fiber\":{\"value\":0.9900000000000000},\"protein\":{\"value\":2.0010000000000000},\"calories\":{\"value\":189.9000000000000000}},\"fdcId\":415269}";

        when(httpResponseMock.body()).thenReturn(json);

        // Assert
        FoodReport actual = client.getFoodReportById(415269);
    }
}
