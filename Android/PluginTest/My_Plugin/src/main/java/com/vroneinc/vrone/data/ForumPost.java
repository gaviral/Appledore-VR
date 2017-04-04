package com.vroneinc.vrone.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vroneinc.vrone.ForumActivity;

import static com.vroneinc.vrone.TopicActivity.ANONYMOUS;

/**
 * Created by Emre on 27/03/2017.
 * General class for a forum post
 */

public class ForumPost {
    private String postId;
    private String userId;
    private String userName;
    private String message;
    private String avatarUrl;
    private Long timestamp;
    private String category;
    private String topicId;

    private static final String TOPICS = "Topics";
    private static final String POSTS = "posts";


    public ForumPost() { } // default constructor

    public ForumPost(String message, Long timestamp, String category) {
        this.message = message;
        this.timestamp = timestamp;
        this.category = category;
    }

    public ForumPost(String message, Long timestamp, String category, String topicId) {
        this.message = message;
        this.timestamp = timestamp;
        this.category = category;
        this.topicId = topicId;
    }

    public FirebaseUser initUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            setUserId(ANONYMOUS);
            setUserName(ANONYMOUS);
            // setAvatarUrl(); TODO: add a template for anonymous avatar
        }
        else {
            setUserId(user.getUid());
            setUserName(user.getDisplayName());
            setAvatarUrl(user.getPhotoUrl().toString());
        }

        return user;
    }

    public ForumPost writePost() {
        DatabaseReference forum = ForumActivity.getForumDatabase();
        initUser();

        String id = forum.child(category).child(TOPICS).child(topicId).child(POSTS).push().getKey();
        setPostId(id);

        forum.child(category).child(TOPICS).child(topicId).child(POSTS).child(postId).setValue(this);

        return this;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setPostId(String postId){
        this.postId = postId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

}
