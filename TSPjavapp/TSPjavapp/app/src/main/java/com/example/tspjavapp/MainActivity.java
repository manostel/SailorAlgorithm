package com.example.tspjavapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TSP_APP";
    private static final int NUM_CITIES = 12;
    private int bestScore = 0;

    private List<City> bestRoute = null;
    private TextView bestScoreTextView=null;
    private List<City> cities = new ArrayList<>();
    private TextView resultTextView;
    private Button runButton;
    private CanvasView canvasView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = findViewById(R.id.canvasView);
        resultTextView = findViewById(R.id.resultTextView);
        runButton = findViewById(R.id.runButton);
        bestScoreTextView = findViewById(R.id.bestScoreTextView);

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTSP();
            }
        });
        Button generateCitiesButton = findViewById(R.id.generateCitiesButton);
        generateCitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cities = generateCities(NUM_CITIES);
                StringBuilder sb = new StringBuilder();
                for (City city : cities) {
                    sb.append(city.getName()).append(": x = ").append(city.getX()).append(", y = ").append(city.getY()).append("\n");
                }
                for (City city : cities) {
                    canvasView.drawCircle(city.getX(), city.getY(), 10, Color.BLUE);
                }
                resultTextView.setText(sb.toString());
            }
        });


        // Initialize the best score TextView with the initial value of the best score
        bestScoreTextView.setText("Best Score: " + bestScore);
    }
    /**
     * Generate a list of cities with random coordinates
     *
     * @return The list of cities
     */
    /**
     * Generate a list of cities with random coordinates
     *
     * @param numCities The number of cities to generate
     * @return The list of cities
     */
    private List<City> generateCities(int numCities) {
        List<City> cities = new ArrayList<>();
        Random random = new Random();
        int canvasWidth = canvasView.getWidth();
        int canvasHeight = canvasView.getHeight();
        int padding = 50; // Padding to keep the drawings within bounds

        if (canvasWidth > 0 && canvasHeight > 0) {
            int maxX = canvasWidth - 100;
            int maxY = canvasHeight - 250;

            for (int i = 0; i < numCities; i++) {
                int x = random.nextInt(maxX - padding) + padding;
                int y = random.nextInt(maxY - padding) + padding;
                cities.add(new City("Πόλη " + (i + 1), x, y));
            }
        }

        return cities;
    }




    /**
     * Find the best route using the nearest neighbor method
     *
     * @param cities List of cities to visit
     * @return The best route
     */
    /**
     * Find the best route using the nearest neighbor method
     *
     * @paof cities to visit
     * @return The best route
     */
    private ArrayList<City> tspNearestNeighbor(ArrayList<City> cities, int numTries) {
        ArrayList<City> bestRoute = null;
        int bestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < numTries; i++) {
            ArrayList<City> route = new ArrayList<>();
            City startCity = cities.get(0);
            route.add(startCity);
            ArrayList<City> remainingCities = new ArrayList<>(cities);
            remainingCities.remove(startCity);

            int distance = 0;

            while (!remainingCities.isEmpty()) {
                City currentCity = route.get(route.size() - 1);
                City nextCity = null;
                int minDistance = Integer.MAX_VALUE;

                for (City city : remainingCities) {
                    int dist = currentCity.distanceTo(city);
                    if (dist < minDistance) {
                        minDistance = dist;
                        nextCity = city;
                    }
                }

                route.add(nextCity);
                remainingCities.remove(nextCity);
                distance += minDistance;
            }

            // Add distance from last city to start city
            distance += route.get(route.size() - 1).distanceTo(startCity);

            if (distance < bestDistance) {
                bestDistance = distance;
                bestRoute = route;
            }

        }

        return bestRoute;
    }


    private List<City> tspRandom(List<City> cities, double selectivity) {
        List<City> route = new ArrayList<>(cities);
        City startCity = route.get(0);
        route.remove(startCity);
        Random random = new Random();

        for (int i = route.size() - 1; i > 0; i--) {
            int index = selectivityBasedIndex(i + 1, selectivity, random);
            City temp = route.get(index);
            route.set(index, route.get(i));
            route.set(i, temp);
        }

        route.add(0, startCity); // Add the start city back at the beginning of the route
        return route;
    }

    private int selectivityBasedIndex(int size, double selectivity, Random random) {
        double[] probabilities = new double[size];
        double sum = 0;

        for (int i = 0; i < size; i++) {
            double modifiedIndex = Math.pow(i, selectivity); // Apply selectivity
            probabilities[i] = modifiedIndex;
            sum += modifiedIndex;
        }

        // Normalize probabilities
        for (int i = 0; i < size; i++) {
            probabilities[i] /= sum;
        }

        // Select index based on probabilities
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0;

        for (int i = 0; i < size; i++) {
            cumulativeProbability += probabilities[i];

            if (randomValue <= cumulativeProbability) {
                return i;
            }
        }

        return size - 1; // Fallback
    }



    /**
     * Calculate the score of a given route
     *
     * @param route The route to calculate the score for
     * @return The score of the route
     */
    private int calculateScore(List<City> route) {
        int score = 0;
        if (route.size() >= 2) {
            for (int i = 0; i < route.size() - 1; i++) {
                City city1 = route.get(i);
                City city2 = route.get(i + 1);
                score += city1.distanceTo(city2);
            }
            // Add distance from last city to first city
            City lastCity = route.get(route.size() - 1);
            City firstCity = route.get(0);
            score += lastCity.distanceTo(firstCity);
        }
        return score;
    }


    /**
     * Visualize the TSP process
     *
     * @param cities List of cities to visit
     * @param route1 The route found using the nearest neighbor method
     * @param route2 The route found using the random method
     */
    /**
     * Visualize the TSP process
     *
     * @param cities List of cities to visit
     * @param route1 The route found using the nearest neighbor method
     * @param route2 The route found using the random method
     */
    /**
     * Visualize the TSP process
     *
     * @param cities          List of cities to visit
     * @param route1          The route found using the nearest neighbor method
     * @param route2          The route found using the random method
     * @param bestRouteNN     The best route found using the nearest neighbor method
     * @param bestRouteRandom The best route found using the random method
     */
    private void visualize(List<City> cities, List<City> route1, List<City> route2, List<City> bestRouteNN, List<City> bestRouteRandom) {
        CanvasView canvasView = findViewById(R.id.canvasView);

        // Clear the canvas
        canvasView.clearCanvas();

        // Draw the cities
        for (City city : cities) {
            canvasView.drawCircle(city.getX(), city.getY(), 20, Color.BLACK);
        }

        // Draw the current iteration route for the nearest neighbor method in red
        for (int i = 0; i < route1.size() - 1; i++) {
            City city1 = route1.get(i);
            City city2 = route1.get(i + 1);
            canvasView.drawLine(city1.getX(), city1.getY(), city2.getX(), city2.getY(), Color.WHITE);
        }
        // Connect the last city with the first city
        City lastCity = route1.get(route1.size() - 1);
        City firstCity = route1.get(0);
        canvasView.drawLine(lastCity.getX(), lastCity.getY(), firstCity.getX(), firstCity.getY(), Color.WHITE);

        // Draw the best route for the nearest neighbor method in blue
        for (int i = 0; i < bestRouteNN.size() - 1; i++) {
            City city1 = bestRouteNN.get(i);
            City city2 = bestRouteNN.get(i + 1);
            canvasView.drawLine(city1.getX(), city1.getY(), city2.getX(), city2.getY(), Color.BLUE);
        }
        // Connect the last city with the first city
        lastCity = bestRouteNN.get(bestRouteNN.size() - 1);
        firstCity = bestRouteNN.get(0);
        canvasView.drawLine(lastCity.getX(), lastCity.getY(), firstCity.getX(), firstCity.getY(), Color.BLUE);

        // Draw the current iteration route for the random method in green
        for (int i = 0; i < route2.size() - 1; i++) {
            City city1 = route2.get(i);
            City city2 = route2.get(i + 1);
            canvasView.drawLine(city1.getX(), city1.getY(), city2.getX(), city2.getY(), Color.GREEN);
        }
        // Connect the last city with the first city
        lastCity = route2.get(route2.size() - 1);
        firstCity = route2.get(0);
        canvasView.drawLine(lastCity.getX(), lastCity.getY(), firstCity.getX(), firstCity.getY(), Color.GREEN);

        // Draw the best route for the random method in yellow
        for (int i = 0; i < bestRouteRandom.size() - 1; i++) {
            City city1 = bestRouteRandom.get(i);
            City city2 = bestRouteRandom.get(i + 1);
            canvasView.drawLine(city1.getX(), city1.getY(), city2.getX(), city2.getY(), Color.RED);
        }
        // Connect the last city with the first city
        lastCity = bestRouteRandom.get(bestRouteRandom.size() - 1);
        firstCity = bestRouteRandom.get(0);
        canvasView.drawLine(lastCity.getX(), lastCity.getY(), firstCity.getX(), firstCity.getY(), Color.RED);
    }

                /**
                 * Run the TSP algorithm and display the results
                 */
    /**
     * Run the TSP algorithm and display the results
     */
    // Add a class-level variable to store the best route for tspRandom
    private List<City> bestRouteRandom = null;
    private int bestScoreRandom = Integer.MAX_VALUE;

