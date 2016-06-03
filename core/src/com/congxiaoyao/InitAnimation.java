package com.congxiaoyao;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.congxiaoyao.anim.DelayAnimator;
import com.congxiaoyao.anim.LambdaAnimator;
import com.congxiaoyao.anim.OvershootInterpolator;
import com.congxiaoyao.utils.U;

/**
 * 用于展示初始化效果的类，其实这个类有点尴尬，就是他的draw方法不会真正的去画东西
 * 它实际上只是一个
 *
 * Created by congxiaoyao on 2016/6/3.
 */
public class InitAnimation {

    private Sprite hockey;
    private Sprite playerS;
    private Sprite playerN;

    private LambdaAnimator hockeyAnimator;
    private DelayAnimator playerAnimator;

    private boolean isAnimating = false;

    public InitAnimation(Sprite hockey, Sprite playerS, Sprite playerN) {
        this.hockey = hockey;
        this.playerS = playerS;
        this.playerN = playerN;

        hockeyAnimator = new LambdaAnimator(0.4f, 1, 0.5f)
                .beforeStart(() -> {
                    isAnimating = true;
                    playerN.setAlpha(0);
                    playerS.setAlpha(0);
                    handleHockeyScale(0.4f);
                })
                .animating(this::handleHockeyScale)
                .afterEnd(() -> handleHockeyScale(1));
        hockeyAnimator.setInterpolator(new OvershootInterpolator());

        playerAnimator = (DelayAnimator) new DelayAnimator(0.4f, 1, 0.5f)
                .setStartDelay(0.25f)
                .beforeStart(() -> handlePlayerScale(0.4f))
                .animating(this::handlePlayerScale)
                .afterEnd(() -> {
                    handlePlayerScale(1);
                    isAnimating = false;
                });
        playerAnimator.setInterpolator(new OvershootInterpolator());
    }

    private void handleHockeyScale(float value) {
        hockey.setScale(value * U.Screen.SCALE_X);
    }

    private void handlePlayerScale(float value) {
        playerN.setScale(value * U.Screen.SCALE_X);
        playerS.setScale(value * U.Screen.SCALE_X);
        playerN.setAlpha(1);
        playerS.setAlpha(1);
    }

    public void showInitAnimation() {
        playerAnimator.start();
        hockeyAnimator.start();
    }

    public void update() {
        playerAnimator.update();
        hockeyAnimator.update();
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public InputProcessor getInputProcessor() {
        return new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return isAnimating;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return isAnimating;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return isAnimating;
            }
        };
    }
}
