package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.utils.Constants;
import com.raidify.mobi.smartrice.utils.RiceHistoryAdapter;
import com.raidify.mobi.smartrice.utils.RiceInventoryAdapter;

import org.json.JSONArray;

public class check extends Fragment {

    private CheckViewModel mViewModel;

    //declare UI Components
    MaterialButton trackBtn;
    TextInputEditText trackRiceIdText;
    RecyclerView riceHistoryRecyclerView;
    RiceHistoryAdapter riceHistoryAdapter;

    public static check newInstance() {
        return new check();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckViewModel.class);
        getView().setBackgroundColor(getResources().getColor(R.color.background));
        trackRiceIdText = getView().findViewById(R.id.riceIDMText);
        trackBtn = getView().findViewById(R.id.trackRiceBtn);
        riceHistoryRecyclerView = getView().findViewById(R.id.trackRecyclerView);

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //find details of entered asset from server
                String riceId = trackRiceIdText.getText().toString();
                mViewModel.getRiceHistoryFromBlockchain(Constants.RICE_ID_PREFIX + riceId);
            }
        });

        riceHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //observer for monitoring answers
        final Observer<JSONArray> riceHistObserver = new Observer<JSONArray>() {
            @Override
            public void onChanged(JSONArray riceArray) {

                //Setup Recycler View
                riceHistoryAdapter = new RiceHistoryAdapter(mViewModel.getRiceHistoryData());
                riceHistoryRecyclerView.setAdapter(riceHistoryAdapter);
            }
        };
        //get Rice List asynchronously from block chain
        mViewModel.getRiceHistoryData().observe(getViewLifecycleOwner(), riceHistObserver);
    }

}