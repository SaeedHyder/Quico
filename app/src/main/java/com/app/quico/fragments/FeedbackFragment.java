package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.quico.R;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRatingBar;
import com.app.quico.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

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

    public static FeedbackFragment newInstance() {
        Bundle args = new Bundle();

        FeedbackFragment fragment = new FeedbackFragment();
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
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.rate_review));
    }

    private boolean isvalidated() {
        if (edtContactUs.getText().toString().isEmpty() || edtContactUs.getText().toString().length() < 3) {
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
        if(isvalidated()){
            getDockActivity().popFragment();
        }
    }
}
