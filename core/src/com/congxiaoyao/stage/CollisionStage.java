package com.congxiaoyao.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.congxiaoyao.utils.U;

import java.util.Iterator;

/**
 * 此类专门用来显示碰撞后的粒子效果
 *
 * Created by congxiaoyao on 2016/6/2.
 */
public class CollisionStage extends Stage {

    private Batch batch;
    private ParticleEffectPool pool;
    private Array<ParticleEffectPool.PooledEffect> effects;

    public CollisionStage() {
        batch = getBatch();
        ParticleEffect effect = new ParticleEffect();
        effect.load(U.EFFECT_FILE, U.PARTICLE_DIR);
        effect.scaleEffect(U.Screen.SCALE);
        pool = new ParticleEffectPool(effect, 2, 8);
        effects = new Array<>(5);
    }

    public void showCollision(Vector2 position) {
        ParticleEffectPool.PooledEffect effect = getEffect(pool, position);
        effects.add(effect);
    }

    private ParticleEffectPool.PooledEffect getEffect(ParticleEffectPool pool, Vector2 position) {
        ParticleEffectPool.PooledEffect effect = pool.obtain();
        effect.setPosition(position.x, position.y);
        effect.start();
        return effect;
    }

    @Override
    public void draw() {
        batch.begin();
        Iterator<ParticleEffectPool.PooledEffect> iterator = effects.iterator();
        while (iterator.hasNext()) {
            ParticleEffectPool.PooledEffect effect = iterator.next();
            effect.draw(batch, U.DT());
            if(effect.isComplete()) {
                effect.free();
                iterator.remove();
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        for (ParticleEffectPool.PooledEffect effect : effects) {
            effect.free();
            effect.dispose();
        }
        effects.clear();
    }
}
