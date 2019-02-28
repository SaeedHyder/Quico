package com.app.quico.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.quico.R;
import com.app.quico.entities.GoogleEnt;
import com.app.quico.entities.UserEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.FacebookLoginHelper;
import com.app.quico.helpers.GoogleHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.TitleBar;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.Login;
import static com.app.quico.global.WebServiceConstants.Signup;
import static com.app.quico.global.WebServiceConstants.SocialLogin;


public class LoginFragment extends BaseFragment implements FacebookLoginHelper.FacebookLoginListener, GoogleHelper.GoogleHelperInterfce {


    private static boolean submitPressed = true;
    private static String SUBMIT_PRESSED = "SUBMIT_PRESSED";

    @BindView(R.id.edt_email)
    TextInputEditText edtEmail;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.btn_forgot_pass)
    AnyTextView btnForgotPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.rl_card_view)
    RelativeLayout rlCardView;
    @BindView(R.id.btn_fb)
    ImageView btnFb;
    @BindView(R.id.btn_google)
    ImageView btnGoogle;
    @BindView(R.id.btn_signup)
    AnyTextView btnSignup;
    Unbinder unbinder;

    private CallbackManager callbackManager;
    private FacebookLoginHelper facebookLoginHelper;
    private FacebookLoginHelper.FacebookLoginEnt facebookLoginEnt;
    private GoogleHelper googleHelper;
    private static final int RC_SIGN_IN = 007;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        setupFacebookLogin();
        setupGoogleSignup();
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        facebookLoginHelper = new FacebookLoginHelper(getDockActivity(), this, this);
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginHelper);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            googleHelper.handleGoogleResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupGoogleSignup() {
        googleHelper = GoogleHelper.getInstance();
        googleHelper.setGoogleHelperInterface(this);
        googleHelper.configGoogleApiClient(this);
    }


    @OnClick({R.id.btn_forgot_pass, R.id.btn_login, R.id.btn_fb, R.id.btn_google, R.id.btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_forgot_pass:
                getDockActivity().replaceDockableFragment(ForgotPasswordFragment.newInstance(), "ForgotPasswordFragment");
                break;
            case R.id.btn_login:
                if (isvalidated()) {
                    serviceHelper.enqueueCall(webService.loginUser(edtEmail.getText().toString(), edtPassword.getText().toString(), AppConstants.Device_Type, FirebaseInstanceId.getInstance().getToken()), Login);

                }
                break;
            case R.id.btn_fb:
                LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, facebookLoginHelper.getPermissionNeeds());
                break;
            case R.id.btn_google:
                googleHelper.intentGoogleSign();
                break;
            case R.id.btn_signup:
                getDockActivity().replaceDockableFragment(SignupFragment.newInstance(), "SignupFragment");
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case Login:
                UserEnt userEnt = (UserEnt) result;
                prefHelper.putUser(userEnt);
                prefHelper.set_TOKEN(userEnt.getUser().getAccessToken());

                if (userEnt.getUser().getIs_verified() == 1) {
                    prefHelper.setLoginStatus(true);
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                    if (getMainActivity() != null) {
                        getMainActivity().refreshSideMenu();
                    }
                } else {
                    getDockActivity().replaceDockableFragment(PhoneVerificationFragment.newInstance(), "PhoneVerificationFragment");
                }
                break;

            case SocialLogin:
                UserEnt socialUserEnt = (UserEnt) result;
                prefHelper.putUser(socialUserEnt);
                prefHelper.set_TOKEN(socialUserEnt.getUser().getAccessToken());
                prefHelper.setSocialLogin(true);
                prefHelper.setLoginStatus(true);
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
                if (getMainActivity() != null) {
                    getMainActivity().refreshSideMenu();
                }

                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
        /*getDockActivity().onLoadingStarted();
        outState.putBoolean(SUBMIT_PRESSED, submitPressed);
        submitPressed = true;*/
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if (submitPressed) {

            // no need to try-catch this, because we are not in a callback
            submitPressed = false;
        }*/
    }

    private boolean isvalidated() {
        if (edtEmail.getText() == null || edtEmail.getText().toString().trim().isEmpty() ||
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
        } else
            return true;

    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginHelper.FacebookLoginEnt LoginEnt) {

        facebookLoginEnt = LoginEnt;

        if (facebookLoginEnt.getFacebookEmail() != null && !facebookLoginEnt.getFacebookEmail().equals("") && !facebookLoginEnt.getFacebookEmail().isEmpty()) {
            serviceHelper.enqueueCall(webService.socialLogin(AppConstants.facebook, facebookLoginEnt.getFacebookUID(), facebookLoginEnt.getFacebookToken(), facebookLoginEnt.getFacebookFullName(), facebookLoginEnt.getFacebookEmail(),
                    AppConstants.User, AppConstants.PushNotification, FirebaseInstanceId.getInstance().getToken(), AppConstants.Device_Type,facebookLoginEnt.getFacebookUProfilePicture()), SocialLogin);
        } else {
            serviceHelper.enqueueCall(webService.socialLogin(AppConstants.facebook, facebookLoginEnt.getFacebookUID(), facebookLoginEnt.getFacebookToken(), facebookLoginEnt.getFacebookFullName(), "",
                    AppConstants.User, AppConstants.PushNotification, FirebaseInstanceId.getInstance().getToken(), AppConstants.Device_Type,facebookLoginEnt.getFacebookUProfilePicture()), SocialLogin);
        }
        //   getDockActivity().replaceDockableFragment(SocialSignupFragment.newInstance(facebookLoginEnt,true), "SocialSignupFragment");

    }

    @Override
    public void onGoogleSignInResult(GoogleSignInAccount result) {
        GoogleEnt entGoogle = new GoogleEnt(result.getId(), result.getDisplayName(), result.getEmail(), String.valueOf(result.getPhotoUrl()), result.getIdToken());

        if (entGoogle.getEmail() != null && !entGoogle.getEmail().equals("") && !entGoogle.getEmail().isEmpty()) {
            serviceHelper.enqueueCall(webService.socialLogin(AppConstants.google, entGoogle.getId(), entGoogle.getName(), entGoogle.getEmail(),
                    AppConstants.User, AppConstants.PushNotification, FirebaseInstanceId.getInstance().getToken(), AppConstants.Device_Type,entGoogle.getPhoto()), SocialLogin);
        } else {
            serviceHelper.enqueueCall(webService.socialLogin(AppConstants.google, entGoogle.getId(), entGoogle.getName(), "",
                    AppConstants.User, AppConstants.PushNotification, FirebaseInstanceId.getInstance().getToken(), AppConstants.Device_Type,entGoogle.getPhoto()), SocialLogin);
        }

        //  getDockActivity().replaceDockableFragment(SocialSignupFragment.newInstance(ent), "SocialSignupFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        googleHelper.ConnectGoogleAPi();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleHelper.DisconnectGoogleApi();
    }

    @Override
    public void onPause() {
        super.onPause();
        googleHelper.DisconnectGoogleApi();

    }
}
