package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class login extends Fragment {

    private LoginViewModel mViewModel;
    private EditText userIDText;
    private Button signInBtn;
    private Button registerBtn;
    private Button trackBtn;
    private String passPhrase = "";
    private TextView passPhraseMsgText;

    //UI chips
    Chip chip0; Chip chip1; Chip chip2; Chip chip3; Chip chip4;
    Chip chip5; Chip chip6; Chip chip7; Chip chip8; Chip chip9;


    Set<String> passPhraseSet = new HashSet<String>();
    private String[] passPhraseString = new String[] {"School", "Computer","Garri", "Oshodi", "Yam", "Tractor",
            "Cutlass", "Effurun", "Bag", "Mature"};

    SessionManager sessionManager;
    public static login newInstance() {
        return new login();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireActivity());
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    //  automatically navigate to welcome screen if already logged on
        if(mViewModel.isLoggedIn()) Navigation.findNavController(getView()).navigate(R.id.action_login2_to_welcome);

        getView().setBackgroundColor(getResources().getColor(R.color.background));

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
        userIDText = getView().findViewById(R.id.username);
        signInBtn = getView().findViewById(R.id.loginBtn);
        registerBtn = getView().findViewById(R.id.registerBtn);
        trackBtn = getView().findViewById(R.id.trackRiceByIDBtn);
        passPhraseMsgText = getView().findViewById(R.id.passPhraseMesgText);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Find the user registration details
                mViewModel.getAccountDataFromServer(userIDText.getText().toString());
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to Rice tracking screen
                Navigation.findNavController(getView()).navigate(R.id.action_login2_to_check);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Navigate to account registration page
                Navigation.findNavController(getView()).navigate(R.id.action_login2_to_registration);
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
        final Observer<JSONObject> userObserver = new Observer<JSONObject>() {
            @Override
            public void onChanged(@Nullable final JSONObject jsonObject) {
                try {

                    if(computePassPhrase().equals(jsonObject.getString("Pass_phrase"))) {
                        String id = jsonObject.getString("ID");
                        String name = jsonObject.getString("Name");
                        String midName = jsonObject.getString("Midname");
                        String surname = jsonObject.getString("Surname");
                        String role = jsonObject.getString("Role");
                        sessionManager.createLoginSession(id, name + " " +midName+ ""+ surname, role);
                        passPhrase = "";
                       Navigation.findNavController(getView()).navigate(R.id.action_login2_to_welcome);

                    }
                    else{ // Update message on wrong pass phrase
                       passPhraseMsgText.setText("Wrong user name or pass phrase. Try again");
                    };
                } catch (JSONException e) {
                    Log.i("ndboy", "Login Error: " +e.toString());
                }
            }
        };
        mViewModel.getAccountDetail().observe(getViewLifecycleOwner(), userObserver);
    }

    private String computePassPhrase(){
        this.passPhrase = "";
        for (String word: passPhraseSet){
            this.passPhrase = this.passPhrase + word;
        }
        Log.i("ndboy", passPhrase);
        return this.passPhrase;
    }
    private void computePassPhraseEntry(String word){
        if (passPhraseSet.contains(word) ) {
            passPhraseSet.remove(word);
        } else {passPhraseSet.add(word);}

        this.passPhraseMsgText.setText("Pass Phrase: " + this.computePassPhrase());
    }

}