package com.app.quico.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.BottomSheetDialogHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ServiceListingBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ServiceListingFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.rv_serviceDetail)
    CustomRecyclerView rvServiceDetail;
    Unbinder unbinder;
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.btnSort)
    AnyTextView btnSort;
    @BindView(R.id.Main_frame)
    CoordinatorLayout MainFrame;

    private ArrayList<String> collection;

    public static ServiceListingFragment newInstance() {
        Bundle args = new Bundle();

        ServiceListingFragment fragment = new ServiceListingFragment();
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
        View view = inflater.inflate(R.layout.fragment_servie_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
        searchListner();

    }

    private void searchListner() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    UIHelper.hideSoftKeyboard(getDockActivity(), edtSearch);
                    return true;
                }
                return false;
            }
        });
    }

    private void setData() {

        collection = new ArrayList<>();
        collection.add("drawable://" + R.drawable.circle1);
        collection.add("drawable://" + R.drawable.circle2);
        collection.add("drawable://" + R.drawable.circle3);
        collection.add("drawable://" + R.drawable.circle4);
        collection.add("drawable://" + R.drawable.circle1);
        collection.add("drawable://" + R.drawable.circle2);
        collection.add("drawable://" + R.drawable.circle3);
        collection.add("drawable://" + R.drawable.circle4);


        rvServiceDetail.BindRecyclerView(new ServiceListingBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.Electician_Services));
    }


    @Override
    public void onClick(Object entity, int position) {
      getDockActivity().replaceDockableFragment(ServiceDetailFragment.newInstance(),"ServiceDetailFragment");
    }


    @OnClick(R.id.btnSort)
    public void onViewClicked() {
        BottomSheetDialogHelper dialoge = new BottomSheetDialogHelper(getDockActivity(), MainFrame, R.layout.bottomsheet_sort);
        dialoge.initSortingDialoge();
        dialoge.showDialog();
    }
}
