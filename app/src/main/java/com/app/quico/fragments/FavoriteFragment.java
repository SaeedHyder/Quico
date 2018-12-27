package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.FavoritesBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.rv_favorites)
    CustomRecyclerView rvFavorites;
    Unbinder unbinder;

    private ArrayList<String> collection;

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
        setData();
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


        rvFavorites.BindRecyclerView(new FavoritesBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
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
        getDockActivity().replaceDockableFragment(ServiceDetailFragment.newInstance(),"ServiceDetailFragment");
    }
}
