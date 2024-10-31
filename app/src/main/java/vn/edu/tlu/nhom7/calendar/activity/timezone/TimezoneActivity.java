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

        adapter = new TimezoneAdapter(this, timezonesList);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimezoneActivity.this, SelectTimezoneActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TIMEZONE);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCurrentTimeAndDate();
                updateAllTimezones();
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TIMEZONE && resultCode == RESULT_OK) {
            String selectedTimezone = data.getStringExtra("selectedTimezone");
            String city = data.getStringExtra("city");

            timezonesList.add(new TimezoneInfo(selectedTimezone, city, getCurrentTime(selectedTimezone)));
            adapter.notifyDataSetChanged();
        }
    }

    private void updateCurrentTimeAndDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date now = new Date();

        currentDateTextView.setText(dateFormat.format(now));

        currentTimeTextView.setText(timeFormat.format(now));
    }


    private String getCurrentTime(String timezoneId) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timezoneId));
        return sdf.format(new Date()); // Trả về giờ và phút theo múi giờ đã chọn
    }


    private void updateAllTimezones() {
        for (TimezoneInfo timezone : timezonesList) {
            timezone.setCurrentTime(getCurrentTime(timezone.getTimezoneId())); // Cập nhật thời gian
        }
        adapter.notifyDataSetChanged(); // Cập nhật ListView
    }
}
