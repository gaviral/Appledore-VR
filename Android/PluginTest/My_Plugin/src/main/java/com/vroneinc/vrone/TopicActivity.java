package com.vroneinc.vrone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vroneinc.vrone.adapters.MessageAdapter;
import com.vroneinc.vrone.data.ForumPost;
import com.vroneinc.vrone.data.ForumTopic;
import com.vroneinc.vrone.data.ListForumTopic;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vroneinc.vrone.ForumActivity.getForumDatabase;


public class TopicActivity extends Activity {

    private static final String TAG = "TopicActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final int DEFAULT_TITLE_LENGTH_LIMIT = 200;
    public static final int MIN_TITLE_LENGTH = 5;

    public static final int RC_SIGN_IN = 1; //RC = request code
    private static final int RC_PHOTO_PICKER =  2;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private Button mSendButton;
    private EditText mTitleEditText;
    private Button mSetTitleButton;

    private String mUsername;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mForumDatabase;
    private DatabaseReference mTopicDatabase;
    private DatabaseReference mListTopicDatabase;

    private int mTopicType;
    private int mTopicViews;
    private String mTopicId;
    private String mTopicTitle;
    private String mCategory;
    private List<ForumPost> mForumPosts;

    private boolean mTitleSet = false;

    private Intent mIntent;
    private ActionBar mActionBar;
    private ValueEventListener mPostListener;

    private Context mContext;
    private Resources mResources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_activity_main);

        mContext = getApplicationContext();
        mResources = getResources();

        // TODO: change this when sign in is working
        mUsername = ANONYMOUS;

        //Initialize Firebase components
        //This is the main access point to our database
        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mFirebaseAuth = FirebaseAuth.getInstance();

        //This will be used to access specific (messages) part of the database
        mForumDatabase = getForumDatabase();

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        //mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mTitleEditText = (EditText) findViewById(R.id.titleEditText);
        mSetTitleButton = (Button) findViewById(R.id.setTitleButton);

        mActionBar = getActionBar();
        //mActionBar.setBackgroundDrawable(new ColorDrawable(DKGRAY));

        mIntent = getIntent();
        mCategory = mIntent.getStringExtra("Category");
        mTopicType = mIntent.getIntExtra("Type", 0);
        if(mTopicType == 1) {
            mTopicId = mIntent.getStringExtra("Topic");
            mTopicTitle = mIntent.getStringExtra("Title");
            mTopicViews = mIntent.getIntExtra("Views", 0);
            mActionBar.setTitle(getString(R.string.cur_topic_ab, mCategory, mTopicTitle));
            mTopicDatabase = mForumDatabase.child(mCategory).child(mResources.getString(R.string.database_topics)).child(mTopicId);
            mListTopicDatabase = mForumDatabase.child(mCategory).child(mResources.getString(R.string.database_list_topics)).child(mTopicId);
            // Initialize message ListView and its adapter
            mForumPosts = new ArrayList<>();
            mMessageAdapter = new MessageAdapter(this, R.layout.item_message, mForumPosts);
            mMessageListView.setAdapter(mMessageAdapter);
            getForumPosts(new FetchDataCallback() {
                @Override
                public void onDataFetched() {
                    mMessageAdapter.notifyDataSetChanged();
                    // increment the views
                    mListTopicDatabase.child(mResources.getString(R.string.database_views)).setValue(mTopicViews + 1);
                }
            });
        }
        else {
            mActionBar.setTitle(getString(R.string.new_topic_ab, mCategory));
            mSetTitleButton.setVisibility(View.VISIBLE);
            mTitleEditText.setVisibility(View.VISIBLE);
            mTitleEditText.setEnabled(true);
        }

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        // Enable Set Title button when the title length is more than minimum limit
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mActionBar.setTitle(getString(R.string.set_title_ab, mCategory));
                mTitleSet = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mTitleEditText.getText().toString().trim().length() >= MIN_TITLE_LENGTH) {
                    mSetTitleButton.setEnabled(true);
                } else {
                    mSetTitleButton.setEnabled(false);
                }

                mActionBar.setTitle(mActionBar.getTitle() + charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTitleSet = true;
            }
        });
        mTitleEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_TITLE_LENGTH_LIMIT)});


        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageEditText.getText().toString();
                Long timestamp = System.currentTimeMillis();

                // If it is a new topic
                if (mTopicType == 0) {
                    String title = mTitleEditText.getText().toString();
                    if(title.length() >= MIN_TITLE_LENGTH) {
                        // New topic
                        List<ForumPost> topicPosts = new ArrayList<>();
                        ForumTopic forumTopic = new ForumTopic(title, mCategory);
                        forumTopic.openTopic();
                        ForumPost firstPost = new ForumPost(message, timestamp, mCategory, forumTopic.getTopicId());
                        topicPosts.add(firstPost);
                        forumTopic.setPosts(topicPosts);
                        firstPost.writePost();
                        ListForumTopic listForumTopic = new ListForumTopic(forumTopic);
                        listForumTopic.pushListTopic();
                        finish();
                    }
                    else {
                        Toast toast = Toast.makeText(mContext, getString(R.string.no_title_toast, MIN_TITLE_LENGTH), Toast.LENGTH_SHORT);
                        // position the toast to the title edit text
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 400);
                        toast.show();
                    }
                }
                else {
                    // otherwise, just post to the topic
                    ForumPost forumPost = new ForumPost(message, timestamp, mCategory, mTopicId);
                    forumPost.writePost();
                    // we can use the size of the posts array to set the replies
                    // forum posts contain all the posts, so the replies should be
                    // size of array - 1, however, we are posting a new reply, so this works
                    mListTopicDatabase.child(mResources.getString(R.string.database_replies)).setValue(mForumPosts.size());
                    // Clear input box
                    mMessageEditText.setText("");
                }
            }
        });

        mSetTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleSet) {
                    mTitleSet = false;
                    mTitleEditText.clearFocus();
                    hideKeyboard();
                }
                else {
                    mTitleSet = true;
                    mTitleEditText.requestFocus();
                    showKeyboard();
                }
            }
        });
    }

    // Helper methods to hide/show the keyboards
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) TopicActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTitleEditText, InputMethodManager.SHOW_IMPLICIT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.sign_out_menu) {//sign out
           // AuthUI.getInstance().signOut(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() { //Activity in the foreground
        super.onResume();
    }

    @Override
    protected void onPause() { //Activity no longer in the foreground
        super.onPause();


        // Following two lines of code will also make sure that when the activity is destroyed in a
        // way that has nothing to do with signing out such as app rotation that the listener is
        // cleaned up
        //detachDatabaseReadListener();
        //mMessageAdapter.clear();
    }

    private void detachDatabaseReadListener(){
        if(mPostListener != null){
            mForumDatabase.removeEventListener(mPostListener);
            mPostListener = null;
        }
    }

    // Method to fetch the forum posts from the database
    public List<ForumPost> getForumPosts(final FetchDataCallback callback) {
        DatabaseReference posts = mTopicDatabase.child(mResources.getString(R.string.database_posts));

        if (mPostListener != null) {
            posts.removeEventListener(mPostListener);
        }
        mPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!mForumPosts.isEmpty()) {
                    mForumPosts.clear();
                }

                for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                    ForumPost post = post_snapshot.getValue(ForumPost.class);
                    mForumPosts.add(post);
                }

                callback.onDataFetched();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        posts.addValueEventListener(mPostListener);

        return mForumPosts;
    }

}
