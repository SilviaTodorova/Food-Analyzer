package bg.sofia.uni.fmi.food.analyzer.server.core.clients;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.NO_FOODS_WERE_FOUND_MESSAGE;
import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;


public class FoodClientImpl implements FoodClient {
    private static final String SPACE_SYMBOL = "%20";
    private static final String CANNOT_GET_DATA_FROM_API = "Cannot get data from api!";
    private static final String GENERAL_SEARCH = "/search?generalSearchInput=";
    private static final String API_KEY_PARAM = "api_key=";
    private static final String REQUIRE_ALL_WORDS_TRUE_PARAM = "&requireAllWords=true";
    private static final String SEPARATOR = "/";

    private final HttpClient client;
    private final String apiKey;
    private final Gson gson;

    public FoodClientImpl(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    @Override
    public Collection<Food> getFoodByName(String name) throws FoodNotFoundException {
        name = String.join(SPACE_SYMBOL, name.split(DELIMITER));

        try {
            return sendGeneralSearchRequest(name);
        } catch (IOException | InterruptedException ex) {
            throw new FoodNotFoundException(CANNOT_GET_DATA_FROM_API);
        } catch (Exception ex) {
            throw new FoodNotFoundException(NO_FOODS_WERE_FOUND_MESSAGE);
        }
    }

    @Override
    public Collection<Food> getFoodByBarcode(String barcode) throws FoodBarcodeNotFoundException {
        try {
            return sendGeneralSearchRequest(barcode);
        } catch (IOException | InterruptedException ex) {
            throw new FoodBarcodeNotFoundException(CANNOT_GET_DATA_FROM_API);
        } catch (Exception ex) {
            throw new FoodBarcodeNotFoundException(NO_FOODS_WERE_FOUND_MESSAGE);
        }
    }

    @Override
    public Food getFoodReportById(long id) throws FoodIdNotFoundException {
        try {
            return sendSearchRequestById(id);
        } catch (IOException | InterruptedException ex) {
            throw new FoodIdNotFoundException(CANNOT_GET_DATA_FROM_API);
        } catch (Exception ex) {
            throw new FoodIdNotFoundException(NO_FOODS_WERE_FOUND_MESSAGE);
        }
    }

    private Collection<Food> sendGeneralSearchRequest(String name) throws IOException, InterruptedException {
        String url = API_URL + GENERAL_SEARCH + name + REQUIRE_ALL_WORDS_TRUE_PARAM + "&" + API_KEY_PARAM + apiKey;
        HttpRequest request = createHttpRequest(url);
        JsonObject obj = sendRequest(request);

        if (!obj.has(FOODS)) {
            throw new IllegalArgumentException(NO_FOODS_WERE_FOUND_MESSAGE);
        }

        String jsonFoods = obj.get(FOODS).toString();
        Type type = new TypeToken<List<Food>>() {
        }.getType();
        return gson.fromJson(jsonFoods, type);
    }

    private Food sendSearchRequestById(long fdcId) throws IOException, InterruptedException {
        String url = API_URL + SEPARATOR + fdcId + "?" + API_KEY_PARAM + apiKey;
        HttpRequest request = createHttpRequest(url);
        JsonObject obj = sendRequest(request);
        return gson.fromJson(obj, Food.class);
    }

    private JsonObject sendRequest(HttpRequest request) throws IOException, InterruptedException {
        String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        return new Gson().fromJson(jsonResponse, JsonObject.class);
    }

    private HttpRequest createHttpRequest(String url) {
        return HttpRequest.newBuilder().uri(URI.create(url)).build();
    }
}
