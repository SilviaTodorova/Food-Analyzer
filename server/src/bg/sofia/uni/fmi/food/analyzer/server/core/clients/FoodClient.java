package bg.sofia.uni.fmi.food.analyzer.server.core.clients;

import bg.sofia.uni.fmi.food.analyzer.server.models.Food;
import bg.sofia.uni.fmi.food.analyzer.server.models.FoodReport;
import bg.sofia.uni.fmi.food.analyzer.server.models.Nutrient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.food.analyzer.server.commands.common.CommandConstants.*;


public class FoodClient {
    private static final String API_URL = "https://api.nal.usda.gov/fdc/v1";

    private HttpClient client;
    private String apiKey;
    private Gson gson;

    public FoodClient(HttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public List<Food> getFoodByBarcode(String barcode) {
        String URL = API_URL + "/search" + "?api_key=" + apiKey;
        String requestBody = "{\"generalSearchInput\":\"" + barcode + "\"}";
       // System.out.println(URL);
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

    public Collection<Food> getFoodByName(String name) {
        name = Arrays.stream(name.split(" ")).collect(Collectors.joining("%20"));
        String URL = API_URL + "/search?generalSearchInput=" + name + "&requireAllWords=true&api_key=" + apiKey;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
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


    public FoodReport getFoodReportById(long id) {
        String URL = API_URL + "/" + id + "?api_key=" + apiKey;
        // System.out.println(URL);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        try {
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            Food food = new Gson().fromJson(jsonResponse, Food.class);

            // Refactoring
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
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        }
    }
}
