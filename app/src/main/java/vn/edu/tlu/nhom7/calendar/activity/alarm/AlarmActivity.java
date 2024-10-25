package vn.edu.tlu.nhom7.calendar.activity.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

import vn.edu.tlu.nhom7.calendar.R;

public class AlarmActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSetAlarm, btnStopAlarm;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timePicker = findViewById(R.id.timePicker);
        btnSetAlarm = findViewById(R.id.btn_set_alarm);
        btnStopAlarm = findViewById(R.id.btn_stop_alarm);

        timePicker.setIs24HourView(true);

        // Khởi tạo AlarmManager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        btnSetAlarm.setOnClickListener(v -> setAlarm());
        btnStopAlarm.setOnClickListener(v -> stopAlarm());
    }

    private void setAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Nếu thời gian đã qua, đặt cho ngày hôm sau
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                        ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        : PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            String alarmTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            Toast.makeText(this, "Báo thức đã được cài lúc " + alarmTime, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi: Không thể cài đặt báo thức", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAlarm() {
        // Hủy báo thức
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Báo thức đã được hủy", Toast.LENGTH_SHORT).show();
        }

        // Dừng chuông
        AlarmReceiver.stopSound();
    }
}
