package bg.sofia.uni.fmi.food.analyzer.server.core.clients;

import bg.sofia.uni.fmi.food.analyzer.server.core.contracts.FoodClient;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodBarcodeNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodIdNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.exceptions.FoodNotFoundException;
import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants.*;


public class FoodClientImpl implements FoodClient {
    private static final String API_URL = "https://api.nal.usda.gov/fdc/v1";
    private static final String FOODS = "foods";
    private static final String CANNOT_GET_DATA_FROM_API = "Cannot get data from api!";
    private static final String INVALID_DATA_FROM_API = "Invalid data from api!";

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
        name = String.join("%20", name.split(" "));
        String URL = API_URL + "/search?generalSearchInput=" + name + "&requireAllWords=true&api_key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        try {
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            JsonObject obj = new Gson().fromJson(jsonResponse, JsonObject.class);
            String jsonFoods = obj.get(FOODS).toString();

            Type type = new TypeToken<List<Food>>() {
            }.getType();
            return gson.fromJson(jsonFoods, type);
        } catch (IOException | InterruptedException ex) {
            throw new FoodNotFoundException(CANNOT_GET_DATA_FROM_API);
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALID_DATA_FROM_API);
        }
    }

    @Override
    public FoodReport getFoodReportById(long id) throws FoodIdNotFoundException {
        String URL = API_URL + "/" + id + "?api_key=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        try {
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            JsonObject obj = new Gson().fromJson(jsonResponse, JsonObject.class);

            long fdcId = Long.parseLong(obj.get(FDC_ID_FIELD).getAsString());
            String description = obj.get(DESC_FIELD).getAsString();
            String ingredients = obj.get(INGREDIENTS_FIELD).getAsString();
            String labelNutrients = obj.get(NUTRIENTS_FIELD).toString();
            JsonObject nutrients = new Gson().fromJson(labelNutrients, JsonObject.class);

            FoodReport report = new FoodReport(fdcId, description, ingredients);
            report.addNutrient(parseJsonObject(nutrients, CALORIES_FIELD));
            report.addNutrient(parseJsonObject(nutrients, PROTEIN_FIELD));
            report.addNutrient(parseJsonObject(nutrients, FAT_FIELD));
            report.addNutrient(parseJsonObject(nutrients, CARBOHYDRATES_FIELD));
            report.addNutrient(parseJsonObject(nutrients, FIBER_FIELD));

            return report;
        } catch (IOException | InterruptedException ex) {
            throw new FoodIdNotFoundException(CANNOT_GET_DATA_FROM_API);
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALID_DATA_FROM_API);
        }
    }

    @Override
    public Collection<Food> getFoodByBarcode(String barcode) throws FoodBarcodeNotFoundException {
        String URL = API_URL + "/search?generalSearchInput=" + barcode + "&requireAllWords=true&api_key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        try {
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            JsonObject obj = new Gson().fromJson(jsonResponse, JsonObject.class);
            String jsonFoods = obj.get(FOODS).toString();

            Type type = new TypeToken<List<Food>>() {
        }.getType();
            return gson.fromJson(jsonFoods, type);
        } catch (IOException | InterruptedException ex) {
            throw new FoodBarcodeNotFoundException(CANNOT_GET_DATA_FROM_API);
        } catch (Exception ex) {
            throw new IllegalArgumentException(INVALID_DATA_FROM_API);
        }
    }

    private Nutrient parseJsonObject(JsonObject nutrients, String label) {
        JsonElement obj = nutrients.get(label);
        double value = Double.parseDouble(obj.getAsJsonObject().get(VALUE_FIELD).toString());
        return new Nutrient(label, value);
    }
}
