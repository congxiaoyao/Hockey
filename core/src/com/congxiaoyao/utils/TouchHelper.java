package com.congxiaoyao.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * 分析用户输入的点的信息从而确定是哪个player在移动或松手了
 * 这里有playerN 和 playerS的概念，在竖屏情况下
 * playerN代表了北方玩家(上北下南) playerS代表了位于南方的玩家
 *
 * Created by congxiaoyao on 2016/5/29.
 */
public class TouchHelper extends InputAdapter{

    public static final float MIN_X = U.toWorld(U.byStdHor(150));
    public static final float MAX_X = U.toWorld(U.byStdHor(1080-150));
    public static final float MIN_Y = U.toWorld(U.byStdVer(150));
    public static final float MAX_Y = U.toWorld(U.byStdVer(1920-150));
    private static final float CENTER_Y = U.World.CENTER_WORLD.y;

    private int playerNPointer = -1;
    private int playerSPointer = -1;

    private TouchCallback callbackS;
    private TouchCallback callbackN;
    private Runnable releaseS;
    private Runnable releaseN;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer >= 2) return false;
        if (screenY < U.S_H / 2 && playerNPointer == -1){
            playerNPointer = pointer;
            return true;
        }
        if (screenY >= U.S_H / 2 && playerSPointer == -1) {
            playerSPointer = pointer;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 mouse = U.MouseW(pointer);
        if (pointer == playerNPointer) {
            if (callbackN != null) callbackN.playerTouched(limitMouseN(mouse));
            return true;
        }
        if (pointer == playerSPointer) {
            if (callbackS != null) callbackS.playerTouched(limitMouseS(mouse));
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == playerSPointer) {
            if (releaseS != null) releaseS.run();
            playerSPointer = -1;
            return true;
        }
        if(pointer == playerNPointer){
            playerNPointer = -1;
            if (releaseN != null) releaseN.run();
            return true;
        }
        return false;
    }

    private Vector2 limitMouseS(Vector2 mouse) {
        if (mouse.y < MIN_Y) mouse.y = MIN_Y;
        else if (mouse.y > CENTER_Y) mouse.y = CENTER_Y;
        if (mouse.x < MIN_X) mouse.x = MIN_X;
        else if (mouse.x > MAX_X) mouse.x = MAX_X;
        return mouse;
    }

    private Vector2 limitMouseN(Vector2 mouse) {
        if (mouse.y > MAX_Y) mouse.y = MAX_Y;
        else if (mouse.y < CENTER_Y) mouse.y = CENTER_Y;
        if (mouse.x < MIN_X) mouse.x = MIN_X;
        else if (mouse.x > MAX_X) mouse.x = MAX_X;
        return mouse;
    }

    public void notifyTouched() {
        int pointer = 0;
        for (; pointer < 2; pointer++) {
            if (Gdx.input.isTouched(pointer)) {
                touchDragged(Gdx.input.getX(pointer), Gdx.input.getY(pointer), pointer);
            }
        }
    }

    public void clear() {
        playerNPointer = -1;
        playerSPointer = -1;
    }

    public TouchHelper playerSTouched(TouchCallback callbackS) {
        this.callbackS = callbackS;
        return this;
    }

    public TouchHelper playerNTouched(TouchCallback callbackN) {
        this.callbackN = callbackN;
        return this;
    }

    public TouchHelper playerSReleased(Runnable releaseS) {
        this.releaseS = releaseS;
        return this;
    }

    public TouchHelper playerNReleased(Runnable releaseN) {
        this.releaseN = releaseN;
        return this;
    }

    /**
     * 玩家在物理世界中的坐标输入
     */
    public interface TouchCallback {

        /**
         * player在屏幕中点击的位置
         * @param point
         */
        void playerTouched(Vector2 point);
    }
}
