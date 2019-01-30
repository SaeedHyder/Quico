package com.app.quico.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.quico.R;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.entities.SocialDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.OfferedServiceBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ServiceAboutFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.txt_about)
    AnyTextView txtAbout;
    Unbinder unbinder;
    @BindView(R.id.rv_services)
    CustomRecyclerView rvServices;
    @BindView(R.id.address)
    AnyTextView address;
    @BindView(R.id.txt_address)
    AnyTextView txtAddress;
    @BindView(R.id.phoneNumber)
    AnyTextView phoneNumber;
    @BindView(R.id.txt_phoneNo)
    AnyTextView txtPhoneNo;
    @BindView(R.id.email)
    AnyTextView email;
    @BindView(R.id.txt_email)
    AnyTextView txtEmail;
    @BindView(R.id.website)
    AnyTextView website;
    @BindView(R.id.txt_services_offered)
    AnyTextView txtServicesOffered;
    @BindView(R.id.txt_website)
    AnyTextView txtWebsite;
    @BindView(R.id.btn_fb)
    ImageView btnFb;
    @BindView(R.id.btn_insta)
    ImageView btnInsta;
    @BindView(R.id.btn_google)
    ImageView btnGoogle;
    @BindView(R.id.btn_linkedin)
    ImageView btnLinkedin;
    @BindView(R.id.btn_twitter)
    ImageView btnTwitter;
    @BindView(R.id.btn_chat_with_us)
    Button btnChatWithUs;
    @BindView(R.id.btnAddress)
    RelativeLayout btnAddress;
    @BindView(R.id.btnPhoneNumber)
    RelativeLayout btnPhoneNumber;
    @BindView(R.id.btnEmail)
    RelativeLayout btnEmail;
    @BindView(R.id.btnWebsite)
    RelativeLayout btnWebsite;


    private CompanyDetail companyDetail;
    private static String companyDetailKey = "companyDetailKey";
    private String facebookUrl, instagramUrl, googlePlusUrl, twitterUrl, linkedinUrl;

    public static ServiceAboutFragment newInstance(CompanyDetail data) {
        Bundle args = new Bundle();
        args.putString(companyDetailKey, new Gson().toJson(data));
        ServiceAboutFragment fragment = new ServiceAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(companyDetailKey);

            if (json != null) {
                companyDetail = new Gson().fromJson(json, CompanyDetail.class);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_about, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();

    }

    private void setData() {

        if (companyDetail != null) {

            if(companyDetail.isBackBtn()){
                btnChatWithUs.setVisibility(View.GONE);
            }

            txtAbout.setText(companyDetail.getAbout());
            txtAddress.setText(companyDetail.getLocation());

            if (companyDetail.getCountryCode() != null && !companyDetail.getCountryCode().equals("") && !companyDetail.getCountryCode().equals("null")) {
                txtPhoneNo.setText(companyDetail.getCountryCode() + companyDetail.getPhone());
            } else {
                txtPhoneNo.setText(companyDetail.getPhone());
            }
            txtEmail.setText(companyDetail.getEmail());
            txtWebsite.setText(companyDetail.getWebsite());

            if (companyDetail.getServicesDetails() != null && companyDetail.getServicesDetails().size() > 0) {
                txtServicesOffered.setVisibility(View.VISIBLE);
                rvServices.setVisibility(View.VISIBLE);

                rvServices.BindRecyclerView(new OfferedServiceBinder(getDockActivity(), prefHelper, this), companyDetail.getServicesDetails(),
                        new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false)
                        , new DefaultItemAnimator());
            } else {
                txtServicesOffered.setVisibility(View.GONE);
                rvServices.setVisibility(View.GONE);
            }

            for (SocialDetail item : companyDetail.getSocialDetails()) {
                if (item.getType().equals(AppConstants.Facebook)) {
                    facebookUrl = item.getLink();
                } else if (item.getType().equals(AppConstants.Instagram)) {
                    instagramUrl = item.getLink();
                } else if (item.getType().contains(AppConstants.GooglePlus)) {
                    googlePlusUrl = item.getLink();
                } else if (item.getType().equals(AppConstants.Twitter)) {
                    twitterUrl = item.getLink();
                } else if (item.getType().equals(AppConstants.Linkedin)) {
                    linkedinUrl = item.getLink();
                }
            }


        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @Override
    public void onClick(Object entity, int position) {
        UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
    }


    @OnClick({R.id.btn_fb, R.id.btn_insta, R.id.btn_google, R.id.btn_linkedin, R.id.btn_twitter, R.id.btn_chat_with_us, R.id.btnAddress, R.id.btnPhoneNumber, R.id.btnEmail, R.id.btnWebsite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_fb:
                if (facebookUrl != null && !facebookUrl.equals("")) {
                    openWebPage(facebookUrl);
                }
                break;
            case R.id.btn_insta:
                if (instagramUrl != null && !instagramUrl.equals("")) {
                    openWebPage(instagramUrl);
                }
                break;
            case R.id.btn_google:
                if (googlePlusUrl != null && !googlePlusUrl.equals("")) {
                    openWebPage(googlePlusUrl);
                }
                break;
            case R.id.btn_linkedin:
                if (linkedinUrl != null && !linkedinUrl.equals("")) {
                    openWebPage(linkedinUrl);
                }
                break;
            case R.id.btn_twitter:
                if (twitterUrl != null && !twitterUrl.equals("")) {
                    openWebPage(twitterUrl);
                }
                break;
            case R.id.btn_chat_with_us:
                getDockActivity().replaceDockableFragment(ChatFragment.newInstance(), "ChatFragment");
                break;
            case R.id.btnAddress:
                if (companyDetail != null && companyDetail.getLatitude() != null && companyDetail.getLongitude() != null && !companyDetail.getLatitude().equals("")) {
                    String geoUri = "http://maps.google.com/maps?q=loc:" + companyDetail.getLatitude() + "," + companyDetail.getLongitude();
                    Intent intentAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    startActivity(intentAddress);
                } else {

                }
                //   UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btnPhoneNumber:
                if (companyDetail.getPhone() != null && !companyDetail.getPhone().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    if (companyDetail.getCountryCode() != null && !companyDetail.getCountryCode().equals("")) {
                        intent.setData(Uri.parse("tel:" + companyDetail.getCountryCode() + companyDetail.getPhone()));
                    }else{
                        intent.setData(Uri.parse("tel:" + companyDetail.getPhone()));
                    }
                    startActivity(intent);
                }
                //  UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btnEmail:
                try {
                    if (companyDetail.getEmail() != null && !companyDetail.getEmail().isEmpty()) {
                        Intent intentEmail = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + companyDetail.getEmail()));
                        startActivity(intentEmail);
                    }
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
                // UIHelper.showShortToastInDialoge(getDockActivity(), getResString(R.string.will_be_implemented));
                break;
            case R.id.btnWebsite:
                if (companyDetail.getWebsite() != null && !companyDetail.getWebsite().isEmpty()) {
                    openWebPage(companyDetail.getWebsite());
                }
                break;
        }
    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                webpage = Uri.parse("http://" + url);
            }
            startActivity(new Intent(Intent.ACTION_VIEW, webpage));
        } catch (ActivityNotFoundException e) {
            //TODO smth
        }
    }


}
