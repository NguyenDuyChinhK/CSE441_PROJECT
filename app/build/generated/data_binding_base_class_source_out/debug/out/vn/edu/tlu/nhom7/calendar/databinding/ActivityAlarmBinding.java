// Generated by view binder compiler. Do not edit!
package vn.edu.tlu.nhom7.calendar.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import vn.edu.tlu.nhom7.calendar.R;

public final class ActivityAlarmBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnSetAlarm;

  @NonNull
  public final Button btnStopAlarm;

  @NonNull
  public final TimePicker timePicker;

  @NonNull
  public final TextView tvAlarmTitle;

  private ActivityAlarmBinding(@NonNull LinearLayout rootView, @NonNull Button btnSetAlarm,
      @NonNull Button btnStopAlarm, @NonNull TimePicker timePicker,
      @NonNull TextView tvAlarmTitle) {
    this.rootView = rootView;
    this.btnSetAlarm = btnSetAlarm;
    this.btnStopAlarm = btnStopAlarm;
    this.timePicker = timePicker;
    this.tvAlarmTitle = tvAlarmTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAlarmBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAlarmBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_alarm, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAlarmBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_set_alarm;
      Button btnSetAlarm = ViewBindings.findChildViewById(rootView, id);
      if (btnSetAlarm == null) {
        break missingId;
      }

      id = R.id.btn_stop_alarm;
      Button btnStopAlarm = ViewBindings.findChildViewById(rootView, id);
      if (btnStopAlarm == null) {
        break missingId;
      }

      id = R.id.timePicker;
      TimePicker timePicker = ViewBindings.findChildViewById(rootView, id);
      if (timePicker == null) {
        break missingId;
      }

      id = R.id.tv_alarmTitle;
      TextView tvAlarmTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvAlarmTitle == null) {
        break missingId;
      }

      return new ActivityAlarmBinding((LinearLayout) rootView, btnSetAlarm, btnStopAlarm,
          timePicker, tvAlarmTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}