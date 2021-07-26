package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.model.AccountAsset;
import com.raidify.mobi.smartrice.utils.Constants;

import java.util.Date;
import java.util.Random;

public class registration extends Fragment implements View.OnClickListener {

    //UI components
    private TextInputEditText accountIDEditText;
    private TextInputEditText nameEditText;
   private  TextInputEditText surnameEditText;
    private TextInputEditText midnameEditText;
    private RadioGroup roleGroup;
    private ChipGroup passPhraseChipGroup;
    private MaterialButton saveProfileBtn;


    //UI chips
    Chip chip0;
    Chip chip1;
    Chip chip2;
    Chip chip3;
    Chip chip4;
    Chip chip5;
    Chip chip6;
    Chip chip7;
    Chip chip8;
    Chip chip9;

    AccountAsset newAccount = new AccountAsset();
    private RegistrationViewModel mViewModel;
    private boolean[] passPhraseIndex = new boolean[10];
    private String[] passPhraseString = new String[] {"School", "Computer","Garri", "Oshodi", "Yam", "Tractor",
                                                        "Cutlass", "Effurun", "Graduate", "Mature"};
    public static registration newInstance() {
        return new registration();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        accountIDEditText = getView().findViewById(R.id.accountIDMText);
        nameEditText = getView().findViewById(R.id.nameMText);
        surnameEditText = getView().findViewById(R.id.surnameMText);
        midnameEditText = getView().findViewById(R.id.otherNameMText);
       chip0 = getView().findViewById(R.id.chip0);
        chip1 = getView().findViewById(R.id.chip1);
        chip2 = getView().findViewById(R.id.chip2);
        chip3 = getView().findViewById(R.id.chip3);
        chip4 = getView().findViewById(R.id.chip4);
        chip5 = getView().findViewById(R.id.chip5);
        chip6 = getView().findViewById(R.id.chip6);
        chip7 = getView().findViewById(R.id.chip7);
        chip8 = getView().findViewById(R.id.chip8);
        chip9 = getView().findViewById(R.id.chip9);
        saveProfileBtn = getView().findViewById(R.id.saveProfileBtn);

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setAccountAssetDetails();
            //Send to blockchain
            }
        });

        //Set listeners for all chips
        chip0.setOnClickListener(this);
        chip1.setOnClickListener(this);
        chip2.setOnClickListener(this);
        chip3.setOnClickListener(this);
        chip4.setOnClickListener(this);
        chip5.setOnClickListener(this);
        chip6.setOnClickListener(this);
        chip7.setOnClickListener(this);
        chip8.setOnClickListener(this);
        chip9.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.chip0:
                passPhraseIndex[0] = !passPhraseIndex[0];
            case R.id.chip1:
                passPhraseIndex[1] = !passPhraseIndex[1];
            case R.id.chip2:
                passPhraseIndex[2] = !passPhraseIndex[2];
            case R.id.chip3:
                passPhraseIndex[3] = !passPhraseIndex[3];
            case R.id.chip4:
                passPhraseIndex[4] = !passPhraseIndex[4];
            case R.id.chip5:
                passPhraseIndex[5] = !passPhraseIndex[5];
            case R.id.chip6:
                passPhraseIndex[6] = !passPhraseIndex[6];
            case R.id.chip7:
                passPhraseIndex[7] = !passPhraseIndex[7];
            case R.id.chip8:
                passPhraseIndex[8] = !passPhraseIndex[8];
            case R.id.chip9:
                passPhraseIndex[9] = !passPhraseIndex[9];
        }
    }

    private AccountAsset setAccountAssetDetails(){
        newAccount.ID = getSelectedRole() + accountIDEditText.getText();
        newAccount.firstName=nameEditText.getText().toString();
        newAccount.midName = midnameEditText.getText().toString();
        newAccount.surname = surnameEditText.getText().toString();
        newAccount.CreationDate = new Date().getTime();
        newAccount.location = "324125451";
        newAccount.pass_phrase = computePassPhrase();
        newAccount.role = getSelectedRole();

        return this.newAccount;
    }

    private String computePassPhrase(){
        String phrase = "";
        for (int i = 0; i <passPhraseIndex.length ; i++) {
                if(passPhraseIndex[i]) phrase = phrase + passPhraseString[i];
        }
        return phrase;
    }


    private String getSelectedRole(){
        switch(this.roleGroup.getCheckedRadioButtonId()){
            case R.id.farmerBtn:
                return Constants.FARMER_ID_PREFIX;
            case R.id.processorBtn:
                return Constants.PROCESSOR_ID_PREFIX;
            case R.id.wholesalerBtn:
                return Constants.WHOLESALER_ID_PREFIX;
            case R.id.retailerBtn:
                return Constants.RETAILER_ID_PREFIX;
            case R.id.consumerBtn:
                return Constants.CONSUMER_ID_PREFIX;
        }
        return "Unknown";
    }
}