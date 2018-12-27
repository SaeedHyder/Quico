package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.PinEntryEditText;
import com.app.quico.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PhoneVerificationFragment extends BaseFragment {
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.btnResendCode)
    AnyTextView btnResendCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    private static boolean isFromForgot = false;

    public static PhoneVerificationFragment newInstance() {
        Bundle args = new Bundle();
        isFromForgot = false;
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PhoneVerificationFragment newInstance(boolean isForgot) {
        Bundle args = new Bundle();
        isFromForgot = isForgot;
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_phone_verification, container, false);
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
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.phone_verification));
    }


    @OnClick({R.id.btnResendCode, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnResendCode:
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btn_submit:
                if (isvalidated()) {
                    if (isFromForgot) {
                        getDockActivity().replaceDockableFragment(ResetPasswordFragment.newInstance(), "ResetPasswordFragment");
                    } else {
                        getDockActivity().popBackStackTillEntry(0);
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                    }
                }
                break;
        }
    }

    private boolean isvalidated() {
        if (txtPinEntry.getText().toString().isEmpty() || txtPinEntry.getText().toString().equals("")) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.enter_valid_pincode));
            return false;
        } else if (txtPinEntry.getText().toString().length() < 4) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.enter_valid_pincode));
            return false;
        } else
            return true;

    }
}
