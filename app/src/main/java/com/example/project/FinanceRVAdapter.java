package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FinanceRVAdapter extends RecyclerView.Adapter<FinanceRVAdapter.ViewHolder> {

    // variable for our array list and context
    ArrayList<FinanceModel> financeModelArrayList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        // java variables for text views
        TextView txtAmount, txtCategory, txtDescription, txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialize java variables with xml variables
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }

    // constructor
    public FinanceRVAdapter(ArrayList<FinanceModel> financeModelArrayList, Context context) {
        this.financeModelArrayList = financeModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finance_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Setting data to views of recycler view
        FinanceModel model = financeModelArrayList.get(position);
        //holder.txtAmount.setText(String.valueOf(model.getSpentAmount())); //This converts a double value into a string value but it won't always show 2 decimal values so i stopped using it. However, it may be useful later and therefore is still here
        double amount = model.getSpentAmount();
        Log.d("Debug", "Amount before formatting: " + amount);
        String amountFormatted = String.format("%.2f", amount);
        Log.d("Debug", "Formatted amount: " + amountFormatted);
        holder.txtAmount.setText(amountFormatted);
        holder.txtCategory.setText(model.getCategory());
        holder.txtDescription.setText(model.getDescription());
        holder.txtDate.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        // returning size of array list
        return financeModelArrayList.size();
    }
}