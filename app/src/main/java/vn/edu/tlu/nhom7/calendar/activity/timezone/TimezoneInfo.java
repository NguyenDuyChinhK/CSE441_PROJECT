package vn.edu.tlu.nhom7.calendar.activity.timezone;

public class TimezoneInfo {
    private String timezoneId;   // Múi giờ
    private String city;          // Tên thành phố
    private String currentTime;   // Thời gian hiện tại

    public TimezoneInfo(String timezoneId, String city, String currentTime) {
        this.timezoneId = timezoneId;
        this.city = city;
        this.currentTime = currentTime;
    }

    public String getTimezoneId() {
        return timezoneId;
    }

    public String getCity() {
        return city;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime; // Cập nhật thời gian
    }
}