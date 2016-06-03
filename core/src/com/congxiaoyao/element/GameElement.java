package com.congxiaoyao.element;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.congxiaoyao.utils.U;

/**
 * Created by congxiaoyao on 2016/5/31.
 */
public class GameElement {

    protected Body body;
    protected Fixture fixture;

    public GameElement(Body body, Fixture fixture) {
        this.body = body;
        this.fixture = fixture;
        body.setAwake(true);
    }

    /**
     * @return box2D中的Body
     */
    public Body getBody() {
        return body;
    }

    /**
     * @return box2D中的Fixture
     */
    public Fixture getFixture() {
        return fixture;
    }

    /**
     * @return 在屏幕中中心点的坐标
     */
    public Vector2 getCenterInScreen() {
        Vector2 center = body.getPosition();
        U.toScreen(center);
        return center;
    }

    /**
     * @return 在box2D中的世界里中心点的坐标
     */
    public Vector2 getCenterInWorld() {
        return body.getPosition();
    }

    /**
     * 设置速度
     * @param velocity
     */
    public void setVelocity(Vector2 velocity) {
        body.setLinearVelocity(velocity);
    }

    public void stopIt() {
        body.setLinearVelocity(0, 0);
    }

    public boolean isMe(Fixture fixture) {
        return this.fixture == fixture;
    }

    public boolean isMeContact(Contact contact) {
        return isMe(contact.getFixtureB()) || isMe(contact.getFixtureA());
    }

}
