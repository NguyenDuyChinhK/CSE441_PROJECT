package vn.edu.tlu.nhom7.calendar.activity.api;

public class WeatherService {
    private ApiClient apiClient;

    public WeatherService() {
        apiClient = new ApiClient();
    }

    public String getWeather(String city, String apiKey, String units) {
        String endpoint = "weather?q=" + city + "&appid=" + apiKey + "&units=" + units;
        return apiClient.getWeatherData(endpoint);
    }

    public String getCurrentWeather(String cityName, String apiKey, String units) {
        return getWeather(cityName, apiKey, units);
    }
}
