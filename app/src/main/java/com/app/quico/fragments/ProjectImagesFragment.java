package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.entities.ProjectDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.adapters.CustomPageAdapter;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class ProjectImagesFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    Unbinder unbinder;

    private CustomPageAdapter customPageAdapter;
    private ProjectDetail projectDetail;
    private static String projectKey = "projectKey";

    public static ProjectImagesFragment newInstance(ProjectDetail data) {
        Bundle args = new Bundle();
        args.putString(projectKey, new Gson().toJson(data));
        ProjectImagesFragment fragment = new ProjectImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(projectKey);
            if (json != null) {
                projectDetail = new Gson().fromJson(json, ProjectDetail.class);
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

       if(projectDetail!=null  && projectDetail.getMedia()!=null && projectDetail.getMedia().size()>0) {

           customPageAdapter = new CustomPageAdapter(getMainActivity(), projectDetail.getMedia());
           viewPager.setAdapter(customPageAdapter);
           indicator.setViewPager(viewPager);
       }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.photos));
    }
}
