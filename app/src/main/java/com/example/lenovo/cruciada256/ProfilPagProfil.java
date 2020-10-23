package com.example.lenovo.cruciada256;

public class ProfilPagProfil {

    private String Name,image,reads;

    public ProfilPagProfil()
    {

    }

    public ProfilPagProfil(String Name,String image,String reads)
    {

        this.Name = Name;
        this.image = image;
        this.reads = reads;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReads() {
        return reads;
    }

    public void setReads(String reads) {
        this.reads = reads;
    }
}
