package com.vroneinc.vrone.adapters;

/**
 * Created by avi on 3/20/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vroneinc.vrone.R;
import com.vroneinc.vrone.data.ForumPost;

import java.io.InputStream;
import java.util.List;

import static com.vroneinc.vrone.TopicActivity.ANONYMOUS;


public class MessageAdapter extends ArrayAdapter<ForumPost> {
    private static class ViewHolder {
        TextView userName;
        TextView timestamp;
        TextView post;
        ImageView userPic;
    }

    public MessageAdapter(Context context, int resource, List<ForumPost> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the posts
        ForumPost forumPost = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.post = (TextView) convertView.findViewById(R.id.post);
            viewHolder.userPic = (ImageView) convertView.findViewById(R.id.userPic);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // view recycled, retrieve view holder object
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (forumPost.getUserName().equals(ANONYMOUS)) {
            viewHolder.userPic.setImageResource(R.drawable.anonymous);
        }
        else {
            // This is for loading the user profile picture asynchronously as an avatar
            new DownloadImageTask(viewHolder.userPic).execute(forumPost.getAvatarUrl());
        }

        viewHolder.userName.setText(forumPost.getUserName());
        viewHolder.post.setText(forumPost.getMessage());

        String timeSpan = DateUtils.getRelativeTimeSpanString(forumPost.getTimestamp(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        viewHolder.timestamp.setText(timeSpan);

        // Return the completed view
        return convertView;
    }

    // class for displaying user image asynchronously
    // using Picasso would have been much easier, but it does not work with Unity unfortunately
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
