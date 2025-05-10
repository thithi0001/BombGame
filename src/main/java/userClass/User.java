package userClass;

import java.util.*;

import res.LoadResource;

public class User {
    String userName;
    int level;
    int[] lvScore;
    int[] hardLvScore;
    public User() {
        level = 1;
        lvScore = new int[LoadResource.maxMap];
        hardLvScore = new int[LoadResource.maxMap + 1];
    }

    public User(String userName) {
        this.userName = userName;
        level = 1;
        lvScore = new int[LoadResource.maxMap];
        hardLvScore = new int[LoadResource.maxMap + 1];
    }

    public void setLevel(int lv) {
        if (level < lv && lv <= LoadResource.maxMap)
            level = lv;
    }

    public void setScore(int lv, int score) {
        lvScore[lv - 1] = score;
    }

    public void sethardLvScore(int lv, int score){
        hardLvScore[lv -1] = score;
    }

    public int getLevel() {
        return level;
    }

    public int getScoreLv(int lv) {
        return lvScore[lv - 1];
    }

    public int getHardScoreLv(int lv) {
        return hardLvScore[lv - 1];
    }

    public int getScore() {
        int score = 0;
        for (int j : lvScore) {
            score += j;
        }
        return score;
    }

    public int getHardScore() {
        int score = 0;
        for (int j : hardLvScore) {
            score += j;
        }
        return score;
    }

    public String getUserName() {
        return userName;
    }

    public static User getUser(Scanner in) {
        User re = new User(in.next());
        re.level = in.nextInt();
        for (int i = 0; i < re.level; i++) {
            re.lvScore[i] = in.nextInt();
        }
        for (int i = 0; i < re.level; i++) {
            re.hardLvScore[i] = in.nextInt();
        }
        return re;
    }


    @Override
    public String toString() {
        return userName;
    }
}
