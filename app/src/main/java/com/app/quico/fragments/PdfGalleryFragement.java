package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.quico.R;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.SpacesItemDecoration;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.adapters.CustomImagesAdapter;
import com.app.quico.ui.binders.PdfBinder;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PdfGalleryFragement extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.rv_pdf_files)
    CustomRecyclerView rvPdfFiles;
    Unbinder unbinder;

    private ArrayList<DocumentDetail> pdfFiles;
    private static String filesKey = "filesKey";

    public static PdfGalleryFragement newInstance(ArrayList<DocumentDetail> data) {
        Bundle args = new Bundle();
        args.putString(filesKey, new Gson().toJson(data));
        PdfGalleryFragement fragment = new PdfGalleryFragement();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(filesKey);
            if (json != null) {
                pdfFiles = new Gson().fromJson(json, new TypeToken<List<DocumentDetail>>() {
                }.getType());
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_gallery, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();
    }

    private void setData() {

        if (pdfFiles != null && pdfFiles.size() > 0) {

            rvPdfFiles.BindRecyclerView(new PdfBinder(getDockActivity(), prefHelper, this), pdfFiles,
                    new GridLayoutManager(getDockActivity(), 3)
                    , new DefaultItemAnimator());
        }


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.pdf_files));
    }

    @Override
    public void onClick(Object entity, int position) {

        DocumentDetail data=(DocumentDetail)entity;
        getDockActivity().addDockableFragment(PdfReaderFragment.newInstance(data), "PdfReaderFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
