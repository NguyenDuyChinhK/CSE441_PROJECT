package vn.edu.tlu.nhom7.calendar.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.view.Menu;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.tlu.nhom7.calendar.R;
import vn.edu.tlu.nhom7.calendar.activity.alarm.AlarmActivity;
import vn.edu.tlu.nhom7.calendar.activity.home.CalendarFragment;
import vn.edu.tlu.nhom7.calendar.activity.task.CreateTaskActivity;
import vn.edu.tlu.nhom7.calendar.activity.task.TaskFragment;
import vn.edu.tlu.nhom7.calendar.activity.timer.TimerActivity;

import vn.edu.tlu.nhom7.calendar.activity.user.UserProfile;
import vn.edu.tlu.nhom7.calendar.activity.weather.WeatherFragment;
import vn.edu.tlu.nhom7.calendar.activity.map.MapActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "1";

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Thiết lập Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        if (getIntent().hasExtra("key_task")) {
            loadFragment(new TaskFragment(), false);
            getIntent().removeExtra("key_task");
        } else {
            loadFragment(new CalendarFragment(), false);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    loadFragment(new CalendarFragment(), false);
                } else if (itemId == R.id.nav_taskManager) {
                    loadFragment(new TaskFragment(), false);
                } else if (itemId == R.id.nav_userProfile) {
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.nav_weather) {
                    loadFragment(new WeatherFragment(), false);
                } else if (itemId == R.id.nav_alarm) {
                    Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                    startActivity(intent);
                    return true; // Ngăn việc xử lý thêm
                } else if (itemId == R.id.nav_map) {  // Xử lý mở MapActivity
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                    return true;
                }
                return true;
            }
        });

        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_userProfile) {

            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_timer) {
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Channel";
            String description = "Channel for task notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
