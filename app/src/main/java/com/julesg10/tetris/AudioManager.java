package com.julesg10.tetris;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

public class AudioManager
{
    private static final int MAX_STREAMS = 100;
    private boolean loaded;
    private SoundPool soundPool;

    public AudioManager()
    {
        this.initSound();
    }

    private void initSound()
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        else {
            this.soundPool = new SoundPool(MAX_STREAMS, android.media.AudioManager.STREAM_MUSIC, 0);
        }

        this.soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> loaded = true);
    }

    private int load(Context context,int resId)
    {
        return this.soundPool.load(context, resId,1);
    }

    public int play(int id,int loop,float volume)
    {
        if(this.loaded)
        {
            float left = volume;
            float right =  volume;
            return this.soundPool.play(id,left, right, 1, loop, 1f);
        }

        return -1;
    }
}
