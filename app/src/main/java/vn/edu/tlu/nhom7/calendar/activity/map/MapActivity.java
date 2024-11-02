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
    private MapView mapView;                  // Hiển thị bản đồ
    private GestureDetector gestureDetector;   // Nhận diện các cử chỉ trên bản đồ
    private FusedLocationProviderClient fusedLocationClient; // Lấy vị trí người dùng
    private GeoPoint userLocation;            // Lưu vị trí người dùng
    private Marker userMarker;                // Đánh dấu vị trí của người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cấu hình OSMDroid để tránh lỗi hiển thị bản đồ
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_map);

        // Khởi tạo MapView
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);  // Bật điều khiển đa chạm cho MapView

        // Khởi tạo FusedLocationProviderClient để lấy vị trí người dùng
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Kiểm tra quyền truy cập vị trí và lấy vị trí người dùng
        if (checkLocationPermission()) {
            getUserLocation();
        } else {
            requestLocationPermission(); // Yêu cầu quyền truy cập nếu chưa được cấp
        }

        // Khởi tạo GestureDetector để nhận diện cử chỉ double-tap
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Lấy tọa độ của vị trí được chạm trên bản đồ
                GeoPoint tappedPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
                addMarkerAtLocation(tappedPoint);  // Thêm marker tại vị trí chạm
                drawRouteTo(tappedPoint);          // Vẽ đường đến vị trí đã chọn
                return true;
            }
        });

        // Gán sự kiện chạm cho MapView để kích hoạt GestureDetector
        mapView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    // Kiểm tra quyền truy cập vị trí
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    // Yêu cầu quyền truy cập vị trí nếu chưa được cấp
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Lấy vị trí hiện tại của người dùng
    private void getUserLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Lưu vị trí người dùng và hiển thị trên bản đồ
                        userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        updateUserLocationMarker();  // Cập nhật marker vị trí người dùng
                        mapView.getController().setZoom(15.0);  // Đặt mức thu phóng
                        mapView.getController().setCenter(userLocation); // Đặt trung tâm bản đồ tại vị trí người dùng
                    } else {
                        Toast.makeText(MapActivity.this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Cập nhật marker cho vị trí người dùng trên bản đồ
    private void updateUserLocationMarker() {
        if (userMarker == null) {
            userMarker = new Marker(mapView);
            userMarker.setTitle("Vị trí của bạn");
            userMarker.setIcon(getResources().getDrawable(R.drawable.ic_user_location)); // Đặt biểu tượng cho marker
            mapView.getOverlays().add(userMarker); // Thêm marker vào bản đồ
        }
        userMarker.setPosition(userLocation); // Đặt vị trí của marker
        mapView.invalidate(); // Cập nhật lại bản đồ
    }

    // Thêm marker tại vị trí đã chọn trên bản đồ
    private void addMarkerAtLocation(GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);       // Đặt vị trí marker
        marker.setTitle("Điểm đến");     // Đặt tiêu đề cho marker
        mapView.getOverlays().add(marker); // Thêm marker vào bản đồ
        mapView.invalidate();             // Cập nhật lại bản đồ
    }

    // Vẽ đường từ vị trí người dùng đến điểm đã chọn
    private void drawRouteTo(GeoPoint destination) {
        if (userLocation == null) {
            Toast.makeText(this, "Vị trí người dùng chưa được xác định", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo danh sách các điểm của tuyến đường
        List<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(userLocation); // Thêm vị trí người dùng vào tuyến đường
        routePoints.add(destination);  // Thêm điểm đến vào tuyến đường

        // Vẽ một đường nối giữa các điểm
        Polyline routeLine = new Polyline();
        routeLine.setPoints(routePoints);
        routeLine.setTitle("Lộ trình"); // Đặt tiêu đề cho tuyến đường

        mapView.getOverlays().add(routeLine); // Thêm tuyến đường vào bản đồ
        mapView.invalidate();                 // Cập nhật lại bản đồ
    }

    // Xử lý kết quả khi người dùng phản hồi yêu cầu cấp quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation(); // Nếu được cấp quyền, lấy vị trí người dùng
            } else {
                Toast.makeText(this, "Yêu cầu quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Tạm dừng hoạt động của MapView khi ứng dụng bị tạm dừng
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // Tiếp tục hoạt động của MapView khi ứng dụng hoạt động trở lại
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
