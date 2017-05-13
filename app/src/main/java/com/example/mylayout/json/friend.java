package com.example.mylayout.json;

/**
 * Created by ShadowAnt on 2017/5/13.
 */
//"docName": "林涛",
//"docId": "340621199604270312",
//"docAccount": "E41414049"
public class friend {

    public String docName;
    public String docId;
    public String docAccount;

    public friend(String docName, String docId, String docAccount){
        this.docName = docName;
        this.docId = docId;
        this.docAccount = docAccount;
    }

    public String getDocAccount() {
        return docAccount;
    }

    public void setDocAccount(String docAccount) {
        this.docAccount = docAccount;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }


}
