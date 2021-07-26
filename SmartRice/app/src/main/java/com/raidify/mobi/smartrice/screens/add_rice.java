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
import com.raidify.mobi.smartrice.model.RiceAsset;
import com.raidify.mobi.smartrice.utils.Constants;

import java.util.Date;
import java.util.Random;

public class add_rice extends Fragment {

    //UI Component variables
    TextInputEditText idText;
    TextInputEditText quantityText;
    TextInputEditText batchText;
    RadioGroup typeRadioButton;

    MaterialButton submitBtn;

    private AddRiceViewModel mViewModel;
    private final RiceAsset newrice = new RiceAsset();

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

    private RiceAsset getAssetDetails(){

        long currentDate = new Date().getTime();
        // Let upper bound for randomly generated number be used
        Random random = new Random();
        newrice.rice_id = Constants.RICE_ID_PREFIX + String.valueOf(random.nextInt(Constants.RANDOM_NUM_UPPER_BOUND));

        newrice.source_id = "-";
        newrice.quantity = Double.valueOf(quantityText.getText().toString());
        newrice.owner = idText.getText().toString();
        newrice.riceType = getSelectedRiceType();
        newrice.last_owner="-";
        newrice.creation_date = currentDate;
        newrice.last_update_date = currentDate;
        newrice.batchName = batchText.getText().toString();
        newrice.state = "harvested";
        newrice.status = "fresh";
        newrice.farm_location = "221432";

        return this.newrice;
    }

    //Returns a string description for the rice the user selected on the UI
    private String getSelectedRiceType() {
        switch (this.typeRadioButton.getCheckedRadioButtonId()){
            case R.id.ofadaRiceRBtn:
                return "Ofada";
            case R.id.long_grainRBtn:
                return "Long Grain";
            case R.id.brownRiceRBtn:
                return "Brown Rice";
            case R.id.short_grainRBtn:
                return "Short Grain Rice";

            default:
                return "Unknown";
        }
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