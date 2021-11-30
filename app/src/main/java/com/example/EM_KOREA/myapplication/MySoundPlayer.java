package com.example.EM_KOREA.myapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import java.util.HashMap;



public class MySoundPlayer {

    private static MySoundPlayer mMySoundPlayer;
    public static final int bee = R.raw.sound_bee;
    public static final int SUCCESS = R.raw.success;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;



    // sound media initialize
    public static void initSounds(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();

        soundPoolMap = new HashMap(2);
        soundPoolMap.put(bee, soundPool.load(context, bee, 1));
        //soundPoolMap.put(SUCCESS, soundPool.load(context, SUCCESS, 2));
    }

    public static void play(int raw_id){
       // if( soundPoolMap.containsKey(raw_id) ) {
         //   soundPool.play(soundPoolMap.get(raw_id), 1, 1, 1, 0, 1f);
       // }

        soundPool.setOnLoadCompleteListener (new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundId, int status) {
                soundPool.play(soundPoolMap.get(bee), 1f, 1f, 0, 0, 3f);
            }
        });


    }





}
