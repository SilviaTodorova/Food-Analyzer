package bg.sofia.uni.fmi.food.analyzer.server;

import bg.sofia.uni.fmi.food.analyzer.server.core.FoodAnalyzerServer;

public class Startup {
    public static void main(String[] args) {
        FoodAnalyzerServer server = new FoodAnalyzerServer();
        // server.start();
        server.test();
    }
}
