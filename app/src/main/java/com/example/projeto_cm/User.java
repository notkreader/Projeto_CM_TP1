package com.example.projeto_cm;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    public String name, email;
    public boolean isGuide;
    public HashMap<String,Requests> messages;

    public User(){
    }
    public User(String name, String email, boolean isGuide){
        this.name = name;
        this.email = email;
        this.isGuide=isGuide;
        this.messages= new HashMap<>();
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


}