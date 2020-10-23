package com.example.lenovo.cruciada256;

public class Recenzie {

    private String Nume;
    private String image;
    private String Nota;
    private String Parere;
    private String Puncte;

    public Recenzie(){


    }

    public Recenzie(String Nume, String image, String Nota, String Parere, String Puncte)
    {
        this.Nume = Nume;
        this.image = image;
        this.Nota = Nota;
        this.Parere = Parere;
        this.Puncte = Puncte;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private String Date;

    public String getNume() {
        return Nume;
    }

    public void setNume(String nume) {
        Nume = nume;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNota() {
        return Nota;
    }

    public void setNota(String nota) {
        Nota = nota;
    }

    public String getParere() {
        return Parere;
    }

    public void setParere(String parere) {
        Parere = parere;
    }

    public String getPuncte() {
        return Puncte;
    }

    public void setPuncte(String puncte) {
        Puncte = puncte;
    }
}
