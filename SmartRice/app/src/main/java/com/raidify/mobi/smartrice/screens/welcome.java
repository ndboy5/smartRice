package com.raidify.mobi.smartrice.screens;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.utils.SessionManager;

public class welcome extends Fragment implements View.OnClickListener {

    private WelcomeViewModel mViewModel;
    private MaterialCardView newRiceCard;
    private MaterialCardView tradeRiceCard;
    private MaterialCardView inventoryCard;
    private MaterialCardView trackRiceCard;

    public static welcome newInstance() {
        return new welcome();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.welcome_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);

        //locate the UI components
        newRiceCard = getView().findViewById(R.id.newRiceCard);
        tradeRiceCard = getView().findViewById(R.id.tradeCard);
        inventoryCard = getView().findViewById(R.id.inventoryCard);
        trackRiceCard = getView().findViewById(R.id.trackRiceCard);

       newRiceCard.setOnClickListener(this);
        trackRiceCard.setOnClickListener(this);
        tradeRiceCard.setOnClickListener(this);
        inventoryCard.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newRiceCard:
                //Navigate to the new rice fragment
                Navigation.findNavController(view).navigate(R.id.action_welcome_to_add_rice);
                break;
            case R.id.tradeCard:
                //navigate to the trading/shopping screen
                Navigation.findNavController(view).navigate(R.id.action_welcome_to_trading);
                break;
            case R.id.inventoryCard:
                //
                Navigation.findNavController(view).navigate(R.id.action_welcome_to_inventory);
                break;
            case R.id.trackRiceCard:
                //
                Navigation.findNavController(view).navigate(R.id.action_welcome_to_check);
                break;
        }
    }
}