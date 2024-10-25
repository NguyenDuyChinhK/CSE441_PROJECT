package vn.edu.tlu.nhom7.calendar.activity.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;
import vn.edu.tlu.nhom7.calendar.R;

public class AlarmReceiver extends BroadcastReceiver {

    private static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mediaPlayer == null) {
            // Phát âm thanh báo thức
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
            mediaPlayer.start();

            // Giải phóng tài nguyên khi âm thanh đã phát xong
            mediaPlayer.setOnCompletionListener(mp -> stopSound());

            Toast.makeText(context, "Báo thức kêu!", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm dừng chuông và giải phóng tài nguyên
    public static void stopSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
