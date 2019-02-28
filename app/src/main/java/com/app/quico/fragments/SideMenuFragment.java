package com.app.quico.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.entities.SideMenuEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.DialogHelper;
import com.app.quico.helpers.GoogleHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.SideMenuBinder;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.Logout;

public class SideMenuFragment extends BaseFragment implements RecyclerClickListner {


    @BindView(R.id.SideMenu)
    CustomRecyclerView SideMenu;
    Unbinder unbinder;
    private ArrayList<SideMenuEnt> collection;
    private long mLastClickTime = 0;
    private int previousSelectedPos = 0;

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);

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
        collection.add(new SideMenuEnt(AppConstants.Home, R.drawable.home2, R.drawable.home, true));
        collection.add(new SideMenuEnt(AppConstants.Favorites, R.drawable.fav, R.drawable.fav2));
        collection.add(new SideMenuEnt(AppConstants.MyChat, R.drawable.mychat, R.drawable.mychat2));
        collection.add(new SideMenuEnt(AppConstants.AccountSetting, R.drawable.accountsettings, R.drawable.accountsettings2));
        collection.add(new SideMenuEnt(AppConstants.AboutUs, R.drawable.aboutus, R.drawable.aboutus2));
        collection.add(new SideMenuEnt(AppConstants.BEQUICO, R.drawable.contactus, R.drawable.contactus2));
        collection.add(new SideMenuEnt(AppConstants.TermsOfServices, R.drawable.termsofservice, R.drawable.termsofservice2));
        collection.add(new SideMenuEnt(AppConstants.Logout, R.drawable.logout, R.drawable.logout2));


        SideMenu.BindRecyclerView(new SideMenuBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
        SideMenu.setNestedScrollingEnabled(false);
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onClick(Object entity, int position) {

      /*  if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }*/
        collection.get(position).setSelected(true);
        SideMenu.notifyItemChanged(position);
        if (previousSelectedPos != position) {
            collection.get(previousSelectedPos).setSelected(false);
            SideMenu.notifyItemChanged(previousSelectedPos);
        }
        previousSelectedPos = position;

        SideMenuEnt ent = (SideMenuEnt) entity;
        // getDockActivity().popBackStackTillEntry(1);

        if (ent.getTitle().equals(AppConstants.Home)) {
            getDockActivity().popFragment();
            getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment", false);

        } else if (ent.getTitle().equals(AppConstants.Favorites)) {
            getDockActivity().popFragment();
            getDockActivity().replaceDockableFragment(FavoriteFragment.newInstance(), "FavoriteFragment", false);

        } else if (ent.getTitle().equals(AppConstants.MyChat)) {
          //  getDockActivity().popFragment();
          //  getDockActivity().replaceDockableFragment(MyChatThreadFragment.newInstance(), "MyChatThreadFragment", false);
            UIHelper.showShortToastInDialoge(getDockActivity(),getResString(R.string.will_be_implemented));

        } else if (ent.getTitle().equals(AppConstants.AccountSetting)) {
            getDockActivity().popFragment();
            getDockActivity().replaceDockableFragment(SettingFragment.newInstance(), "SettingFragment", false);

        } else if (ent.getTitle().equals(AppConstants.AboutUs)) {
            getDockActivity().popFragment();
            getDockActivity().replaceDockableFragment(CmsFragment.newInstance(getResString(R.string.AboutUs)), "CmsFragment", false);

        } else if (ent.getTitle().equals(AppConstants.BEQUICO)) {
            getDockActivity().popFragment();
            getDockActivity().replaceDockableFragment(ContactUsFragment.newInstance(), "ContactUsFragment", false);

        } else if (ent.getTitle().equals(AppConstants.TermsOfServices)) {
            getDockActivity().popFragment();
            getDockActivity().replaceDockableFragment(CmsFragment.newInstance(getResString(R.string.TermsOfService)), "CmsFragment", false);

        } else if (ent.getTitle().equals(AppConstants.Logout)) {

            DialogHelper dialoge = new DialogHelper(getDockActivity());
            dialoge.initlogout(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceHelper.enqueueCall(headerWebService.logout(), Logout);
                    dialoge.hideDialog();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialoge.hideDialog();
                }
            });
            dialoge.showDialog();
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case Logout:
                getDockActivity().popBackStackTillEntry(0);
                prefHelper.setLoginStatus(false);
                prefHelper.setSocialLogin(false);
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }

              /*  if (GoogleHelper.mGoogleHelper.getGoogleApiClient().isConnected()) {
                    GoogleHelper.mGoogleHelper.googleSignOut();
                }*/

                NotificationManager notificationManager = (NotificationManager) getDockActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                UIHelper.showShortToastInCenter(getDockActivity(), getResString(R.string.logout_successfully));
                break;

        }
    }

    public void refreshSideMenuData() {

        previousSelectedPos = 0;
        collection = new ArrayList<>();
        collection.add(new SideMenuEnt(AppConstants.Home, R.drawable.home2, R.drawable.home, true));
        collection.add(new SideMenuEnt(AppConstants.Favorites, R.drawable.fav, R.drawable.fav2));
        collection.add(new SideMenuEnt(AppConstants.MyChat, R.drawable.mychat, R.drawable.mychat2));
        collection.add(new SideMenuEnt(AppConstants.AccountSetting, R.drawable.accountsettings, R.drawable.accountsettings2));
        collection.add(new SideMenuEnt(AppConstants.AboutUs, R.drawable.aboutus, R.drawable.aboutus2));
        collection.add(new SideMenuEnt(AppConstants.BEQUICO, R.drawable.contactus, R.drawable.contactus2));
        collection.add(new SideMenuEnt(AppConstants.TermsOfServices, R.drawable.termsofservice, R.drawable.termsofservice2));
        collection.add(new SideMenuEnt(AppConstants.Logout, R.drawable.logout, R.drawable.logout2));

        SideMenu.BindRecyclerView(new SideMenuBinder(getDockActivity(), prefHelper, this), collection,
                new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                , new DefaultItemAnimator());
    }



}
