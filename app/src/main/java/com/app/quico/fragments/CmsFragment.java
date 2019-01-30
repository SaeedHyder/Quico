package com.app.quico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.app.quico.R;
import com.app.quico.entities.Cms;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import static com.app.quico.global.WebServiceConstants.CmsService;

public class CmsFragment extends BaseFragment {
    @BindView(R.id.txt_text)
    AnyTextView txtText;
    Unbinder unbinder;
    private static String title = "";

    public static CmsFragment newInstance(String text) {
        Bundle args = new Bundle();
        title=text;
        CmsFragment fragment = new CmsFragment();
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
        View view = inflater.inflate(R.layout.fragment_cms, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(title.equals(getResString(R.string.TermsCondition))){

            serviceHelper.enqueueCall(headerWebService.Cms("3"), CmsService);

        }else if(title.equals(getResString(R.string.TermsOfService))){
            serviceHelper.enqueueCall(headerWebService.Cms("2"), CmsService);

        }else if(title.equals(getResString(R.string.AboutUs))){
            serviceHelper.enqueueCall(headerWebService.Cms("1"), CmsService);
        }

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(title);
        if(title.equals(getResString(R.string.TermsCondition))){
            titleBar.showBackButton();
        }else{
            titleBar.showMenuButton();
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag){
            case CmsService:
               Cms data=(Cms)result;
               txtText.setText(data.getContent());
                break;
        }
    }
}
