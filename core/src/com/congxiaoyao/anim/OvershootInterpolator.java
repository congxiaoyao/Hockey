package com.congxiaoyao.anim;

/**
 * 一种带有回弹效果的插值器
 * 效果就是竖直快速增长慢慢超过所设定的最大值，然后在回到最大值
 *
 * Created by congxiaoyao on 2016/6/1.
 */
public class OvershootInterpolator implements Interpolator {

    private float tension = 2;

    public OvershootInterpolator(float tension) {
        this.tension = tension;
    }

    public OvershootInterpolator() {

    }

    @Override
    public float getInterpolation(float t) {
        t -= 1.0f;
        return (t * t * (((tension + 1) * t) + tension)) + 1.0f;
    }
}
