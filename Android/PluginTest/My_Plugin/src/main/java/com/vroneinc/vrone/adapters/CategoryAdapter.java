package com.vroneinc.vrone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vroneinc.vrone.R;
import com.vroneinc.vrone.data.ForumCategory;

import java.util.List;

/**
 * Created by Emre on 28/03/2017.
 */

public class CategoryAdapter extends ArrayAdapter<ForumCategory> {
    private static class ViewHolder {
        TextView text;
        TextView description;
    }

    public CategoryAdapter(Context context, int resource, List<ForumCategory> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the event for this position
        ForumCategory category = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.category);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // view recycled, retrieve view holder object
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(category.getText());
        viewHolder.description.setText(category.getDescription());

        // Return the completed view
        return convertView;
    }

}
