package com.app.quico.fragments;


import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.quico.R;
import com.app.quico.entities.CitiesEnt;
import com.app.quico.entities.LocationEnt;
import com.app.quico.entities.ServicesEnt;
import com.app.quico.fragments.abstracts.BaseFragment;
import com.app.quico.helpers.UIHelper;
import com.app.quico.interfaces.AreaInterface;
import com.app.quico.interfaces.RecyclerClickListner;
import com.app.quico.ui.adapters.ArrayListExpandableAdapter;
import com.app.quico.ui.binders.AreaExpendableBinder;
import com.app.quico.ui.views.AnyEditTextView;
import com.app.quico.ui.views.AnyTextView;
import com.app.quico.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.app.quico.global.WebServiceConstants.Cities;

public class SelectAreaFragment extends BaseFragment implements AreaInterface {
    @BindView(R.id.edt_search)
    AnyEditTextView edtSearch;
    @BindView(R.id.elv_area)
    ExpandableListView elvArea;
    Unbinder unbinder;
    @BindView(R.id.btn_cross)
    ImageView btnCross;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;

    private ArrayListExpandableAdapter<CitiesEnt, ArrayList<LocationEnt>> adapter;
    private ArrayList<CitiesEnt> collectionGroup;
    private ArrayList<LocationEnt> collectionChild;
    private HashMap<CitiesEnt, ArrayList<LocationEnt>> listDataChild;
    private AreaInterface areaInterface;

    public static SelectAreaFragment newInstance() {
        Bundle args = new Bundle();
        SelectAreaFragment fragment = new SelectAreaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }


    public void setAreaListner(AreaInterface areaInterface) {
        this.areaInterface = areaInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_area, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.enqueueCall(headerWebService.getCities(), Cities);

        searchListner();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) {
        super.ResponseSuccess(result, Tag, message);
        switch (Tag) {
            case Cities:
                ArrayList<CitiesEnt> data = (ArrayList<CitiesEnt>) result;
                setAreaData(data);
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
                if (s.toString().length() > 0) {
                    setAreaData(getSearchedArray(s.toString()));
                } else {
                    serviceHelper.enqueueCall(headerWebService.getCities(), Cities);
                }
            }
        });

        edtSearch.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            UIHelper.hideSoftKeyboard(getDockActivity(),edtSearch);
                            return true;
                        }
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });
    }


    public ArrayList<CitiesEnt> getSearchedArray(String keyword) {
        if (collectionGroup == null) {
            return new ArrayList<>();
        }
        if (collectionGroup != null && collectionGroup.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<CitiesEnt> arrayList = new ArrayList<>();

        String UserName = "";
        for (CitiesEnt item : collectionGroup) {
            for(LocationEnt areaItem :item.getLocations()){
                UserName = areaItem.getLocation();
                if (Pattern.compile(Pattern.quote(keyword.trim().toLowerCase()), Pattern.CASE_INSENSITIVE).matcher(UserName.trim().toLowerCase()).find()) {
                    arrayList.add(item);
                }
            }

        }
        return arrayList;

    }


    private void setAreaData(ArrayList<CitiesEnt> data) {

        collectionGroup = new ArrayList<>();
        collectionChild = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (CitiesEnt item : data) {
            collectionGroup.add(item);

            for (LocationEnt subCategory : item.getLocations()) {
                collectionChild.add(subCategory);
            }
            listDataChild.put(item, collectionChild);
            collectionChild = new ArrayList<>();
        }

        if (collectionGroup.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            elvArea.setVisibility(View.VISIBLE);

            adapter = new ArrayListExpandableAdapter<>(getDockActivity(), collectionGroup, listDataChild, new AreaExpendableBinder(getDockActivity(), prefHelper, getMainActivity(), this));
            elvArea.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            elvArea.setVisibility(View.GONE);
        }

    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResString(R.string.select_area));
    }


    @OnClick(R.id.btn_cross)
    public void onViewClicked() {
        edtSearch.getText().clear();
        btnCross.setVisibility(View.INVISIBLE);
    }


    @Override
    public void selectArea(Object entity, int position) {
        LocationEnt data = (LocationEnt) entity;
        areaInterface.selectArea( entity, position);
        getDockActivity().popFragment();
    }

    @Override
    public void selectService(String selectedIds, String names) {

    }

}
