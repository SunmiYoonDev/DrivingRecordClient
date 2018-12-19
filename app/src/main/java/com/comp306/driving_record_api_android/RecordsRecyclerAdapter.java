package com.comp306.driving_record_api_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;

class RecordsRecyclerAdapter extends RecyclerView.Adapter<RecordsRecyclerAdapter.RecordViewHolder> {

    private ArrayList<Record> driving_record;
    public ImageView overflow;
    private Activity mContext;
    private DrivingRecordAPIClient histClient;
    private RecordsRecyclerAdapter adapter;

    public RecordsRecyclerAdapter(ArrayList<Record> driving_record, Activity mContext) {
        this.driving_record = driving_record;
        this.mContext = mContext;
        histClient = DrivingRecordAPIClient.getInstance();
        adapter = this;
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView rid;
        public AppCompatTextView license_id;
        public AppCompatTextView ltype;
        public AppCompatTextView atype;
        public AppCompatTextView date;
        public AppCompatTextView location;
        public Button btn;
        public Button mapBtn;
        public ImageView overflow;

        public RecordViewHolder(View view) {
            super(view);
            rid = (AppCompatTextView) view.findViewById(R.id.rid);
            license_id = (AppCompatTextView) view.findViewById(R.id.license_id);
            ltype = (AppCompatTextView) view.findViewById(R.id.ltype);
            atype = (AppCompatTextView) view.findViewById(R.id.atype);
            date = (AppCompatTextView) view.findViewById(R.id.date);
            location = (AppCompatTextView) view.findViewById(R.id.location);
            btn = (Button) view.findViewById(R.id.delete);
            mapBtn = (Button) view.findViewById(R.id.map_btn);
        }
    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_driving_record, parent, false);
        return new RecordViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecordViewHolder holder, final int position) {
        holder.rid.setText(driving_record.get(position).getRid());
        holder.license_id.setText(driving_record.get(position).getLicenseID());
        holder.ltype.setText(driving_record.get(position).getLtype());
        holder.atype.setText(driving_record.get(position).getaType());
        holder.date.setText(driving_record.get(position).getaDate());
        holder.location.setText(driving_record.get(position).getaLocation());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = holder.rid.getText().toString();
                histClient.deleteData(item, new JsonHttpResponseHandler());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
        holder.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(mContext, MapsActivity.class);
                String item = holder.location.getText().toString();
                String[] locItem = item.split(",");
                mapIntent.putExtra("lat", locItem[0].trim());
                mapIntent.putExtra("long", locItem[1].trim());
                mContext.startActivity(mapIntent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.driving_record.size();
    }
}
