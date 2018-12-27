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

public class ChangePasswordFragment extends BaseFragment {
    @BindView(R.id.edt_old_password)
    TextInputEditText edtOldPassword;
    @BindView(R.id.edt_new_password)
    TextInputEditText edtNewPassword;
    @BindView(R.id.edt_confirpassword)
    TextInputEditText edtConfirpassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (edtOldPassword.requestFocus()) {
            setEditTextFocus(edtOldPassword);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.change_password));
    }

    private boolean isvalidate() {
        if (edtOldPassword.getText() == null || (edtOldPassword.getText().toString().isEmpty())) {
            edtOldPassword.setError(getString(R.string.enter_password));
            if (edtOldPassword.requestFocus()) {
                setEditTextFocus(edtOldPassword);
            }
            return false;
        } else if (edtNewPassword.getText() == null || (edtNewPassword.getText().toString().isEmpty())) {
            edtNewPassword.setError(getString(R.string.enter_password));
            if (edtNewPassword.requestFocus()) {
                setEditTextFocus(edtNewPassword);
            }
            return false;
        } else if (edtNewPassword.getText().toString().equals(edtOldPassword.getText().toString())) {
            edtNewPassword.setError(getString(R.string.samePassword));
            if (edtNewPassword.requestFocus()) {
                setEditTextFocus(edtNewPassword);
            }
            return false;
        } else if (edtNewPassword.getText().toString().length() < 6) {
            edtNewPassword.setError(getString(R.string.passwordLength));
            if (edtNewPassword.requestFocus()) {
                setEditTextFocus(edtNewPassword);
            }
            return false;
        } else if (edtConfirpassword.getText() == null || (edtConfirpassword.getText().toString().isEmpty())) {
            edtConfirpassword.setError(getString(R.string.enter_confirm_password));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else if (edtConfirpassword.getText().toString().length() < 6) {
            edtConfirpassword.setError(getString(R.string.confirmpasswordLength));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else if (!edtConfirpassword.getText().toString().equals(edtNewPassword.getText().toString())) {
            edtConfirpassword.setError(getString(R.string.conform_password_error));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else {
            return true;
        }
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidate()) {
            getDockActivity().popBackStackTillEntry(0);
           getDockActivity().replaceDockableFragment(HomeFragment.newInstance(),"HomeFragment");
        }
    }
}
