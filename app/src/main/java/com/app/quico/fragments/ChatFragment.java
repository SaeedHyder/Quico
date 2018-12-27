package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ChatBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.rv_chat)
    CustomRecyclerView rvChat;
    Unbinder unbinder;
    @BindView(R.id.btn_attachMedia)
    AnyTextView btnAttachMedia;
    @BindView(R.id.btn_attachDocument)
    AnyTextView btnAttachDocument;
    @BindView(R.id.btn_attachLocation)
    AnyTextView btnAttachLocation;
    @BindView(R.id.ll_attachment)
    LinearLayout llAttachment;
    @BindView(R.id.edt_message)
    AnyEditTextView edtMessage;
    @BindView(R.id.btnAttachment)
    ImageView btnAttachment;
    @BindView(R.id.btnSend)
    ImageView btnSend;

    private ArrayList<String> collection;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void setData() {

        collection = new ArrayList<>();
        collection.add("left");
        collection.add("");
        collection.add("left");
        collection.add("");
        collection.add("left");
        collection.add("");
        collection.add("left");
        collection.add("");


        rvChat.BindRecyclerView(new ChatBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading("ABC Company");
    }

    @Override
    public void onClick(Object entity, int position) {

    }


    @OnClick({R.id.btn_attachMedia, R.id.btn_attachDocument, R.id.btn_attachLocation, R.id.btnAttachment, R.id.btnSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_attachMedia:
                llAttachment.setVisibility(View.GONE);
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btn_attachDocument:
                llAttachment.setVisibility(View.GONE);
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btn_attachLocation:
                llAttachment.setVisibility(View.GONE);
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btnAttachment:
            /*    Animation anim = AnimationUtils.loadAnimation(getDockActivity(), R.anim.scale_up);
                llAttachment.setAnimation(anim);*/
                llAttachment.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSend:
                edtMessage.getText().clear();
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
        }
    }
}
