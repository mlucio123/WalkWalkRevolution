package com.example.cse110_project;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MultiViewTypeAdapter extends RecyclerView.Adapter {

    private ArrayList<TeamateModel>dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;

    public static class AcceptViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;

        public AcceptViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
        }
    }

    public static class PendingViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;

        public PendingViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
        }
    }


    public MultiViewTypeAdapter(ArrayList<TeamateModel>data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TeamateModel.ACCEPT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_type, parent, false);
                return new AcceptViewHolder(view);
            case TeamateModel.PENDING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_type, parent, false);
                return new PendingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return TeamateModel.ACCEPT_TYPE;
            case 1:
                return TeamateModel.PENDING_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        TeamateModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case TeamateModel.ACCEPT_TYPE:
                    ((AcceptViewHolder) holder).txtType.setText(object.text);

                    break;
                case TeamateModel.PENDING_TYPE:
                    ((PendingViewHolder) holder).txtType.setText(object.text);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
