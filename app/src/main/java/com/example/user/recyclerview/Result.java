package com.example.user.recyclerview;

public class Result {

    private String namePlayer1;
    private String namePlayer2;
    private int player1Scored;
    private int player2Scored;


    public Result(){}

    public Result(String namePlayer1, String getNamePlayer2, int player1Scored, int player2Scored) {
        this.namePlayer1 = namePlayer1;
        this.namePlayer2 = getNamePlayer2;
        this.player1Scored = player1Scored;
        this.player2Scored = player2Scored;
    }

    public String getNamePlayer1() {
        return namePlayer1;
    }

    public String getNamePlayer2() {
        return namePlayer2;
    }

    public int getPlayer1Scored() {
        return player1Scored;
    }

    public int getPlayer2Scored() {
        return player2Scored;
    }

    public void setNamePlayer1(String namePlayer1) {
        this.namePlayer1 = namePlayer1;
    }

    public void setNamePlayer2(String namePlayer2) {
        this.namePlayer2 = namePlayer2;
    }

    public void setPlayer1Scored(int player1Scored) {
        this.player1Scored = player1Scored;
    }

    public void setPlayer2Scored(int player2Scored) {
        this.player2Scored = player2Scored;
    }
}
