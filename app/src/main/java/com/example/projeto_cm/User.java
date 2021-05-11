package com.example.projeto_cm;

import androidx.annotation.NonNull;

public class User {

    public String name, email;

    public User(){
    }
    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return name+ " " + email;
    }
}
