package com.congxiaoyao.anim;

/**
 * Created by congxiaoyao on 2016/6/1.
 */
public interface Interpolator {

    /**
     * 传入一个从0到1的数 返回插值后的结果，可能大于1
     * @param t
     */
    float getInterpolation(float t);
}
