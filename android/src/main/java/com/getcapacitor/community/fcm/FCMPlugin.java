package com.getcapacitor.community.fcm;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Please read the Capacitor Android Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/android
 *
 * Created by Stewan Silva on 1/23/19.
 */
@CapacitorPlugin(
        name = "FCM"
)
public class FCMPlugin extends Plugin {

    @PluginMethod()
    public void subscribeTo(final PluginCall call) {
        final String topicName = call.getString("topic");

        FirebaseMessaging
                .getInstance()
                .subscribeToTopic(topicName)
                .addOnSuccessListener(aVoid -> {
                    JSObject ret = new JSObject();
                    ret.put("message", "Subscribed to topic " + topicName);
                    call.resolve(ret);
                })
                .addOnFailureListener(e -> call.reject("Cant subscribe to topic" + topicName, e));

    }

    @PluginMethod()
    public void unsubscribeFrom(final PluginCall call) {
        final String topicName = call.getString("topic");

        FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic(topicName)
                .addOnSuccessListener(aVoid -> {
                    JSObject ret = new JSObject();
                    ret.put("message", "Unsubscribed from topic " + topicName);
                    call.resolve(ret);
                })
                .addOnFailureListener(e -> call.reject("Cant unsubscribe from topic" + topicName, e));

    }

    @PluginMethod()
    public void deleteToken(final PluginCall call) {
        FirebaseMessaging
                .getInstance()
                .deleteToken()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("delete token success");
                    call.resolve();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    call.reject("Cant delete Firebase Instance ID", e);
                });
    }

    @PluginMethod()
    public void deleteInstance(final PluginCall call) {
        FirebaseInstallations.getInstance().delete()
                .addOnSuccessListener(aVoid -> call.resolve())
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    call.reject("Cant delete Firebase Instance ID", e);
                });
    }

    @PluginMethod()
    public void getToken(final PluginCall call) {
        FirebaseInstallations.getInstance().getToken(false).addOnSuccessListener(getActivity(), instanceIdResult -> {
            JSObject data = new JSObject();
            data.put("token", instanceIdResult.getToken());
            call.resolve(data);
        });
        FirebaseInstallations.getInstance().getId().addOnFailureListener(e -> call.reject("Failed to get instance FirebaseID", e));
    }

    @PluginMethod()
    public void setAutoInit(final PluginCall call) {
        final boolean enabled = call.getBoolean("enabled", false);
        FirebaseMessaging.getInstance().setAutoInitEnabled(enabled);
        call.resolve();
    }

    @PluginMethod()
    public void isAutoInitEnabled(final PluginCall call) {
        final boolean enabled = FirebaseMessaging.getInstance().isAutoInitEnabled();
        JSObject data = new JSObject();
        data.put("enabled", enabled);
        call.resolve(data);
    }
}
