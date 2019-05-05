package com.example.quizapp;

public class User {
    String name = "";
    String user_id = "";
    String phy_qno = "";
    String bio_qno = "";
    String che_qno = "";
    String phy_score = "";
    String bio_score = "";
    String che_score = "";
    String rank = "";

    public User(String name, String user_id, String phy_qno, String bio_qno, String che_qno, String rank) {
        this.name = name;
        this.user_id = user_id;
        this.phy_qno = phy_qno;
        this.bio_qno = bio_qno;
        this.che_qno = che_qno;
        this.phy_score = phy_score;
        this.bio_score = bio_score;
        this.che_score = che_score;
        this.rank = rank;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhy_qno() {
        return phy_qno;
    }

    public void setPhy_qno(String phy_qno) {
        this.phy_qno = phy_qno;
    }

    public String getBio_qno() {
        return bio_qno;
    }

    public void setBio_qno(String bio_qno) {
        this.bio_qno = bio_qno;
    }

    public String getChe_qno() {
        return che_qno;
    }

    public void setChe_qno(String che_qno) {
        this.che_qno = che_qno;
    }

    public String getPhy_score() {
        return phy_score;
    }

    public void setPhy_score(String phy_score) {
        this.phy_score = phy_score;
    }

    public String getBio_score() {
        return bio_score;
    }

    public void setBio_score(String bio_score) {
        this.bio_score = bio_score;
    }

    public String getChe_score() {
        return che_score;
    }

    public void setChe_score(String che_score) {
        this.che_score = che_score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", phy_qno='" + phy_qno + '\'' +
                ", bio_qno='" + bio_qno + '\'' +
                ", che_qno='" + che_qno + '\'' +
                ", phy_score='" + phy_score + '\'' +
                ", bio_score='" + bio_score + '\'' +
                ", che_score='" + che_score + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }
}