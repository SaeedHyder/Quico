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
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ProjectBinder;
import com.app.quico.ui.binders.ReviewBinder;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.SpacesItemDecoration;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceProjectsFragment extends BaseFragment implements RecyclerClickListner{
    @BindView(R.id.rv_projects)
    CustomRecyclerView rvProjects;
    Unbinder unbinder;

    private ArrayList<String> collection;
  //  private StaggeredGridLayoutManager GridLayoutManager;

    public static ServiceProjectsFragment newInstance() {
        Bundle args = new Bundle();

        ServiceProjectsFragment fragment = new ServiceProjectsFragment();
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

        collection = new ArrayList<>();
        collection.add("drawable://" + R.drawable.image2);
        collection.add("drawable://" + R.drawable.image3);
        collection.add("drawable://" + R.drawable.image4);
        collection.add("drawable://" + R.drawable.image5);
        collection.add("drawable://" + R.drawable.image2);
        collection.add("drawable://" + R.drawable.image3);
        collection.add("drawable://" + R.drawable.image4);
        collection.add("drawable://" + R.drawable.image5);
        collection.add("drawable://" + R.drawable.image2);
        collection.add("drawable://" + R.drawable.image3);
        collection.add("drawable://" + R.drawable.image4);
        collection.add("drawable://" + R.drawable.image5);

        StaggeredGridLayoutManager GridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        rvProjects.BindRecyclerView(new ProjectBinder(getDockActivity(), prefHelper, this), collection,
                GridLayoutManager
                , new DefaultItemAnimator());
        rvProjects.setNestedScrollingEnabled(false);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @Override
    public void onClick(Object entity, int position) {
        getDockActivity().addDockableFragment(ProjectImagesFragment.newInstance(),"ProjectImagesFragment");
       // UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
    }
}
