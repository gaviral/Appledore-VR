package com.vroneinc.vrone.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vroneinc.vrone.ForumActivity;

import static com.vroneinc.vrone.TopicActivity.ANONYMOUS;

/**
 * Created by Emre on 28/03/2017.
 */

public class ListForumTopic {
    private String title;
    private Long timestamp;
    private String topicId;
    private String userName;
    private String category;
    private int replies;
    private int views;

    private static String mType = "ListTopics";

    public ListForumTopic() { }

    public ListForumTopic(String title, Long timestamp, String category, int replies, int views) {
        this.title = title;
        this.timestamp = timestamp;
        this.category = category;
        this.replies = replies;
        this.views = views;
    }

    // new topic pushed
    public ListForumTopic(ForumTopic topic) {
        this.title = topic.getTopicName();
        this.timestamp = topic.getPosts().get(0).getTimestamp();
        this.category = topic.getCategory();
        this.userName = topic.getPosts().get(0).getUserName();
        this.topicId = topic.getTopicId();
        this.replies = 0;
        this.views = 0;
    }

    // This will be called only if constructor with ForumTopic is called
    public ListForumTopic pushListTopic() {
        DatabaseReference forum = ForumActivity.getForumDatabase();

        forum.child(category).child(mType).child(topicId).setValue(this);

        return this;
    }

    public String getTitle() { return title; }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getUserName() { return userName; }

    public String getCategory() { return category; }

    public int getReplies() { return replies; }

    public int getViews() { return views; }

    public void setTitle(String title) { this.title = title; }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setUserName(String userName) { this.userName = userName; }

    public void setCategory(String category) { this.category = category; }

    public void setReplies(int replies) { this.replies = replies; }

    public void setViews(int views) { this.views = views; }

}
