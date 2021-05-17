package com.example.projeto_cm;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    public String name, email;
    public boolean isGuide;
    public HashMap<String,Requests> messages;
    public String uid;

    public User(){
    }
    public User(String name, String email, boolean isGuide, String uid){
        this.name = name;
        this.email = email;
        this.isGuide=isGuide;
        this.messages= new HashMap<>();
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString() {
        return name+ " " + email;
    }

    public boolean getIsGuide(){
        return this.isGuide;
    }

    public void addMessage(String str,Requests requests) {
        this.messages.put(str,requests);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGuide() {
        return isGuide;
    }

    public void setGuide(boolean guide) {
        isGuide = guide;
    }

    public HashMap<String, Requests> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, Requests> messages) {
        this.messages = messages;
    }
}