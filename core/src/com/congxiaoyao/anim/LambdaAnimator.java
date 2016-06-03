package com.congxiaoyao.anim;

import com.congxiaoyao.utils.U;

/**
 * 终于能在安卓中用lambda表达式了，所以重新设计动画进度回调接口
 *
 * Created by congxiaoyao on 2016/6/2.
 */
public class LambdaAnimator extends Animator {

    private Runnable before;
    private Runnable after;
    private AnimationCallback callback;

    /**
     * 给定起始值以及结束值，Animator会在duration的时间内
     * 不断计算当前时间对应的值 这里的计算是线性插值的，没工夫写插值器了！！
     * 动画的过程会通过回调接口通知外界，外界可以通过映射出来的值对动画进行处理
     *
     * @param from
     * @param to
     * @param duration 动画执行时间 秒
     */
    public LambdaAnimator(float from, float to, float duration) {
        super(from, to, duration);
    }

    /**
     * 请在draw方法中调用此方法以保证动画不断执行
     */
    @Override
    public void update() {
        if(!canStart) return;
        if (isEnd) return;
        if (nowDuration == 0) {
            if (before != null) before.run();
        }
        if (nowDuration > duration) {
            nowDuration = duration;
            isEnd = true;
        }
        float rate = nowDuration / duration;
        if(interpolator != null){
            rate = interpolator.getInterpolation(rate);
        }
        if (callback != null) callback.animating(map(from, to, rate));
        if (isEnd) {
            if (after != null) after.run();
        }
        nowDuration += U.DT();
    }

    public LambdaAnimator afterEnd(Runnable after) {
        this.after = after;
        return this;
    }

    public LambdaAnimator beforeStart(Runnable before) {
        this.before = before;
        return this;
    }

    public LambdaAnimator animating(AnimationCallback callback) {
        this.callback = callback;
        return this;
    }

    @Deprecated
    @Override
    public void setAnimationListener(AnimationListener listener) {
    }

    public interface AnimationCallback {

        void animating(float value);
    }

}
