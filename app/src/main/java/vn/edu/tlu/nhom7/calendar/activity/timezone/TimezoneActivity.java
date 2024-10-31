package vn.edu.tlu.nhom7.calendar.activity.timezone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import vn.edu.tlu.nhom7.calendar.R;
import vn.edu.tlu.nhom7.calendar.activity.MainActivity;

public class TimezoneActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_TIMEZONE = 1;
    private ListView listView;
    private TextView currentDateTextView;
    private TextView currentTimeTextView;
    private List<TimezoneInfo> timezonesList = new ArrayList<>();
    private TimezoneAdapter adapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timezone);

        currentDateTextView = findViewById(R.id.currentDateTextView);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        listView = findViewById(R.id.listView);
        Button addButton = findViewById(R.id.addButton);

        // Thiết lập Adapter cho ListView
        adapter = new TimezoneAdapter(this, timezonesList);
        listView.setAdapter(adapter);

        // Xử lý khi ấn nút "Thêm"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimezoneActivity.this, SelectTimezoneActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TIMEZONE);
            }
        });

        // Cập nhật thời gian hiện tại và tất cả múi giờ
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCurrentTimeAndDate(); // Cập nhật thời gian hiện tại
                updateAllTimezones(); // Cập nhật tất cả múi giờ
                handler.postDelayed(this, 1000); // Cập nhật mỗi giây
            }
        }, 0);
    }

    // Nhận kết quả khi chọn múi giờ từ View 2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TIMEZONE && resultCode == RESULT_OK) {
            String selectedTimezone = data.getStringExtra("selectedTimezone");
            String city = data.getStringExtra("city"); // Nhận tên thành phố từ Intent

            // Loại bỏ "Asia/" từ tên múi giờ


            // Thêm mới thông tin múi giờ và thành phố
            timezonesList.add(new TimezoneInfo(selectedTimezone, city, getCurrentTime(selectedTimezone)));
            adapter.notifyDataSetChanged(); // Cập nhật ListView
        }
    }

    // Cập nhật ngày tháng và thời gian hiện tại
    private void updateCurrentTimeAndDate() {
        // Lấy thời gian hiện tại theo múi giờ hệ thống
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date now = new Date();

        // Hiển thị ngày tháng
        currentDateTextView.setText(dateFormat.format(now));

        // Hiển thị giờ và phút
        currentTimeTextView.setText(timeFormat.format(now));
    }

    // Phương thức lấy thời gian hiện tại theo múi giờ
    private String getCurrentTime(String timezoneId) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timezoneId));
        return sdf.format(new Date()); // Trả về giờ và phút theo múi giờ đã chọn
    }

    // Cập nhật thời gian cho tất cả các múi giờ
    private void updateAllTimezones() {
        for (TimezoneInfo timezone : timezonesList) {
            timezone.setCurrentTime(getCurrentTime(timezone.getTimezoneId())); // Cập nhật thời gian
        }
        adapter.notifyDataSetChanged(); // Cập nhật ListView
    }
}
