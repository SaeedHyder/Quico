package com.app.quico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.entities.NotificationEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.DateHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.LoadMoreListener;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.NotificationBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.Notificaions;
import static com.app.quico.global.WebServiceConstants.NotificaionsPaging;

public class NotificationsFragment extends BaseFragment implements RecyclerClickListner {


    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_notification)
    CustomRecyclerView lvNotification;
    Unbinder unbinder;

    private LinearLayoutManager linearLayoutManager;
    boolean canCallForMore = true;
    boolean isOnCall;
    int currentPageNumber = 1;
    int totalCount = 10;
    boolean firstTime = true;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.notification));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* currentPageNumber = 1;
        firstTime = true;
        canCallForMore = true;*/

        prefHelper.setNotificationCount(0);
        serviceHelper.enqueueCall(headerWebService.notifications(), Notificaions);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case Notificaions:
                ArrayList<NotificationEnt> data = (ArrayList<NotificationEnt>) result;

                if (data != null && data.size() > 0) {
                    lvNotification.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);

                    linearLayoutManager = new LinearLayoutManager(getDockActivity());

                    lvNotification.BindRecyclerView(new NotificationBinder(getDockActivity(), prefHelper, this), data,
                            new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                            , new DefaultItemAnimator());

                } else {
                    lvNotification.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
                   /* if (firstTime) {
                        linearLayoutManager = new LinearLayoutManager(getDockActivity());
                        lvNotification.clearList();
                        lvNotification.BindRecyclerView(new NotificationBinder(getDockActivity(), prefHelper, this), data,
                                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                                , new DefaultItemAnimator());
                        firstTime = false;
                    } else {
                        lvNotification.addAll(data);
                        lvNotification.notifyItemRangeChanged(linearLayoutManager.findFirstVisibleItemPosition(), data.size());
                    }


                    if (lvNotification.getAdapter() != null) {
                        lvNotification.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
                            @Override
                            public void onLoadMoreItem(int position) {
                                if (canCallForMore) {
                                    if (!isOnCall) {
                                        currentPageNumber = currentPageNumber + 1;
                                        //  progressBar.setVisibility(View.VISIBLE);
                                        isOnCall = true;
                                        serviceHelper.enqueueCall(headerWebService.notifications(currentPageNumber, totalCount), NotificaionsPaging);
                                    }
                                }
                            }
                        });
                    }*/


                break;

           /* case NotificaionsPaging:

                ArrayList<NotificationEnt> dataPaging = (ArrayList<NotificationEnt>) result;

                isOnCall = false;
                if (dataPaging.size() > 0) {
                    lvNotification.addAll(dataPaging);
                } else {
                    canCallForMore = false;
                }
                break;*/
        }
    }

    @Override
    public void onClick(Object entity, int position) {
        NotificationEnt data = (NotificationEnt) entity;
        if (data.getActionType() != null && data.getActionType().equals(AppConstants.companyPush)) {
            getDockActivity().replaceDockableFragment(ServiceDetailFragment.newInstance(data.getRefId() + ""), "ServiceDetailFragment");
        }
    }
}
