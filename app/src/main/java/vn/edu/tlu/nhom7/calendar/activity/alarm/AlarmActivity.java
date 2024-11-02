package vn.edu.tlu.nhom7.calendar.activity.alarm;

// Khai báo các thư viện cần thiết
import android.app.AlarmManager; // Quản lý báo thức trong hệ thống Android
import android.app.PendingIntent; // Tạo intent trì hoãn, kích hoạt trong tương lai
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar; // Làm việc với ngày và giờ
import java.util.Locale;

import vn.edu.tlu.nhom7.calendar.R;

public class AlarmActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    private TimePicker timePicker; // Bộ chọn giờ
    private Button btnSetAlarm, btnStopAlarm; // Nút để cài và hủy báo thức
    private PendingIntent pendingIntent; // Intent để báo thức hoạt động
    private AlarmManager alarmManager; // Quản lý báo thức

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Liên kết các thành phần giao diện với XML
        timePicker = findViewById(R.id.timePicker);
        btnSetAlarm = findViewById(R.id.btn_set_alarm);
        btnStopAlarm = findViewById(R.id.btn_stop_alarm);

        // Đặt TimePicker sử dụng định dạng 24 giờ
        timePicker.setIs24HourView(true);

        // Khởi tạo AlarmManager để quản lý báo thức
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Gán sự kiện cho các nút
        btnSetAlarm.setOnClickListener(v -> setAlarm());
        btnStopAlarm.setOnClickListener(v -> stopAlarm());
    }

    private void setAlarm() {
        // Lấy giờ và phút được chọn từ TimePicker
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Tạo lịch để thiết lập giờ cho báo thức
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Kiểm tra nếu thời gian đã qua, đặt báo thức cho ngày hôm sau
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Tạo Intent để gửi tới AlarmReceiver khi báo thức kêu
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                        ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                        : PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Đặt báo thức chính xác và cho phép báo thức khi ở chế độ tiết kiệm pin
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, // Thức thiết bị khi đến giờ
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            // Hiển thị thông báo khi báo thức được cài đặt thành công
            String alarmTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            Toast.makeText(this, "Báo thức đã được cài lúc " + alarmTime, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi: Không thể cài đặt báo thức", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAlarm() {
        // Hủy báo thức nếu có tồn tại
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Báo thức đã được hủy", Toast.LENGTH_SHORT).show();
        }

        // Dừng âm báo thức (nếu có)
        AlarmReceiver.stopSound();
    }
}

