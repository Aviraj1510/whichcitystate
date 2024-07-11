package com.example.whichcitystate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

public class PostOfficeAdapter extends RecyclerView.Adapter<PostOfficeAdapter.ViewHolder> {

    private ArrayList<JSONObject> postOfficeList;

    public PostOfficeAdapter(ArrayList<JSONObject> postOfficeList) {
        this.postOfficeList = postOfficeList;
    }

    public PostOfficeAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject postOffice = postOfficeList.get(position);
            holder.nameTV.setText(postOffice.getString("Name"));
            holder.stateTV.setText(postOffice.getString("State"));
            holder.districtTV.setText(postOffice.getString("District"));
            holder.countryTV.setText(postOffice.getString("Country"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return postOfficeList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nameTV, stateTV, districtTV, countryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVName);
            stateTV = itemView.findViewById(R.id.idTVState);
            districtTV = itemView.findViewById(R.id.idTVDistrict);
            countryTV = itemView.findViewById(R.id.idTVCountry);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
