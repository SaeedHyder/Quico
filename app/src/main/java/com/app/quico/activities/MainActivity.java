package com.app.quico.activities;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.quico.R;
import com.app.quico.entities.LocationModel;
import com.app.quico.fragments.ChatFragment;
import com.app.quico.fragments.HomeFragment;
import com.app.quico.fragments.LanguageFragment;
import com.app.quico.fragments.LoginFragment;
import com.app.quico.fragments.NotificationsFragment;
import com.app.quico.fragments.ServiceDetailFragment;
import com.app.quico.fragments.SideMenuFragment;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.global.SideMenuChooser;
import com.app.quico.global.SideMenuDirection;
import com.app.quico.helpers.ScreenHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.ImageSetter;
import com.app.quico.interfaces.OnSettingActivateListener;
import com.app.quico.interfaces.UpdateThreadId;
import com.app.quico.residemenu.ResideMenu;
import com.app.quico.ui.views.TitleBar;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.quico.global.AppConstants.chatPush;
import static com.app.quico.global.AppConstants.companyPush;
import static com.app.quico.global.AppConstants.deletePush;
import static com.app.quico.global.AppConstants.inactivePush;


public class MainActivity extends DockActivity implements OnClickListener, ImageChooserListener {
    private final static String TAG = "MainActivity";
    public TitleBar titleBar;
    @BindView(R.id.sideMneuFragmentContainer)
    public FrameLayout sideMneuFragmentContainer;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private MainActivity mContext;
    private boolean loading;

    public ResideMenu resideMenu;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String filePath;
    private String originalFilePath;
    private ImageSetter imageSetter;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;
    private boolean isActivityResultOver = false;

    private float lastTranslate = 0.0f;

    private String sideMenuType;
    private String sideMenuDirection;
    private AlertDialog alert;
    public final int WifiResultCode = 2;
    public final int LocationResultCode = 1;
    private String address = "";
    private String country = "";
    private LocationManager locationManager;
    private OnSettingActivateListener settingActivateListener;
    protected BroadcastReceiver broadcastReceiver;
    private boolean isFirstTime = true;
    private UpdateThreadId updateThreadId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        titleBar = header_main;
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;
        Log.i("Screen Density", ScreenHelper.getDensity(this) + "");

        sideMenuType = SideMenuChooser.DRAWER.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();
        lockDrawer();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        settingSideMenu(sideMenuType, sideMenuDirection);
        printHashKey(getDockActivity());
        onNotificationReceived();

