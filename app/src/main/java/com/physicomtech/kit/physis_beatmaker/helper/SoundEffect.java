package com.physicomtech.kit.physis_beatmaker.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.physicomtech.kit.physis_beatmaker.R;

import java.util.ArrayList;
import java.util.List;

public class SoundEffect {

    private Context context;
    private SoundPool soundPool = null;

    private List<String> soundNames = new ArrayList<>();                // 사운드명 리스트
    private List<Integer> soundResIDs = new ArrayList<>();              // 사운드 리소스 리스트
    private List<Integer> soundIDs = new ArrayList<>();                 // 사운드 ID 리스트

    private final float LEFT_VOLUME = 1f;                               // 사운드 볼륨 ( 범위 : 0.0f ~ 1.0f )
    private final float RIGHT_VOLUME = 1f;
    private final int BLOW_PRIORITY = 0;                                // 우선 순위
    private final int BLOW_LOOP = 0;                                    // 반복 여부 ( -1 : 무한 반복, 0 : 반복 X )
    private final float BLOW_RATE = 1f;                                 // 재생 속도 ( 범위 : 0.5f ~ 2.0f )

    // 사운도 종류 구분 ( SetDialog = Tab Tag )
    public static final int SCALE_SOUND = 0;
    public static final int ANIMAL_SOUND = 1;

    public int startScaleIndex = 0;
    public int startAnimalIndex = 0;

    public SoundEffect(Context context){
        this.context = context;
        setSoundList();
        createSoundPool();
    }

    /*
        # 효과음 추가 및 항목 설정
     */
    private void setSoundList() {
        startScaleIndex = soundNames.size();
        addItem("음계-도", R.raw.scale_c1);
        addItem("음계-레", R.raw.scale_d1);
        addItem("음계-미", R.raw.scale_e1);
        addItem("음계-파", R.raw.scale_f1);
        addItem("음계-솔", R.raw.scale_g1);
        addItem("음계-라", R.raw.scale_a2);
        addItem("음계-시", R.raw.scale_b2);

        startAnimalIndex = soundNames.size();
        addItem("동물-강아지", R.raw.dog);
        addItem("동물-염소", R.raw.goat);
        addItem("동물-오리", R.raw.duck);
        addItem("동물-닭", R.raw.chicken);
    }


    /*
        # 사운드 항목 추가
     */
    private void addItem(String name, int resID) {
        if(soundNames.contains(name))
            return;
        soundNames.add(name);
        soundResIDs.add(resID);
        soundIDs.add(0);
    }

    /*
        # 사운드 풀 생성 및 설정
        - 사운드풀은 효과음을 출력하기 위해 필요한 클래스
     */
    private void createSoundPool(){
        // 안드로이드 버전에 따른 객체 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes).setMaxStreams(soundNames.size()).build();
        }
        else {
            soundPool = new SoundPool(soundNames.size(), AudioManager.STREAM_NOTIFICATION, 0);
        }

        // 로드 리스너 이벤트 설정
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                // 사운드 출력
                soundPool.play(sampleId, LEFT_VOLUME, RIGHT_VOLUME, BLOW_PRIORITY, BLOW_LOOP, BLOW_RATE);
            }
        });
    }

    /*
        # 사운드 출력
        - 사운드 ID 의 유무에 따라서 사운드 출력
        - 사운드 ID 존재 시, play 함수를 통해 바로 실행 가능
        - 사운드 ID가 없을 경우, load 함수를 통해 파일을 읽어들여 로드 리스너 이벤트를 통해 출력
     */
    public void output(int index){
        if(soundIDs.get(index) == 0){
            soundIDs.set(index, soundPool.load(context, soundResIDs.get(index), 1));
        }else{
            soundPool.play(soundIDs.get(index), LEFT_VOLUME, RIGHT_VOLUME, BLOW_PRIORITY, BLOW_LOOP, BLOW_RATE);
        }
    }

    /*
        # 설정(등록된) 사운드명 리스트 리턴
     */
    public List<String> getSoundItems(){
        return soundNames;
    }

    public List<String> getSoundList(int soundType){
        if(soundType == SCALE_SOUND){
            return soundNames.subList(startScaleIndex, startAnimalIndex);
        }else{
            return soundNames.subList(startAnimalIndex, soundNames.size());
        }
    }

    public String getSoundName(int index){
        return soundNames.get(index);
    }

    public Drawable getSoundImage(int index){
        if(index < startAnimalIndex)
            return context.getDrawable(R.drawable.ic_scale);
        else
            return context.getDrawable(R.drawable.ic_animal);
    }

    public int getStartIndex(int soundType){
        if(soundType == SCALE_SOUND){
            return startScaleIndex;
        }else{
            return startAnimalIndex;
        }
    }
}
