// Generated by view binder compiler. Do not edit!
package com.my.test.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.my.test.EcgShowView;
import com.my.test.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button button2;

  @NonNull
  public final ConstraintLayout ecgShow;

  @NonNull
  public final EcgShowView ecgShowView;

  @NonNull
  public final TextView greetings;

  @NonNull
  public final Button onClickConnect;

  @NonNull
  public final Button onClickPowerOff;

  @NonNull
  public final Button onClickReadDeviceInformation;

  @NonNull
  public final Button onClickShare;

  @NonNull
  public final Button onClickShow;

  @NonNull
  public final Button onClickStartSession;

  @NonNull
  public final Button onClickStopSession;

  @NonNull
  public final SeekBar rateSlider;

  @NonNull
  public final Spinner selectFrequency;

  @NonNull
  public final TextView showDeviceInformation;

  @NonNull
  public final TextView textView;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button button2,
      @NonNull ConstraintLayout ecgShow, @NonNull EcgShowView ecgShowView,
      @NonNull TextView greetings, @NonNull Button onClickConnect, @NonNull Button onClickPowerOff,
      @NonNull Button onClickReadDeviceInformation, @NonNull Button onClickShare,
      @NonNull Button onClickShow, @NonNull Button onClickStartSession,
      @NonNull Button onClickStopSession, @NonNull SeekBar rateSlider,
      @NonNull Spinner selectFrequency, @NonNull TextView showDeviceInformation,
      @NonNull TextView textView) {
    this.rootView = rootView;
    this.button2 = button2;
    this.ecgShow = ecgShow;
    this.ecgShowView = ecgShowView;
    this.greetings = greetings;
    this.onClickConnect = onClickConnect;
    this.onClickPowerOff = onClickPowerOff;
    this.onClickReadDeviceInformation = onClickReadDeviceInformation;
    this.onClickShare = onClickShare;
    this.onClickShow = onClickShow;
    this.onClickStartSession = onClickStartSession;
    this.onClickStopSession = onClickStopSession;
    this.rateSlider = rateSlider;
    this.selectFrequency = selectFrequency;
    this.showDeviceInformation = showDeviceInformation;
    this.textView = textView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button2;
      Button button2 = ViewBindings.findChildViewById(rootView, id);
      if (button2 == null) {
        break missingId;
      }

      ConstraintLayout ecgShow = (ConstraintLayout) rootView;

      id = R.id.ecgShowView;
      EcgShowView ecgShowView = ViewBindings.findChildViewById(rootView, id);
      if (ecgShowView == null) {
        break missingId;
      }

      id = R.id.greetings;
      TextView greetings = ViewBindings.findChildViewById(rootView, id);
      if (greetings == null) {
        break missingId;
      }

      id = R.id.onClickConnect;
      Button onClickConnect = ViewBindings.findChildViewById(rootView, id);
      if (onClickConnect == null) {
        break missingId;
      }

      id = R.id.onClickPowerOff;
      Button onClickPowerOff = ViewBindings.findChildViewById(rootView, id);
      if (onClickPowerOff == null) {
        break missingId;
      }

      id = R.id.onClickReadDeviceInformation;
      Button onClickReadDeviceInformation = ViewBindings.findChildViewById(rootView, id);
      if (onClickReadDeviceInformation == null) {
        break missingId;
      }

      id = R.id.onClickShare;
      Button onClickShare = ViewBindings.findChildViewById(rootView, id);
      if (onClickShare == null) {
        break missingId;
      }

      id = R.id.onClickShow;
      Button onClickShow = ViewBindings.findChildViewById(rootView, id);
      if (onClickShow == null) {
        break missingId;
      }

      id = R.id.onClickStartSession;
      Button onClickStartSession = ViewBindings.findChildViewById(rootView, id);
      if (onClickStartSession == null) {
        break missingId;
      }

      id = R.id.onClickStopSession;
      Button onClickStopSession = ViewBindings.findChildViewById(rootView, id);
      if (onClickStopSession == null) {
        break missingId;
      }

      id = R.id.rateSlider;
      SeekBar rateSlider = ViewBindings.findChildViewById(rootView, id);
      if (rateSlider == null) {
        break missingId;
      }

      id = R.id.selectFrequency;
      Spinner selectFrequency = ViewBindings.findChildViewById(rootView, id);
      if (selectFrequency == null) {
        break missingId;
      }

      id = R.id.showDeviceInformation;
      TextView showDeviceInformation = ViewBindings.findChildViewById(rootView, id);
      if (showDeviceInformation == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, button2, ecgShow, ecgShowView,
          greetings, onClickConnect, onClickPowerOff, onClickReadDeviceInformation, onClickShare,
          onClickShow, onClickStartSession, onClickStopSession, rateSlider, selectFrequency,
          showDeviceInformation, textView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
