package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LanguageFragment extends BaseFragment {
    @BindView(R.id.btn_english)
    Button btnEnglish;
    @BindView(R.id.btn_arabic)
    Button btnArabic;
    Unbinder unbinder;

    public static LanguageFragment newInstance() {
        Bundle args = new Bundle();

        LanguageFragment fragment = new LanguageFragment();
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
        View view = inflater.inflate(R.layout.fragment_language, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @OnClick({R.id.btn_english, R.id.btn_arabic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_english:
                prefHelper.setLanguageSelected(true);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(),"LoginFragment");
                break;
            case R.id.btn_arabic:
                UIHelper.showShortToastInDialoge(getDockActivity(),getResString(R.string.will_be_implemented));
                break;
        }
    }
}
