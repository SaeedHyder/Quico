package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.quico.R;
import com.app.quico.entities.CompanyDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.WebServiceConstants;
import com.app.quico.helpers.UIHelper;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRatingBar;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.quico.global.WebServiceConstants.Rating;

public class FeedbackFragment extends BaseFragment {
    @BindView(R.id.logo)
    CircleImageView logo;
    @BindView(R.id.txt_name)
    AnyTextView txtName;
    @BindView(R.id.txt_address)
    AnyTextView txtAddress;
    @BindView(R.id.rbParlourRating)
    CustomRatingBar rbParlourRating;
    @BindView(R.id.edt_contactUs)
    AnyEditTextView edtContactUs;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;

    private CompanyDetail companyDetail;
    private static String companyDetailKey = "companyDetailKey";
    private ImageLoader imageLoader;

    public static FeedbackFragment newInstance(CompanyDetail data) {
        Bundle args = new Bundle();
        args.putString(companyDetailKey, new Gson().toJson(data));
        FeedbackFragment fragment = new FeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
            String json = getArguments().getString(companyDetailKey);
            if (json != null) {
                companyDetail = new Gson().fromJson(json, CompanyDetail.class);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCompanyData();
        rbParlourRating.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    rbParlourRating.setScore(1.0f);
            }
        });

    }

    private void setCompanyData() {
        txtName.setText(companyDetail.getName());
        txtAddress.setText(companyDetail.getLocation());
        imageLoader.displayImage(companyDetail.getIconUrl(), logo);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.rate_review));
    }

    private boolean isvalidated() {
        if (edtContactUs.getText().toString().trim().isEmpty() || edtContactUs.getText().toString().trim().length() < 3) {
            edtContactUs.setError(getString(R.string.enter_your_message));
            if (edtContactUs.requestFocus()) {
                setEditTextFocus(edtContactUs);
            }
            return false;
        } else
            return true;

    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isvalidated()) {
            serviceHelper.enqueueCall(headerWebService.rating(companyDetail.getId() + "", String.valueOf(Math.round(rbParlourRating.getScore())), edtContactUs.getText().toString()), Rating);

        }

    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case Rating:
                UIHelper.showShortToastInCenter(getDockActivity(), message);
                CompanyDetail entity=(CompanyDetail)result;
                getDockActivity().UpdateCompantData();

                break;
        }
    }

}
