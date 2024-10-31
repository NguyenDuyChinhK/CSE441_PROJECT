package vn.edu.tlu.nhom7.calendar.activity.timer;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Locale;
import vn.edu.tlu.nhom7.calendar.R;



import java.util.Locale;


public class TimerActivity extends AppCompatActivity {
    private static final long DEFAULT_TIME_IN_MILLIS = 60000;
    private Button btnSetTime;
    private ImageButton btnPlay, btnPause, btnStop;
    private TextView tvTimer;

    private Spinner spinnerMusic;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 0;
    private long selectedTimeInMillis = DEFAULT_TIME_IN_MILLIS;
    private MediaPlayer mediaPlayer;
    private Dialog finishDialog;
    private int selectedMusicResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTimer = findViewById(R.id.tv_timer);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        spinnerMusic = findViewById(R.id.spinner_music);
        btnSetTime = findViewById(R.id.btn_set_time);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.music_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMusic.setAdapter(adapter);
        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedMusicResId = R.raw.digital_alarm_clock;
                        break;
                    case 1:
                        selectedMusicResId = R.raw.super_idol;
                        break;
                    case 2:
                        selectedMusicResId = R.raw.samsung_alarm_sound;
                        break;
                    case 3:
                        selectedMusicResId = R.raw.nevergonnagiveyouup;
                        break;
                    case 4:
                        selectedMusicResId = R.raw.iphone_alarm_ring;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        timeLeftInMillis = selectedTimeInMillis;
        updateTimerUI();

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog();
            }
        });
        btnPlay.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> togglePause());
        btnStop.setOnClickListener(v -> stopTimer());
    }

    private void openTimePickerDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_set_time);
        NumberPicker numberPickerHours = dialog.findViewById(R.id.number_picker_hours);
        NumberPicker numberPickerMinutes = dialog.findViewById(R.id.number_picker_minutes);
        NumberPicker numberPickerSeconds = dialog.findViewById(R.id.number_picker_seconds);
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);
        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);

        // Thiết lập giá trị mặc định cho NumberPicker
        numberPickerHours.setValue((int) (selectedTimeInMillis / 3600000));
        numberPickerMinutes.setValue((int) ((selectedTimeInMillis % 3600000) / 60000));
        numberPickerSeconds.setValue((int) ((selectedTimeInMillis % 60000) / 1000));

        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = numberPickerHours.getValue();
                int minutes = numberPickerMinutes.getValue();
                int seconds = numberPickerSeconds.getValue();
                selectedTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
                timeLeftInMillis = selectedTimeInMillis;
                updateTimerUI();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();

            // Nút hủy
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
             btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss(); // Đóng dialog mà không cập nhật thời gian
                }
        });

        dialog.show();

    }

    private void startTimer() {
        if (selectedTimeInMillis <= 0) {
            return; // Không làm gì nếu thời gian đã đặt là 0
        }

        if (timeLeftInMillis == 0) {
            timeLeftInMillis = selectedTimeInMillis;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerUI();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                showFinishDialog();
                playSound();
                resetTimer();

                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
            }
        }.start();

        isRunning = true;
        btnPlay.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
    }

    private void togglePause() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
            btnPause.setImageResource(R.drawable.ic_play);
        } else {
            startTimer();
            btnPause.setImageResource(R.drawable.ic_pause);
        }
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        resetTimer();
        btnPause.setImageResource(R.drawable.ic_pause);
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
    }

    private void resetTimer() {
        timeLeftInMillis = selectedTimeInMillis;
        updateTimerUI();
        isRunning = false;
    }

    private void showFinishDialog() {
        finishDialog = new Dialog(this);
        finishDialog.setContentView(R.layout.dialog_finish_timer);
        finishDialog.findViewById(R.id.btn_stop).setOnClickListener(v -> {
            stopSound();
            finishDialog.dismiss();
        });
        finishDialog.setCancelable(false);
        finishDialog.show();
    }

    private void playSound() {
        if (mediaPlayer == null && selectedMusicResId != 0) {
            mediaPlayer = MediaPlayer.create(this, selectedMusicResId);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void updateTimerUI() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        tvTimer.setText(timeFormatted);
    }
}
