package com.app.quico.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.quico.R;
import com.app.quico.entities.Chat.DocumentDetail;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.ui.views.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class PdfReaderFragment extends BaseFragment implements DownloadFile.Listener{
    @BindView(R.id.pdfViewPager)
    PDFViewPager pdfViewPager;
    Unbinder unbinder;

    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;

    private DocumentDetail pdfFile;
    private static String filesKey = "filesKey";

    public static PdfReaderFragment newInstance(DocumentDetail data) {
        Bundle args = new Bundle();
        args.putString(filesKey, new Gson().toJson(data));
        PdfReaderFragment fragment = new PdfReaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(filesKey);
            if (json != null) {
                pdfFile = new Gson().fromJson(json,DocumentDetail.class);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_reader, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(pdfFile!=null) {
            getDockActivity().onLoadingStarted();
            remotePDFViewPager = new RemotePDFViewPager(getDockActivity(), pdfFile.getFileUrl().trim(), this);
        }


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.pdf));
    }


    @Override
    public void onSuccess(String url, String destinationPath) {
        getDockActivity().onLoadingFinished();
        adapter = new PDFPagerAdapter(getDockActivity(), FileUtil.extractFileNameFromURL(url));
        pdfViewPager.setAdapter(adapter);


    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }



}
