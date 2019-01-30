package com.app.quico.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.app.quico.R;
import com.app.quico.entities.UserEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.TitleBar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.Signup;

public class SignupFragment extends BaseFragment {
    @BindView(R.id.edt_username)
    TextInputEditText edtUsername;
    @BindView(R.id.edt_email)
    TextInputEditText edtEmail;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.edt_confirpassword)
    TextInputEditText edtConfirpassword;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.cb_termsCondition)
    CheckBox cbTermsCondition;
    @BindView(R.id.termsCondition)
    AnyTextView termsCondition;
    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;
    Unbinder unbinder;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;

    private PhoneNumberUtil phoneUtil;

    public static SignupFragment newInstance() {
        Bundle args = new Bundle();

        SignupFragment fragment = new SignupFragment();
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        termsCondition.setPaintFlags(termsCondition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phoneUtil = PhoneNumberUtil.getInstance();
        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());

      //  Countrypicker.registerCarrierNumberEditText(edtPhone);

    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.create_account));
    }

    private boolean isvalidated() {
        if (edtUsername.getText().toString().trim().isEmpty() || edtUsername.getText().toString().trim().length() < 3) {
            edtUsername.setError(getString(R.string.enter_fullname));
            if (edtUsername.requestFocus()) {
                setEditTextFocus(edtUsername);
            }
            return false;
        } else if (edtEmail.getText() == null || edtEmail.getText().toString().trim().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            edtEmail.setError(getString(R.string.enter_valid_email));
            if (edtEmail.requestFocus()) {
                setEditTextFocus(edtEmail);
            }
            return false;
        } else if (edtPassword.getText().toString().trim().isEmpty()) {
            edtPassword.setError(getString(R.string.enter_password));
            if (edtPassword.requestFocus()) {
                setEditTextFocus(edtPassword);
            }
            return false;
        } else if (edtPassword.getText().toString().trim().length() < 6) {
            edtPassword.setError(getString(R.string.passwordLength));
            if (edtPassword.requestFocus()) {
                setEditTextFocus(edtPassword);
            }
            return false;
        } else if (edtConfirpassword.getText().toString().trim().isEmpty()) {
            edtConfirpassword.setError(getString(R.string.enter_password));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else if (!edtConfirpassword.getText().toString().trim().equals(edtPassword.getText().toString().trim())) {
            edtConfirpassword.setError(getString(R.string.confirm_password_error));
            if (edtConfirpassword.requestFocus()) {
                setEditTextFocus(edtConfirpassword);
            }
            return false;
        } else  if (edtPhone.getText().toString().trim().isEmpty() || edtPhone.getText().toString().trim().length() < 3) {
            edtPhone.setError(getString(R.string.enter_phonenumber));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        }else if (!isPhoneNumberValid()) {
            edtPhone.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (!cbTermsCondition.isChecked()) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getString(R.string.select_terms_and_conditions));
            return false;
        } else
            return true;

    }

    private void removeErrorsFromEditText(){
        edtUsername.setError(null);
        edtEmail.setError(null);
        edtPassword.setError(null);
        edtConfirpassword.setError(null);
        edtPhone.setError(null);
        edtEmail.setError(null);
        edtEmail.setError(null);
    }

    private boolean isPhoneNumberValid() {

        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(edtPhone.getText().toString(), Countrypicker.getSelectedCountryNameCode());
            if (phoneUtil.isValidNumber(number)) {
                return true;
            } else {

                return false;
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            edtPhone.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            return false;

        }
    }


    @OnClick({R.id.termsCondition, R.id.btn_create_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.termsCondition:
                removeErrorsFromEditText();
                getDockActivity().addDockableFragment(CmsFragment.newInstance(getResString(R.string.TermsCondition)), "CmsFragment");
                break;
            case R.id.btn_create_account:
                if (isvalidated()) {
                    serviceHelper.enqueueCall(webService.registerUser(edtUsername.getText().toString(),Countrypicker.getSelectedCountryCodeWithPlus().toString(),
                            edtPhone.getText().toString(),edtEmail.getText().toString(),edtPassword.getText().toString(),edtConfirpassword.getText().toString(),
                            AppConstants.User, AppConstants.PushNotification,AppConstants.Device_Type, FirebaseInstanceId.getInstance().getToken()), Signup);
                }
                break;
        }
    }


    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag){
            case Signup:
                UserEnt userEnt=(UserEnt)result;
                prefHelper.putUser(userEnt);
                prefHelper.set_TOKEN(userEnt.getUser().getAccessToken());
                getDockActivity().replaceDockableFragment(PhoneVerificationFragment.newInstance(), "PhoneVerificationFragment");
                break;
        }
    }
}
