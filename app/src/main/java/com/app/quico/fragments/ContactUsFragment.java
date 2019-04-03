package com.app.quico.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.TitleBar;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.ContactUs;

public class ContactUsFragment extends BaseFragment {
    @BindView(R.id.edt_username)
    TextInputEditText edtUsername;
    @BindView(R.id.edt_email)
    TextInputEditText edtEmail;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.edt_contactUs)
    AnyEditTextView edtContactUs;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    @BindView(R.id.Countrypicker)
    CountryCodePicker Countrypicker;
    @BindView(R.id.edt_companyName)
    TextInputEditText edtCompanyName;

    private PhoneNumberUtil phoneUtil;

    public static ContactUsFragment newInstance() {
        Bundle args = new Bundle();

        ContactUsFragment fragment = new ContactUsFragment();
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
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (edtCompanyName.requestFocus()) {
            setEditTextFocus(edtCompanyName);
        }
        phoneUtil = PhoneNumberUtil.getInstance();
        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        // Countrypicker.registerCarrierNumberEditText(edtPhone);
        listner();
        setData();
    }

    private void setData() {
        edtUsername.setText(prefHelper.getUser().getUser().getName() + "");
        edtEmail.setText(prefHelper.getUser().getUser().getEmail() + "");

        if (prefHelper.getUser().getUser().getCountryCode() != null && !prefHelper.getUser().getUser().getCountryCode().equals("") &&
                prefHelper.getUser().getUser().getPhone() != null && !prefHelper.getUser().getUser().getPhone().equals("")) {
            Countrypicker.setCountryForPhoneCode(Integer.parseInt(prefHelper.getUser().getUser().getCountryCode()));
            edtPhone.setText(prefHelper.getUser().getUser().getPhone() + "");
        }
    }

    private void listner() {

        edtContactUs.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getResString(R.string.bequico));
        titleBar.showMenuButton();
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidated()) {
            serviceHelper.enqueueCall(headerWebService.contactUs(edtUsername.getText().toString(), edtEmail.getText().toString(), Countrypicker.getSelectedCountryCodeWithPlus().toString() + edtPhone.getText().toString(), edtContactUs.getText().toString(),edtCompanyName.getText().toString()), ContactUs);

        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case ContactUs:
                UIHelper.showShortToastInCenter(getDockActivity(), getResString(R.string.submitted_successfully));
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                getMainActivity().refreshSideMenu();
                break;
        }
    }

    private boolean isvalidated() {
        if (edtCompanyName.getText().toString().trim().isEmpty() || edtCompanyName.getText().toString().trim().length() < 3) {
            edtCompanyName.setError(getString(R.string.enter_company_name));
            if (edtCompanyName.requestFocus()) {
                setEditTextFocus(edtCompanyName);
            }
            return false;
        }
        else if (edtUsername.getText().toString().trim().isEmpty() || edtUsername.getText().toString().trim().length() < 3) {
            edtUsername.setError(getString(R.string.enter_name));
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
        } else if (edtPhone.getText().toString().trim().isEmpty() || edtPhone.getText().toString().length() < 3) {
            edtPhone.setError(getString(R.string.enter_phonenumber));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (!isPhoneNumberValid()) {
            edtPhone.setError(getDockActivity().getResources().getString(R.string.enter_valid_number_error));
            if (edtPhone.requestFocus()) {
                setEditTextFocus(edtPhone);
            }
            return false;
        } else if (edtContactUs.getText().toString().trim().isEmpty() || edtContactUs.getText().toString().length() < 3) {
            edtContactUs.setError(getString(R.string.enter_your_message));
            if (edtContactUs.requestFocus()) {
                setEditTextFocus(edtContactUs);
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


}
