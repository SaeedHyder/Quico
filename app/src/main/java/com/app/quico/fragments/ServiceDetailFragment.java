package com.app.quico.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.quico.R;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.InternetHelper;
import com.app.quico.helpers.ShareIntentHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRatingBar;
import com.app.quico.ui.views.TitleBar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.quico.activities.DockActivity.KEY_FRAG_FIRST;
import static com.app.quico.global.WebServiceConstants.BatchCountService;
import static com.app.quico.global.WebServiceConstants.CompanyDetailKey;
import static com.app.quico.global.WebServiceConstants.Favorite;
import static com.app.quico.global.WebServiceConstants.Services;
import static com.app.quico.global.WebServiceConstants.UpdateReviews;

public class ServiceDetailFragment extends BaseFragment {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.logo)
    CircleImageView logo;
    @BindView(R.id.txt_name)
    AnyTextView txtName;
    @BindView(R.id.rbParlourRating)
    CustomRatingBar rbParlourRating;
    @BindView(R.id.txt_rating)
    AnyTextView txtRating;
    @BindView(R.id.cb_fav)
    CheckBox cbFav;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    Unbinder unbinder;
    @BindView(R.id.btn_share)
    ImageView btnShare;
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.mainFrameLayout)
    CoordinatorLayout mainFrameLayout;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private static String companyId;
    private CompanyDetail companyDetail;
    private ImageLoader imageLoader;
    private static boolean backBtn = false;

    public static ServiceDetailFragment newInstance(String id) {
        Bundle args = new Bundle();
        companyId = id;
        backBtn = false;
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ServiceDetailFragment newInstance(String id, boolean showBtn) {
        Bundle args = new Bundle();
        companyId = id;
        backBtn = showBtn;
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();

        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (backBtn) {
            btnBack.setVisibility(View.GONE);
        }
        mainFrameLayout.setVisibility(View.GONE);
        callService();
        favListner();


    }

    private void callService() {
        if (InternetHelper.CheckInternetConectivityand(getDockActivity())) {
            txtNoData.setVisibility(View.GONE);
            setTabLayout();
            serviceHelper.enqueueCall(headerWebService.getCompanyDetail(companyId,prefHelper.isLanguageArabian()? AppConstants.Arabic:AppConstants.English), CompanyDetailKey);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
    }


    private void favListner() {

        cbFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                serviceHelper.enqueueCall(headerWebService.favorite(companyId, b ? AppConstants.like : AppConstants.unlike), Favorite, false);
            }
        });
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case CompanyDetailKey:
                mainFrameLayout.setVisibility(View.VISIBLE);
                companyDetail = (CompanyDetail) result;
                if (backBtn) {
                    companyDetail.setBackBtn(true);
                }
                setCompanyDetail();
                tabLayoutistner();
                break;

            case UpdateReviews:
                companyDetail = (CompanyDetail) result;
                replaceFragment(ServiceReviewsFragment.newInstance(companyDetail));
                break;

            case Favorite:

                break;
        }
    }

    private void setCompanyDetail() {
        if (companyDetail != null) {
            //   imageLoader.displayImage(companyDetail.getImageUrl(), image);
            Picasso.get().load(companyDetail.getImageUrl()).placeholder(R.drawable.placeholder_thumb).into(image);
            imageLoader.displayImage(companyDetail.getIconUrl(), logo);
            txtName.setText(companyDetail.getName());
            if (companyDetail.getReviewCount() != 0) {
                txtRating.setVisibility(View.VISIBLE);
                txtRating.setText(companyDetail.getReviewCount() + " " + getResString(R.string.reviews));
            }else{
                txtRating.setVisibility(View.GONE);
            }
            rbParlourRating.setScore(companyDetail.getAvgRate());

            if (companyDetail.getIsFavorite()!=null && companyDetail.getIsFavorite() == 1) {
                cbFav.setChecked(true);
            } else {
                cbFav.setChecked(false);
            }
        }

    }

    private void setTabLayout() {

        if (tabLayout != null) {
            tabLayout.removeAllTabs();
            tabLayout.addTab(tabLayout.newTab().setText(getResString(R.string.about)));
            tabLayout.addTab(tabLayout.newTab().setText(getResString(R.string.photos)));
            tabLayout.addTab(tabLayout.newTab().setText(getResString(R.string.reviews_1)));
        }
    }

    private void tabLayoutistner() {

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        setData(tab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                setData(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setData(TabLayout.Tab tab) {

        if (tab.getPosition() == 0) {
            replaceFragment(ServiceAboutFragment.newInstance(companyDetail));
        } else if (tab.getPosition() == 1) {
            replaceFragment(ServiceProjectsFragment.newInstance(companyDetail));
        } else {
            replaceFragment(ServiceReviewsFragment.newInstance(companyDetail));
        }

    }

    public void replaceFragment(Fragment frag) {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, frag);
        transaction.addToBackStack(manager.getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST : null).commitAllowingStateLoss();


    }


    @OnClick({R.id.btnBack, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                getDockActivity().popFragment();
                break;
            case R.id.btn_share:
                requestStoragePermission();
                break;
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(getDockActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            if (companyDetail != null && companyDetail.getName() != null && !companyDetail.getName().equals("")) {
                                ShareIntentHelper.shareTextIntent(getDockActivity(), companyDetail.getLink());
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestStoragePermission();

                        } else if (report.getDeniedPermissionResponses().size() > 0) {
                            requestStoragePermission();
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
                        UIHelper.showShortToastInCenter(getDockActivity(), "Grant Storage Permission to processed");
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

    public void updateReviews() {
        serviceHelper.enqueueCall(headerWebService.getCompanyDetail(companyId,prefHelper.isLanguageArabian()? AppConstants.Arabic:AppConstants.English), UpdateReviews, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
