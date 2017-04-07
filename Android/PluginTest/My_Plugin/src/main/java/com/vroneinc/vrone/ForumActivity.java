package com.vroneinc.vrone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vroneinc.vrone.adapters.CategoryAdapter;
import com.vroneinc.vrone.adapters.MessageAdapter;
import com.vroneinc.vrone.adapters.TopicAdapter;
import com.vroneinc.vrone.data.ForumCategory;
import com.vroneinc.vrone.data.ForumTopic;
import com.vroneinc.vrone.data.ListForumTopic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ForumActivity extends Activity {

    private static final String TAG = "ForumActivity";

    private ListView mCategoryListView;
    private ListView mTopicListView;
    private TextView mEmptyList;
    private Button mCreateTopicButton;
    private CategoryAdapter mCategoryAdapter;
    private TopicAdapter mTopicAdapter;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mForumDatabase;
    private ValueEventListener mTopicListener;

    private Context mContext;
    private Resources mResources;

    private ForumCategory mCurCategory;
    private List<ForumCategory> mForumCategories = new ArrayList<>();
    private List<ListForumTopic> mForumTopics = new ArrayList<>();
    private boolean mCategorySelected;

    private ActionBar mActionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity_main);

        mContext = this;
        mResources = getResources();

        // Action bar stuff
        mActionBar = getActionBar();
        mActionBar.setTitle(mResources.getString(R.string.forum_title));
        //mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9800")));

        //Initialize Firebase components
        //This is the main access point to our database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mForumDatabase = mFirebaseDatabase.getReference(mResources.getString(R.string.database_forum));

        mCategoryListView = (ListView) findViewById(R.id.categoryListView);
        mTopicListView = (ListView) findViewById(R.id.topicListView);
        mEmptyList = (TextView) findViewById(R.id.empty);

        mCreateTopicButton = (Button) findViewById(R.id.createTopicButton);

        getForumCategories();

        mCategoryAdapter = new CategoryAdapter(this, R.layout.item_category, mForumCategories);
        mTopicAdapter = new TopicAdapter(this, R.layout.item_topic, mForumTopics);

        mCategoryListView.setAdapter(mCategoryAdapter);
        mTopicListView.setAdapter(mTopicAdapter);

        mCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumCategory category = (ForumCategory) parent.getAdapter().getItem(position);
                mCurCategory = category;
                mCategoryListView.setVisibility(View.GONE);
                mTopicListView.setVisibility(View.VISIBLE);
                // TODO handle empty view
                //mEmptyList.setVisibility(View.VISIBLE);
                //mTopicListView.setEmptyView(mEmptyList);
                mCreateTopicButton.setVisibility(View.VISIBLE);
                mCreateTopicButton.setEnabled(true);
                mCategorySelected = true;
                mActionBar.setTitle(mActionBar.getTitle() + " - " + mCurCategory.getText());

                // Pass in the callback interface to wait until data is fetched
                getForumTopics(new FetchDataCallback() {
                    @Override
                    public void onDataFetched() {
                        mTopicAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        mTopicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListForumTopic forumTopic = (ListForumTopic) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, TopicActivity.class);

                intent.putExtra("Topic", forumTopic.getTopicId());
                intent.putExtra("Title", forumTopic.getTitle());
                intent.putExtra("Category", mCurCategory.getText());
                intent.putExtra("Views", forumTopic.getViews());
                // Type 1 means the topic exists
                intent.putExtra("Type", 1);

                mContext.startActivity(intent);
            }
        });


        // OnClick listener for the button
        mCreateTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopicActivity.class);
                intent.putExtra("Category", mCurCategory.getText());
                // Type 0 means start activity for creation
                intent.putExtra("Type", 0);

                mContext.startActivity(intent);
            }
        });

    }

    public List<ForumCategory> getForumCategories() {
        ForumCategory general = new ForumCategory(mResources.getString(R.string.title_general), mResources.getString(R.string.description_general));
        ForumCategory mnemonics = new ForumCategory(mResources.getString(R.string.title_mnemonics), mResources.getString(R.string.description_mnemonics));
        ForumCategory support = new ForumCategory(mResources.getString(R.string.title_support), mResources.getString(R.string.description_support));
        if (mForumCategories.isEmpty()) {
            mForumCategories.add(general);
            mForumCategories.add(mnemonics);
            mForumCategories.add(support);
        }

        return mForumCategories;
    }

    public List<ListForumTopic> getForumTopics(final FetchDataCallback callback) {
        DatabaseReference topics = mForumDatabase.child(mCurCategory.getText()).child(mResources.getString(R.string.database_list_topics));

        if (mTopicListener != null) {
            topics.removeEventListener(mTopicListener);
        }
        mTopicListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mForumTopics.isEmpty()) {
                    mForumTopics.clear();
                }

                for (DataSnapshot topic_snapshot : dataSnapshot.getChildren()) {
                    ListForumTopic topic = topic_snapshot.getValue(ListForumTopic.class);
                    mForumTopics.add(topic);
                }
                // reverse the order so the most recent is at the top
                Collections.reverse(mForumTopics);

                callback.onDataFetched();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        topics.addValueEventListener(mTopicListener);

        return mForumTopics;
    }

    // Helper to restore the forum view
    private void setCategoryView() {
        mTopicListView.setVisibility(View.GONE);
        // TODO handle empty view
        //mEmptyList.setVisibility(View.VISIBLE);
        //mTopicListView.setEmptyView(mEmptyList);
        mCreateTopicButton.setVisibility(View.GONE);
        mCreateTopicButton.setEnabled(false);
        mCategoryListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mCategorySelected) {
            setCategoryView();
            mCategorySelected = false;
            mActionBar.setTitle(mResources.getString(R.string.forum_title));
        }
        else {
            super.onBackPressed();
        }
    }

    public static DatabaseReference getForumDatabase() {
        return mForumDatabase;
    }

}
