package vn.edu.tlu.nhom7.calendar.activity.map;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import vn.edu.tlu.nhom7.calendar.R;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cấu hình OSMDroid để tránh lỗi
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_map);  // Gắn layout cho activity

        // Khởi tạo MapView
        mapView = findViewById(R.id.map);
        mapView.setMultiTouchControls(true);  // Hỗ trợ zoom cảm ứng đa điểm

        // Đặt vị trí ban đầu tại Hà Nội
        GeoPoint startPoint = new GeoPoint(21.0285, 105.8542);
        mapView.getController().setZoom(15.0);
        mapView.getController().setCenter(startPoint);

        // Khởi tạo GestureDetector để nhận diện double-tap
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Lấy tọa độ từ sự kiện double-tap
                GeoPoint tappedPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
                // Đặt marker tại điểm đã double-tap
                addMarkerAtLocation(tappedPoint);
                return true;
            }
        });

        // Gán sự kiện chạm cho MapView
        mapView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    // Thêm marker vào vị trí đã chọn
    private void addMarkerAtLocation(GeoPoint point) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle("Điểm đã chọn");
        mapView.getOverlays().clear();  // Xóa các marker trước đó nếu cần
        mapView.getOverlays().add(marker);
        mapView.invalidate();  // Làm mới bản đồ
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();  // Dừng bản đồ khi tạm dừng activity
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();  // Khởi động lại bản đồ khi activity tiếp tục
    }
}
