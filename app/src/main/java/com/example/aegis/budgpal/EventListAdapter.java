package com.example.aegis.budgpal;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aegis on 3/17/17.
 */

public class EventListAdapter extends ArrayAdapter<Event>{

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Event[] objects) {
        super(context, resource, objects);
    }

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull Event[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }





    @Override
    public View getView(int code, View view, ViewGroup group){
        View horse;
        LayoutInflater hay = LayoutInflater.from(getContext());

        horse = hay.inflate(R.layout.event_list_item, null);

        Event bail = getItem(code);

        TextView first = (TextView)horse.findViewById(R.id.firstText);
        TextView second = (TextView)horse.findViewById(R.id.secondText);
        first.setText(bail.getDescription());
        second.setText(bail.getStartDate());

        return horse;
    }
}
