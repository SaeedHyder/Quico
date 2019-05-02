package com.app.quico.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.entities.BatchCount;
import com.app.quico.entities.LocationEnt;
import com.app.quico.entities.LocationModel;
import com.app.quico.entities.ServicesEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.InternetHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.OnSettingActivateListener;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ServiesBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.BatchCountService;
import static com.app.quico.global.WebServiceConstants.Services;


public class HomeFragment extends BaseFragment implements RecyclerClickListner, AreaInterface, OnSettingActivateListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.btnMenu)
    ImageView btnMenu;
    @BindView(R.id.txt_subHead)
    ImageView txtSubHead;
    @BindView(R.id.btnNotification)
    ImageView btnNotification;
    @BindView(R.id.txtBadge)
    AnyTextView txtBadge;
    @BindView(R.id.btn_current_location)
    ImageView btnCurrentLocation;
    @BindView(R.id.btn_find_quico)
    Button btnFindQuico;
    @BindView(R.id.rv_services)
    CustomRecyclerView rvServices;
    Unbinder unbinder;
    @BindView(R.id.txtAddress)
    AnyTextView txtAddress;
    @BindView(R.id.txtServices)
    AnyTextView txtServices;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    private ArrayList<String> collection;
    private Double locationLat = 0.0;
    private Double locationLng = 0.0;
    private String cityId;
    private String areaId;
    private String serviceId;
    private int i = 0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationListener locationListener;
    private FusedLocationProviderClient fusedLocationProviderApi;
    private GoogleApiClient googleApiClient;
    private long LOCATION_INTERVAL = 10 * 1000;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().setOnSettingActivateListener(this);
        txtServices.setSelected(true);
        txtAddress.setSelected(true);


        getDockActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                requestLocationPermission();
            }
        });


        HomeServiceCall();
        pullRefreshListner();
    }

    private void setLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //      locationRequest.setInterval(LOCATION_INTERVAL);
        //     locationRequest.setFastestInterval(LOCATION_INTERVAL);
        fusedLocationProviderApi = LocationServices.getFusedLocationProviderClient(getDockActivity());
        googleApiClient = new GoogleApiClient.Builder(getDockActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        //requestLocationPermission();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        if (googleApiClient != null)
            googleApiClient.disconnect();
    }

    private void pullRefreshListner() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HomeServiceCall();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void HomeServiceCall() {
        if (InternetHelper.CheckInternetConectivityand(getDockActivity())) {
            txtNoData.setVisibility(View.GONE);
            serviceHelper.enqueueCall(headerWebService.getServices(prefHelper.isLanguageArabian() ? AppConstants.Arabic : AppConstants.English), Services);
            serviceHelper.enqueueCall(headerWebService.bacthCount(), BatchCountService, false);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @OnClick({R.id.btnMenu, R.id.btnNotification, R.id.btn_current_location, R.id.btn_find_quico, R.id.txtAddress, R.id.txtServices})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnMenu:
                getMainActivity().getDrawerLayout().clearFocus();
                getMainActivity().getDrawerLayout().clearAnimation();
                getMainActivity().getDrawerLayout().openDrawer(Gravity.LEFT);
                break;
            case R.id.btnNotification:
                getDockActivity().replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
                break;
            case R.id.btn_current_location:


                getDockActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        requestLocationPermission();
                    }
                });
                //requestLocationPermission();
                break;
            case R.id.btn_find_quico:
                if (isValidate()) {
                    getDockActivity().addDockableFragment(ServiceListingFragment.newInstance(getResString(R.string.companies), serviceId, cityId, areaId, locationLat + "", locationLng + ""), "ServiceListingFragment");
                }
                break;
            case R.id.txtAddress:
                SelectAreaFragment selectAreaFragment = new SelectAreaFragment();
                selectAreaFragment.setAreaListner(this);
                getDockActivity().addDockableFragment(selectAreaFragment, "SelectAreaFragment");
                break;
            case R.id.txtServices:
                SelectServicesFragment selectServicesFragment = new SelectServicesFragment();
                selectServicesFragment.setAreaListner(this);
                selectServicesFragment.setSelectedServices(serviceId);
                getDockActivity().addDockableFragment(selectServicesFragment, "SelectAreaFragment");
                break;
        }
    }


    private void requestLocationPermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            getLastLocationNewMethod();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            //requestLocationPermission();
                            getDockActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    requestLocationPermission();
                                }
                            });

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            //requestLocationPermission();

                            getDockActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    requestLocationPermission();
                                }
                            });
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant LocationEnt Permission to processed");
                        //openSettings();
                        // requestLocationPermission();
                        getDockActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                requestLocationPermission();
                            }
                        });
                    }
                })

                .onSameThread()
                .check();


    }

    private void openSettings() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        Uri uri = Uri.fromParts("package", getDockActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case BatchCountService:
                BatchCount data = (BatchCount) result;
                if (data.getCount() > 0) {
                    txtBadge.setVisibility(View.VISIBLE);
                    txtBadge.setText(data.getCount() + "");
                } else {
                    txtBadge.setVisibility(View.GONE);
                }
                break;

            case Services:
                ArrayList<ServicesEnt> entity = (ArrayList<ServicesEnt>) result;

                rvServices.BindRecyclerView(new ServiesBinder(getDockActivity(), prefHelper, this), entity,
                        new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                        , new DefaultItemAnimator());

                break;

        }
    }

    @Override
    public void onClick(Object entity, int position) {
        ServicesEnt data = (ServicesEnt) entity;
        if (locationLat != null && !locationLat.equals(0.0) && locationLng != null) {
            getDockActivity().addDockableFragment(ServiceListingFragment.newInstance(data.getId() + "", data.getName(), locationLat + "", locationLng + ""), "ServiceListingFragment");
        } else {
            getDockActivity().addDockableFragment(ServiceListingFragment.newInstance(data.getId() + "", data.getName()), "ServiceListingFragment");
        }
    }


    @Override
    public void onLocationActivateListener() {
        requestLocationPermission();
    }


    @SuppressLint("MissingPermission")
    private void getLastLocationNewMethod() {

        if (getMainActivity() != null && getMainActivity().statusCheck()) {
            getDockActivity().onLoadingStarted();

            fusedLocationProviderApi.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                try {
                                    getDockActivity().onLoadingFinished();
                                    if (location != null && location.getLatitude() > 0.00 && location.getLongitude() > 0.00) {
                                        txtAddress.setText(getMainActivity().getCurrentAddress(location.getLatitude(), location.getLongitude()));
                                        locationLat = location.getLatitude();
                                        locationLng = location.getLongitude();
                                        cityId = "";
                                        areaId = "";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    if (googleApiClient.isConnected()) {
                                        LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                                            @Override
                                            public void onLocationChanged(Location location) {
                                                try {
                                                    if (location != null) {
                                                        getDockActivity().onLoadingFinished();
                                                        if (location != null && location.getLatitude() > 0.00 && location.getLongitude() > 0.00) {
                                                            txtAddress.setText(getMainActivity().getCurrentAddress(location.getLatitude(), location.getLongitude()));
                                                            locationLat = location.getLatitude();
                                                            locationLng = location.getLongitude();
                                                            cityId = "";
                                                            areaId = "";
                                                        }
                                                    } else {
                                                        getDockActivity().onLoadingFinished();
                                                        UIHelper.showShortToastInDialoge(getDockActivity(), "Gps is not working, try again...");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    } else {
                                        getDockActivity().onLoadingFinished();
                                    }
                                } catch (Exception e) {
                                    getDockActivity().onLoadingFinished();
                                    e.printStackTrace();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getDockActivity().onLoadingFinished();
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            UIHelper.showShortToastInDialoge(getDockActivity(), "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });


        }
    }

    private void getLocation() {
        if (getMainActivity() != null && getMainActivity().statusCheck()) {
            LocationModel locationModel = getMainActivity().getMyCurrentLocation();
            if (locationModel != null && locationModel.getAddress() != null) {
                txtAddress.setText(locationModel.getAddress());
                locationLat = locationModel.getLat();
                locationLng = locationModel.getLng();

            } else {
                getDockActivity().onLoadingFinished();
                getLastLocationNewMethod();

            }
        }

    }

    @Override
    public void selectArea(Object entity, int position) {
        try {
            LocationEnt data = (LocationEnt) entity;
            cityId = data.getParentId() + "";
            areaId = data.getId() + "";

            locationLat = Double.parseDouble(data.getLatitude());
            locationLng = Double.parseDouble(data.getLongitude());
            txtAddress.setText(data.getLocation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectService(String selectedIds, String names) {
        serviceId = selectedIds;
        txtServices.setText(names);
    }

    private boolean isValidate() {
        if (txtAddress.getText() == null || txtAddress.getText().toString().trim().isEmpty()) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.select_city_to_proceed));
            return false;
        } else if (txtServices.getText() == null || txtServices.getText().toString().trim().isEmpty()) {
            UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.select_service_to_proceed));
            return false;
        } else {
            return true;
        }
    }


}

