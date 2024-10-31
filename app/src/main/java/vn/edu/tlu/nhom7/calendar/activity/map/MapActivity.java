package vn.edu.tlu.nhom7.calendar.activity.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.nhom7.calendar.R;

public class MapActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapView mapView;
    private GestureDetector gestureDetector;
    private FusedLocationProviderClient fusedLocationClient;
    private GeoPoint userLocation;  // Lưu vị trí người dùng
    private Marker userMarker;      // Marker của người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cấu hình OSMDroid để tránh lỗi
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_map);

        // Khởi tạo MapView
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Kiểm tra quyền và lấy vị trí người dùng
        if (checkLocationPermission()) {
            getUserLocation();
        } else {
            requestLocationPermission();
        }

        // GestureDetector cho sự kiện double-tap
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                GeoPoint tappedPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
                addMarkerAtLocation(tappedPoint);
                drawRouteTo(tappedPoint);  // Dẫn đường đến điểm đã chọn
                return true;
            }
        });

        // Gán sự kiện chạm cho MapView
        mapView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getUserLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        updateUserLocationMarker();  // Cập nhật marker vị trí người dùng
                        mapView.getController().setZoom(15.0);
                        mapView.getController().setCenter(userLocation);
                    } else {
                        Toast.makeText(MapActivity.this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Cập nhật marker vị trí người dùng
    private void updateUserLocationMarker() {
        if (userMarker == null) {
            userMarker = new Marker(mapView);
            userMarker.setTitle("Vị trí của bạn");
            userMarker.setIcon(getResources().getDrawable(R.drawable.ic_user_location)); // Thêm biểu tượng cho marker
            mapView.getOverlays().add(userMarker);
        }
        userMarker.setPosition(userLocation);
        mapView.invalidate();
    }

    // Thêm marker tại vị trí đã chọn
    private void addMarkerAtLocation(GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle("Điểm đến");
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    // Vẽ đường từ vị trí người dùng đến điểm đã chọn
    private void drawRouteTo(GeoPoint destination) {
        if (userLocation == null) {
            Toast.makeText(this, "Vị trí người dùng chưa được xác định", Toast.LENGTH_SHORT).show();
            return;
        }

        List<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(userLocation);
        routePoints.add(destination);

        Polyline routeLine = new Polyline();
        routeLine.setPoints(routePoints);
        routeLine.setTitle("Lộ trình");

        mapView.getOverlays().add(routeLine);
        mapView.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, "Yêu cầu quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
