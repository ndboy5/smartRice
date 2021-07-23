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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.model.Asset;

import java.util.Date;

public class add_rice extends Fragment {

    //UI Component variables
    TextInputEditText idText;
    TextInputEditText quantityText;
    TextInputEditText batchText;
    RadioGroup typeRadioButton;

    MaterialButton submitBtn;

    private AddRiceViewModel mViewModel;
    private final Asset newrice = new Asset();

    public static add_rice newInstance() {
        return new add_rice();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_rice_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddRiceViewModel.class);

        idText = getView().findViewById(R.id.riceIDMText);
        quantityText = getView().findViewById(R.id.quantiyMText);
        batchText = getView().findViewById(R.id.batchNameText);
        typeRadioButton = getView().findViewById(R.id.riceTypeRadioGroup);
        submitBtn = getView().findViewById(R.id.trackRiceBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ndboy", "Test point reached");
                //validate the date entered and send to several
       // if (validateBatchName() && validatePhoneNo() && validateQuantity())
            mViewModel.addNewAsset(getAssetDetails());
                Toast.makeText(getContext(),"Your rice asset ID is 12321231AB. Make a note of this key or save to device", Toast.LENGTH_SHORT);
            }
        });

    }

    private Asset getAssetDetails(){
        long currentDate = new Date().getTime();
        newrice.rice_id = "123456A";
        newrice.source_id = "-";
        newrice.quantity = Double.valueOf(100);
        newrice.owner = idText.getText().toString();
        newrice.riceType = "Ofada";
        newrice.last_owner="-";
        newrice.creation_date = currentDate;
        newrice.last_update_date = currentDate;
        newrice.batchName = batchText.getText().toString();
        newrice.state = "harvested";
        newrice.status = "fresh";
        newrice.farm_location = "221432";

        return this.newrice;
    }

    private boolean validatePhoneNo(){
        //TODO: Validate the user phone number / ID
        return true;
    }
    private boolean validateQuantity(){
        //TODO: Validate the  asset's Quantity
        return true;
    }
    private boolean validateBatchName(){
        //TODO: Validate the asset's Batch name
        return true;
    }
}