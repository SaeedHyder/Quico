package com.app.quico.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.app.quico.R;
import com.app.quico.entities.Chat.ChatThreadEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.DialogHelper;
import com.app.quico.interfaces.DeleteChatInterface;
import com.app.quico.interfaces.LoadMoreListener;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.MyChatThreadBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.quico.global.AppConstants.chatPush;
import static com.app.quico.global.WebServiceConstants.ChatThreads;
import static com.app.quico.global.WebServiceConstants.ChatThreadsPaging;
import static com.app.quico.global.WebServiceConstants.DeleteThread;

public class MyChatThreadFragment extends BaseFragment implements RecyclerClickListner, DeleteChatInterface {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_mychat)
    CustomRecyclerView rvMychat;
    Unbinder unbinder;

    private LinearLayoutManager linearLayoutManager;
    boolean canCallForMore = true;
    int currentPageNumber = 0;
    int totalCount = 15;
    int offset;
    boolean firstTime = true;
    private int deletePosition = 0;
    protected BroadcastReceiver broadcastReceiver;

    public static MyChatThreadFragment newInstance() {
        Bundle args = new Bundle();

        MyChatThreadFragment fragment = new MyChatThreadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mychat_thread, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onNotificationReceived();
        offset = 0;
        currentPageNumber = 0;
        firstTime = true;
        canCallForMore = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(getDockActivity(), R.anim.layout_animation_from_right);
            rvMychat.setLayoutAnimation(anim);
        }

        serviceHelper.enqueueCall(headerWebService.getChatThreads(offset, totalCount), ChatThreads);

    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case ChatThreads:
                ArrayList<ChatThreadEnt> data = (ArrayList<ChatThreadEnt>) result;

                if (data != null && data.size() > 0) {

                    rvMychat.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);

                    if (firstTime) {
                        linearLayoutManager = new LinearLayoutManager(getDockActivity());

                        rvMychat.clearList();
                        rvMychat.BindRecyclerView(new MyChatThreadBinder(getDockActivity(), prefHelper, this, this), data,
                                linearLayoutManager
                                , new DefaultItemAnimator());
                        firstTime = false;
                    } else {
                        rvMychat.clearList();
                        rvMychat.addAll(data);
                        rvMychat.notifyItemRangeChanged(linearLayoutManager.findFirstVisibleItemPosition(), data.size());
                    }
                } else {
                    rvMychat.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }


                if (rvMychat.getAdapter() != null) {
                    rvMychat.getAdapter().setOnLoadMoreListener(new LoadMoreListener() {
                        @Override
                        public void onLoadMoreItem(int position) {
                            if (canCallForMore) {
                                currentPageNumber = (currentPageNumber + 1);
                                offset = currentPageNumber * totalCount;
                                serviceHelper.enqueueCall(headerWebService.getChatThreads(offset, totalCount), ChatThreadsPaging);
                            }
                        }
                    });
                }


                break;

            case ChatThreadsPaging:

                ArrayList<ChatThreadEnt> dataPaging = (ArrayList<ChatThreadEnt>) result;

                if (dataPaging.size() > 0) {
                    rvMychat.addAll(dataPaging);
                } else {
                    canCallForMore = false;
                }

                break;

            case DeleteThread:
                rvMychat.getAdapter().remove(deletePosition);
                if (rvMychat.getList() != null && rvMychat.getList().size() > 0) {
                    rvMychat.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);
                } else {
                    rvMychat.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getResString(R.string.myChats));
        titleBar.showMenuButton();
    }

    @Override
    public void onClick(Object entity, int position) {

        ChatThreadEnt data = (ChatThreadEnt) entity;
        getDockActivity().replaceDockableFragment(ChatFragment.newInstance(data.getId() + ""), "ChatFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));


    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    Bundle bundle = intent.getExtras();

                    if (bundle != null) {
                        String Type = bundle.getString("actionType");
                        String Title = bundle.getString("title");
                        String id = bundle.getString("redId");

                        if (Type != null && Type.equals(chatPush)) {
                            serviceHelper.enqueueCall(headerWebService.getChatThreads(0, totalCount), ChatThreads, false);
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onClick(ChatThreadEnt entity, int position) {

        DialogHelper dialoge = new DialogHelper(getDockActivity());
        dialoge.commonDialoge(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePosition = position;
                serviceHelper.enqueueCall(headerWebService.deleteThread(entity.getId() + ""), DeleteThread);
                dialoge.hideDialog();
            }
        }, getResString(R.string.delete_chat), getResString(R.string.are_you_sure_delete_chat));

        dialoge.showDialog();

    }
}
