package com.congxiaoyao.stage;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.congxiaoyao.BorderAnimation;
import com.congxiaoyao.InitAnimation;
import com.congxiaoyao.utils.U;

/**
 * Created by congxiaoyao on 2016/5/31.
 */
public class MainStage extends Stage {

    public static final int HOCKEY_CENTER = 0;
    public static final int HOCKEY_TOP = 1;
    public static final int HOCKEY_BOTTOM = 2;

    private Sprite hockey;
    private Sprite playerN;
    private Sprite playerS;
    private Sprite scoreBoardN;
    private Sprite scoreBoardS;
    private Sprite border;

    private Batch batch;
    private Vector2 tempV;

    private int scoreN = 0;
    private int scoreS = 0;

    private InitAnimation initAnimation;
    private BorderAnimation borderAnimation;

    public MainStage() {
        tempV = new Vector2();
        batch = getBatch();

        border = new Sprite(U.Image.BORDER);
        border.setCenter(U.Screen.CENTER_SCREEN.x,U.Screen.CENTER_SCREEN.y);
        border.setScale(U.Screen.SCALE);

        hockey = new Sprite(U.Image.HOCKEY);
        hockey.setScale(U.Screen.SCALE_X);

        playerN = new Sprite(U.Image.PLAYER_NORTH);
        playerN.setScale(U.Screen.SCALE_X);

        playerS = new Sprite(U.Image.PLAYER_SOUTH);
        playerS.setScale(U.Screen.SCALE_X);

        tempV.set(U.Screen.SCORE_BOARD_TOP_POSITION);
        scoreBoardN = new Sprite(U.Image.SCORE(scoreN));
        scoreBoardN.setScale(U.Screen.SCALE_X, U.Screen.SCALE_Y);
        scoreBoardN.setCenter(tempV.x, tempV.y);

        tempV.set(U.Screen.SCORE_BOARD_BOTTOM_POSITION);
        scoreBoardS = new Sprite(U.Image.SCORE(scoreS));
        scoreBoardS.setScale(U.Screen.SCALE_X, U.Screen.SCALE_Y);
        scoreBoardS.setCenter(tempV.x, tempV.y);

        initPlayersPos();
        initHockeyPos(HOCKEY_CENTER);

        initAnimation = new InitAnimation(hockey, playerS, playerN);
        borderAnimation = new BorderAnimation(border);
    }

    @Override
    public void draw() {
        initAnimation.update();
        borderAnimation.update();
        batch.begin();
        batch.draw(U.Image.BACKGROUND, 0, 0, U.S_W, U.S_H);
        hockey.draw(batch);
        playerS.draw(batch);
        playerN.draw(batch);
        border.draw(batch);
        scoreBoardN.draw(batch);
        scoreBoardS.draw(batch);
        batch.end();
    }

    public void updatePlayS(Vector2 pos) {
        playerS.setCenter(pos.x, pos.y);
    }

    public void updatePlayS(float x,float y ) {
        playerS.setCenter(x,y);
    }

    public void updatePlayN(Vector2 pos) {
        playerN.setCenter(pos.x, pos.y);
    }

    public void updatePlayN(float x,float y ) {
        playerS.setCenter(x,y);
    }

    public void updateHockey(float x,float y) {
        hockey.setCenter(x,y);
    }

    public void updateHockey(Vector2 pos) {
        hockey.setCenter(pos.x,pos.y);
    }

    public void initPlayersPos() {
        tempV.set(U.Screen.PLAYER_NORTH_START);
        playerN.setCenter(tempV.x, tempV.y);
        tempV.set(U.Screen.PLAYER_SOUTH_START);
        playerS.setCenter(tempV.x, tempV.y);
    }

    public void initHockeyPos(int posType) {
        if (posType == HOCKEY_TOP) {
            tempV.set(U.Screen.CENTER_TOP_SERVICE_LINE);
        } else if (posType == HOCKEY_BOTTOM) {
            tempV.set(U.Screen.CENTER_BOTTOM_SERVICE_LINE);
        } else {
            tempV.set(U.Screen.CENTER_SCREEN);
        }
        hockey.setCenter(tempV.x, tempV.y);
    }

    public void setScoreS(int scoreS) {
        this.scoreS = scoreS;
        scoreBoardS.setTexture(U.Image.SCORE(scoreS));
    }

    public void setScoreN(int scoreN) {
        this.scoreN = scoreN;
        scoreBoardN.setTexture(U.Image.SCORE(scoreN));
    }

    public boolean isIniting() {
        return initAnimation.isAnimating();
    }

    public void showInitingAnimation() {
        initAnimation.showInitAnimation();
    }

    public void showBorderAnimation(Vector2 velocity) {
        borderAnimation.startAnimation(velocity);
    }

    public InputProcessor getInputProcessor() {
        return initAnimation.getInputProcessor();
    }
}