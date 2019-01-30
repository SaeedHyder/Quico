package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.quico.R;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ReviewBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ServiceReviewsFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.rv_reviews)
    CustomRecyclerView rvReviews;
    @BindView(R.id.btn_writeReview)
    Button btnWriteReview;
    Unbinder unbinder;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private CompanyDetail companyDetail;
    private static String companyDetailKey = "companyDetailKey";

    public static ServiceReviewsFragment newInstance(CompanyDetail data) {
        Bundle args = new Bundle();
        args.putString(companyDetailKey, new Gson().toJson(data));
        ServiceReviewsFragment fragment = new ServiceReviewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_service_reviews, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();


    }

    private void setData() {

        if(companyDetail.isBackBtn()){
            btnWriteReview.setVisibility(View.GONE);
        }

        if (companyDetail != null && companyDetail.getReviewsDetails()!=null && companyDetail.getReviewsDetails().size() > 0) {
            txtNoData.setVisibility(View.GONE);
            rvReviews.setVisibility(View.VISIBLE);

            rvReviews.BindRecyclerView(new ReviewBinder(getDockActivity(), prefHelper, this), companyDetail.getReviewsDetails(),
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                    , new DefaultItemAnimator());
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            rvReviews.setVisibility(View.GONE);
        }


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @OnClick(R.id.btn_writeReview)
    public void onViewClicked() {
        getDockActivity().addDockableFragment(FeedbackFragment.newInstance(companyDetail), "FeedbackFragment");
    }

    @Override
    public void onClick(Object entity, int position) {
        UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
