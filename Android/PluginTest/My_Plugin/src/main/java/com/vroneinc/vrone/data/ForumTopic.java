package com.vroneinc.vrone.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vroneinc.vrone.ForumActivity;

import java.util.List;

/**
 * Created by Emre on 27/03/2017.
 * Special class for a Forum Topic (handle database stuff differently)
 */

public class ForumTopic {
    private String topicName;
    private String topicId;
    private String category;
    private List<ForumPost> posts;

    private static String mType = "Topics";

    public ForumTopic() { }

    public ForumTopic(String topicName, String topicId, String category, List<ForumPost> posts) {
        this.topicName = topicName;
        this.topicId = topicId;
        this.category = category;
        this.posts = posts;
    }

    public ForumTopic(String topicName, String category, List<ForumPost> posts) {
        this.topicName = topicName;
        this.posts = posts;
        this.category = category;
    }

    public ForumTopic(String topicName, String category) {
        this.topicName = topicName;
        this.category = category;
    }

    public ForumTopic openTopic() {
        DatabaseReference forum = ForumActivity.getForumDatabase();

        String category = getCategory();
        String id = forum.child(category).push().getKey();
        setTopicId(id);
        //setPosts(getPosts());

        forum.child(category).child(mType).child(getTopicId()).setValue(this);

        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicId() { return topicId; }

    public String getCategory() { return category; }

    public List<ForumPost> getPosts() {
        return posts;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicId(String topicId) { this.topicId = topicId; }

    public void setCategory(String category) { this.category = category; }

    public void setPosts(List<ForumPost> posts) {
        this.posts = posts;
    }

}
