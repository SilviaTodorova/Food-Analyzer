package bg.sofia.uni.fmi.food.analyzer.server.core.clients;

import bg.sofia.uni.fmi.food.analyzer.server.common.GlobalConstants;
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


public class FoodClient {
    private static final String API_URL = "https://api.nal.usda.gov/fdc/v1";
    private static final String FOODS = "foods";

    private final HttpClient client;
    private final String apiKey;
    private final Gson gson;

    public FoodClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public Collection<Food> getFoodByName(String name) throws IOException, InterruptedException {
        name = String.join("%20", name.split(" "));
        String URL = API_URL + "/search?generalSearchInput=" + name + "&requireAllWords=true&api_key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();

        String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        JsonObject obj = new Gson().fromJson(jsonResponse, JsonObject.class);
        String jsonFoods = obj.get(FOODS).toString();

        Type type = new TypeToken<List<Food>>() {}.getType();
        return gson.fromJson(jsonFoods, type);
    }

    public FoodReport getFoodReportById(long id) throws IOException, InterruptedException {
        String URL = API_URL + "/" + id + "?api_key=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();

        String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        Food food = new Gson().fromJson(jsonResponse, Food.class);

        // Refactoring
        // 1. Get description
        // 2. Get labelNutrients
        JsonObject obj = new Gson().fromJson(jsonResponse, JsonObject.class);
        String labelNutrients = obj.get("labelNutrients").toString();
        JsonObject nutrients = new Gson().fromJson(labelNutrients, JsonObject.class);

        List<Nutrient> attributes = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> entrySet = nutrients.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String key = entry.getKey();
            double value = Double.parseDouble(entry.getValue().getAsJsonObject().get("value").toString());
            Nutrient nut = new Nutrient(key, value);
            attributes.add(nut);
        }

        FoodReport report = new FoodReport(food.getFdcId(), food.getDescription(), food.getGtinUpc(), food.getIngredients());
        report.setLabelNutrients(attributes);

        return report;
    }

    public List<Food> getFoodByBarcode(String barcode) {
        String URL = API_URL + "/search" + "?api_key=" + apiKey;
        String requestBody = "{\"generalSearchInput\":\"" + barcode + "\"}";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        try {
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            JsonObject obj = new Gson().fromJson(jsonResponse, JsonObject.class);
            String jsonFoods = obj.get("foods").toString();

            Type type = new TypeToken<List<Food>>() {
            }.getType();
            return gson.fromJson(jsonFoods, type);
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        }
    }





}
