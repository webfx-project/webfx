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

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

//import static com.gluonhq.attach.util.Platform.isDesktop;


public class Config {
    public static final String      PROPERTIES_FILE_NAME                = "spacefx.properties";
    //public static final Rectangle2D VISUAL_BOUNDS                       = isDesktop() ? new Rectangle2D(0, 0, 700, 900) : Screen.getPrimary().getVisualBounds();
    public static final Rectangle2D VISUAL_BOUNDS                       = new Rectangle2D(0, 0, SpaceFX.WINDOW_WIDTH, SpaceFX.WINDOW_HEIGHT);
    public static final boolean     IS_PORTRAIT_MODE                    = VISUAL_BOUNDS.getHeight() > VISUAL_BOUNDS.getWidth();
    public static final double      BKG_SCALING_FACTOR                  = IS_PORTRAIT_MODE ? (VISUAL_BOUNDS.getHeight() / 900) : (VISUAL_BOUNDS.getWidth() / 700);
    //public static final double      SCALING_FACTOR                      = isDesktop() ? 1.0 : 0.7;
    public static final double      SCALING_FACTOR                      = 1.0;
    public static final double      SWITCH_POINT                        = 2079 * BKG_SCALING_FACTOR;
    public static final double      WIDTH                               = VISUAL_BOUNDS.getWidth();
    public static final double      HEIGHT                              = VISUAL_BOUNDS.getHeight();
    public static final double      VELOCITY_FACTOR_X                   = BKG_SCALING_FACTOR;
    public static final double      VELOCITY_FACTOR_Y                   = BKG_SCALING_FACTOR;
    public static final double      VELOCITY_FACTOR_R                   = BKG_SCALING_FACTOR;
    public static final boolean     PLAY_SOUND                          = true;
    public static final boolean     PLAY_MUSIC                          = true;
    public static final boolean     SHOW_BACKGROUND                     = true;
    public static final boolean     SHOW_STARS                          = true;
    public static final boolean     SHOW_ENEMY_BOSS                     = true;
    public static final boolean     SHOW_ENEMIES                        = true;
    public static final boolean     SHOW_ASTEROIDS                      = true;
    //public static final boolean     SHOW_BUTTONS                        = !isDesktop();
    //public static final boolean     SHOW_BUTTONS                        = false; //Platform.isSupported(ConditionalFeature.INPUT_MULTITOUCH);
    public static final int         NO_OF_STARS                         = SHOW_STARS ? (int) (SCALING_FACTOR * 200) : 0;
    public static final int         NO_OF_ASTEROIDS = SHOW_ASTEROIDS ? (int) (SCALING_FACTOR * 10) : 0;
    public static final int         NO_OF_LIFES                         = 5;
    public static final int         NO_OF_SHIELDS                       = 10;
    public static final long        DEFLECTOR_SHIELD_TIME               = 5_000_000_000l;
    public static final long        BIG_TORPEDO_TIME                    = 40_000_000_000l;
    public static final long        STARBURST_TIME                      = 40_000_000_000l;
    public static final int         MAX_NO_OF_ROCKETS                   = 3;
    public static final double      TORPEDO_SPEED                       = 6 * VELOCITY_FACTOR_Y;
    public static final double      BIG_TORPEDO_SPEED                   = 3 * VELOCITY_FACTOR_Y;
    public static final double      ROCKET_SPEED                        = 4 * VELOCITY_FACTOR_Y;
    public static final double      ENEMY_SPEED                         = 4 * VELOCITY_FACTOR_Y;
    public static final double      ENEMY_TORPEDO_SPEED                 = 5 * VELOCITY_FACTOR_Y;
    public static final double      ENEMY_BOMB_SPEED                    = 3 * VELOCITY_FACTOR_Y;
    public static final int         NO_OF_ENEMY_BOMBS                   = 3;
    public static final double      ENEMY_BOSS_TORPEDO_SPEED            = 6 * VELOCITY_FACTOR_Y;
    public static final double      ENEMY_BOSS_ROCKET_SPEED             = 4 * VELOCITY_FACTOR_Y;
    public static final double      ENEMY_BOSS_SPEED                    = 2 * VELOCITY_FACTOR_Y;
    public static final double      LEVEL_BOSS_SPEED                    = 1 * VELOCITY_FACTOR_Y;
    public static final double      LEVEL_BOSS_TORPEDO_SPEED            = 6 * VELOCITY_FACTOR_Y;
    public static final double      LEVEL_BOSS_BOMB_SPEED               = 4 * VELOCITY_FACTOR_Y;
    public static final double      OUT_OF_SENSING_HEIGHT               = HEIGHT * 0.8;
    public static final int         TORPEDO_DAMAGE                      = 1;
    public static final int         BIG_TORPEDO_DAMAGE                  = 3;
    public static final int         ROCKET_DAMAGE                       = 3;
    public static final int         SHIELD_DAMAGE                       = 5;
    public static final long        ENEMY_BOSS_ATTACK_INTERVAL          = 25_000_000_000l;
    public static final long        SHIELD_UP_SPAWN_INTERVAL            = 25_000_000_000l;
    public static final long        LIFE_UP_SPAWN_INTERVAL              = 55_000_000_000l;
    public static final long        WAVE_SPAWN_INTERVAL                 = 10_000_000_000l;
    public static final long        BOMB_DROP_INTERVAL                  = 1_000_000_000l;
    public static final long        MIN_TORPEDO_INTERVAL                = 50_000_000l;
    public static final long        MIN_BIG_TORPEDO_INTERVAL            = 50_000_000l;
    public static final long        MIN_STARBURST_INTERVAL              = 300_000_000l;
    public static final long        BIG_TORPEDO_BONUS_INTERVAL          = 60_000_000_000l;
    public static final long        STARBURST_BONUS_INTERVAL            = 100_000_000_000l;
    public static final int         NO_OF_KILLS_STAGE_1                 = 50;
    public static final int         NO_OF_KILLS_STAGE_2                 = 100;
    public static final double      FIRST_QUARTER_WIDTH                 = WIDTH * 0.25;
    public static final double      LAST_QUARTER_WIDTH                  = WIDTH * 0.75;
    public static final double      SHIELD_INDICATOR_X                  = WIDTH * 0.73;
    public static final double      SHIELD_INDICATOR_Y                  = HEIGHT * 0.06;
    public static final double      SHIELD_INDICATOR_WIDTH              = WIDTH * 0.26;
    public static final double      SHIELD_INDICATOR_HEIGHT             = HEIGHT * 0.01428571;
    public static final long        FPS_60                              = 0_016_666_666l;
    public static final long        FPS_30                              = 0_033_333_333l;
    public static final long        FPS_10                              = 0_100_000_000l;
    public static final long        FPS_2                               = 0_500_000_000l;
    public static final Color       SPACEFX_COLOR                       = Color.rgb(51, 210, 206);
    public static final Color       SPACEFX_COLOR_TRANSLUCENT           = Color.rgb(51, 210, 206, 0.5);
    public static final WaveType[]  WAVE_TYPES_SLOW                     = { WaveType.TYPE_1_SLOW,
                                                                            WaveType.TYPE_2_SLOW,
                                                                            WaveType.TYPE_3_SLOW,
                                                                            WaveType.TYPE_4_SLOW,
                                                                            WaveType.TYPE_5_SLOW,
                                                                            WaveType.TYPE_6_SLOW,
                                                                            WaveType.TYPE_7_SLOW,
                                                                            WaveType.TYPE_8_SLOW,
                                                                            WaveType.TYPE_9_SLOW,
                                                                            WaveType.TYPE_10_SLOW,
                                                                            WaveType.TYPE_11_SLOW,
                                                                            WaveType.TYPE_12_SLOW,
                                                                            WaveType.TYPE_13_SLOW};
    public static final WaveType[]  WAVE_TYPES_MEDIUM                   = { WaveType.TYPE_1_MEDIUM,
                                                                            WaveType.TYPE_2_MEDIUM,
                                                                            WaveType.TYPE_3_MEDIUM,
                                                                            WaveType.TYPE_4_MEDIUM,
                                                                            WaveType.TYPE_5_MEDIUM,
                                                                            WaveType.TYPE_6_MEDIUM,
                                                                            WaveType.TYPE_7_MEDIUM,
                                                                            WaveType.TYPE_8_MEDIUM,
                                                                            WaveType.TYPE_9_MEDIUM,
                                                                            WaveType.TYPE_10_MEDIUM,
                                                                            WaveType.TYPE_11_MEDIUM,
                                                                            WaveType.TYPE_12_MEDIUM,
                                                                            WaveType.TYPE_13_MEDIUM};
    public static final WaveType[]  WAVE_TYPES_FAST                     = { WaveType.TYPE_1_FAST,
                                                                            WaveType.TYPE_2_FAST,
                                                                            WaveType.TYPE_3_FAST,
                                                                            WaveType.TYPE_4_FAST,
                                                                            WaveType.TYPE_5_FAST,
                                                                            WaveType.TYPE_6_FAST,
                                                                            WaveType.TYPE_7_FAST,
                                                                            WaveType.TYPE_8_FAST,
                                                                            WaveType.TYPE_9_FAST,
                                                                            WaveType.TYPE_10_FAST,
                                                                            WaveType.TYPE_11_FAST,
                                                                            WaveType.TYPE_12_FAST,
                                                                            WaveType.TYPE_13_FAST};
    public static final double      TORPEDO_BUTTON_X                    = 15;
    public static final double      TORPEDO_BUTTON_Y                    = HEIGHT * 0.7;
    public static final double      TORPEDO_BUTTON_R                    = 64 * SCALING_FACTOR * 0.5;
    public static final double      TORPEDO_BUTTON_CX                   = TORPEDO_BUTTON_X + TORPEDO_BUTTON_R;
    public static final double      TORPEDO_BUTTON_CY                   = TORPEDO_BUTTON_Y + TORPEDO_BUTTON_R;
    public static final double      ROCKET_BUTTON_X                     = 15;
    public static final double      ROCKET_BUTTON_Y                     = HEIGHT * 0.8;
    public static final double      ROCKET_BUTTON_R                     = 64 * SCALING_FACTOR * 0.5;
    public static final double      ROCKET_BUTTON_CX                    = ROCKET_BUTTON_X + ROCKET_BUTTON_R;
    public static final double      ROCKET_BUTTON_CY                    = ROCKET_BUTTON_Y + ROCKET_BUTTON_R;
    public static final double      SHIELD_BUTTON_X                     = 15;
    public static final double      SHIELD_BUTTON_Y                     = HEIGHT * 0.9;
    public static final double      SHIELD_BUTTON_R                     = 64 * SCALING_FACTOR * 0.5;
    public static final double      SHIELD_BUTTON_CX                    = SHIELD_BUTTON_X + SHIELD_BUTTON_R;
    public static final double      SHIELD_BUTTON_CY                    = SHIELD_BUTTON_Y + SHIELD_BUTTON_R;
    public static final double      ENEMY_ROCKET_EXPLOSION_FRAME_WIDTH  = 128 * SCALING_FACTOR;
    public static final double      ENEMY_ROCKET_EXPLOSION_FRAME_HEIGHT = 128 * SCALING_FACTOR;
    public static final double      ASTEROID_EXPLOSION_FRAME_WIDTH      = 128 * SCALING_FACTOR;
    public static final double      ASTEROID_EXPLOSION_FRAME_HEIGHT     = 128 * SCALING_FACTOR;
    public static final double      ASTEROID_EXPLOSION_FRAME_CENTER     = 128 * SCALING_FACTOR * 0.5;
    public static final double      EXPLOSION_FRAME_WIDTH               = 256 * SCALING_FACTOR;
    public static final double      EXPLOSION_FRAME_HEIGHT              = 256 * SCALING_FACTOR;
    public static final double      UP_EXPLOSION_FRAME_WIDTH            = 100 * SCALING_FACTOR;
    public static final double      UP_EXPLOSION_FRAME_HEIGHT           = 100 * SCALING_FACTOR;
    public static final double      UP_EXPLOSION_FRAME_CENTER           = 100 * SCALING_FACTOR * 0.5;
    public static final double      SPACESHIP_EXPLOSION_FRAME_WIDTH     = 100 * SCALING_FACTOR;
    public static final double      SPACESHIP_EXPLOSION_FRAME_HEIGHT    = 100 * SCALING_FACTOR;
    public static final double      SPACESHIP_EXPLOSION_FRAME_CENTER    = 100 * SCALING_FACTOR * 0.5;
    public static final double      ROCKET_EXPLOSION_FRAME_WIDTH        = 192 * SCALING_FACTOR;
    public static final double      ROCKET_EXPLOSION_FRAME_HEIGHT       = 192 * SCALING_FACTOR;
    public static final double      ROCKET_EXPLOSION_FRAME_CENTER       = 192 * SCALING_FACTOR * 0.5;
    public static final double      HIT_FRAME_WIDTH                     = 80 * SCALING_FACTOR;
    public static final double      HIT_FRAME_HEIGHT                    = 80 * SCALING_FACTOR;
    public static final double      HIT_FRAME_CENTER                    = 80 * SCALING_FACTOR * 0.5;
    public static final double      ENEMY_HIT_FRAME_WIDTH               = 80 * SCALING_FACTOR;
    public static final double      ENEMY_HIT_FRAME_HEIGHT              = 80 * SCALING_FACTOR;
    public static final double      ENEMY_HIT_FRAME_CENTER              = 80 * SCALING_FACTOR * 0.5;
    public static final double      ENEMY_BOSS_EXPLOSION_FRAME_WIDTH    = 200 * SCALING_FACTOR;
    public static final double      ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT   = 200 * SCALING_FACTOR;
    public static final double      LEVEL_BOSS_EXPLOSION_FRAME_WIDTH    = 256 * SCALING_FACTOR;
    public static final double      LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT   = 256 * SCALING_FACTOR;
    public static final double      LEVEL_BOSS_EXPLOSION_FRAME_CENTER   = 256 * SCALING_FACTOR * 0.5;
    public static final double      SCORE_FONT_SIZE                     = 60 * (IS_PORTRAIT_MODE ? SCALING_FACTOR / 2 : SCALING_FACTOR);
}
