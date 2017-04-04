package com.vroneinc.vrone.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vroneinc.vrone.R;
import com.vroneinc.vrone.data.ListForumTopic;

import java.util.List;


public class TopicAdapter extends ArrayAdapter<ListForumTopic> {
    private static final int MAX_TOPIC_VIEW_LEN = 60;
    private Resources mResources;

    private static class ViewHolder {
        TextView title;
        TextView timestamp;
        TextView userName;
        TextView replies;
        TextView views;
    }

    public TopicAdapter(Context context, int resource, List<ListForumTopic> objects) {
        super(context, resource, objects);
        mResources = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the event for this position
        ListForumTopic topic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        TopicAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new TopicAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_topic, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.replies = (TextView) convertView.findViewById(R.id.replies);
            viewHolder.views = (TextView) convertView.findViewById(R.id.views);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // view recycled, retrieve view holder object
            viewHolder = (TopicAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(topic.getTitle());
        // check if title is too long, if so truncate it
        truncateText(viewHolder.title, MAX_TOPIC_VIEW_LEN);

        viewHolder.userName.setText(topic.getUserName());
        viewHolder.replies.setText(mResources.getString(R.string.replies_topic_adapter, Integer.toString(topic.getReplies())));
        viewHolder.views.setText(mResources.getString(R.string.views_topic_adapter, Integer.toString(topic.getViews())));

        String timeSpan = DateUtils.getRelativeTimeSpanString(topic.getTimestamp(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();

        viewHolder.timestamp.setText(timeSpan);

        // Return the completed view
        return convertView;
    }


    // Helper method to truncate the length of a textview
    private void truncateText(@Nullable TextView textView, int maxLength) {
        if (textView != null) {
            if (textView.length() > maxLength) {
                String text = (String) textView.getText();
                String newText = text.substring(0, maxLength) + "...";
                textView.setText(newText);
            }
        }
    }

}
