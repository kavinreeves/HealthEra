package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import healthera.quiz.kavin.healthera.R;
import model.Pharmacy;

/**
 * Created by Kavin on 17/08/17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Pharmacy> mItems;

    public CardAdapter(List<Pharmacy> mItems) {
        this.mItems = mItems;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Pharmacy pharmacy = mItems.get(i);
        viewHolder.name.setText(pharmacy.getName());
        viewHolder.distance.setText(pharmacy.getDistance());
        viewHolder.id.setText(pharmacy.getId());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView distance;
        public TextView id;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            distance = (TextView) itemView.findViewById(R.id.distance);
            id = (TextView) itemView.findViewById(R.id.pharm_id);

        }
    }
}

