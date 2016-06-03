package com.congxiaoyao;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.congxiaoyao.element.GameElement;
import com.congxiaoyao.element.Hockey;
import com.congxiaoyao.element.Player;
import com.congxiaoyao.element.Wall;
import com.congxiaoyao.utils.ContactAdapter;
import com.congxiaoyao.utils.TouchHelper;
import com.congxiaoyao.utils.U;

/**
 * Created by congxiaoyao on 2016/5/31.
 */
public class WorldHolder{

    private World world;

    private Hockey hockey;
    private Player playerN;
    private Player playerS;
    private Wall[] walls = new Wall[6];
    private GameElement[] balls = new GameElement[3];

    private BodyDef wallBodyDef;
    private BodyDef ballBodyDef;

    private FixtureDef wallFixDef;
    private FixtureDef ballFixDef;

    private PolygonShape wallShape;
    private CircleShape circleShape;

    private boolean isOutSide = false;
    private Runnable outsideNorth;
    private Runnable outsideSouth;
    private TouchHelper touchHelper;
    private CollisionListener collisionListener;
    private CrashWallListener crashWallListener;

    public WorldHolder() {
        world = new World(new Vector2(0, 0), true);
        initWalls();
        initBalls();
        circleShape.dispose();
        wallShape.dispose();

        touchHelper = new TouchHelper();
        touchHelper.playerSTouched(point -> playerS.setVelocity(point.sub(playerS.getCenterInWorld()).scl(50)))
                .playerNTouched(point -> playerN.setVelocity(point.sub(playerN.getCenterInWorld()).scl(50)))
                .playerSReleased(() -> playerS.stopIt())
                .playerNReleased(() -> playerN.stopIt());

        world.setContactListener(new ContactAdapter(){
            @Override
            public void beginContact(Contact contact) {
                Vector2[] points = contact.getWorldManifold().getPoints();
                if(collisionListener != null) {
                    collisionListener.onCollision(points[0]);
                }
                if (crashWallListener != null && hockey.isMeContact(contact)
                        && getContactWall(contact) != null) {
                    crashWallListener.onCrash(hockey.getVelocity());
                }
            }
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                if (hockey.isMeContact(contact) && getContactWall(contact) != null) {
                    Vector2 velocity = hockey.getVelocity();
                    if (velocity.x < 0.000001f || velocity.y < 0.000001f) {
                        hockey.getBody().applyLinearImpulse(velocity.scl(10000000)
                                .rotate(-45).limit(0.5f),
                                hockey.getCenterInWorld(), true);
                    }
                }
            }

            public Wall getContactWall(Contact contact) {
                for (Wall wall : walls) {
                    if (wall.isMe(contact.getFixtureA()) ||
                            wall.isMe(contact.getFixtureB())) {
                        return wall;
                    }
                }
                return null;
            }
        });
    }

    /**
     * 初始化六面墙
     */
    private void initWalls() {

        wallShape = new PolygonShape();
        wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBodyDef.position.set(0, 0);
        wallFixDef = new FixtureDef();
        wallFixDef.density = 0;
        wallFixDef.restitution = 0f;
        wallFixDef.friction = 0f;
        wallFixDef.shape = wallShape;

        //定义六面墙
        Body tempBody = createWallBody(U.World.WALL_LEFT_CENTER);
        walls[0] = new Wall(tempBody, createVerticalWallFixture(tempBody), Wall.TYPE_L);
        tempBody = createWallBody(U.World.WALL_RIGHT_CENTER);
        walls[1] = new Wall(tempBody, createVerticalWallFixture(tempBody), Wall.TYPE_R);
        tempBody = createWallBody(U.World.WALL_TOP_LEFT_CENTER);
        walls[2] = new Wall(tempBody, createWallHorizontalFixture(tempBody), Wall.TYPE_TL);
        tempBody = createWallBody(U.World.WALL_TOP_RIGHT_CENTER);
        walls[3] = new Wall(tempBody, createWallHorizontalFixture(tempBody), Wall.TYPE_TR);
        tempBody = createWallBody(U.World.WALL_BOTTOM_LEFT_CENTER);
        walls[4] = new Wall(tempBody, createWallHorizontalFixture(tempBody), Wall.TYPE_BL);
        tempBody = createWallBody(U.World.WALL_BOTTOM_RIGHT_CENTER);
        walls[5] = new Wall(tempBody, createWallHorizontalFixture(tempBody), Wall.TYPE_BR);
    }

    /**
     * 初始化PlayerN PlayerS Hockey
     */
    private void initBalls() {
        circleShape = new CircleShape();
        circleShape.setPosition(Vector2.Zero);
        ballBodyDef = new BodyDef();
        ballBodyDef.type = BodyDef.BodyType.DynamicBody;
        ballFixDef = new FixtureDef();
        ballFixDef.friction = 0f;
        ballFixDef.restitution = 1f;
        ballFixDef.density = 0.5f;
        ballFixDef.shape = circleShape;

        //定义hockey
        circleShape.setRadius(U.World.HOCKEY_RADIUS);
        ballBodyDef.position.set(U.World.CENTER_WORLD);
        ballBodyDef.linearDamping = 0.6f;
        ballBodyDef.angularDamping = 0.6f;
        Body tempBody = world.createBody(ballBodyDef);
        hockey = new Hockey(tempBody, tempBody.createFixture(ballFixDef));

        //定义playerN
        circleShape.setRadius(U.World.PLAYER_RADIUS);
        ballBodyDef.position.set(U.World.PLAYER_NORTH_START);
        ballBodyDef.linearDamping = 25f;
        ballBodyDef.angularDamping = 25f;
        tempBody = world.createBody(ballBodyDef);
        ballFixDef.density = 20f;
        playerN = new Player(tempBody, tempBody.createFixture(ballFixDef), Player.TYPE_PLAYER_N);

        //定义playerS
        ballBodyDef.position.set(U.World.PLAYER_SOUTH_START);
        tempBody = world.createBody(ballBodyDef);
        playerS = new Player(tempBody, tempBody.createFixture(ballFixDef), Player.TYPE_PLAYER_S);

        balls[0] = hockey;
        balls[1] = playerN;
        balls[2] = playerS;
    }

    private Body createWallBody(Vector2 center) {
        wallBodyDef.position.set(center);
        return world.createBody(wallBodyDef);
    }

    private Fixture createVerticalWallFixture(Body wallBody) {
        wallShape.setAsBox(U.World.WALL_VER_HALF_WIDTH,
                U.World.WALL_VER_HALF_HEIGHT);
        return wallBody.createFixture(wallFixDef);
    }

    private Fixture createWallHorizontalFixture(Body wallBody) {
        wallShape.setAsBox(U.World.WALL_HOR_HALF_WIDTH,
                U.World.WALL_HOR_HALF_HEIGHT);
        return wallBody.createFixture(wallFixDef);
    }

    /**
     * 重置玩家和小球位置，将小球设置在北方发球线上
     */
    public void resetForNorth() {
        playerN.getBody().setTransform(U.World.PLAYER_NORTH_START, 0);
        playerS.getBody().setTransform(U.World.PLAYER_SOUTH_START, 0);
        hockey.getBody().setTransform(U.World.CENTER_TOP_SERVICE_LINE, 0);

        playerN.setVelocity(Vector2.Zero);
        playerS.setVelocity(Vector2.Zero);
        hockey.setVelocity(Vector2.Zero);
        isOutSide = false;
    }

    /**
     * 重置玩家和小球位置，将小球设置在南方发球线上
     */
    public void resetForSouth() {
        playerN.getBody().setTransform(U.World.PLAYER_NORTH_START, 0);
        playerS.getBody().setTransform(U.World.PLAYER_SOUTH_START, 0);
        hockey.getBody().setTransform(U.World.CENTER_BOTTOM_SERVICE_LINE, 0);

        playerN.setVelocity(Vector2.Zero);
        playerS.setVelocity(Vector2.Zero);
        hockey.setVelocity(Vector2.Zero);
        isOutSide = false;
    }

    public void resetForCenter() {
        playerN.getBody().setTransform(U.World.PLAYER_NORTH_START, 0);
        playerS.getBody().setTransform(U.World.PLAYER_SOUTH_START, 0);
        hockey.getBody().setTransform(U.World.CENTER_WORLD, 0);

        playerN.setVelocity(Vector2.Zero);
        playerS.setVelocity(Vector2.Zero);
        hockey.setVelocity(Vector2.Zero);
        playerN.setScore(0);
        playerS.setScore(0);
        isOutSide = false;
    }

    public void step() {
        world.step(1.0f / 60, 6, 2);
        hockey.limit(30);
        if (isOutSide) return;
        Vector2 hockeyPos = hockey.getCenterInWorld();
        if (hockeyPos.y < 0) {
            isOutSide = true;
            outsideSouth.run();
        } else if (hockeyPos.y > U.W_H){
            isOutSide = true;
            outsideNorth.run();
        }
    }

    public void onOutsideNorth(Runnable runnable) {
        this.outsideNorth = runnable;
    }

    public void onOutsideSouth(Runnable runnable) {
        this.outsideSouth = runnable;
    }

    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    public void setCrashWallListener(CrashWallListener crashWallListener) {
        this.crashWallListener = crashWallListener;
    }

    public Box2DDebugRenderer getRenderer() {
        return new Box2DDebugRenderer();
    }

    public World getWorld() {
        return world;
    }

    public TouchHelper getTouchHelper() {
        return touchHelper;
    }

    public Player getPlayerN() {
        return playerN;
    }

    public Player getPlayerS() {
        return playerS;
    }

    public Hockey getHockey() {
        return hockey;
    }

    public InputProcessor getInputProcessor() {
        return new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return touchHelper.touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return touchHelper.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return touchHelper.touchDown(screenX, screenY, pointer, button);
            }
        };
    }

    public interface CollisionListener{

        /**
         * 碰撞发生时候的回调
         *
         * @param point 碰撞点
         */
        void onCollision(Vector2 point);
    }

    public interface CrashWallListener{

        /**
         * 球类碰到墙的回调
         * @param velocity
         */
        void onCrash(Vector2 velocity);
    }
}