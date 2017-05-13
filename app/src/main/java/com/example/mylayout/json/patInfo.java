package com.example.mylayout.json;

/**
 * Created by ShadowAnt on 2017/5/13.
 */

//"patIdCard": "1111111",
// "patName": "病人1",
// "patAccount": "P1111111"
public class patInfo {
    public String patIdCard;
    public String patName;
    public String patAccount;

    public String getPatAccount() {
        return patAccount;
    }

    public void setPatAccount(String patAccount) {
        this.patAccount = patAccount;
    }

    public String getPatIdCard() {
        return patIdCard;
    }

    public void setPatIdCard(String patIdCard) {
        this.patIdCard = patIdCard;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }
}
