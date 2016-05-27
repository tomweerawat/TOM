package com.raywenderlich.android.arewethereyet.JSON;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import com.raywenderlich.android.arewethereyet.AllGeofencesActivity;
import com.raywenderlich.android.arewethereyet.MapsActivity;
import com.raywenderlich.android.arewethereyet.R;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private ArrayList<AndroidVersion> android;
    private Context context;


    public DataAdapter(ArrayList<AndroidVersion> android) {
        this.android = android;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        final AndroidVersion androidVersion = android.get(i);
        viewHolder.tv_name.setText(android.get(i).getName());

        Log.d("onBindViewHolder","onBindViewHolder");
        viewHolder.tv_version.setText(android.get(i).getVer());
        viewHolder.tv_api_level.setText(android.get(i).getApi());

       /* viewHolder.setClickListener(new ItemClickListener() {
            @Override public void onClickItem(int pos) {
                Log.e("clickitem","clickitem"+getItemId(pos));
                // System.exit(0);
                //Toast.makeText(context, "CLICK : " + androidVersion.getApi(), Toast.LENGTH_SHORT).show();

            }

            @Override public void onLongClickItem(int pos) {
                //Toast.makeText(context, "LONG CLICK : " + androidVersion.getName(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return android.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener, View.OnLongClickListener  {
        private TextView tv_name, tv_version, tv_api_level;
        private ItemClickListener mListener;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_version = (TextView) view.findViewById(R.id.tv_version);
            tv_api_level = (TextView) view.findViewById(R.id.tv_api_level);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        public void setClickListener(ItemClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onClick(View v) {
            Log.d("Click","Click");
            Toast.makeText(v.getContext(), "OnClick Version :", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onLongClickItem(getLayoutPosition());
            return false;
        }
    }
    public interface ItemClickListener {
        void onClickItem(int pos);

        void onLongClickItem(int pos);
    }


}