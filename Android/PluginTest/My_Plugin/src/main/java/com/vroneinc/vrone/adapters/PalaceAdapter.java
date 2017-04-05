package com.vroneinc.vrone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vroneinc.vrone.R;
import com.vroneinc.vrone.data.ForumCategory;
import com.vroneinc.vrone.data.User;

import java.util.List;

/**
 * Created by Emre on 04/04/2017.
 */

public class PalaceAdapter extends ArrayAdapter<User> {
    private static class ViewHolder {
        TextView palaceTitle;
        TextView palaceUser;
    }

    public PalaceAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the event for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_palace, parent, false);
            viewHolder.palaceTitle = (TextView) convertView.findViewById(R.id.palaceTitle);
            viewHolder.palaceUser = (TextView) convertView.findViewById(R.id.palaceUser);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // view recycled, retrieve view holder object
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.palaceTitle.setText(user.getUid());
        viewHolder.palaceUser.setText(user.getName());

        // Return the completed view
        return convertView;
    }

}
