package com.congxiaoyao.element;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by congxiaoyao on 2016/5/31.
 */
public class Player extends GameElement {

    public static final int TYPE_PLAYER_N = 'N';
    public static final int TYPE_PLAYER_S = 'S';

    private int score = 0;
    private int type = -1;

    public Player(Body body, Fixture fixture, int type) {
        super(body, fixture);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void addScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
