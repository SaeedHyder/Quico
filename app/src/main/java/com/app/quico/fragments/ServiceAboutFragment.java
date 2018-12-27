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
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.OfferedServiceBinder;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;

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

    private ArrayList<String> collection;

    public static ServiceAboutFragment newInstance() {
        Bundle args = new Bundle();

        ServiceAboutFragment fragment = new ServiceAboutFragment();
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

        collection = new ArrayList<>();
        collection.add("Installation");
        collection.add("Faucet Fixing");
        collection.add("Leackage");
        collection.add("Installation");
        collection.add("Faucet Fixing");
        collection.add("Leackage");


        rvServices.BindRecyclerView(new OfferedServiceBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.HORIZONTAL, false)
                , new DefaultItemAnimator());
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));
                break;
            case R.id.btn_insta:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com")));
                break;
            case R.id.btn_google:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.plus.google.com")));
                break;
            case R.id.btn_linkedin:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com")));
                break;
            case R.id.btn_twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com")));
                break;
            case R.id.btn_chat_with_us:
                getDockActivity().replaceDockableFragment(ChatFragment.newInstance(), "ChatFragment");
                break;
            case R.id.btnAddress:
                String geoUri = "http://maps.google.com/maps?q=loc:" + 25.2048 + "," + 55.2708;
                Intent intentAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intentAddress);
                break;
            case R.id.btnPhoneNumber:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
                break;
            case R.id.btnEmail:
                try{
                    Intent intentEmail = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "your_email"));
                    intentEmail.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intentEmail.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intentEmail);
                }catch(ActivityNotFoundException e){
                    //TODO smth
                }
                break;
            case R.id.btnWebsite:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")));
                break;
        }
    }


}
