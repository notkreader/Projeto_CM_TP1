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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
