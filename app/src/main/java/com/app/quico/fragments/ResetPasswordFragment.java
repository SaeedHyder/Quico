package com.app.quico.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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

import static com.app.quico.global.WebServiceConstants.ResetPassword;

public class ResetPasswordFragment extends BaseFragment {
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.edt_confirpassword)
    TextInputEditText edtConfirpassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    private static String pinCode;

    public static ResetPasswordFragment newInstance(String code) {
        Bundle args = new Bundle();
        pinCode=code;
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_pasword, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (edtPassword.requestFocus()) {
            setEditTextFocus(edtPassword);
        }
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidate()) {
            serviceHelper.enqueueCall(webService.resetPassword(prefHelper.getUser().getUser().getCountryCode(),prefHelper.getUser().getUser().getPhone(),pinCode,edtPassword.getText().toString(),edtConfirpassword.getText().toString()), ResetPassword);
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag){
            case ResetPassword:
                UIHelper.showShortToastInCenter(getDockActivity(),"Password updated successfully");
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(),"LoginFragment");
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.forgot_password));
    }

    private boolean isvalidate() {
        if (edtPassword.getText() == null || (edtPassword.getText().toString().trim().isEmpty())) {
            edtPassword.setError(getResString(R.string.enter_password));
            if (edtPassword.requestFocus()) {
                setEditTextFocus(edtPassword);
            }
            return false;
        } else if (edtPassword.getText().toString().trim().length() < 6) {
            edtPassword.setError(getResString(R.string.passwordLength));
            if (edtPassword.requestFocus()) {
                setEditTextFocus(edtPassword);
            }
            return false;
        } else if (edtConfirpassword.getText() == null || (edtConfirpassword.getText().toString().trim().isEmpty())) {
            edtConfirpassword.setError(getResString(R.string.enter_confirm_password));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else if (edtConfirpassword.getText().toString().trim().length() < 6) {
            edtConfirpassword.setError(getResString(R.string.confirmpasswordLength));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else if (!edtConfirpassword.getText().toString().trim().equals(edtPassword.getText().toString().trim())) {
            edtConfirpassword.setError(getResString(R.string.conform_password_error));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else {
            return true;
        }
    }
}
