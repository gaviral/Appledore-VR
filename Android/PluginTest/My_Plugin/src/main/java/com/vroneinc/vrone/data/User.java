package com.vroneinc.vrone.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vroneinc.vrone.R;

/**
 * Created by Emre on 04/04/2017.
 */

public class User {
    private String name;
    private String email;
    private String uid;

    private static final String USERS = "Users";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String UID = "uid";

    public User() { }

    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public User(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public void uploadUser() {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference(USERS);
        // We have to set everything individually rather than push the whole class in case this method is
        // somehow called again, in order to not overwrite other fields that may exist in the database

        // Set the name of the user in the database
        users.child(uid).child(NAME).setValue(name);
        // Set the email of the user in the database
        users.child(uid).child(EMAIL).setValue(email);
        // Set the uid of the user in the database for reference
        users.child(uid).child(UID).setValue(uid);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