// ...

    /**
     * Run the TSP algorithm and display the results
     */
    private void runTSP() {
        if (cities == null || cities.isEmpty()) {
            resultTextView.setText("Παρακαλώ πατήστε να δημιουργήσετε πόλεις");
            return;
        }

        BestResult bestNearestNeighbor = new BestResult(Integer.MAX_VALUE, new ArrayList<>());
        BestResult bestRandom = new BestResult(Integer.MAX_VALUE, new ArrayList<>());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    final int iteration = i + 1; // Create effectively final variable for iteration count

                    List<City> routeNearestNeighbor = tspNearestNeighbor(new ArrayList<>(cities), 10);
                    int scoreNearestNeighbor = calculateScore(routeNearestNeighbor);

                    List<City> routeRandom = tspRandom(new ArrayList<>(cities), 2.0);
                    int scoreRandom = calculateScore(routeRandom);

                    if (scoreNearestNeighbor < bestNearestNeighbor.getScore()) {
                        bestNearestNeighbor.setScore(scoreNearestNeighbor);
                        bestNearestNeighbor.setRoute(routeNearestNeighbor);
                    }

                    if (scoreRandom < bestRandom.getScore()) {
                        bestRandom.setScore(scoreRandom);
                        bestRandom.setRoute(routeRandom);
                        bestScoreRandom = scoreRandom; // Update the best score for tspRandom
                        bestRouteRandom = routeRandom; // Update the best route for tspRandom
                        visualize(cities, routeNearestNeighbor, routeRandom, bestNearestNeighbor.getRoute(), bestRouteRandom); // Visualize the new best route for tspRandom
                    }
                    if(bestNearestNeighbor.getScore() < bestRandom.getScore()){
                        bestScore=bestNearestNeighbor.getScore();
                    }
                    if(bestNearestNeighbor.getScore() > bestRandom.getScore()){
                        bestScore=bestRandom.getScore();
                    }

                    visualize(cities, routeNearestNeighbor, routeRandom, bestNearestNeighbor.getRoute(), bestRouteRandom);
                    // Create a list to store all routes tried in the current iteration
                    List<List<City>> allRoutes = new ArrayList<>();
                    allRoutes.add(routeNearestNeighbor);
                    allRoutes.add(routeRandom);

                    // Update the text of the TextViews
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setText("Επανάληψη " + iteration + "\n" +
                                    "Γειτονική πόλη Score: " + scoreNearestNeighbor + "\n" +
                                    "Τυχαία πόλη Score: " + scoreRandom + "\n" +
                                    "Καλύτερη γειτονική πόλη Score: " + bestNearestNeighbor.getScore() + "\n" +
                                    "Καλύτερη τυχαία πόλη Score: " + bestRandom.getScore());
                            bestScoreTextView.setText("Καλύτερο Score: " + bestScore);

                        }
                    });

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private static class BestResult {
        private int score;
        private List<City> route;

        public BestResult(int score, List<City> route) {
            this.score = score;
            this.route = route;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public List<City> getRoute() {
            return route;
        }

        public void setRoute(List<City> route) {
            this.route = route;
        }
    }



}
