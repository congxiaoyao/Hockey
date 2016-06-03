package com.congxiaoyao.stage;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.congxiaoyao.anim.Animator;
import com.congxiaoyao.anim.LambdaAnimator;
import com.congxiaoyao.anim.OvershootInterpolator;
import com.congxiaoyao.utils.U;

/**
 * 此stage用于展示一些结果如某方进球或获得胜利的提示效果
 *
 * Created by congxiaoyao on 2016/6/1.
 */
public class ResultStage extends Stage {

    private Sprite sprite;
    private Sprite retrySprite;
    private Sprite goalSprite;
    private Batch batch;
    private LambdaAnimator scaleAnimator;
    private LambdaAnimator alphaAnimator;
    private LambdaAnimator disappearAnimator;

    private boolean hide = true;
    private boolean isAnimating = false;
    private boolean waitingRetry = false;

    private Runnable finishCallback;
    private Runnable retryRunnable;

    public ResultStage() {
        retrySprite = new Sprite(U.Image.RETRY);
        goalSprite = new Sprite(U.Image.GOAL_NORTH);
        batch = getBatch();
        scaleAnimator = new LambdaAnimator(0.55f, 1f, 0.6f);
        scaleAnimator.setInterpolator(new OvershootInterpolator());
        scaleAnimator.beforeStart(() -> isAnimating = true)
                .animating(value -> sprite.setScale(value * U.Screen.SCALE))
                .afterEnd(() -> {
                    if(sprite == retrySprite){
                        isAnimating = false;
                        waitingRetry = true;
                    } else disappearAnimator.start();
                });

        alphaAnimator = new LambdaAnimator(0.4f, 1, 0.6f);
        alphaAnimator.animating(value -> sprite.setAlpha(value));

        disappearAnimator = new LambdaAnimator(1.4f, 0.55f, 0.75f);
        disappearAnimator.animating(value -> {
            if (value > 1) return;
            float alpha = Animator.map(1, 0.55f, 1, 0, value);
            sprite.setAlpha(alpha);
            sprite.setScale(value * U.Screen.SCALE);
        }).afterEnd(() -> {
            sprite.setAlpha(0);
            isAnimating = false;
            if (finishCallback != null) {
                finishCallback.run();
            }
        });

    }

    @Override
    public void draw() {
        if (hide) return;
        alphaAnimator.update();
        scaleAnimator.update();
        disappearAnimator.update();
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public void showPlayerSGoal(Runnable callBack) {
        this.finishCallback = callBack;
        hide = false;
        Vector2 center = U.Screen.CENTER_BOTTOM_SERVICE_LINE;
        sprite = goalSprite;
        sprite.setCenter(center.x, center.y);
        sprite.setRotation(0);
        sprite.setTexture(U.Image.GOAL_SOUTH);
        alphaAnimator.start();
        scaleAnimator.start();
    }

    public void showPlayerNGoal(Runnable callBack) {
        this.finishCallback = callBack;
        hide = false;
        Vector2 center = U.Screen.CENTER_TOP_SERVICE_LINE;
        sprite = goalSprite;
        sprite.setCenter(center.x, center.y);
        sprite.setRotation(0);
        sprite.setTexture(U.Image.GOAL_NORTH);
        alphaAnimator.start();
        scaleAnimator.start();
    }

    public void showPlayerSWin() {
        hide = false;
        Vector2 center = U.Screen.CENTER_SCREEN;
        sprite = retrySprite;
        sprite.setCenter(center.x, center.y);
        sprite.setRotation(0);
        sprite.setTexture(U.Image.RETRY);
        alphaAnimator.start();
        scaleAnimator.start();
    }


    public void showPlayerNWin() {
        hide = false;
        Vector2 center = U.Screen.CENTER_SCREEN;
        sprite = retrySprite;
        sprite.setCenter(center.x, center.y);
        sprite.setRotation(180);
        sprite.setTexture(U.Image.RETRY);
        alphaAnimator.start();
        scaleAnimator.start();
    }

    public void onRequestRetry(Runnable runnable) {
        this.retryRunnable = runnable;
    }

    public void hide() {
        hide =true;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public boolean canTouch() {
        return !(isAnimating || waitingRetry);
    }

    public InputProcessor getInputProcessor() {
        return new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return isAnimating || waitingRetry;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return isAnimating || waitingRetry;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (waitingRetry && isInRetryButton(U.MouseS(pointer))) {
                    waitingRetry = false;
                    if(retryRunnable != null) retryRunnable.run();
                }
                return isAnimating || waitingRetry;
            }

            private boolean isInRetryButton(Vector2 mouse) {
                Rectangle rectangle = new Rectangle(0, 0,
                        U.Screen.RETRY_BUTTON_WIDTH, U.Screen.RETRY_BUTTON_HEIGHT);
                Vector2 center = U.Screen.CENTER_SCREEN;
                rectangle.setCenter(center.x, center.y);
                return rectangle.contains(mouse);
            }
        };
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}