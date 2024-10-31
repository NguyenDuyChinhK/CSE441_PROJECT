package vn.edu.tlu.nhom7.calendar.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import vn.edu.tlu.nhom7.calendar.R;
import vn.edu.tlu.nhom7.calendar.activity.alarm.AlarmActivity;
import vn.edu.tlu.nhom7.calendar.activity.home.CalendarFragment;

import vn.edu.tlu.nhom7.calendar.activity.task.TaskFragment;
import vn.edu.tlu.nhom7.calendar.activity.timer.TimerActivity;


import vn.edu.tlu.nhom7.calendar.activity.map.MapActivity;

import vn.edu.tlu.nhom7.calendar.activity.user.UserProfile;
import vn.edu.tlu.nhom7.calendar.activity.weather.WeatherFragment;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "1";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Thiết lập Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập DrawerLayout và NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý sự kiện chọn menu trong NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Kiểm tra nếu có dữ liệu truyền vào Intent
        if (getIntent().hasExtra("key_task")) {
            loadFragment(new TaskFragment(), false);
            getIntent().removeExtra("key_task");
        } else {
            loadFragment(new CalendarFragment(), false);
        }

        createNotificationChannel();
    }

    private void handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            loadFragment(new CalendarFragment(), false);
        } else if (itemId == R.id.nav_taskManager) {
            loadFragment(new TaskFragment(), false);
        } else if (itemId == R.id.nav_userProfile) {
            startActivity(new Intent(this, UserProfile.class));
            finish();
        } else if (itemId == R.id.nav_weather) {
            loadFragment(new WeatherFragment(), false);
        } else if (itemId == R.id.nav_alarm) {
            startActivity(new Intent(this, AlarmActivity.class));
        } else if (itemId == R.id.nav_map) {
            startActivity(new Intent(this, MapActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_timer) {
            startActivity(new Intent(this, TimerActivity.class));
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
