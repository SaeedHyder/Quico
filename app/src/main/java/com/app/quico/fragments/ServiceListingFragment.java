package com.app.quico.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.quico.R;
import com.app.quico.entities.CompanyEnt;
import com.app.quico.entities.ServicesEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.global.AppConstants;
import com.app.quico.helpers.BottomSheetDialogHelper;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.BottomSheetClickListner;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.ServiceListingBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.GetCompanies;

public class ServiceListingFragment extends BaseFragment implements RecyclerClickListner, BottomSheetClickListner {
    @BindView(R.id.rv_serviceDetail)
    CustomRecyclerView rvServiceDetail;
    Unbinder unbinder;
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.btnSort)
    AnyTextView btnSort;
    @BindView(R.id.Main_frame)
    CoordinatorLayout MainFrame;
    @BindView(R.id.btn_cross)
    ImageView btnCross;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayList<CompanyEnt> collection;
    private static String serviceId;
    private static String serviceName;
    private static String cityId;
    private static String areaId;
    private static String latitude;
    private static String longitude;

    private String selectedSortType;

    public static ServiceListingFragment newInstance() {
        Bundle args = new Bundle();
        serviceId = "";
        serviceName = "";
        cityId = "";
        areaId = "";
        latitude = "";
        longitude = "";
        ServiceListingFragment fragment = new ServiceListingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ServiceListingFragment newInstance(String Name, String serviceIdKey, String cityIdKey, String areaIdKey, String latitudeKey, String longitudeKey) {
        Bundle args = new Bundle();
        serviceId = serviceIdKey;
        serviceName = Name;
        cityId = cityIdKey;
        areaId = areaIdKey;
        latitude = latitudeKey;
        longitude = longitudeKey;
        ServiceListingFragment fragment = new ServiceListingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ServiceListingFragment newInstance(String id, String Name) {
        Bundle args = new Bundle();
        serviceId = id;
        serviceName = Name;
        cityId = "";
        areaId = "";
        latitude = "";
        longitude = "";
        ServiceListingFragment fragment = new ServiceListingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static ServiceListingFragment newInstance(String id, String Name, String latitudeKey, String longitudeKey) {
        Bundle args = new Bundle();
        serviceId = id;
        serviceName = Name;
        cityId = "";
        areaId = "";
        latitude = latitudeKey;
        longitude = longitudeKey;
        ServiceListingFragment fragment = new ServiceListingFragment();
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
        View view = inflater.inflate(R.layout.fragment_servie_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (serviceId != null && !serviceId.equals("") && cityId != null && !cityId.equals("")) {
            serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, cityId, areaId, latitude, longitude, 0, 0, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
        } else if (serviceId != null && !serviceId.equals("") && latitude != null && !latitude.equals("")) {
            serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, latitude, longitude, 0, 0, 0, 1,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
        }  else if (serviceId != null && !serviceId.equals("")) {
            serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, 0, 0, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
        }
        searchListner();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LayoutAnimationController anim = AnimationUtils.loadLayoutAnimation(getDockActivity(), R.anim.layout_animation_from_right);
            rvServiceDetail.setLayoutAnimation(anim);
        }

    }


    private void searchListner() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    UIHelper.hideSoftKeyboard(getDockActivity(), edtSearch);
                    return true;
                }
                return false;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch.getText().toString().length() > 0) {
                    btnCross.setVisibility(View.VISIBLE);
                } else {
                    btnCross.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setData(getSearchedArray(s.toString()));
            }
        });
    }

    public ArrayList<CompanyEnt> getSearchedArray(String keyword) {
        if (collection == null) {
            return new ArrayList<>();
        }
        if (collection != null && collection.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<CompanyEnt> arrayList = new ArrayList<>();

        String UserName = "";
        for (CompanyEnt item : collection) {
            UserName = item.getName();
            if (Pattern.compile(Pattern.quote(keyword.trim().toLowerCase()), Pattern.CASE_INSENSITIVE).matcher(UserName.trim().toLowerCase()).find()) {
                arrayList.add(item);
            }
        }
        return arrayList;

    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case GetCompanies:
                collection = (ArrayList<CompanyEnt>) result;
                setData(collection);
                break;
        }
    }

    private void setData(ArrayList<CompanyEnt> entity) {

        if (entity != null && entity.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            rvServiceDetail.setVisibility(View.VISIBLE);

            rvServiceDetail.BindRecyclerView(new ServiceListingBinder(getDockActivity(), prefHelper, this), entity,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                    , new DefaultItemAnimator());
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            rvServiceDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        if (serviceName != null) {
            titleBar.setSubHeading(serviceName);
        } else {
            titleBar.setSubHeading(getResString(R.string.company));
        }

    }


    @Override
    public void onClick(Object entity, int position) {
        CompanyEnt data = (CompanyEnt) entity;
        UIHelper.hideSoftKeyboard(getDockActivity(),edtSearch);

        if (data.getStatus() != 0) {
            getDockActivity().addDockableFragment(ServiceDetailFragment.newInstance(data.getId() + ""), "ServiceDetailFragment");
        }else{
            UIHelper.showShortToastInDialoge(getDockActivity(),getResString(R.string.compnay_is_inactive));
        }

    }


    @OnClick({R.id.btn_cross, R.id.btnSort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cross:
                edtSearch.getText().clear();
                btnCross.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnSort:
                BottomSheetDialogHelper dialoge = new BottomSheetDialogHelper(getDockActivity(), MainFrame, R.layout.bottomsheet_sort, this);
                dialoge.initSortingDialoge(selectedSortType);
                dialoge.showDialog();
                break;
        }
    }

    @Override
    public void onClick(String sortString) {

        selectedSortType=sortString;
        if (sortString.equals(AppConstants.FeaturedSort)) {
            if (serviceId != null && !serviceId.equals("") && cityId != null && !cityId.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, cityId, areaId, latitude, longitude, 1, 0, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else if (serviceId != null && !serviceId.equals("") && latitude != null && !latitude.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, latitude, longitude, 1, 0, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, 1, 0, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            }

        } else if (sortString.equals(AppConstants.NearestSort)) {
            if (serviceId != null && !serviceId.equals("") && cityId != null && !cityId.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, cityId, areaId, latitude, longitude, 0, 0, 0, 1,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else if (serviceId != null && !serviceId.equals("") && latitude != null && !latitude.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, latitude, longitude, 0, 0, 0, 1,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, 0, 0, 0, 1,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            }

        } else if (sortString.equals(AppConstants.RatingSort)) {
            if (serviceId != null && !serviceId.equals("") && cityId != null && !cityId.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, cityId, areaId, latitude, longitude, 0, 1, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else if (serviceId != null && !serviceId.equals("") && latitude != null && !latitude.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, latitude, longitude, 0, 1, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, 0, 1, 0, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            }

        } else if (sortString.equals(AppConstants.ReviewsSort)) {
            if (serviceId != null && !serviceId.equals("") && cityId != null && !cityId.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, cityId, areaId, latitude, longitude, 0, 0, 1, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else if (serviceId != null && !serviceId.equals("") && latitude != null && !latitude.equals("")) {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, latitude, longitude, 0, 0, 1, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            } else {
                serviceHelper.enqueueCall(headerWebService.getCompanies(serviceId, 0, 0, 1, 0,prefHelper.isLanguageArabian()?AppConstants.Arabic:AppConstants.English), GetCompanies);
            }

        }
    }


}
