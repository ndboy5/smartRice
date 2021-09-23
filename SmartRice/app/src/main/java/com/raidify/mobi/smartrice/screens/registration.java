package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.model.AccountAsset;
import com.raidify.mobi.smartrice.utils.Constants;
import com.raidify.mobi.smartrice.utils.SessionManager;

import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class registration extends Fragment {

    //UI components
    private TextInputEditText accountIDEditText;
    private TextInputEditText nameEditText;
   private  TextInputEditText surnameEditText;
    private TextInputEditText midnameEditText;
    private RadioGroup roleGroup;
    private ChipGroup passPhraseChipGroup;
    private MaterialButton saveProfileBtn;
    private TextView passPhraseText;

    //UI chips
    Chip chip0; Chip chip1; Chip chip2; Chip chip3; Chip chip4;
    Chip chip5; Chip chip6; Chip chip7; Chip chip8; Chip chip9;

    AccountAsset newAccount = new AccountAsset();
    private RegistrationViewModel mViewModel;
    SessionManager sessionManager;

    Set<String> passPhraseSet = new HashSet<String>();
    private String[] passPhraseString = new String[] {"School", "Computer","Garri", "Oshodi", "Yam", "Tractor",
                                                        "Cutlass", "Effurun", "Bag", "Mature"};
    String phrase = "";
    public static registration newInstance() {
        return new registration();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireActivity());
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        getView().setBackgroundColor(getResources().getColor(R.color.background));
        accountIDEditText = getView().findViewById(R.id.accountIDMText);
        nameEditText = getView().findViewById(R.id.nameMText);
        surnameEditText = getView().findViewById(R.id.surnameMText);
        midnameEditText = getView().findViewById(R.id.otherNameMText);
        passPhraseText = getView().findViewById(R.id.passPhraseText);
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
        roleGroup = getView().findViewById(R.id.roleRadioGroup);

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setAccountAssetDetails();
            //validate data and send to blockchain
                if(isValidEntry()) {
                    mViewModel.sendAccountDetailsToNetwork(newAccount);
                    sessionManager.createLoginSession(newAccount.ID, newAccount.firstName + " " + newAccount.midName+
                            " "+ newAccount.surname, newAccount.role);
                    Toast toast = Toast.makeText(getContext(), "Your new account ID is : " + newAccount.ID +
                            " and your pass phrase: " + phrase, Toast.LENGTH_LONG );
                    toast.show();
                    Navigation.findNavController(getView()).navigate(R.id.action_registration_to_welcome);
                } else {
                    Toast toast = Toast.makeText(getContext(), "Kindly enter the account details correctly", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //Set listeners for all chips
        chip0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[0]);
            }
        });

        chip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[1]);
            }
        });

        chip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[2]);
            }
        });

        chip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[3]);
            }
        });

        chip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[4]);
            }
        });

        chip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[5]);
            }
        });

        chip6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[6]);
            }
        });

        chip7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[7]);
            }
        });

        chip8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                computePassPhraseEntry(passPhraseString[8]);
            }
        });

        chip9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                computePassPhraseEntry(passPhraseString[9]);
            }
        });

    }
//
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
        this.phrase = "";
        for (String word: passPhraseSet){
            this.phrase = this.phrase + word;
        }
        Log.i("ndboy", phrase);
        return this.phrase;
    }

    private void computePassPhraseEntry(String word){
        if (passPhraseSet.contains(word) ) {
            passPhraseSet.remove(word);
        } else passPhraseSet.add(word);
        passPhraseText.setText("Pass Phrase: " + computePassPhrase());

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

    private boolean isValidEntry() {
        //Check for phone number string length
        int phoneNoLength = accountIDEditText.getText().toString().length();
        if (phoneNoLength < 11 || phoneNoLength >= 16) {
            Toast toast = Toast.makeText(getContext(), "Kindly enter a valid phone number", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (nameEditText.getText().toString().length() < 1 || surnameEditText.getText().toString().length() < 1) {

            Toast toast = Toast.makeText(getContext(), "Kindly enter a valid name and surname", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        //Validate the pass phrase
       if(this.phrase.length()<3){
           Toast toast = Toast.makeText(getContext(), "Kindly choose and note your pass-phrase", Toast.LENGTH_SHORT);
           toast.show();
           return false;
       }
        return true;
    }
}