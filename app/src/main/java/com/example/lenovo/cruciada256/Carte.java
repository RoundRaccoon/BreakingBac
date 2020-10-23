package com.example.lenovo.cruciada256;

public class Carte {

    private String image,nume,autor;

    //private String cap,cap_progres;

    public Carte()
    {

    }

    public Carte(String image, String nume, String autor) {
        this.image = image;
        this.nume = nume;
        this.autor = autor;
        //this.cap = cap;
        //this.cap_progres = cap_progres;
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

    /**public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCap_progres() {
        return cap_progres;
    }

    public void setCap_progres(String cap_progres) {
        this.cap_progres = cap_progres;
    }

     */

}
