package com.vroneinc.vrone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vroneinc.vrone.adapters.PalaceAdapter;
import com.vroneinc.vrone.data.User;

import java.util.ArrayList;
import java.util.List;

public class PalaceSelectionActivity extends FragmentActivity {
    private Context mContext;
    private Resources mResources;
    private ActionBar mActionBar;

    private ListView mPalaceListView;
    private PalaceAdapter mPalaceAdapter;

    private static String mCurPalaceUserId = null;
    // TODO this would be preferably more than a string
    private List<User> mUserPalaces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palace_selection);

        mContext = getApplicationContext();
        mResources = getResources();

        mActionBar = getActionBar();
        mActionBar.setTitle(mResources.getString(R.string.palace_selection_title_ab));

        mPalaceListView = (ListView) findViewById(R.id.palaceListView);
        mPalaceAdapter = new PalaceAdapter(this, R.layout.item_palace, mUserPalaces);
        mPalaceListView.setAdapter(mPalaceAdapter);

        mPalaceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                mCurPalaceUserId = user.getUid();
                Intent intent = new Intent(PalaceSelectionActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        getUserPalaces(new FetchDataCallback() {
            @Override
            public void onDataFetched() {
                mPalaceAdapter.notifyDataSetChanged();
            }
        });

    }


    public List<User> getUserPalaces(final FetchDataCallback callback) {
        if (!mUserPalaces.isEmpty()) {
            mUserPalaces.clear();
        }
        DatabaseReference users = FirebaseDatabase.getInstance().getReference(mResources.getString(R.string.database_users));
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild(mResources.getString(R.string.database_palace))) {
                        User user = snapshot.getValue(User.class);
                        mUserPalaces.add(user);
                        Log.i("USER ID", user.getUid());
                    }
                }

                callback.onDataFetched();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mUserPalaces;
    }


    // Method for Unity to retrieve the user id
    public static String getCurUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Method for Unity to retrieve the user id
    public static String getCurPalaceUserId() {
        return mCurPalaceUserId;
    }
}
