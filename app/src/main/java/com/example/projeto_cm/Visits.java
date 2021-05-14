package com.example.projeto_cm;

import java.util.ArrayList;

public class Visits {

    public String titulo, descricao, id, location;
    public ArrayList<String> images;
    public int visits;

    public Visits(){
    }

    public Visits(String titulo, String descricao, String id, ArrayList<String> images, String location){
        this.titulo=titulo;
        this.descricao=descricao;
        this.visits=0;
        this.id=id;
        this.images = images;
        this.location = location;
    }

    public Visits(String titulo, String descricao, String id, String image, String location){
        this.titulo=titulo;
        this.descricao=descricao;
        this.visits=0;
        this.id=id;
        this.images = new ArrayList<>();
        this.images.add(image);
        this.location = location;
    }

    public void visit(){
        this.visits++;
    }

    public void addImage(String image){
        this.images.add(image);
    }

}
