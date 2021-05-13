package com.example.projeto_cm;

import androidx.annotation.NonNull;

public class User {

    public String name, email;
    public boolean isGuide;

    public User(){
    }
    public User(String name, String email, boolean isGuide){
        this.name = name;
        this.email = email;
        this.isGuide=isGuide;
    }

    @NonNull
    @Override
    public String toString() {
        return name+ " " + email;
    }

    public boolean getIsGuide(){
        return this.isGuide;
    }
}
