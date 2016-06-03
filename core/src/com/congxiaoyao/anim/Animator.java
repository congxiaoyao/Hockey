package com.congxiaoyao.anim;

import com.congxiaoyao.utils.U;

/**
 * libgdx简直蛋疼的一笔都没有正常的动画框架
 * 所以这里实现一个简单的动画器用来计算动画进度
 * 动画结果会以回调接口的形式通知外界
 * 记得一定要不断调用run方法才能使得Animator工作，所以建议把他放到一个draw（或render）方法里
 *
 * 动画器支持自定义插值器 只要将Interpolator接口的实现类传入即可
 * 如果不设置插值器，则使用线性插值
 * 包内提供了OvershootInterpolator可供使用，计算公式摘自安卓源码
 *
 * Created by congxiaoyao on 2016/6/1.
 */
public class Animator {

    protected float from;
    protected float to;
    protected float duration;

    protected float nowDuration = 0;
    protected boolean isEnd = false;
    protected boolean canStart = false;

    protected AnimationListener listener;
    protected Interpolator interpolator;

    /**
     * 给定起始值以及结束值，Animator会在duration的时间内
     * 不断计算当前时间对应的值 这里的计算是线性插值的，没工夫写插值器了！！
     * 动画的过程会通过回调接口通知外界，外界可以通过映射出来的值对动画进行处理
     *
     * @param from
     * @param to
     * @param duration 动画执行时间 秒
     */
    public Animator(float from, float to, float duration) {
        this.from = from;
        this.to = to;
        this.duration = duration;
    }

    /**
     * 请在draw方法中调用此方法以保证动画不断执行
     */
    public void update() {
        if (listener == null) return;
        if(!canStart) return;
        if (isEnd) return;
        if (nowDuration == 0) {
            listener.beforeStart();
        }
        if (nowDuration > duration) {
            nowDuration = duration;
            isEnd = true;
        }
        float rate = nowDuration / duration;
        if(interpolator != null){
            rate = interpolator.getInterpolation(rate);
        }
        listener.animating(map(from, to, rate));
        if (isEnd) {
            listener.afterEnd();
        }
        nowDuration += U.DT();
    }

    public void start() {
        canStart = true;
        isEnd = false;
        nowDuration = 0;
    }

    public void stop() {
        canStart = false;
    }

    public static float map(float from, float to, float progress) {
        return from + (to - from) * progress;
    }

    public static float map(float from1, float from2, float to1, float to2, float value) {
        float progress = (value - from1) / (from2 - from1);
        return map(to1, to2, progress);
    }

    public void setAnimationListener(AnimationListener listener) {
        this.listener = listener;
    }


    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * 动画过程回调
     */
    public interface AnimationListener{

        /**
         * 动画开始前的回调
         */
        void beforeStart();

        /**
         * 动画结束后的回调
         */
        void afterEnd();

        /**
         * 动画过程的回调
         * @param value 构造函数中from到to的一个值
         */
        void animating(float value);
    }

    public static class AnimationAdapter implements AnimationListener {

        @Override
        public void beforeStart() {

        }

        @Override
        public void afterEnd() {

        }

        @Override
        public void animating(float value) {

        }
    }
    
    public static void main(String[] args) {
        System.out.println(map(1, 0.55f, 1, 0, 0.6f));
    }
}
