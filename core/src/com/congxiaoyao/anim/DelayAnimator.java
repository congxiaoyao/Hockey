package com.congxiaoyao.anim;

/**
 * 给LambdaAnimator添加delay的功能
 * 正常就不该这么设计，动画包做的太仓促 很多细节来不及考虑代码就上去了
 * 不想修改LambdaAnimator中的逻辑因为没时间调试了 离交作业只剩几个小时了
 * 所以用了点奇技淫巧来实现animator的startDelay的功能，就是有点浪费内存。。
 * 真不想说这代码是我写的。。。
 *
 * Created by congxiaoyao on 2016/6/3.
 */
public class DelayAnimator extends LambdaAnimator {

    private LambdaAnimator delayAnimator;
    private float startDelay;    //秒

    /**
     * 给定起始值以及结束值，Animator会在duration的时间内
     * 不断计算当前时间对应的值 这里的计算是线性插值的，没工夫写插值器了！！
     * 动画的过程会通过回调接口通知外界，外界可以通过映射出来的值对动画进行处理
     *
     * @param from
     * @param to
     * @param duration 动画执行时间 秒
     */
    public DelayAnimator(float from, float to, float duration) {
        super(from, to, duration);
    }

    /**
     * 设置动画开始延时
     * @param startDelay 单位秒
     * @return
     */
    public DelayAnimator setStartDelay(float startDelay) {
        this.startDelay = startDelay;
        delayAnimator = new LambdaAnimator(0, 0, startDelay);
        delayAnimator.afterEnd(() -> delayStart());
        return this;
    }

    @Override
    public void update() {
        delayAnimator.update();
        super.update();
    }

    @Override
    public void start() {
        if (delayAnimator == null) {
            delayStart();
        }else {
            delayAnimator.start();
        }
    }

    private void delayStart() {
        super.start();
    }
}
