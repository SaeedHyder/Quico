package com.app.quico.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.app.quico.R;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.entities.ProjectDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.adapters.CustomImagesAdapter;
import com.app.quico.ui.adapters.CustomPageAdapter;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class ChatImageViewerFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    Unbinder unbinder;

    private CustomImagesAdapter customPageAdapter;
    private ArrayList<DocumentDetail> images;
    private static String imagesKey = "imagesKey";

    public static ChatImageViewerFragment newInstance(ArrayList<DocumentDetail> data) {
        Bundle args = new Bundle();
        args.putString(imagesKey, new Gson().toJson(data));
        ChatImageViewerFragment fragment = new ChatImageViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(imagesKey);
            if (json != null) {
                images = new Gson().fromJson(json, new TypeToken<List<DocumentDetail>>(){}.getType());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_images, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewPager();
    }

    private void setViewPager() {

        if (images != null  && images.size() > 0) {
            customPageAdapter = new CustomImagesAdapter(getMainActivity(), images);
            viewPager.setAdapter(customPageAdapter);
            indicator.setViewPager(viewPager);
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.images));
    }
    @Override
    public void onResume() {
        super.onResume();
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


}

