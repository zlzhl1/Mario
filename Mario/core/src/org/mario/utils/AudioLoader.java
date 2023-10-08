package org.mario.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 */
public class AudioLoader {
    public static Music backgroundMusic;
    public static Sound coinSound;
    public static Sound bumpSound;
    public static Sound breakBlockSound;
    public static Sound powerUpSound;
    public static Sound stompSound;
    public static Sound marioDieSound;
    public static Sound powerDownSound;

    public AudioLoader() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/background.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("audio/coin.wav"));
        bumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/bump.wav"));
        breakBlockSound = Gdx.audio.newSound(Gdx.files.internal("audio/breakBlock.wav"));
        powerUpSound = Gdx.audio.newSound(Gdx.files.internal("audio/oneUp.wav"));
        stompSound = Gdx.audio.newSound(Gdx.files.internal("audio/stomp.wav"));
        marioDieSound = Gdx.audio.newSound(Gdx.files.internal("audio/marioDies.wav"));
        powerDownSound = Gdx.audio.newSound(Gdx.files.internal("audio/powerDown.wav"));
    }
}
