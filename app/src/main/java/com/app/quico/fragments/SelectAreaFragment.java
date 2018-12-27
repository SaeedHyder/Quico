package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.adapters.ArrayListExpandableAdapter;
import com.app.quico.ui.binders.AreaExpendableBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectAreaFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.elv_area)
    ExpandableListView elvArea;
    Unbinder unbinder;

    private ArrayListExpandableAdapter<String, ArrayList<String>> adapter;
    private ArrayList<String> collectionGroup;
    private ArrayList<String> collectionChild;
    private HashMap<String, ArrayList<String>> listDataChild;
    private AreaInterface areaInterface;

    public static SelectAreaFragment newInstance() {
        Bundle args = new Bundle();
        SelectAreaFragment fragment = new SelectAreaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }


    public void setAreaListner(AreaInterface areaInterface) {
        this.areaInterface = areaInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_area, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAreaData();
    }

    private void setAreaData() {


        collectionGroup = new ArrayList<>();
        collectionChild = new ArrayList<>();

        listDataChild = new HashMap<>();

        collectionGroup.add("Dubai");
        collectionGroup.add("Abu Dhabi");
        collectionGroup.add("Dubai");
        collectionGroup.add("Sharjah");
        collectionGroup.add("Ajman");
        collectionGroup.add("Al Ain");


        for (String item : collectionGroup) {
            collectionChild.add("Emirates Hills");
            collectionChild.add("Dubai Hills");
            collectionChild.add("Abu Dhabi Hills");
            listDataChild.put(item, collectionChild);
            collectionChild = new ArrayList<>();
        }


        adapter = new ArrayListExpandableAdapter<>(getDockActivity(), collectionGroup, listDataChild, new AreaExpendableBinder(getDockActivity(), prefHelper, getMainActivity(), this));
        elvArea.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        /*for (AdditionalJobsEnt item : result) {
            collectionGroup.add(item.getName());

            collectionChild.add(item.getItems());
            listDataChild.put(item.getName(), collectionChild);
            collectionChild = new ArrayList<>();
        }

        if (collectionGroup.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            lvAdditionalTasks.setVisibility(View.VISIBLE);
            adapter = new ArrayListExpandableAdapter<>(getDockActivity(), collectionGroup, listDataChild, new AdditionalTaskBinder(getDockActivity(), prefHelper, getMainActivity()));
            lvAdditionalTasks.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            lvAdditionalTasks.setVisibility(View.GONE);
        }*/

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.select_area));
    }


    @Override
    public void onClick(Object entity, int position) {
        areaInterface.selectArea((String)entity);
        getDockActivity().popFragment();
    }
}
