package com.amit7itz.motivator.motivator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.AppDatabase;

import java.util.List;

public class ActivityTypesAdapter extends RecyclerView.Adapter<ActivityTypesAdapter.ViewHolder> {
    private AppDatabase db;
    private List<ActivityType> alertTypes;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout container;
        public TextView activityName, activityDesctiption, activityReward;
        public ImageView majorIcon;
        public ViewHolder(RelativeLayout v) {
            super(v);
            activityName = v.findViewById(R.id.activity_type_name);
            activityDesctiption = v.findViewById(R.id.activity_type_description);
            activityReward = v.findViewById(R.id.activity_type_reward);
            majorIcon = v.findViewById(R.id.major_icon);
            container = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ActivityTypesAdapter(AppDatabase db) {
        this.db = db;
        this.alertTypes = this.db.activityTypeDao().getAll();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ActivityTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_type, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ActivityType at = this.alertTypes.get(position);
        holder.activityName.setText(String.format("%s", at.getName()));
        holder.activityDesctiption.setText(String.format("%s", at.getDescription()));
        holder.activityReward.setText(String.format("%s", at.getReward()));
        if (! at.getMajor()) {
            holder.majorIcon.setVisibility(View.INVISIBLE);
        }
        holder.container.setTag(at.getId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return alertTypes.size();
    }
}