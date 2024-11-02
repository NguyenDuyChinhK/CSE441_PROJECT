package vn.edu.tlu.nhom7.calendar.activity.home;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.ChineseCalendar;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import vn.edu.tlu.nhom7.calendar.R;
import vn.edu.tlu.nhom7.calendar.adapter.EventAdapter;
import vn.edu.tlu.nhom7.calendar.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendar;
    private CalendarDay selectedDate;
    private TextView tvDuong, tvAm;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private List<Event> lunarEventList;
    private List<Event> filteredEventList;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendar = rootView.findViewById(R.id.calendarView);
        tvDuong = rootView.findViewById(R.id.tv_duong);
        tvAm = rootView.findViewById(R.id.tv_am);
        recyclerView = rootView.findViewById(R.id.list_event);

        selectedDate = CalendarDay.today();
        calendar.setDateSelected(selectedDate, true);
        updateDateTextViews(selectedDate);

        calendar.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(selectedDate);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.setDaysDisabled(false);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.parseColor("#6750a4"));
                drawable.setCornerRadius(8);
                view.setBackgroundDrawable(drawable);
                view.addSpan(new ForegroundColorSpan(Color.WHITE));
            }
        });

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date;
                updateDateTextViews(selectedDate);
                updateEventListForSelectedDate(selectedDate);
                calendar.invalidateDecorators();
            }
        });

        loadEventData();
        loadLunarEventData();
        filteredEventList = new ArrayList<>();
        eventAdapter = new EventAdapter(filteredEventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);

        updateEventListForSelectedDate(selectedDate);


        try {
            highlightDates(eventList);
            highlightLunarDates(lunarEventList);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return rootView;
    }

    private void updateDateTextViews(CalendarDay date) {
        String solarDate = date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
        tvDuong.setText(solarDate);

        String lunarDate = convertSolarToLunar(date);
        tvAm.setText(lunarDate);
    }

    private String convertSolarToLunar(CalendarDay date) {
        ChineseCalendar chineseCalendar = new ChineseCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDay());
        chineseCalendar.setTimeInMillis(calendar.getTimeInMillis());

        int lunarYear = chineseCalendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int lunarMonth = chineseCalendar.get(ChineseCalendar.MONTH) + 1;
        int lunarDay = chineseCalendar.get(ChineseCalendar.DAY_OF_MONTH);
        boolean isLeapMonth = chineseCalendar.get(ChineseCalendar.IS_LEAP_MONTH) == 1;

        String lunarDate = (isLeapMonth ? "Leap " : "") + lunarDay + "/" + lunarMonth + "/" + lunarYear;

        return lunarDate;
    }

    private void loadEventData() {
        eventList = new ArrayList<>();
        eventList.add(new Event("01/01", "Tết Dương lịch"));
        eventList.add(new Event("14/02", "Ngày lễ tình nhân"));
        eventList.add(new Event("03/02", "Ngày thành lập Đảng Cộng sản Việt Nam"));
        eventList.add(new Event("27/02", "Ngày Thầy thuốc Việt Nam"));
        eventList.add(new Event("08/03", "Ngày Quốc tế Phụ nữ"));
        eventList.add(new Event("20/03", "Ngày Quốc tế Hạnh phúc"));
        eventList.add(new Event("26/03", "Ngày thành lập Đoàn TNCS Hồ Chí Minh"));
        eventList.add(new Event("22/04", "Ngày Trái Đất"));
        eventList.add(new Event("30/04", "Ngày Giải phóng miền Nam, thống nhất đất nước"));
        eventList.add(new Event("01/05", "Ngày Quốc tế Lao động"));
        eventList.add(new Event("07/05", "Ngày Chiến thắng Điện Biên Phủ"));
        eventList.add(new Event("09/05", "Ngày của Mẹ"));
        eventList.add(new Event("19/05", "Ngày sinh Chủ tịch Hồ Chí Minh"));
        eventList.add(new Event("01/06", "Ngày Quốc tế Thiếu nhi"));
        eventList.add(new Event("05/06", "Ngày Môi trường Thế giới"));
        eventList.add(new Event("17/06", "Ngày phòng chống sa mạc hóa và hạn hán thế giới"));
        eventList.add(new Event("28/06", "Ngày Gia đình Việt Nam"));
        eventList.add(new Event("11/07", "Ngày Dân số Thế giới"));
        eventList.add(new Event("27/07", "Ngày Thương binh liệt sĩ"));
        eventList.add(new Event("19/08", "Ngày Cách mạng tháng Tám thành công"));
        eventList.add(new Event("02/09", "Ngày Quốc khánh"));
        eventList.add(new Event("07/09", "Ngày thành lập Đài Truyền hình Việt Nam"));
        eventList.add(new Event("10/10", "Ngày Giải phóng Thủ đô"));
        eventList.add(new Event("13/10", "Ngày Doanh nhân Việt Nam"));
        eventList.add(new Event("14/10", "Ngày thành lập Hội Nông dân Việt Nam"));
        eventList.add(new Event("20/10", "Ngày Phụ nữ Việt Nam"));
        eventList.add(new Event("20/11", "Ngày Nhà giáo Việt Nam"));
        eventList.add(new Event("24/12", "Đêm Giáng sinh"));
        eventList.add(new Event("25/12", "Lễ Giáng Sinh"));
    }

    private void loadLunarEventData() {
        lunarEventList = new ArrayList<>();
        lunarEventList.add(new Event("23/12", "Ngày Đưa Ông Táo Về Trời (Âm lịch)"));
        lunarEventList.add(new Event("01/01", "Tết Nguyên đán (Âm lịch)"));
        lunarEventList.add(new Event("02/01", "Tết Nguyên đán (Âm lịch)"));
        lunarEventList.add(new Event("03/01", "Tết Nguyên đán (Âm lịch)"));
        lunarEventList.add(new Event("15/01", "Tết Nguyên Tiêu (Âm lịch)"));
        lunarEventList.add(new Event("03/03", "Tết Hàn Thực (Âm lịch)"));
        lunarEventList.add(new Event("10/03", "Giỗ tổ Hùng Vương (Âm lịch)"));
        lunarEventList.add(new Event("15/04", "Lễ Phật Đản (Âm lịch)"));
        lunarEventList.add(new Event("05/05", "Tết Đoan Ngọ (Âm lịch)"));
        lunarEventList.add(new Event("15/07", "Tết Trung nguyên / Lễ Vu-lan (Âm lịch)"));
        lunarEventList.add(new Event("15/08", "Tết Trung Thu (Âm lịch)"));

    }


    private void updateEventListForSelectedDate(CalendarDay date) {
        String selectedDay = String.format("%02d/%02d", date.getDay(), date.getMonth() + 1);
        filteredEventList.clear();

        // Lọc sự kiện dương lịch
        for (Event event : eventList) {
            if (event.getDate().equals(selectedDay)) {
                filteredEventList.add(event);
            }
        }

        // Lọc sự kiện âm lịch
        for (Event lunarEvent : lunarEventList) {
            // Chuyển đổi ngày âm lịch sang dương lịch
            Calendar lunarCalendar = convertLunarToSolar(lunarEvent.getDate(), date.getYear());

            if (lunarCalendar != null) {
                // So sánh với ngày đã chọn
                CalendarDay lunarCalendarDay = CalendarDay.from(lunarCalendar);
                if (lunarCalendarDay.equals(date)) {
                    filteredEventList.add(lunarEvent);
                }
            }
        }

        // Cập nhật danh sách sự kiện trên UI
        eventAdapter.notifyDataSetChanged();
    }

    private Calendar convertLunarToSolar(String lunarDate, int year) {
        String[] parts = lunarDate.split("/");
        int lunarDay = Integer.parseInt(parts[0]);
        int lunarMonth = Integer.parseInt(parts[1]);

        // Kiểm tra năm âm lịch hợp lệ
        if (year < 1900 || year > 2100) {
            Log.e("Lunar Conversion", "Invalid year: " + year);
            throw new IllegalArgumentException("Năm không hợp lệ");
        }

        // Kiểm tra ngày và tháng âm lịch hợp lệ
        if (lunarDay < 1 || lunarDay > 30 || lunarMonth < 1 || lunarMonth > 12) {
            Log.e("Lunar Conversion", "Invalid lunar date: " + lunarDate);
            throw new IllegalArgumentException("Ngày hoặc tháng âm không hợp lệ");
        }

        ChineseCalendar chineseCalendar = new ChineseCalendar();
        chineseCalendar.set(ChineseCalendar.EXTENDED_YEAR, year + 2637);
        chineseCalendar.set(ChineseCalendar.MONTH, lunarMonth - 1);
        chineseCalendar.set(ChineseCalendar.DAY_OF_MONTH, lunarDay);

        // Tạo lịch dương
        Calendar solarCalendar = Calendar.getInstance();
        solarCalendar.setTimeInMillis(chineseCalendar.getTimeInMillis());


        return solarCalendar;
    }




    private void highlightDates(List<Event> eventList) throws ParseException {
        for (Event event : eventList) {
            String date;
            int color = ContextCompat.getColor(requireContext(), R.color.color_hightlighEvent);

            for (int year = 2020; year <= 2029; year++) {
                date = event.getDate() + "/" + year;
                try {
                    Date eventDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                    CalendarDay calendarDay = CalendarDay.from(eventDate);
                    CurrentDayDecorator decorator = new CurrentDayDecorator(eventDate, color);
                    calendar.addDecorator(decorator);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void highlightLunarDates(List<Event> lunarEventList) throws ParseException {
        int color = ContextCompat.getColor(requireContext(), R.color.color_hightlighBlue);

        for (Event lunarEvent : lunarEventList) {
            String[] lunarDateParts = lunarEvent.getDate().split("/");
            if (lunarDateParts.length < 2) continue;

            int lunarDay = Integer.parseInt(lunarDateParts[0]);
            int lunarMonth = Integer.parseInt(lunarDateParts[1]);

            for (int year = 2020; year <= 2029; year++) {
                ChineseCalendar chineseCalendar = new ChineseCalendar();
                chineseCalendar.set(ChineseCalendar.EXTENDED_YEAR, year + 2637);
                chineseCalendar.set(ChineseCalendar.MONTH, lunarMonth - 1);
                chineseCalendar.set(ChineseCalendar.DAY_OF_MONTH, lunarDay);


                Date lunarDate = new Date(chineseCalendar.getTimeInMillis());
                CalendarDay calendarDay = CalendarDay.from(lunarDate);
                CurrentDayDecorator decorator = new CurrentDayDecorator(lunarDate, color);
                calendar.addDecorator(decorator);

            }
        }
    }

}

