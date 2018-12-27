package com.app.quico.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.entities.LocationModel;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ServiesBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
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


public class HomeFragment extends BaseFragment implements RecyclerClickListner, AreaInterface {


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

    private ArrayList<String> collection;
    private Double locationLat = 0.0;
    private Double locationLng = 0.0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setData();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    private void setData() {

        collection = new ArrayList<>();
        collection.add("drawable://" + R.drawable.plubmer);
        collection.add("drawable://" + R.drawable.electricina);
        collection.add("drawable://" + R.drawable.interiordesign);
        collection.add("drawable://" + R.drawable.automobile);


        rvServices.BindRecyclerView(new ServiesBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
    }

    @OnClick({R.id.btnMenu, R.id.btnNotification, R.id.btn_current_location, R.id.btn_find_quico, R.id.txtAddress,R.id.txtServices})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnMenu:
                getMainActivity().drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.btnNotification:
                getDockActivity().replaceDockableFragment(NotificationsFragment.newInstance(), "NotificationsFragment");
                break;
            case R.id.btn_current_location:
                requestLocationPermission();
                break;
            case R.id.btn_find_quico:
                UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.txtAddress:
                SelectAreaFragment selectAreaFragment = new SelectAreaFragment();
                selectAreaFragment.setAreaListner(this);
                getDockActivity().addDockableFragment(selectAreaFragment, "SelectAreaFragment");
                break;
            case R.id.txtServices:
                SelectServicesFragment selectServicesFragment = new SelectServicesFragment();
                selectServicesFragment.setAreaListner(this);
                getDockActivity().addDockableFragment(selectServicesFragment, "SelectAreaFragment");
                break;
        }
    }

    @Override
    public void onClick(Object entity, int position) {
        getDockActivity().replaceDockableFragment(ServiceListingFragment.newInstance(), "ServiceListingFragment", false);
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
                            getLocation();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestLocationPermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestLocationPermission();
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
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Location Permission to processed");
                        openSettings();
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

    private void getLocation() {
        if (getMainActivity() != null && getMainActivity().statusCheck()) {
            LocationModel locationModel = getMainActivity().getMyCurrentLocation();
            if (locationModel != null) {
                txtAddress.setText(locationModel.getAddress());

                locationLat = locationModel.getLat();
                locationLng = locationModel.getLng();


            } else {
                getLocation();
            }
        }
    }




    @Override
    public void selectArea(String name) {
        txtAddress.setText(name);
    }

    @Override
    public void selectService(String name) {
        txtServices.setText(name);
    }
}

