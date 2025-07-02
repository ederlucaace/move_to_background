package com.sayegh.move_to_background;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** MoveToBackgroundPlugin */
public class MoveToBackgroundPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

  private static final String CHANNEL_NAME = "move_to_background";
  private MethodChannel channel;
  private static Activity activity;

  // === FlutterPlugin ===
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    setupChannel(binding.getBinaryMessenger(), binding.getApplicationContext());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    teardownChannel();
  }

  // === Channel helpers ===
  private void setupChannel(BinaryMessenger messenger, Context context) {
    channel = new MethodChannel(messenger, CHANNEL_NAME);
    channel.setMethodCallHandler(this);
  }

  private void teardownChannel() {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
  }

  // === MethodCallHandler ===
  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if ("moveTaskToBack".equals(call.method)) {
      if (activity != null) {
        activity.moveTaskToBack(true);
      } else {
        Log.e("MoveToBackgroundPlugin", "moveTaskToBack failed: activity=null");
      }
      result.success(true);
    } else {
      result.notImplemented();
    }
  }

  // === ActivityAware ===
  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;
  }
}
