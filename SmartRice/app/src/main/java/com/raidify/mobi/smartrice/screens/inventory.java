package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.utils.RiceInventoryAdapter;

import org.json.JSONArray;

public class inventory extends Fragment {
    //Declare UI Components
    TextInputEditText searchOwnerMText;
    MaterialButton searchBtn;
    MaterialButton findAvailableRiceBtn;

    private InventoryViewModel mViewModel;
    RecyclerView riceListRecyclerView;
    private RiceInventoryAdapter riceInventoryAdapter;

    public static inventory newInstance() {
        return new inventory();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventory_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        getView().setBackgroundColor(getResources().getColor(R.color.background));
        //locate UI components
        searchOwnerMText = getView().findViewById(R.id.searchRiceMText);
        searchBtn = getView().findViewById(R.id.searchRiceBtn);
        findAvailableRiceBtn = getView().findViewById(R.id.findOpenRiceBtn);
        riceListRecyclerView = getView().findViewById(R.id.riceRecyclerView);

        riceListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //observer for monitoring answers
        final Observer<JSONArray> riceListObserver = new Observer<JSONArray>() {
            @Override
            public void onChanged(JSONArray riceArray) {

                //Setup Recycler View
                riceInventoryAdapter = new RiceInventoryAdapter(mViewModel.getRiceAssetList());
                riceListRecyclerView.setAdapter(riceInventoryAdapter);
            }
        };
        //get Rice List asynchronously from block chain
        mViewModel.getRiceAssetList().observe(getViewLifecycleOwner(), riceListObserver);
        mViewModel.getOpenRiceAssets();

        //Set on Click events for Search buttons
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getAllRiceAssetsOwnedByUser(searchOwnerMText.getText().toString());
            }
        });
        findAvailableRiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getOpenRiceAssets();
            }
        });
    }

}