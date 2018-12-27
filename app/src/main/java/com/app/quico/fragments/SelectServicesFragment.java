package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.SelectServicesBinder;
import com.app.quico.ui.binders.ServiesBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectServicesFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.rv_services)
    CustomRecyclerView rvServices;
    Unbinder unbinder;

    private ArrayList<String> collection;
    private AreaInterface areaInterface;

    public static SelectServicesFragment newInstance() {
        Bundle args = new Bundle();

        SelectServicesFragment fragment = new SelectServicesFragment();
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
        View view = inflater.inflate(R.layout.fragment_select_services, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void setAreaListner(AreaInterface areaInterface) {
        this.areaInterface = areaInterface;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
    }

    private void setData() {

        collection = new ArrayList<>();
        collection.add("Technician");
        collection.add("Plumber");
        collection.add("Electrician");
        collection.add("Technician");
        collection.add("Plumber");
        collection.add("Electrician");
        collection.add("Technician");
        collection.add("Plumber");
        collection.add("Electrician");


        rvServices.BindRecyclerView(new SelectServicesBinder(getDockActivity(), this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.select_services));
    }


    @Override
    public void onClick(Object entity, int position) {
        areaInterface.selectService((String)entity);
        getDockActivity().popFragment();
    }
}
