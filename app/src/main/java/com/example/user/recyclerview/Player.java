package com.example.user.recyclerview;

public class Player {

    private String name;
    private String imagePath;
    private int games;
    private int scoring;
    private int wins;
    private int losses;
    private int draws;
    private int goals_scored;
    private int goals_got;
    private int difference;
    private String clubPath;
    private int championship;

    public Player() {
    }

    public Player(String name, String imagePath, int games, int scoring, int wins, int losses, int draws, int goals_scored, int goals_got, int difference, String clubPath, int championship) {
        this.name = name;
        this.imagePath = imagePath;
        this.games = games;
        this.scoring = scoring;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.goals_scored = goals_scored;
        this.goals_got = goals_got;
        this.difference = difference;
        this.clubPath = clubPath;
        this.championship = championship;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getGames() {
        return games;
    }

    public int getScoring() {
        return scoring;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getGoals_scored() {
        return goals_scored;
    }

    public int getGoals_got() {
        return goals_got;
    }

    public int getDifference() {
        return difference;
    }

    public String getClubPath() {
        return clubPath;
    }

    public int getChampionship() {
        return championship;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void setScoring(int scoring) {
        this.scoring = scoring;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setGoals_scored(int goals_scored) {
        this.goals_scored = goals_scored;
    }

    public void setGoals_got(int goals_got) {
        this.goals_got = goals_got;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public void setClubPath(String clubPath) {
        this.clubPath = clubPath;
    }

    public void setChampionship(int championship) {
        this.championship = championship;
    }

    public void resetData() {
        this.games = 0;
        this.scoring = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goals_scored = 0;
        this.goals_got = 0;
        this.difference = 0;

    }

    public void gamesUpdate() {
        this.games += 1;
    }

    public void scoringUpdate(int pts) {
        this.scoring += pts;
    }

    public void winsUpdate()
    {
        this.wins+=1;
    }

    public void drawsUpdate()
    {
        this.draws+=1;
    }

    public void lossesUpdate()
    {
        this.losses+=1;
    }

    public void goalsScoredUpdate(int n)
    {
        this.goals_scored+=n;
    }

    public void goalsGotUpdate(int n)
    {
        this.goals_got+=n;
    }

    public void differenceUpdate(int n)
    {
        this.difference+=n;
    }


}



