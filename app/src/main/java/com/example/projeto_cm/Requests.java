package com.example.projeto_cm;

import java.util.ArrayList;

public class Requests extends Visits{

    public String userEmail;

    public Requests(){
        super();
    }

    public Requests(String titulo, String descricao, String id, ArrayList<String> images, String location, String userEmail){
        super(titulo, descricao, id, images, location);
        this.userEmail=userEmail;
    }

    public Requests(String titulo, String descricao, String id, String images, String location, String userEmail){
        super(titulo, descricao, id, images, location);
        this.userEmail=userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
