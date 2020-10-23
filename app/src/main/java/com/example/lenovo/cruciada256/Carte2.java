package com.example.lenovo.cruciada256;

public class Carte2 {

    private String image,nume,autor;

    private String cap;

    public Carte2()
    {

    }

    public Carte2(String image, String nume, String autor,String cap) {
        this.image = image;
        this.nume = nume;
        this.autor = autor;
        this.cap = cap;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

}
