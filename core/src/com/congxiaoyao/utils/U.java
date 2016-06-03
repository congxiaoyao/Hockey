package com.congxiaoyao.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by congxiaoyao on 2016/5/27.
 */
public class U {

    private static final String ASSETS_BASE = "assets/";

    private static final Vector2 mouse = new Vector2();
    //屏幕宽度
    public static final float S_W = Gdx.graphics.getWidth();
    //屏幕高度
    public static final float S_H = Gdx.graphics.getHeight();
    //屏幕或世界的高宽比
    public static final float WH_RADIO = S_H / S_W;
    //物理世界的宽度（米）
    public static final float W_W = 7.2f;

    //物理世界的高度（米）
    public static final float W_H = W_W * WH_RADIO;
    //屏幕像素数与物理世界的大小的比
    public static final float SW_RADIO = S_W / W_W;

    //屏幕宽度与标准屏幕的比例
    public static final float STD_HOR_RADIO = S_W / 1080;

    //屏幕高度与标准屏幕的比例
    public static final float STD_VER_RADIO = S_H / 1920;

    //粒子系统定义文件
    public static final FileHandle EFFECT_FILE = Gdx.files.internal(ASSETS_BASE + "triangle.p");
    //粒子图片
    public static final FileHandle PARTICLE_DIR = Gdx.files.internal(ASSETS_BASE);
    /**
     * 在屏幕上的各个物体显示出来的宽高及位置等常量
     */
    public static final class Screen {
        //冰球的直径
        public static final float HOCKEY_BALL_DIAMETER = byStdHor(136f);
        //玩家的直径
        public static final float PLAYER_BALL_DIAMETER = byStdHor(236f);
        //重试按钮的宽度
        public static final float RETRY_BUTTON_WIDTH = byStdHor(331);
        //重试按钮的高度
        public static final float RETRY_BUTTON_HEIGHT = byStdVer(219);
        //上方玩家起始位置的中心坐标
        public static final Vector2 PLAYER_SOUTH_START = new Vector2(S_W / 2, byStdVer(200));
        //下方玩家起始位置的中心坐标
        public static final Vector2 PLAYER_NORTH_START = new Vector2(S_W / 2, byStdVer(1720));

        //上面的计分板的中心点
        public static final Vector2 SCORE_BOARD_TOP_POSITION =
                new Vector2(byStdHor(934.5f), byStdVer(1120));
        //下面的计分板的中心点
        public static final Vector2 SCORE_BOARD_BOTTOM_POSITION =
                new Vector2(byStdHor(934.5f), byStdVer(800));
        //屏幕中心
        public static final Vector2 CENTER_SCREEN = new Vector2(S_W / 2, S_H / 2);
        //上罚球线的中心点
        public static final Vector2 CENTER_TOP_SERVICE_LINE =
                new Vector2(S_W / 2, S_H * 2 / 3);
        //下罚球线的中心点
        public static final Vector2 CENTER_BOTTOM_SERVICE_LINE =
                new Vector2(S_W / 2, S_H / 3);

        public static final float SCALE_X = S_W / 1080;
        public static final float SCALE_Y = S_H / 1920;
        public static final float SCALE = SCALE_X;
    }

    /**
     * 在物理世界中各个物体的位置尺寸参数
     */
    public static final class World {
        //左边的墙的中心点
        public static final Vector2 WALL_LEFT_CENTER =
                new Vector2(toWorld(byStdHor(42.5f)), W_H / 2);
        //右边的墙的中心点
        public static final Vector2 WALL_RIGHT_CENTER =
                new Vector2(toWorld(byStdHor(1080 - 42.5f)), W_H / 2);
        //竖直墙半高
        public static final float WALL_VER_HALF_HEIGHT = toWorld(byStdVer(960));

        //竖直墙半宽
        public static final float WALL_VER_HALF_WIDTH = toWorld(byStdHor(7.5f));
        //右上墙的中心点
        public static final Vector2 WALL_TOP_RIGHT_CENTER =
                new Vector2(toWorld(byStdHor(195f)),toWorld(byStdVer(1920-42.5f)));
        //左上墙的中心点
        public static final Vector2 WALL_TOP_LEFT_CENTER =
                new Vector2(toWorld(byStdHor(1080-195f)),toWorld(byStdVer(1920-42.5f)));
        //右下墙的中心点
        public static final Vector2 WALL_BOTTOM_RIGHT_CENTER =
                new Vector2(toWorld(byStdHor(195f)),toWorld(byStdVer(42.5f)));
        //左下墙的中心点
        public static final Vector2 WALL_BOTTOM_LEFT_CENTER =
                new Vector2(toWorld(byStdHor(1080-195f)),toWorld(byStdVer(42.5f)));
        //水平墙半高
        public static final float WALL_HOR_HALF_HEIGHT = toWorld(byStdVer(7.5f));

