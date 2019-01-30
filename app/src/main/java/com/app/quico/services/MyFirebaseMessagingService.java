package com.app.quico.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.quico.R;
import com.app.quico.activities.MainActivity;
import com.app.quico.global.AppConstants;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.BasePreferenceHelper;
import com.app.quico.helpers.NotificationHelper;
import com.app.quico.retrofit.WebService;
import com.app.quico.retrofit.WebServiceFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private WebService webservice;
    private BasePreferenceHelper preferenceHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            if (remoteMessage.getData().get("action_type")!=null &&( remoteMessage.getData().get("action_type").equals("USER_DELETED") || remoteMessage.getData().get("action_type").equals("USER_STATUS"))) {
                preferenceHelper.setLoginStatus(false);

                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("body");
                String actionType = remoteMessage.getData().get("action_type");
                String redId = remoteMessage.getData().get("ref_id");
                Log.e(TAG, "message: " + message);

                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("actionType", actionType);
                pushNotification.putExtra("redId", redId);

                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);

            } else {

                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("body");
                String actionType = remoteMessage.getData().get("action_type");
                String redId = remoteMessage.getData().get("ref_id");
                Log.e(TAG, "message: " + message);
                Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("message", message);
        resultIntent.putExtra("tapped", true);

        Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
        showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);
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

    private void SendNotification(int count, JSONObject json) {

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationHelper.getInstance().showNotification(context,
                R.drawable.app_icon,
                title,
                message,
                timeStamp,
                intent);
    }


}
