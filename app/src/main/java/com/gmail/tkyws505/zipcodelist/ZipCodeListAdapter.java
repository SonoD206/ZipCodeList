package com.gmail.tkyws505.zipcodelist;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ZipCodeListAdapter extends RecyclerView.Adapter<ZipCodeListAdapter.ViewHolder> {

  public static final String STREET_NAME_FORMAT = "東京都%s";

  private List<ZipCodeModel> zipCodeModels;

  public ZipCodeListAdapter() {
    this.zipCodeModels = new ArrayList<>();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zip_code_cell, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ZipCodeModel model = zipCodeModels.get(position);
    holder.textViewWardName.setText(String.format(STREET_NAME_FORMAT,model.getWardName()));
    if (!model.getStreetName().equals("")){
      holder.textViewStreetName.setVisibility(View.VISIBLE);
      holder.textViewStreetName.setText(model.getStreetName());
    } else {
      holder.textViewStreetName.setVisibility(View.GONE);
    }
    holder.textViewZipCode.setText(model.getZipCode());
  }

  @Override
  public int getItemCount() {
    return zipCodeModels.size();
  }

  @SuppressLint("NotifyDataSetChanged")
  public void updateModels(List<ZipCodeModel> models) {
    this.zipCodeModels.clear();
    this.zipCodeModels = models;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView textViewStreetName;
    TextView textViewWardName;
    TextView textViewZipCode;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewStreetName = itemView.findViewById(R.id.text_view_street_name);
      textViewWardName = itemView.findViewById(R.id.text_view_ward_name);
      textViewZipCode = itemView.findViewById(R.id.text_view_zip_code);
    }
  }
}
