package vn.edu.tlu.nhom7.calendar.activity.weather;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.tlu.nhom7.calendar.activity.api.WeatherService;
import vn.edu.tlu.nhom7.calendar.model.WeatherResponse;
import vn.edu.tlu.nhom7.calendar.R;

public class WeatherFragment extends Fragment {

    private TextView tvCityName, tvTemperature, tvWeatherDescription;
    private ImageView ivWeatherIcon;
    private ProgressBar progressBar;
    private Spinner spinnerCities;

    private static final String API_KEY = "0a02ddfb101a0ba5b138cacd66ba70c7";
    private static final String UNITS = "metric"; // Sử dụng 'imperial' nếu bạn muốn độ F
    private String[] cities = {
            "Hà Nội", "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn",
            "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Cà Mau",
            "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên",
            "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh",
            "Hải Dương", "Hải Phòng", "Hòa Bình", "Hồ Chí Minh", "Khánh Hòa",
            "Kiên Giang", "Kon Tum", "Lào Cai", "Lạng Sơn", "Lâm Đồng",
            "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận",
            "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi",
            "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh",
            "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên - Huế",
            "Tiền Giang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    };

    public WeatherFragment() {
        // Constructor mặc định
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Khởi tạo các thành phần UI
        tvCityName = view.findViewById(R.id.tv_cityName);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvWeatherDescription = view.findViewById(R.id.tv_weatherDescription);
        ivWeatherIcon = view.findViewById(R.id.iv_weatherIcon);
        progressBar = view.findViewById(R.id.progressBar);
        spinnerCities = view.findViewById(R.id.spinner_cities);

        // Tạo Adapter cho Spinner và thiết lập giá trị mặc định
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(adapter);
        spinnerCities.setSelection(0); // Thành phố mặc định

        // Xử lý sự kiện chọn thành phố
        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities[position];
                tvCityName.setText("Thành phố: " + selectedCity);
                fetchWeatherData(selectedCity); // Lấy dữ liệu thời tiết
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có hành động gì
            }
        });

        // Lấy dữ liệu cho thành phố đầu tiên khi khởi chạy
        fetchWeatherData(cities[0]);

        return view;
    }

    private void fetchWeatherData(String cityName) {
        progressBar.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, WeatherResponse>() {
            @Override
            protected WeatherResponse doInBackground(Void... voids) {
                WeatherService weatherService = new WeatherService();
                String response = weatherService.getCurrentWeather(cityName, API_KEY, UNITS);

                if (response != null) {
                    return parseWeatherResponse(response);
                } else {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(WeatherResponse weatherResponse) {
                progressBar.setVisibility(View.GONE);
                if (weatherResponse != null) {
                    tvCityName.setText("Thành phố: " + weatherResponse.getCityName());
                    tvTemperature.setText("Nhiệt độ: " + weatherResponse.getTemperature() + "°C");
                    tvWeatherDescription.setText("Mô tả: " + weatherResponse.getDescription());
                    Glide.with(getContext())
                            .load("https://openweathermap.org/img/wn/" + weatherResponse.getIcon() + "@2x.png")
                            .into(ivWeatherIcon);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private String translateDescription(String description) {
        switch (description.toLowerCase()) {
            case "clear sky":
                return "Trời quang đãng";
            case "few clouds":
                return "Ít mây";
            case "scattered clouds":
                return "Mây rải rác";
            case "broken clouds":
                return "Mây đứt quãng";
            case "shower rain":
                return "Mưa rào";
            case "rain":
                return "Mưa";
            case "thunderstorm":
                return "Giông bão";
            case "snow":
                return "Tuyết rơi";
            case "mist":
                return "Sương mù";
            default:
                return description; // Trả về nguyên văn nếu không có trong danh sách
        }
    }


    private WeatherResponse parseWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String cityName = jsonObject.getString("name");
            String temperature = jsonObject.getJSONObject("main").getString("temp");

            String description = jsonObject.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");
            String translatedDescription = translateDescription(description);

            String icon = jsonObject.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("icon");

            return new WeatherResponse(cityName, temperature, translatedDescription, icon);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
