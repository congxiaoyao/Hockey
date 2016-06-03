package com.congxiaoyao;

import com.badlogic.gdx.*;
import com.congxiaoyao.element.Player;
import com.congxiaoyao.element.Hockey;
import com.congxiaoyao.stage.CollisionStage;
import com.congxiaoyao.stage.MainStage;
import com.congxiaoyao.stage.ResultStage;
import com.congxiaoyao.utils.U;

public class HockeyGame extends ApplicationAdapter {

    private MainStage mainStage;
    private WorldHolder worldHolder;
    private ResultStage resultStage;
    private CollisionStage collisionStage;

	@Override
	public void create () {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        mainStage = new MainStage();
        collisionStage = new CollisionStage();
        worldHolder = new WorldHolder();
        worldHolder.onOutsideNorth(()->handleOutsideNorth());
        worldHolder.onOutsideSouth(()->handleOutsideSouth());
        worldHolder.setCollisionListener(point ->
                collisionStage.showCollision(U.toScreen(point)));
        worldHolder.setCrashWallListener(velocity -> mainStage.showBorderAnimation(velocity));

        resultStage = new ResultStage();
        resultStage.onRequestRetry(()->{
            resultStage.hide();
            worldHolder.getTouchHelper().clear();
            worldHolder.resetForCenter();
        });

        inputMultiplexer.addProcessor(new KeyProcessor());
        inputMultiplexer.addProcessor(resultStage.getInputProcessor());
        inputMultiplexer.addProcessor(mainStage.getInputProcessor());
        inputMultiplexer.addProcessor(worldHolder.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

	@Override
	public void render () {
        worldHolder.getTouchHelper().notifyTouched();
        worldHolder.step();
        if (resultStage.canTouch() && !mainStage.isIniting()) {
            transferData(worldHolder, mainStage);
        }
        mainStage.draw();
        collisionStage.draw();
        resultStage.draw();
    }

    public void handleOutsideNorth() {
        worldHolder.getPlayerS().addScore();
        if (worldHolder.getPlayerS().getScore() >= 5) {
            resultStage.showPlayerSWin();
            return;
        }
        resultStage.showPlayerSGoal(()->{
            resultStage.hide();
            worldHolder.getTouchHelper().clear();
            worldHolder.resetForNorth();
            mainStage.showInitingAnimation();
        });
    }

    public void handleOutsideSouth() {
        worldHolder.getPlayerN().addScore();
        if (worldHolder.getPlayerN().getScore() >= 5) {
            resultStage.showPlayerNWin();
        }
        else resultStage.showPlayerNGoal(() -> {
            resultStage.hide();
            worldHolder.getTouchHelper().clear();
            worldHolder.resetForSouth();
            mainStage.showInitingAnimation();
        });
    }

    /**
     * 将世界中的数据转移到舞台中去
     * @param worldHolder
     * @param mainStage
     */
    public static void transferData(WorldHolder worldHolder, MainStage mainStage) {
        Hockey hockey = worldHolder.getHockey();
        mainStage.updateHockey(hockey.getCenterInScreen());
        Player playerN = worldHolder.getPlayerN();
        mainStage.updatePlayN(playerN.getCenterInScreen());
        mainStage.setScoreN(playerN.getScore());
        Player playerS = worldHolder.getPlayerS();
        mainStage.updatePlayS(playerS.getCenterInScreen());
        mainStage.setScoreS(playerS.getScore());
    }

    @Override
    public void dispose() {
        mainStage.dispose();
        resultStage.dispose();
        collisionStage.dispose();
    }

    class KeyProcessor extends InputAdapter{
        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.ESCAPE) {
                Gdx.app.exit();
                return true;
            }
            return false;
        }
    }
}
