package com.congxiaoyao.element;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by congxiaoyao on 2016/5/31.
 */
public class Hockey extends GameElement {

    public Hockey(Body body, Fixture fixture) {
        super(body, fixture);
    }

    public void limit(float velocity) {
        body.setLinearVelocity(body.getLinearVelocity().limit(velocity));
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }
}
