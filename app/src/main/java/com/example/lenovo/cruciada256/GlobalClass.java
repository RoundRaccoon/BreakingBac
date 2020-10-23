package com.example.lenovo.cruciada256;

import android.app.Application;

public class GlobalClass extends Application {

    private String CurrentBookID;

    public String getCurrentBookID() {
        return CurrentBookID;
    }

    public void setCurrentBookID(String currentBookID) {
        CurrentBookID = currentBookID;
    }

    public String getCurrentBookRecStar() {
        return CurrentBookRecStar;
    }

    public void setCurrentBookRecStar(String currentBookRecStar) {
        CurrentBookRecStar = currentBookRecStar;
    }

    private String CurrentBookRecStar;

    public String getCurrentBookParere() {
        return CurrentBookParere;
    }

    public void setCurrentBookParere(String currentBookParere) {
        CurrentBookParere = currentBookParere;
    }

    private String CurrentBookParere;

    public String getCurrentProfileVisit() {
        return CurrentProfileVisit;
    }

    public void setCurrentProfileVisit(String currentProfileVisit) {
        CurrentProfileVisit = currentProfileVisit;
    }

    private String CurrentProfileVisit;

    public String getCurrentEseuTip() {
        return CurrentEseuTip;
    }

    public void setCurrentEseuTip(String currentEseuTip) {
        CurrentEseuTip = currentEseuTip;
    }

    private String CurrentEseuTip;


}
