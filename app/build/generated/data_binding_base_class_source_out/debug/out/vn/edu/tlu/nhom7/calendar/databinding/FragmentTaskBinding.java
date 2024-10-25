// Generated by view binder compiler. Do not edit!
package vn.edu.tlu.nhom7.calendar.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import vn.edu.tlu.nhom7.calendar.R;

public final class FragmentTaskBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnCreateTask;

  @NonNull
  public final MaterialCalendarView calendarView;

  @NonNull
  public final ImageView gifImageView;

  @NonNull
  public final LinearLayout main;

  @NonNull
  public final RecyclerView taskRecycler;

  private FragmentTaskBinding(@NonNull LinearLayout rootView, @NonNull Button btnCreateTask,
      @NonNull MaterialCalendarView calendarView, @NonNull ImageView gifImageView,
      @NonNull LinearLayout main, @NonNull RecyclerView taskRecycler) {
    this.rootView = rootView;
    this.btnCreateTask = btnCreateTask;
    this.calendarView = calendarView;
    this.gifImageView = gifImageView;
    this.main = main;
    this.taskRecycler = taskRecycler;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentTaskBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentTaskBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_task, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentTaskBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnCreateTask;
      Button btnCreateTask = ViewBindings.findChildViewById(rootView, id);
      if (btnCreateTask == null) {
        break missingId;
      }

      id = R.id.calendarView;
      MaterialCalendarView calendarView = ViewBindings.findChildViewById(rootView, id);
      if (calendarView == null) {
        break missingId;
      }

      id = R.id.gifImageView;
      ImageView gifImageView = ViewBindings.findChildViewById(rootView, id);
      if (gifImageView == null) {
        break missingId;
      }

      LinearLayout main = (LinearLayout) rootView;

      id = R.id.taskRecycler;
      RecyclerView taskRecycler = ViewBindings.findChildViewById(rootView, id);
      if (taskRecycler == null) {
        break missingId;
      }

      return new FragmentTaskBinding((LinearLayout) rootView, btnCreateTask, calendarView,
          gifImageView, main, taskRecycler);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}