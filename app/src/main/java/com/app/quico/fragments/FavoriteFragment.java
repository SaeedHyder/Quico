package com.app.quico.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.app.quico.R;
import com.app.quico.entities.CompanyEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.FavoritesBinder;
import com.app.quico.ui.binders.ServiceListingBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.CompanyDetailKey;
import static com.app.quico.global.WebServiceConstants.GetCompanies;
import static com.app.quico.global.WebServiceConstants.GetFavorites;

public class FavoriteFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_favorites)
    CustomRecyclerView rvFavorites;
    Unbinder unbinder;

    public static FavoriteFragment newInstance() {
        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.enqueueCall(headerWebService.getFavorites(), GetFavorites);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(getDockActivity(), R.anim.layout_animation_from_right);
            rvFavorites.setLayoutAnimation(anim);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag){
            case GetFavorites:
                ArrayList<CompanyEnt> entity=(ArrayList<CompanyEnt>) result;
                setData(entity);
                break;
        }
    }


    private void setData(ArrayList<CompanyEnt> entity) {

        if(entity!=null && entity.size()>0) {
            txtNoData.setVisibility(View.GONE);
            rvFavorites.setVisibility(View.VISIBLE);

            rvFavorites.BindRecyclerView(new FavoritesBinder(getDockActivity(), prefHelper, this), entity,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                    , new DefaultItemAnimator());
        }else{
            txtNoData.setVisibility(View.VISIBLE);
            rvFavorites.setVisibility(View.GONE);
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getResString(R.string.MyFavorites));
        titleBar.showMenuButton();
    }



    @Override
    public void onClick(Object entity, int position) {
        CompanyEnt data=(CompanyEnt)entity;
        getDockActivity().replaceDockableFragment(ServiceDetailFragment.newInstance(data.getId()+""), "ServiceDetailFragment");
    }
}
