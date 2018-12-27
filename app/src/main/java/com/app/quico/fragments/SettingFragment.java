package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.btn_english)
    AnyTextView btnEnglish;
    @BindView(R.id.btn_arabic)
    AnyTextView btnArabic;
    @BindView(R.id.toggleBtn)
    ToggleButton toggleBtn;
    @BindView(R.id.btn_change_password)
    LinearLayout btnChangePassword;
    Unbinder unbinder;
    @BindView(R.id.btn_profile)
    LinearLayout btnProfile;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listner();
    }

    private void listner() {
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getResString(R.string.settings));
        titleBar.showMenuButton();
    }


    @OnClick({R.id.btn_arabic, R.id.btn_english, R.id.btn_change_password,R.id.btn_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_arabic:
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btn_english:
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btn_change_password:
                getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(), "ChangePasswordFragment");
                break;
            case R.id.btn_profile:
                getDockActivity().replaceDockableFragment(ProfileFragment.newInstance(), "ProfileFragment");
                break;
        }
    }



}
