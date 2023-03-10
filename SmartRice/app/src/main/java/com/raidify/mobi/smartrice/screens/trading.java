package com.raidify.mobi.smartrice.screens;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raidify.mobi.smartrice.R;
import com.raidify.mobi.smartrice.model.RiceAsset;
import com.raidify.mobi.smartrice.utils.Constants;
import com.raidify.mobi.smartrice.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class trading extends Fragment {
    //UI components
    MaterialButton findBtn;
    MaterialButton allBtn;
    MaterialButton sellBtn;
    MaterialButton releaseBtn;
    MaterialButton buyBtn;
    TextInputEditText riceIDText;
    TextInputEditText buyAmmountText;
    TextView idText;
    TextView riceTypeText;
    TextView ownerText;
    TextView amountText;
    TextView batchNameText;
    TextView farmLocText;
    TextView harvestDateText;
    TextView statusText;
    TextView cycleStageText;
    TextView unitPriceText;
    TextView transStateText;
    LinearLayout riceDetailLayout;

    private TradingViewModel mViewModel;
    SessionManager sessionManager;
    RiceAsset sourceRiceAsset = new RiceAsset();

    public static trading newInstance() {
        return new trading();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        return inflater.inflate(R.layout.trading_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TradingViewModel.class);
        getView().setBackgroundColor(getResources().getColor(R.color.background));
        findBtn = getView().findViewById(R.id.searchRiceBtn);
        allBtn = getView().findViewById(R.id.allBtn);
        sellBtn = getView().findViewById(R.id.signOutBtn);
        buyBtn = getView().findViewById(R.id.buyRiceBtn);
        releaseBtn = getView().findViewById(R.id.releaseRiceBtn);
        riceIDText = getView().findViewById(R.id.riceIDMText);
        buyAmmountText = getView().findViewById(R.id.buyAmountMText);
        idText = getView().findViewById(R.id.IDEditText);
        riceTypeText = getView().findViewById(R.id.riceTypeText);
        ownerText = getView().findViewById(R.id.ownerEditText);
        amountText = getView().findViewById(R.id.quantityEditText);
        batchNameText = getView().findViewById(R.id.batchNameEditText);
        farmLocText = getView().findViewById(R.id.farmLOCText);
        harvestDateText = getView().findViewById(R.id.creationDateEditText);
        statusText = getView().findViewById(R.id.statusEditText);
        cycleStageText = getView().findViewById(R.id.stateEditText);
        unitPriceText = getView().findViewById(R.id.unitPriceEditText);
        transStateText = getView().findViewById(R.id.transStatusEditText);

       riceDetailLayout = getView().findViewById(R.id.riceDetailLayout);
       findBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mViewModel.getRiceDetailById(Constants.RICE_ID_PREFIX + riceIDText.getText().toString());
           }
       });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double buyQuantity = Double.valueOf(buyAmmountText.getText().toString());
                Double sellQuantity = Double.valueOf(amountText.getText().toString());

                if(sellQuantity>0 && isAuthorisedToBuy()){ //to handle less than zero quantities
                        if(buyQuantity<sellQuantity) {
                            //Save the source asset details before update
                            saveSourceRiceAssetDetails();
                        //Create new asset and save on server
                        Random rand = new Random();
                        String newID = Constants.RICE_ID_PREFIX + String.valueOf(rand.nextInt(Constants.RANDOM_NUM_UPPER_BOUND));
                        mViewModel.buyRicePortion(newID, sessionManager.getUserId(), buyQuantity);
                        Toast.makeText(getContext(), "Rice with the new ID: " + newID

                                + " pending release by seller", Toast.LENGTH_LONG).show();
                            //TODO: Decrement the quantity of the former asset here
                        mViewModel.decreaseQuantityOfSourceAsset(sourceRiceAsset,  sellQuantity - buyQuantity);

                    } else if(buyQuantity.equals(sellQuantity)) {
                            mViewModel.buyRiceAll(sessionManager.getUserId());
                            try {
                                Toast.makeText(getContext(), "Rice " + mViewModel.getRiceDetail().getValue().getString("ID")
                                        + " pending release by seller", Toast.LENGTH_SHORT).show();
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }
                    else{
                        Toast.makeText(getContext(),"Please review your buy quantity", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(),"Please review your buy quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getRiceDetailById(Constants.RICE_ID_PREFIX +riceIDText.getText().toString());
                if(isAuthorisedToSell()){
                    mViewModel.sellRice();
                    try {
                        Toast.makeText(getContext(), "Rice " + mViewModel.getRiceDetail().getValue().getString("ID")
                                + " opened for sale", Toast.LENGTH_SHORT).show();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }
        });


        releaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getRiceDetailById(Constants.RICE_ID_PREFIX +riceIDText.getText().toString());
                if(isAuthorisedToRelease()){
                    mViewModel.releaseRice();
                    try {
                        Toast.makeText(getContext(), "Rice " + mViewModel.getRiceDetail().getValue().getString("ID")
                                + " released to buyer", Toast.LENGTH_SHORT).show();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyAmmountText.setText(amountText.getText());
            }
        });

        final Observer<JSONObject> riceObserver = new Observer<JSONObject>() {
            @Override
            public void onChanged(@Nullable final JSONObject jsonObject) {
                try {
                    idText.setText(jsonObject.getString("ID"));
                    ownerText.setText(jsonObject.getString("Owner"));
                    riceTypeText.setText(jsonObject.getString("Rice_Type"));
                    amountText.setText(jsonObject.getString("Size"));
                    batchNameText.setText(jsonObject.getString("Batch_name"));
                    farmLocText.setText(jsonObject.getString("Farm_location"));
                    harvestDateText.setText(jsonObject.getString("Creation_date"));
                    statusText.setText(jsonObject.getString("Status"));
                    cycleStageText.setText(jsonObject.getString("State"));
                    unitPriceText.setText(jsonObject.getString("Unit_Price"));
                    transStateText.setText(jsonObject.getString("Transaction_Status"));
                } catch (JSONException e) {
                   Log.i("ndboy", "Error: " + e.toString());
                }
            }
        };
        mViewModel.getRiceDetail().observe(getViewLifecycleOwner(), riceObserver);
    }

    private boolean isAuthorisedToSell(){
        String assetOwner = "";
        try {
            assetOwner = mViewModel.getRiceDetail().getValue().getString("Owner");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (assetOwner.equals(sessionManager.getUserId())) return true;
        Toast.makeText(getContext(), "You are not authorised to sell this rice", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isAuthorisedToBuy(){
        String trans_status="";

        try {
            trans_status = mViewModel.getRiceDetail().getValue().getString("Transaction_Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (trans_status.equals("open")) return true;
        Toast.makeText(getContext(), "This asset is not available for purchase", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isAuthorisedToRelease(){

        String trans_status="";
        String last_owner="";
        try {
            trans_status = mViewModel.getRiceDetail().getValue().getString("Transaction_Status");
            last_owner = mViewModel.getRiceDetail().getValue().getString("Last_owner");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (trans_status.equals("pending") && last_owner.equals(sessionManager.getUserId())) return true;
        Toast.makeText(getContext(), "You are not authorised to release this Rice", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void saveSourceRiceAssetDetails(){
       JSONObject  riceJSONData = mViewModel.getRiceDetail().getValue();
        try {
            this.sourceRiceAsset.rice_id = riceJSONData.getString("ID");
            this.sourceRiceAsset.source_id = riceJSONData.getString("Source_ID");
            this.sourceRiceAsset.batchName = riceJSONData.getString("Batch_name");
            this.sourceRiceAsset.creation_date = Long.valueOf(riceJSONData.getString("Creation_date"));
            this.sourceRiceAsset.owner= riceJSONData.getString("Owner");
            this.sourceRiceAsset.last_owner= riceJSONData.getString("Last_owner");
            this.sourceRiceAsset.riceType= riceJSONData.getString("Rice_Type");
            this.sourceRiceAsset.farm_location= riceJSONData.getString("Farm_location");
            this.sourceRiceAsset.last_update_date = Long.valueOf(riceJSONData.getString("Last_update_date"));
            this.sourceRiceAsset.state= riceJSONData.getString("State");
            this.sourceRiceAsset.status= riceJSONData.getString("Status");
            this.sourceRiceAsset.trans_status= riceJSONData.getString("Transaction_Status");
            this.sourceRiceAsset.unitP = Double.valueOf(riceJSONData.getString("Unit_Price"));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }


}