package com.app.quico.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.quico.R;
import com.app.quico.entities.AllServicesEnt;
import com.app.quico.entities.ServicesEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.binders.SelectServicesBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.CustomRecyclerView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.Services;

public class SelectServicesFragment extends BaseFragment implements RecyclerClickListner {
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.rv_services)
    CustomRecyclerView rvServices;
    Unbinder unbinder;
    @BindView(R.id.btn_cross)
    ImageView btnCross;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private AreaInterface areaInterface;
    private ArrayList<AllServicesEnt> collection;
    private ArrayList<AllServicesEnt> collectionFiltered;

    //selectAllServices
    private String selectedServices = "";
    private List<String> selectedServicesArray = new ArrayList<>();

    public static SelectServicesFragment newInstance() {
        Bundle args = new Bundle();

        SelectServicesFragment fragment = new SelectServicesFragment();
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
        View view = inflater.inflate(R.layout.fragment_select_services, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void setAreaListner(AreaInterface areaInterface) {
        this.areaInterface = areaInterface;
    }

    public void setSelectedServices(String services) {
        this.selectedServices = services;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (selectedServices != null && !selectedServices.equals("")) {
            selectedServicesArray = Arrays.asList(selectedServices.split("\\s*,\\s*"));
        }

        serviceHelper.enqueueCall(headerWebService.getAllServices(), Services);

        searchListner();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {

            case Services:
                collection = (ArrayList<AllServicesEnt>) result;
                bindData(collection);

                break;

        }
    }

    private void searchListner() {
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
                bindData(getSearchedArray(s.toString()));
            }
        });

        edtSearch.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            UIHelper.hideSoftKeyboard(getDockActivity(), edtSearch);
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });
    }

    public ArrayList<AllServicesEnt> getSearchedArray(String keyword) {
        if (collection == null) {
            return new ArrayList<>();
        }
        if (collection != null && collection.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<AllServicesEnt> arrayList = new ArrayList<>();

        String UserName = "";
        for (AllServicesEnt item : collection) {
            UserName = item.getName();
            if (Pattern.compile(Pattern.quote(keyword.trim().toLowerCase()), Pattern.CASE_INSENSITIVE).matcher(UserName.trim().toLowerCase()).find()) {
                arrayList.add(item);
            }
        }
        return arrayList;

    }

    private void bindData(ArrayList<AllServicesEnt> entity) {
        collectionFiltered = entity;

       /* if (collectionFiltered != null && collectionFiltered.size() > 0 && selectedServicesArray != null && selectedServicesArray.size() > 0) {
            for (AllServicesEnt item : collectionFiltered) {
                for (String selectedItem : selectedServicesArray) {
                    if (String.valueOf(item.getId()).equals(selectedItem)) {
                        item.setSelected(true);
                    }
                }
            }
        }*/

        Collections.sort(collectionFiltered, new Comparator<AllServicesEnt>() {
            @Override
            public int compare(AllServicesEnt list1, AllServicesEnt list2) {
                return list1.getName().toLowerCase().compareTo(list2.getName().toLowerCase());
            }
        });

        if (collectionFiltered != null && collectionFiltered.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            rvServices.setVisibility(View.VISIBLE);

            rvServices.BindRecyclerView(new SelectServicesBinder(getDockActivity(), this), collectionFiltered,
                    new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false)
                    , new DefaultItemAnimator());
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            rvServices.setVisibility(View.GONE);
        }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.select_services));
    }


    @Override
    public void onClick(Object entity, int position) {
        ServicesEnt data = (ServicesEnt) entity;

        if (collectionFiltered.get(position).isSelected()) {
            collectionFiltered.get(position).setSelected(false);
        } else {
            collectionFiltered.get(position).setSelected(true);
        }

        rvServices.notifyItemChanged(position);


    }


    @OnClick({R.id.btn_cross, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cross:
                edtSearch.getText().clear();
                btnCross.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_done:
                ArrayList<String> selectedServicesIdsArray = new ArrayList<>();
                ArrayList<String> selectedServicesNamesArray = new ArrayList<>();



                if (collectionFiltered != null && collectionFiltered.size() > 0) {
                    for (AllServicesEnt item : collectionFiltered) {
                        if (item.isSelected()) {
                            selectedServicesIdsArray.add(item.getId() + "");
                            selectedServicesNamesArray.add(item.getName() + "");
                        }
                    }
                }
                if(selectedServicesIdsArray!=null && selectedServicesIdsArray.size()>10){
                    UIHelper.showShortToastInDialoge(getDockActivity(),getResString(R.string.you_can_not_select_more_than_10_items));
                    return;
                }
                String selectedServicesIds = android.text.TextUtils.join(",", selectedServicesIdsArray);
                String selectedServicesNames = android.text.TextUtils.join(",", selectedServicesNamesArray);
                areaInterface.selectService(selectedServicesIds, selectedServicesNames);
                getDockActivity().popFragment();
                break;
        }
    }
}
