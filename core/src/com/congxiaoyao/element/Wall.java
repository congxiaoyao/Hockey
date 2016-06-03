package com.congxiaoyao.element;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by congxiaoyao on 2016/5/31.
 */
public class Wall extends GameElement {

    public static final int TYPE_L =  0;
    public static final int TYPE_R =  1;
    public static final int TYPE_TL = 2;
    public static final int TYPE_TR = 3;
    public static final int TYPE_BL = 4;
    public static final int TYPE_BR = 5;

    private int type = -1;

    public Wall(Body body, Fixture fixture, int type) {
        super(body, fixture);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
