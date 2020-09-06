/*
 * Copyright (c) 2020 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.spacefx;

import javafx.scene.image.Image;

import static eu.hansolo.spacefx.Config.BKG_SCALING_FACTOR;
import static eu.hansolo.spacefx.Config.SCALING_FACTOR;
//import static com.gluonhq.attach.util.Platform.isDesktop;
//import static com.gluonhq.attach.util.Platform.isIOS;


public class Level1 implements Level {
    public final int        index                   = 1;
    public final Difficulty difficulty              = Difficulty.EASY;
    public final Image      backgroundImg           = WebFxUtil.newImage("backgroundL1.jpg", 700 * BKG_SCALING_FACTOR, 3379 * BKG_SCALING_FACTOR);
    //public final Image      backgroundImg           = isDesktop() ? WebFxUtil.newImage("backgroundL1.jpg"), 700 * BKG_SCALING_FACTOR, 3379 * BKG_SCALING_FACTOR, true, false) : isIOS() ? WebFxUtil.newImage("backgroundL1.jpg"), 700 * BKG_SCALING_FACTOR, 3379 * BKG_SCALING_FACTOR, true, false) : WebFxUtil.newImage("backgroundL1.png"), 700 * BKG_SCALING_FACTOR, 3379 * BKG_SCALING_FACTOR, true, false);
    public final Image[]    enemyImages             = { WebFxUtil.newImage("enemy1L1.png", 56 * SCALING_FACTOR, 56 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("enemy2L1.png", 50 * SCALING_FACTOR, 50 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("enemy3L1.png", 68 * SCALING_FACTOR, 68 * SCALING_FACTOR) };
    public final Image      explosionImg            = WebFxUtil.newImage("explosionL1.png", 2048 * SCALING_FACTOR, 1792 * SCALING_FACTOR);
    public final Image      enemyTorpedoImg         = WebFxUtil.newImage("enemyTorpedoL1.png", 21 * SCALING_FACTOR, 21 * SCALING_FACTOR);
    public final Image      enemyBossImg0           = WebFxUtil.newImage("enemyBoss0L1.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR);
    public final Image      enemyBossImg1           = WebFxUtil.newImage("enemyBoss1L1.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR);
    public final Image      enemyBossImg2           = WebFxUtil.newImage("enemyBoss2L1.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR);
    public final Image      enemyBossImg3           = WebFxUtil.newImage("enemyBoss3L1.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR);
    public final Image      enemyBossImg4           = WebFxUtil.newImage("enemyBoss4L1.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR);
    public final Image      enemyBombImg            = WebFxUtil.newImage("enemyBombL1.png", 20 * SCALING_FACTOR, 20 * SCALING_FACTOR);
    public final Image      enemyBossTorpedoImg     = WebFxUtil.newImage("enemyBossTorpedoL1.png", 26 * SCALING_FACTOR, 26 * SCALING_FACTOR);
    public final Image      enemyBossRocketImg      = WebFxUtil.newImage("enemyBossRocketL1.png", 17 * SCALING_FACTOR, 42 * SCALING_FACTOR);
    public final Image      enemyBossHitImg         = WebFxUtil.newImage("torpedoHitL1.png", 400 * SCALING_FACTOR, 160 * SCALING_FACTOR);
    public final Image      enemyBossExplosionImg   = WebFxUtil.newImage("enemyBossExplosionL1.png", 800 * SCALING_FACTOR, 1400 * SCALING_FACTOR);
    public final Image      enemyRocketExplosionImg = WebFxUtil.newImage("enemyRocketExplosionL1.png", 512 * SCALING_FACTOR, 896 * SCALING_FACTOR);
    public final Image      levelBossImg            = WebFxUtil.newImage("levelBossL1.png", 300 * SCALING_FACTOR, 300 * SCALING_FACTOR);
    public final Image      levelBossTorpedoImg     = WebFxUtil.newImage("levelBossTorpedoL1.png", 35 * SCALING_FACTOR, 60 * SCALING_FACTOR);
    public final Image      levelBossRocketImg      = WebFxUtil.newImage("levelBossRocketL1.png", 15 * SCALING_FACTOR, 37 * SCALING_FACTOR);
    public final Image      levelBossBombImg        = WebFxUtil.newImage("levelBossBombL1.png", 30 * SCALING_FACTOR, 30 * SCALING_FACTOR);
    public final Image      levelBossExplosionImg   = WebFxUtil.newImage("levelBossExplosionL1.png", 2048 * SCALING_FACTOR, 768 * SCALING_FACTOR);


    @Override public int getIndex() { return index; }

    @Override public Difficulty getDifficulty() { return difficulty; }

    @Override public Image getBackgroundImg() {
        return backgroundImg;
    }

    @Override public Image[] getEnemyImages() {
        return enemyImages;
    }

    @Override public Image getEnemyBossImg0() {
        return enemyBossImg0;
    }

    @Override public Image getEnemyBossImg1() {
        return enemyBossImg1;
    }

    @Override public Image getEnemyBossImg2() {
        return enemyBossImg2;
    }

    @Override public Image getEnemyBossImg3() {
        return enemyBossImg3;
    }

    @Override public Image getEnemyBossImg4() {
        return enemyBossImg4;
    }

    @Override public Image getEnemyBombImg() {
        return enemyBombImg;
    }

    @Override public Image getEnemyTorpedoImg() {
        return enemyTorpedoImg;
    }

    @Override public Image getEnemyBossTorpedoImg() {
        return enemyBossTorpedoImg;
    }

    @Override public Image getEnemyBossRocketImg() {
        return enemyBossRocketImg;
    }

    @Override public Image getExplosionImg() {
        return explosionImg;
    }

    @Override public Image getEnemyBossHitImg() {
        return enemyBossHitImg;
    }

    @Override public Image getEnemyBossExplosionImg() {
        return enemyBossExplosionImg;
    }

    @Override public Image getEnemyRocketExplosionImg() {
        return enemyRocketExplosionImg;
    }

    @Override public Image getLevelBossImg() {
        return levelBossImg;
    }

    @Override public Image getLevelBossTorpedoImg() {
        return levelBossTorpedoImg;
    }

    @Override public Image getLevelBossRocketImg() {
        return levelBossRocketImg;
    }

    @Override public Image getLevelBossBombImg() {
        return levelBossBombImg;
    }

    @Override public Image getLevelBossExplosionImg() {
        return levelBossExplosionImg;
    }
}
