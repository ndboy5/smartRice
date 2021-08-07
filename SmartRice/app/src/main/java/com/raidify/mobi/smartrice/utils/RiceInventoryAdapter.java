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

import org.json.JSONArray;
import org.json.JSONException;

import com.raidify.mobi.smartrice.R;
public class RiceInventoryAdapter extends RecyclerView.Adapter<RiceInventoryAdapter.ViewHolder> {

LiveData<JSONArray> riceAssetList;
    //Constructor to accept data. Remember to modify to suit answer sheet
    public RiceInventoryAdapter(LiveData<JSONArray> riceList) {
        this.riceAssetList = riceList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView riceId;
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
            riceId = (TextView) itemView.findViewById(R.id.riceIdRowText);
            owner = (TextView) itemView.findViewById(R.id.ownerRowText);
            quantity = (TextView) itemView.findViewById(R.id.quantityRowText);
            unitP = (TextView) itemView.findViewById(R.id.unitPRowText);
             harvestDate = (TextView) itemView.findViewById(R.id.harvestDateRowText);
             transactionStatus = (TextView) itemView.findViewById(R.id.tStatusRowText);
             state = (TextView) itemView.findViewById(R.id.riceStateRowText);
        }
        //create public interface for binding the attributes
        public TextView getRiceId(){ return this.riceId;}
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
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.rice_row_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Position starts from zero
        try {
            holder.getRiceId().setText((CharSequence) riceAssetList.getValue().getJSONObject(position).getString("ID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return riceAssetList.getValue().length();
    }
}