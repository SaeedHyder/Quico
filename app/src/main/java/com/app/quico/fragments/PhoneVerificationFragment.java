package com.app.quico.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.PinEntryEditText;
import com.app.quico.ui.views.TitleBar;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.ResendCode;
import static com.app.quico.global.WebServiceConstants.VerifyCode;
import static com.app.quico.global.WebServiceConstants.VerifyForgotPassword;

public class PhoneVerificationFragment extends BaseFragment {
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.btnResendCode)
    AnyTextView btnResendCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_counter)
    AnyTextView tvCounter;
    @BindView(R.id.countDown)
    LinearLayout countDown;
    private CountDownTimer timer;
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


        counter();

        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() == 4) {
                    if (isFromForgot) {
                        serviceHelper.enqueueCall(webService.verifyForgotCode(prefHelper.getUser().getUser().getCountryCode(), prefHelper.getUser().getUser().getPhone(), txtPinEntry.getText().toString()), VerifyForgotPassword);
                    } else {
                        serviceHelper.enqueueCall(webService.verifyCode(prefHelper.getUser().getUser().getCountryCode(), prefHelper.getUser().getUser().getPhone(), txtPinEntry.getText().toString()), VerifyCode);
                    }
                }
            }
        });
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
                serviceHelper.enqueueCall(headerWebService.resendCode(), ResendCode);
                break;
            case R.id.btn_submit:
                if (isvalidated()) {
                    UIHelper.hideSoftKeyboard(getDockActivity(),txtPinEntry);
                    if (isFromForgot) {
                        serviceHelper.enqueueCall(webService.verifyForgotCode(prefHelper.getUser().getUser().getCountryCode(), prefHelper.getUser().getUser().getPhone(), txtPinEntry.getText().toString()), VerifyForgotPassword);
                    } else {
                        serviceHelper.enqueueCall(webService.verifyCode(prefHelper.getUser().getUser().getCountryCode(), prefHelper.getUser().getUser().getPhone(), txtPinEntry.getText().toString()), VerifyCode);
                    }
                }
                break;
        }
    }

    private boolean isvalidated() {
        if (txtPinEntry.getText().toString().trim().isEmpty() || txtPinEntry.getText().toString().equals("")) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.enter_valid_pincode));
            return false;
        } else if (txtPinEntry.getText().toString().trim().length() < 4) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.enter_valid_pincode));
            return false;
        } else
            return true;

    }

    public void counter() {
        timer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {

                String text = String.format(Locale.getDefault(), "%2d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                if (tvCounter != null) {
                    tvCounter.setText(text + "");
                    tvCounter.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }

            public void onFinish() {
                if (tvCounter != null) {
                    countDown.setVisibility(View.GONE);
                    btnResendCode.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case VerifyForgotPassword:
                if (timer != null) {
                    timer.cancel();
                }
                UIHelper.hideSoftKeyboard(getDockActivity(),txtPinEntry);
                getDockActivity().popFragment();
                getDockActivity().replaceDockableFragment(ResetPasswordFragment.newInstance(txtPinEntry.getText().toString()), "ResetPasswordFragment");
                break;

            case VerifyCode:
                if (timer != null) {
                    timer.cancel();
                }
                UIHelper.hideSoftKeyboard(getDockActivity(),txtPinEntry);
                prefHelper.setLoginStatus(true);
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                break;

            case ResendCode:
                UIHelper.showShortToastInDialoge(getDockActivity(), message);
                counter();
                btnResendCode.setVisibility(View.GONE);
                countDown.setVisibility(View.VISIBLE);
                break;
        }
    }
}