        //水平墙半宽
        public static final float WALL_HOR_HALF_WIDTH = toWorld(byStdHor(145f));
        //冰球的半径
        public static final float HOCKEY_RADIUS = toWorld(byStdHor(57));

        //player的半径
        public static final float PLAYER_RADIUS = toWorld(byStdHor(100));
        //在物理世界中playerS的起始位置
        public static final Vector2 PLAYER_SOUTH_START = Screen.PLAYER_SOUTH_START.cpy();
        //在物理世界中playerN的起始位置
        public static final Vector2 PLAYER_NORTH_START = Screen.PLAYER_NORTH_START.cpy();
        //屏幕中心
        public static final Vector2 CENTER_WORLD = Screen.CENTER_SCREEN.cpy();
        //上罚球线的中心点
        public static final Vector2 CENTER_TOP_SERVICE_LINE =
                Screen.CENTER_TOP_SERVICE_LINE.cpy();
        //下罚球线的中心点
        public static final Vector2 CENTER_BOTTOM_SERVICE_LINE =
                Screen.CENTER_BOTTOM_SERVICE_LINE.cpy();
        static {
            toWorld(PLAYER_SOUTH_START);
            toWorld(PLAYER_NORTH_START);
            toWorld(CENTER_WORLD);
            toWorld(CENTER_TOP_SERVICE_LINE);
            toWorld(CENTER_BOTTOM_SERVICE_LINE);
        }
    }

    public static final class Image {

        public static Texture BACKGROUND = new Texture(ASSETS_BASE + "background0.png");
        public static Texture BORDER = new Texture(ASSETS_BASE + "border.png");
        public static Texture HOCKEY = new Texture(ASSETS_BASE + "hockey_yellow.png");
        public static Texture PLAYER_SOUTH = new Texture(ASSETS_BASE + "player_green.png");
        public static Texture PLAYER_NORTH = new Texture(ASSETS_BASE + "player_red.png");
        public static Texture GOAL_SOUTH = new Texture(ASSETS_BASE + "goal_s.png");
        public static Texture GOAL_NORTH = new Texture(ASSETS_BASE + "goal_n.png");
        public static Texture RETRY = new Texture(ASSETS_BASE + "retry.png");

        private static Texture[] scores = new Texture[6];
        static {
            for (int i = 0; i < scores.length; i++) {
                scores[i] = new Texture(ASSETS_BASE + "score_" + i+".png");
            }
        }
        public static Texture SCORE(int score) {
            if (score >= 0 && score <= 5) {
                return scores[score];
            }
            return null;
        }
    }

    /**
     * 将一个适用于屏幕上的值转换到物理世界中去
     * @param value
     * @return 这个值在物理世界中的大小
     */
    public static float toWorld(float value) {
        return value / SW_RADIO;
    }

    public static Vector2 toWorld(Vector2 value) {
        value.scl(1 / SW_RADIO);
        return value;
    }

    /**
     * 将一个适用于物理世界里的值转换到屏幕上去
     * @param value
     * @return 这个值在屏幕上的大小
     */
    public static float toScreen(float value) {
        return value * SW_RADIO;
    }

    public static Vector2 toScreen(Vector2 value) {
        value.scl(SW_RADIO);
        return value;
    }

    /**
     * 由于设计时是按照1080P的屏幕设计的，所以对于每个分辨率的屏幕而言
     * 是有可能会存在一些缩放的，但是我们想利用已经计算好的数据
     * 于是可以通过此方法，将那些以1080P屏幕作为标准得到的水平方向上的数据转换为适应当前屏幕的数据
     * @param value
     * @return 比如在1080P的屏幕中，一个物体的宽度是100px 但是用户的屏幕宽度为108px
     *          所以转换后的物体宽度应为10px
     */
    public static float byStdHor(float value) {
        return value * STD_HOR_RADIO;
    }

    /**
     * 同上 竖直方向上的比例换算
     * @param value
     * @return
     */
    public static float byStdVer(float value) {
        return value * STD_VER_RADIO;
    }

    /**
     * 时间间隔
     * @return
     */
    public static float DT() {
        return Gdx.graphics.getDeltaTime();
    }

    /**
     * 鼠标在物理世界中的坐标
     * @return
     */
    public static Vector2 MouseW(int pointer) {
        float x = Gdx.input.getX(pointer) / SW_RADIO;
        float y = (U.S_H - Gdx.input.getY(pointer)) / SW_RADIO;
        return mouse.set(x, y);
    }

    public static Vector2 MouseS(int pointer) {
        float x = Gdx.input.getX(pointer);
        float y = S_H - Gdx.input.getY(pointer);
        return mouse.set(x, y);
    }
}