package com.app.quico.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.views.TitleBar;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditProfileFragment extends BaseFragment {
    private static final String TAG = "EditProfileFragment";
    @BindView(R.id.edt_username)
    TextInputEditText edtUsername;
    @BindView(R.id.edt_email)
    TextInputEditText edtEmail;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    Unbinder unbinder;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;

    private PhoneNumberUtil phoneUtil;

    public static EditProfileFragment newInstance() {
        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneUtil = PhoneNumberUtil.getInstance();
        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());
       // Countrypicker.registerCarrierNumberEditText(edtPhone);

    }



    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.edit_profile));
    }


    @OnClick(R.id.btn_edit)
    public void onViewClicked() {
        if (isvalidated()) {
            getDockActivity().popFragment();
        }
    }

    private boolean isvalidated() {
        if (edtUsername.getText().toString().isEmpty() || edtUsername.getText().toString().length() < 3) {
            edtUsername.setError(getString(R.string.enter_username));
            if (edtUsername.requestFocus()) {
                setEditTextFocus(edtUsername);
            }
            return false;
        } else if (edtEmail.getText() == null || edtEmail.getText().toString().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            edtEmail.setError(getString(R.string.enter_valid_email));
            if (edtEmail.requestFocus()) {
                setEditTextFocus(edtEmail);
            }
            return false;
        } else if (edtPhone.getText().toString().isEmpty() || edtPhone.getText().toString().length() < 3) {
            edtPhone.setError(getString(R.string.enter_phonenumber));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (!isPhoneNumberValid()) {
            edtPhone.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            //updateHint();
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else
            return true;

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

    private void updateHint() {
        if (edtPhone != null) {
            String formattedNumber = "";
            Phonenumber.PhoneNumber exampleNumber = phoneUtil.getExampleNumberForType(Countrypicker.getSelectedCountryNameCode(), PhoneNumberUtil.PhoneNumberType.MOBILE);
            if (exampleNumber != null) {
                formattedNumber = exampleNumber.getNationalNumber() + "";
                Log.d(TAG, "updateHint: " + formattedNumber);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    formattedNumber = PhoneNumberUtils.formatNumber(Countrypicker.getSelectedCountryCodeWithPlus() + formattedNumber, Countrypicker.getSelectedCountryNameCode());
                } else {
                    formattedNumber = PhoneNumberUtils.formatNumber(Countrypicker.getSelectedCountryCodeWithPlus() + formattedNumber + formattedNumber);
                }
                formattedNumber = formattedNumber.substring(Countrypicker.getSelectedCountryCodeWithPlus().length()).trim();

            } else {
                Log.w(TAG, "updateHint: No example number found for this country (" + Countrypicker.getSelectedCountryNameCode() );
            }
            edtPhone.setError(formattedNumber);
        }
    }

}
