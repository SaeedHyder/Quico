package com.app.quico.helpers;

import android.util.Log;


import com.app.quico.R;
import com.app.quico.activities.DockActivity;
import com.app.quico.entities.ResponseWrapper;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.interfaces.webServiceResponseLisener;
import com.app.quico.retrofit.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.message;

/**
 * Created on 7/17/2017.
 */

public class ServiceHelper<T> {
    private webServiceResponseLisener serviceResponseLisener;
    private DockActivity context;
    private WebService webService;

    public ServiceHelper(webServiceResponseLisener serviceResponseLisener, DockActivity conttext, WebService webService) {
        this.serviceResponseLisener = serviceResponseLisener;
        this.context = conttext;
        this.webService = webService;
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            context.onLoadingStarted();
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<T>> call, Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();
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
                    context.onLoadingFinished();
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag,boolean isLoading) {
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
