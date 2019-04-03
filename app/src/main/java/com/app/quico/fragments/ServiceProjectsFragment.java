package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.entities.ProjectDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ProjectBinder;
import com.app.quico.ui.binders.ReviewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.SpacesItemDecoration;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceProjectsFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.rv_projects)
    CustomRecyclerView rvProjects;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    Unbinder unbinder;

    private CompanyDetail companyDetail;
    private static String companyDetailKey = "companyDetailKey";

    public static ServiceProjectsFragment newInstance(CompanyDetail data) {
        Bundle args = new Bundle();
        args.putString(companyDetailKey, new Gson().toJson(data));
        ServiceProjectsFragment fragment = new ServiceProjectsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(companyDetailKey);

            if (json != null) {
                companyDetail = new Gson().fromJson(json, CompanyDetail.class);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_projects, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
    }

    private void setData() {

        if (companyDetail != null && companyDetail.getProjectDetails() != null && companyDetail.getProjectDetails().size() > 0) {
            txtNoData.setVisibility(View.GONE);
            rvProjects.setVisibility(View.VISIBLE);

            rvProjects.BindRecyclerView(new ProjectBinder(getDockActivity(), prefHelper, this), companyDetail.getProjectDetails(),
                    new GridLayoutManager(getDockActivity(),3)
                    , new DefaultItemAnimator());
            rvProjects.setNestedScrollingEnabled(false);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            rvProjects.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @Override
    public void onClick(Object entity, int position) {
        ProjectDetail data = (ProjectDetail) entity;
        if (data != null && data.getMedia() != null && data.getMedia().size() > 0) {
            getDockActivity().addDockableFragment(ProjectImagesFragment.newInstance(data), "ProjectImagesFragment");
        }
    }
}
