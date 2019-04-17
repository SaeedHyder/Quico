package com.app.quico.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.quico.R;
import com.app.quico.activities.MainActivity;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.NotificationHelper;
import com.app.quico.retrofit.WebService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private BasePreferenceHelper preferenceHelper;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            String actionType = remoteMessage.getData().get("action_type");
            String redId = remoteMessage.getData().get("ref_id");
            Log.e(TAG, "message: " + message);

            if (actionType!=null &&( actionType.equals("USER_DELETED") || actionType.equals("USER_STATUS"))) {
                preferenceHelper.setLoginStatus(false);

                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("actionType", actionType);
                pushNotification.putExtra("redId", redId);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);

            } else if(actionType!=null && isChatNotification(remoteMessage)){
                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("actionType", actionType);
                pushNotification.putExtra("redId", redId);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
            }

            else {

                Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("actionType", actionType);
                resultIntent.putExtra("redId", redId);
                resultIntent.putExtra("tapped", true);

                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("actionType", actionType);
                pushNotification.putExtra("redId", redId);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
                showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);

            }

        } else if (remoteMessage.getNotification() != null) {

            getNotification(remoteMessage);
        }
    }

    private void getNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle().toString();
        String message = remoteMessage.getNotification().getBody().toString();
        Log.e(TAG, "message: " + message);
        Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("message", message);
        resultIntent.putExtra("tapped", true);

        Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
        showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);
    }


    private boolean isChatNotification(RemoteMessage remoteMessage) {

        String actionType = remoteMessage.getData().get("action_type");
        String redId = remoteMessage.getData().get("ref_id");

        if (preferenceHelper.isChatScreen() && preferenceHelper.getChatThreadid().equals(redId) && actionType.equals("chat")) {
            return true;
        } else {
            return false;
        }
    }


    /*private void getNotificaitonCount() {
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(this, WebServiceConstants.Local_SERVICE_URL);
        preferenceHelper = new BasePreferenceHelper(this);
        Call<ResponseWrapper<countEnt>> call = webservice.getNotificationCount(preferenceHelper.getMerchantId());
        call.enqueue(new Callback<ResponseWrapper<countEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<countEnt>> call, Response<ResponseWrapper<countEnt>> response) {
                preferenceHelper.setNotificationCount(response.body().getResult().getCount());
            }

            @Override
            public void onFailure(Call<ResponseWrapper<countEnt>> call, Throwable t) {

            }
        });
    }*/


    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationHelper.getInstance().showNotification(context,
                R.drawable.notification_icon,
                title,
                message,
                timeStamp,
                intent);
    }


}
