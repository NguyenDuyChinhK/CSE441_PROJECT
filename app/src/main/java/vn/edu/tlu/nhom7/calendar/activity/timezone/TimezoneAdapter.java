package vn.edu.tlu.nhom7.calendar.activity.timezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.tlu.nhom7.calendar.R;

public class TimezoneAdapter extends ArrayAdapter<TimezoneInfo> {
    public TimezoneAdapter(Context context, List<TimezoneInfo> timezones) {
        super(context, 0, timezones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra xem view có cần tái sử dụng không
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_timezone, parent, false);
        }

        // Lấy dữ liệu cho vị trí hiện tại
        TimezoneInfo timezoneInfo = getItem(position);

        // Thiết lập TextView cho tên thành phố và thời gian
        TextView cityTextView = convertView.findViewById(R.id.cityTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);

        // Hiển thị dữ liệu
        cityTextView.setText(timezoneInfo.getCity());
        timeTextView.setText(timezoneInfo.getCurrentTime());

        return convertView;
    }
}

