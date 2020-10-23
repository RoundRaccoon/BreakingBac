package com.example.lenovo.cruciada256;

public class Profile {
    private String Name,image,reads,followers;

    public Profile(){


    }

    public Profile(String Name,String image,String reads,String followers){
        this.Name = Name;
        this.image = image;
        this.reads = reads;
        this.followers = followers;
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

    public String getFollowers(){
        return followers;
    }

    public void setFollowers(String followers)
    {
        this.followers = followers;
    }

}
