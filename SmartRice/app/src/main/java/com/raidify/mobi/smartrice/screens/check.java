package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.raidify.mobi.smartrice.R;

public class check extends Fragment {

    private CheckViewModel mViewModel;

    //declare UI Components
    MaterialButton trackBtn;
    TextInputEditText trackRiceIdText;

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
        trackRiceIdText = getView().findViewById(R.id.riceIDMText);
        trackBtn = getView().findViewById(R.id.trackRiceBtn);

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //find details of entered asset from server
                String riceId = trackRiceIdText.getText().toString();
           //     Log.i("ndboy", riceId );
            }
        });
    }

}