package com.congxiaoyao.anim;

/**
 * 正弦插值器数值由0 ~ 1 ~ 0 ~ -1 ~ 0
 *
 * Created by congxiaoyao on 2016/6/3.
 */
public class CycleInterpolator implements Interpolator {

    private float cycles;

    public CycleInterpolator(float cycles) {
        this.cycles = cycles;
    }

    public CycleInterpolator() {
        this(1);
    }

    @Override
    public float getInterpolation(float t) {
        return (float)(Math.sin(2 * cycles * Math.PI * t));
    }
}
