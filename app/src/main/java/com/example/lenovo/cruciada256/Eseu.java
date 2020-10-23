package com.example.lenovo.cruciada256;

public class Eseu {

    public Eseu(){

    }

    public Eseu(String titlu,String pdfdown,String likes,String downs)
    {

        this.titlu = titlu;
        this.pdfdown = pdfdown;
        this.likes = likes;
        this.downs = downs;

    }

    private String titlu,pdfdown,likes,downs;

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getPdfdown() {
        return pdfdown;
    }

    public void setPdfdown(String pdfdown) {
        this.pdfdown = pdfdown;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDowns() {
        return downs;
    }

    public void setDowns(String downs) {
        this.downs = downs;
    }
}
