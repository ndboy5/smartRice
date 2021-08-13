package com.raidify.mobi.smartrice.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.raidify.mobi.smartrice.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RiceHistoryAdapter extends RecyclerView.Adapter<RiceHistoryAdapter.ViewHolder> {

LiveData<JSONArray> riceAssetList;
    //Constructor to accept data. Remember to modify to suit answer sheet
    public RiceHistoryAdapter(LiveData<JSONArray> riceList) {
        this.riceAssetList = riceList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView owner;
        private TextView quantity;
        private TextView unitP;
        private TextView harvestDate;
        private TextView transactionStatus;
        private TextView state;

         public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Navigate to the clicked rice asset to view further details
                    Bundle bundle = new Bundle();
                    bundle.putInt("currRice", getAdapterPosition());
                    Navigation.findNavController(view).navigate(R.id.action_inventory_to_inventoryDetail, bundle);
                }
            });
            owner = (TextView) itemView.findViewById(R.id.ownerRowText);
            quantity = (TextView) itemView.findViewById(R.id.quantityRowText);
            unitP = (TextView) itemView.findViewById(R.id.unitPRowText);
             harvestDate = (TextView) itemView.findViewById(R.id.harvestDateRowText);
             transactionStatus = (TextView) itemView.findViewById(R.id.tStatusRowText);
             state = (TextView) itemView.findViewById(R.id.riceStateRowText);
        }
        //create public interface for binding the attributes
        public TextView getOwner(){ return this.owner;}
        public TextView getQuantity(){ return this.quantity;}
        public TextView getUnitP(){ return this.unitP;}
        public TextView getHarvestDate(){ return this.harvestDate;}
        public TextView getTransactionStatus(){ return this.transactionStatus;}
        public TextView getState(){ return this.state;}
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.rice_history_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Position starts from zero
        try {
            holder.getOwner().setText((CharSequence) riceAssetList.getValue().getJSONObject(position).getJSONObject("Record").getString("Owner"));
            holder.getQuantity().setText((CharSequence) riceAssetList.getValue().getJSONObject(position).getJSONObject("Record").getString("Size"));
            Date date=new Date(Long.valueOf(riceAssetList.getValue().getJSONObject(position).getJSONObject("Record").getString("Creation_date")));
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
            String dateText = df2.format(date);
            holder.getHarvestDate().setText(dateText);
            holder.getUnitP().setText((CharSequence) riceAssetList.getValue().getJSONObject(position).getJSONObject("Record").getString("Unit_Price"));
            holder.getState().setText((CharSequence) riceAssetList.getValue().getJSONObject(position).getJSONObject("Record").getString("State"));
            holder.getTransactionStatus().setText((CharSequence) riceAssetList.getValue().getJSONObject(position).getJSONObject("Record").getString("Transaction_Status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return riceAssetList.getValue().length();
    }
}