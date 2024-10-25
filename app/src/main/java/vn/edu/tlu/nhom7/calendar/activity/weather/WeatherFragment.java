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
    private static final String UNITS = "metric"; // Hoặc "imperial" nếu bạn muốn độ F
    private String[] cities = {
            "Hà Nội", // Mục mặc định
            "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bến Tre",
            "Bình Định", "Bình Dương", "Bình Phước", "Cà Mau", "Cần Thơ",
            "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Gia Lai",
            "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương",
            "Hải Phòng", "Hòa Bình", "Hồ Chí Minh", "Khánh Hòa", "Kiên Giang",
            "Kon Tum", "Lào Cai", "Lạng Sơn", "Lâm Đồng", "Long An",
            "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ",
            "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh",
            "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình",
            "Thái Nguyên", "Thanh Hóa", "Thừa Thiên - Huế", "Tiền Giang", "Vĩnh Long",
            "Vĩnh Phúc", "Yên Bái"
    };

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho fragment Weather
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Khởi tạo các thành phần giao diện
        tvCityName = view.findViewById(R.id.tv_cityName);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvWeatherDescription = view.findViewById(R.id.tv_weatherDescription);
        ivWeatherIcon = view.findViewById(R.id.iv_weatherIcon);
        progressBar = view.findViewById(R.id.progressBar);
        spinnerCities = view.findViewById(R.id.spinner_cities); // Thêm dòng này
        spinnerCities.setSelection(0);
        // Tạo Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(adapter);

        // Thiết lập sự kiện chọn thành phố
        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities[position];
                tvCityName.setText("Thành phố: " + selectedCity);
                fetchWeatherData(selectedCity); // Gọi lại dữ liệu thời tiết cho thành phố đã chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì cả
            }
        });

        // Lấy dữ liệu thời tiết cho thành phố mặc định khi khởi động
        fetchWeatherData(cities[0]); // Gọi với thành phố đầu tiên trong danh sách

        return view;
    }

    private void fetchWeatherData(String cityName) {
        progressBar.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, WeatherResponse>() {
            @Override
            protected WeatherResponse doInBackground(Void... voids) {
                // Tạo một đối tượng WeatherService để gọi API
                WeatherService weatherService = new WeatherService();
                String response = weatherService.getCurrentWeather(cityName, API_KEY, UNITS);

                if (response != null) {
                    // Phân tích dữ liệu thời tiết từ phản hồi JSON
                    return parseWeatherResponse(response);
                } else {
                    return null; // Trả về null nếu không có phản hồi
                }
            }

            @Override
            protected void onPostExecute(WeatherResponse weatherResponse) {
                progressBar.setVisibility(View.GONE);
                if (weatherResponse != null) {
                    // Cập nhật giao diện với dữ liệu thời tiết
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

    private WeatherResponse parseWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String cityName = jsonObject.getString("name");
            String temperature = jsonObject.getJSONObject("main").getString("temp");
            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            String icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            return new WeatherResponse(cityName, temperature, description, icon);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