        setCurrentLocale();


        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {
                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getDockActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getDockActivity().getCurrentFocus().getWindowToken(), 0);
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    resideMenu.openMenu(sideMenuDirection);
                }

            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        titleBar.setNotificationButtonListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
            }
        });

        initFragment();
    }


    private void setCurrentLocale() {
        if (prefHelper.isLanguageArabian()) {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration conf = resources.getConfiguration();
            Locale locale=new Locale("ar");
         //   conf.setLayoutDirection(locale);
            conf.locale = locale;
            resources.updateConfiguration(conf, dm);


        } else {
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration conf = resources.getConfiguration();
            Locale locale=new Locale("en");
          //  conf.setLayoutDirection(locale);
            conf.locale = locale;
            resources.updateConfiguration(conf, dm);


        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


    public void setOnSettingActivateListener(OnSettingActivateListener ActivateListener) {
        this.settingActivateListener = ActivateListener;
    }

    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }

    private void settingSideMenu(String type, String direction) {

        if (type.equals(SideMenuChooser.DRAWER.getValue())) {


            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen.x250), (int) DrawerLayout.LayoutParams.MATCH_PARENT);


            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.LEFT;
                sideMneuFragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.RIGHT;
                sideMneuFragmentContainer.setLayoutParams(params);
            }
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

            drawerLayout.closeDrawers();
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.52f);

            setMenuItemDirection(direction);
        }
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {

            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", direction);

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);

        }

    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }


    public void initFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        if (prefHelper.isLogin()) {
            popBackStackTillEntry(0);
            replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment", false);

        } else {
            if (prefHelper.isLanguageSelected()) {
                replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment", false);
            } else {
                replaceDockableFragment(LanguageFragment.newInstance(), "LanguageFragment", false);
            }
        }

        deepLinkIntent();
        pushNotifications();
    }

    private void pushNotifications() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String Type = "";
            String Title = "";
            String id = "";
            String message = "";


            if (bundle.getString("actionType") != null && !bundle.getString("actionType").equals("") && !bundle.getString("actionType").isEmpty()) {
                Type = bundle.getString("actionType");
            } else {
                Type = bundle.getString("action_type");
            }

            if (bundle.getString("title") != null && !bundle.getString("title").equals("") && !bundle.getString("title").isEmpty()) {
                Title = bundle.getString("title");
            } else {
                Title = bundle.getString("title");
            }

            if (bundle.getString("redId") != null && !bundle.getString("redId").equals("") && !bundle.getString("redId").isEmpty()) {
                id = bundle.getString("redId");
            } else {
                id = bundle.getString("ref_id");
            }

            if (bundle.getString("message") != null && !bundle.getString("message").equals("") && !bundle.getString("message").isEmpty()) {
                message = bundle.getString("message");
            } else {
                message = bundle.getString("body");
            }


            if (prefHelper.isLogin()) {
                if (Type != null && Type.equals(companyPush)) {
                    replaceDockableFragment(ServiceDetailFragment.newInstance(id), "ServiceDetailFragment");
                } else if (Type != null && Type.equals(chatPush)) {
                    if (id != null) {
                        replaceDockableFragment(ChatFragment.newInstance(id), "ChatFragment");
                    }
                } else if (Type != null && Title != null && !Title.equals("")) {
                    replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
                }

            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        deepLinkIntent();
        pushNotifications();
    }

    private void deepLinkIntent() {
        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getData() != null) {
            if (prefHelper.isLogin()) {
                Uri data = intent.getData();
                replaceDockableFragment(ServiceDetailFragment.newInstance(data.getQueryParameter("id"), false), "ServiceDetailFragment");
            }
        }
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {

                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String Type = bundle.getString("actionType");

                        if (Type != null && Type.equals(deletePush)) {
                            UIHelper.showShortToastInDialoge(getDockActivity(), getDockActivity().getResources().getString(R.string.deleted_by_admin));
                            getDockActivity().popBackStackTillEntry(0);
                            prefHelper.setLoginStatus(false);
                            prefHelper.setSocialLogin(false);
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            NotificationManager notificationManager = (NotificationManager) getDockActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                        } else if (Type != null && Type.equals(inactivePush)) {
                            UIHelper.showShortToastInDialoge(getDockActivity(), getDockActivity().getResources().getString(R.string.blocked_by_admin));
                            getDockActivity().popBackStackTillEntry(0);
                            prefHelper.setLoginStatus(false);
                            prefHelper.setSocialLogin(false);
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            NotificationManager notificationManager = (NotificationManager) getDockActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                        }
                    }
                }

            }

        };
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();

                        if (currFrag instanceof ChatFragment) {
                            prefHelper.setChatScreen(true);
                        } else {
                            prefHelper.setChatScreen(false);
                        }
                    }

                }
            }
        };

        return result;
    }

    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.show();
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressBar != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.hide();
        }
        loading = false;

    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {

    }

    @Override
    public void setSubHeading(String subHeadText) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.Fragment currentFragment = getDockActivity().getSupportFragmentManager().findFragmentById(getDockActivity().getDockFrameLayoutId());
        if (updateThreadId != null && currentFragment instanceof ChatFragment) {
            String threadId = ((ChatFragment) currentFragment).getThreadId();
            String entityThreadId = ((ChatFragment) currentFragment).getEntityId();

            if (threadId != null && !threadId.equals("") && !threadId.isEmpty()) {
                super.onBackPressed();
            } else if ((entityThreadId != null && !entityThreadId.equals("") && !entityThreadId.isEmpty() && !entityThreadId.equals("null"))) {
                updateThreadId.onBackPressedActivity(entityThreadId);
            } else {
                super.onBackPressed();
            }
        } else {
            if (loading) {
                UIHelper.showLongToastInCenter(getApplicationContext(), R.string.message_wait);
            } else
                super.onBackPressed();
        }

    }

    @Override
    public void onClick(View view) {

    }

    private void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Coming Soon");
    }

    public DisplayImageOptions getImageLoaderRoundCornerTransformation(int raduis) {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.placeholder_thumb)
                .showImageOnFail(R.drawable.placeholder_thumb).resetViewBeforeLoading(true)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .displayer(new RoundedBitmapDisplayer(raduis))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public void setImageSetter(ImageSetter imageSetter) {
        this.imageSetter = imageSetter;
    }


    public void setUpdateInterface(UpdateThreadId updateThreadId) {
        this.updateThreadId = updateThreadId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        }
        if (resultCode == Activity.RESULT_OK) {
            if (settingActivateListener != null)
                settingActivateListener.onLocationActivateListener();
        }


    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, false);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");

                    // Toast.makeText(getApplication(),thumbnailFilePath,Toast.LENGTH_LONG).show();
                    imageSetter.setImage(originalFilePath);

                    //loadImage(imageViewThumbnail, image.getFileThumbnail());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }

            }
        });

    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                // pbar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });

    }

    public boolean statusCheck() {
        if (isConnected(getApplicationContext())) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                turnLocationOn(null);
                //buildAlertMessageNoGps(R.string.gps_question, Settings.ACTION_LOCATION_SOURCE_SETTINGS, LocationResultCode);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMob = cm.getNetworkInfo(cm.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(cm.TYPE_WIFI);
        if (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) {
            Log.v("TAG", "Mobile Internet connected");
            return true;
        }
        if (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting()) {
            Log.v("TAG", "Wifi Internet connected");
            return true;
        }
        buildAlertMessageNoGps(R.string.wifi_question, Settings.ACTION_WIFI_SETTINGS, WifiResultCode);
        return false;

    }

    private void buildAlertMessageNoGps(final int StringResourceID, final String IntentType, final int requestCode) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getDockActivity());
        builder
                .setMessage(getString(StringResourceID))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.gps_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (StringResourceID == R.string.gps_question) {
                            dialog.cancel();
                            turnLocationOn(null);
                            dialog.dismiss();
                        } else {
                            dialog.cancel();
                            startImpIntent(dialog, IntentType, requestCode);
                            dialog.dismiss();
                        }

                        // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),LocationResultCode);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.gps_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                        dialog.cancel();

                    }
                });
        if (alert == null) {
            alert = builder.create();

        }

        if (!alert.isShowing())
            alert.show();

    }

    public void turnLocationOn(GoogleApiClient googleApiClient) {
        if (googleApiClient == null) {
            GoogleApiClient finalGoogleApiClient = googleApiClient;
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            if (finalGoogleApiClient != null)
                                finalGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("LocationEnt error", "LocationEnt error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setNeedBle(true);
            builder.setAlwaysShow(true);
            Task<LocationSettingsResponse> task =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        if (settingActivateListener != null) {
                            settingActivateListener.onLocationActivateListener();
                        }

                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;

                                    resolvable.startResolutionForResult(getDockActivity(), 1000);
                                } catch (IntentSender.SendIntentException e) {
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // LocationEnt settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });

        }
    }

    private void startImpIntent(DialogInterface dialog, String IntentType, int requestCode) {
        dialog.dismiss();
        Intent i = new Intent(IntentType);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(i, requestCode);
    }

    public LocationModel getMyCurrentLocation() {


        String address = "";
        LocationModel locationObj = new LocationModel();
        //  LocationModel locationObj = new LocationModel(address,24.829759,67.073822);


// instantiate the location manager, note you will need to request permissions in your manifest
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


// get the last know location from your location manager.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
// now get the lat/lon from the location and do something with it.
        Location gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networklocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location passivelocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Location locationchangelocation = locationManager.getLastKnownLocation(LocationManager.KEY_LOCATION_CHANGED);


        if (gpslocation != null) {

            Log.d("LocationEnt", "GPS::" + gpslocation.getLatitude() + "," + gpslocation.getLongitude());
            address = getCurrentAddress(gpslocation.getLatitude(), gpslocation.getLongitude());
            locationObj = new LocationModel(address, gpslocation.getLatitude(), gpslocation.getLongitude());

            return locationObj;

        } else if (networklocation != null) {

            Log.d("LocationEnt", "NETWORK::" + networklocation.getLatitude() + "," + networklocation.getLongitude());
            address = getCurrentAddress(networklocation.getLatitude(), networklocation.getLongitude());
            locationObj = new LocationModel(address, networklocation.getLatitude(), networklocation.getLongitude());

            return locationObj;
        } else if (passivelocation != null) {

            Log.d("LocationEnt", "PASSIVE::" + passivelocation.getLatitude() + "," + passivelocation.getLongitude());
            address = getCurrentAddress(passivelocation.getLatitude(), passivelocation.getLongitude());
            locationObj = new LocationModel(address, passivelocation.getLatitude(), passivelocation.getLongitude());

            return locationObj;
        } else if (locationchangelocation != null) {

            Log.d("LocationEnt", "CHAGELOCATION::" + locationchangelocation.getLatitude() + "," + locationchangelocation.getLongitude());
            address = getCurrentAddress(locationchangelocation.getLatitude(), locationchangelocation.getLongitude());
            locationObj = new LocationModel(address, locationchangelocation.getLatitude(), locationchangelocation.getLongitude());

            return locationObj;
        } else {
            return locationObj;
        }

    }

    public String getCurrentAddress(double lat, double lng) {
        try {


            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            }
// String city = addresses.get(0).getLocality();
// String state = addresses.get(0).getAdminArea();
            if (addresses.size() > 0) {
                country = addresses.get(0).getCountryName();
            }
// String postalCode = addresses.get(0).getPostalCode();
// String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            return address + ", " + country;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    public void refreshSideMenu() {
        sideMenuFragment.refreshSideMenuData();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));

       /* if (!prefHelper.isLogin() && !isFirstTime) {
            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        isFirstTime = false;
    }

    public void lockDrawer() {
        try {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();

    }

    public void releaseDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public void restartActivity() {
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("actionType", "");
            bundle.putString("action_type", "");
            bundle.putString("title", "");
            intent.putExtras(bundle);
        }

        finish();
        startActivity(intent);
    }

}
