package com.congxiaoyao;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.congxiaoyao.anim.CycleInterpolator;
import com.congxiaoyao.anim.LambdaAnimator;
import com.congxiaoyao.utils.U;

/**
 * Created by congxiaoyao on 2016/6/3.
 */
public class BorderAnimation {

    private static float MAX = U.byStdHor(2);  //振幅

    private LambdaAnimator animator;
    private Vector2 velocity = null;
    private Vector2 temp = new Vector2();

    public BorderAnimation(Sprite border) {
        Vector2 center = U.Screen.CENTER_SCREEN;
        animator = new LambdaAnimator(0, 1, 0.2f)
                .animating(value -> {
                    temp.set(velocity).scl(value * MAX).limit(MAX).add(center);
                    border.setCenter(temp.x, temp.y);
                }).afterEnd(() -> border.setCenter(center.x, center.y));
        animator.setInterpolator(new CycleInterpolator());
    }

    public void startAnimation(Vector2 velocity) {
        this.velocity = velocity;
        animator.start();
    }

    public void update() {
        animator.update();
    }
}
