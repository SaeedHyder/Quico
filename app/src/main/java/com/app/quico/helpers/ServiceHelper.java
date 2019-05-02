package com.app.quico.helpers;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.ResponseWrapper;
import com.app.quico.fragments.LoginFragment;
import com.app.quico.interfaces.webServiceResponseLisener;
import com.app.quico.retrofit.WebService;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.quico.global.AppConstants.CODE_401;

/**
 * Created on 7/17/2017.
 */

public class ServiceHelper<T> {
    private webServiceResponseLisener serviceResponseLisener;
    private DockActivity context;
    private WebService webService;
    private BasePreferenceHelper prefHelper;

    public ServiceHelper(webServiceResponseLisener serviceResponseLisener, DockActivity conttext, WebService webService, BasePreferenceHelper prefHelper) {
        this.serviceResponseLisener = serviceResponseLisener;
        this.context = conttext;
        this.webService = webService;
        this.prefHelper = prefHelper;
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            context.onLoadingStarted();
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<T>> call, Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();
                    if (response != null) {
                        if (response.code() != CODE_401) {
                            if (response.body() != null && response.body().isSuccess()) {
                                serviceResponseLisener.ResponseSuccess(response.body().getData(), tag, response.body().getMessage());
                            } else if (response.body() != null && response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                                UIHelper.showShortToastInDialoge(context, response.body().getMessage());
                            }
                        } else {
                            context.popBackStackTillEntry(0);
                            prefHelper.setLoginStatus(false);
                            prefHelper.setSocialLogin(false);
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            context.replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                            UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.deleted_by_admin));
                        }
                    } else {
                        UIHelper.showShortToastInDialoge(context, context.getResources().getString(R.string.no_response));
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    context.onLoadingFinished();
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag, boolean isLoading) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<T>> call, Response<ResponseWrapper<T>> response) {
                    if (response != null && response.body() != null) {
                        if (response.body().isSuccess()) {
                            serviceResponseLisener.ResponseSuccess(response.body().getData(), tag, response.body().getMessage());
                        } else {
                            UIHelper.showShortToastInDialoge(context, response.body().getMessage());
                        }
                    } else {
                        UIHelper.showShortToastInDialoge(context, context.getResources().getString(R.string.no_response));
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }
    }

}
