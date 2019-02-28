package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment {
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.txt_name)
    AnyTextView txtName;
    @BindView(R.id.txtEmail)
    AnyTextView txtEmail;
    @BindView(R.id.txPhonel)
    AnyTextView txPhonel;
    @BindView(R.id.txtAddress)
    AnyTextView txtAddress;
    Unbinder unbinder;

    private ImageLoader imageLoader;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void setData() {

        if (prefHelper.getUser() != null && prefHelper.getUser().getUser() != null) {

            txtName.setText(prefHelper.getUser().getUser().getName() + "");
            txtEmail.setText(prefHelper.getUser().getUser().getEmail() + "");

            if (prefHelper.getUser().getUser().getCountryCode() != null && !prefHelper.getUser().getUser().getCountryCode().equals("") &&
                    prefHelper.getUser().getUser().getPhone() != null && !prefHelper.getUser().getUser().getPhone().equals("")) {
                txPhonel.setText(prefHelper.getUser().getUser().getCountryCode() + "" + prefHelper.getUser().getUser().getPhone() + "");
            }

            if (prefHelper.getUser().getUser().getDetails().getAddress() != null && !prefHelper.getUser().getUser().getDetails().getAddress().equals("") && !prefHelper.getUser().getUser().getDetails().getAddress().equals("null")) {
                txtAddress.setText(prefHelper.getUser().getUser().getDetails().getAddress());
            }
            if (prefHelper.getUser().getUser().getDetails().getImageUrl() != null && !prefHelper.getUser().getUser().getDetails().getImageUrl().equals("")) {
                //   imageLoader.displayImage(prefHelper.getUser().getUser().getDetails().getImageUrl(), profileImage);
                Picasso.get().load(prefHelper.getUser().getUser().getDetails().getImageUrl()).placeholder(R.drawable.placeholder).into(profileImage);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.MyAccount));
        titleBar.showEdithButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDockActivity().replaceDockableFragment(EditProfileFragment.newInstance(), "EditProfileFragment");
            }
        });
    }


}
