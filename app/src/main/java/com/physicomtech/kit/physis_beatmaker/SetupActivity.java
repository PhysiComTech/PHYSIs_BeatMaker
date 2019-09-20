package com.physicomtech.kit.physis_beatmaker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.physicomtech.kit.physis_beatmaker.customize.OnSingleClickListener;
import com.physicomtech.kit.physis_beatmaker.customize.SerialNumberView;
import com.physicomtech.kit.physis_beatmaker.customize.SoundEffectView;
import com.physicomtech.kit.physis_beatmaker.dialog.LoadingDialog;
import com.physicomtech.kit.physis_beatmaker.dialog.MenuDialog;
import com.physicomtech.kit.physis_beatmaker.helper.PHYSIsPreferences;
import com.physicomtech.kit.physis_beatmaker.helper.SoundEffect;
import com.physicomtech.kit.physis_beatmaker.list.SoundAdapter;
import com.physicomtech.kit.physislibrary.PHYSIsBLEActivity;

public class SetupActivity extends PHYSIsBLEActivity {

    private static final String TAG = "SetupActivity";

    SerialNumberView snvSetup;
    Button btnConnect;
    SoundEffectView sevTouch1, sevTouch2, sevTouch3, sevTouch4;

    private PHYSIsPreferences preferences;
    private SoundAdapter soundAdapter;
    private SoundEffect soundEffect;
    private MenuDialog menuDialog;

    private String serialNumber = null;
    private boolean isConnected = false;

    private int TOUCH1_SOUND = 0;
    private int TOUCH2_SOUND = 0;
    private int TOUCH3_SOUND = 0;
    private int TOUCH4_SOUND = 0;

    private final int blinkColor = Color.rgb(246,184, 152);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        init();
    }

    @Override
    protected void onBLEConnectedStatus(int result) {
        super.onBLEConnectedStatus(result);
        LoadingDialog.dismiss();
        setConnectedResult(result);
    }

    @Override
    protected void onBLEReceiveMsg(String msg) {
        super.onBLEReceiveMsg(msg);
        outputSound(msg);
    }

    /*
            Event
     */
    final SerialNumberView.OnSetSerialNumberListener onSetSerialNumberListener = new SerialNumberView.OnSetSerialNumberListener() {
        @Override
        public void onSetSerialNumber(String serialNum) {
            preferences.setPhysisSerialNumber(serialNumber = serialNum);
            Log.e(TAG, "# Set Serial Number : " + serialNumber);
        }
    };

    final OnSingleClickListener onClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()){
                case R.id.btn_connect:
                    if(serialNumber == null){
                        Toast.makeText(getApplicationContext(), "PHYSIs Kit의 시리얼 넘버를 설정하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(isConnected){
                        disconnectDevice();
                    }else{
                        LoadingDialog.show(SetupActivity.this, "Connecting..");
                        connectDevice(serialNumber);
                    }
                    break;
                default:
                    showSoundItemDialog(Integer.valueOf(v.getTag().toString()));
            }
        }
    };

    /*
            Show Dialog
     */
    private void showSoundItemDialog(final int touchTag){
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_selected_sound, null);
        final TabLayout tabLayout = view.findViewById(R.id.tabs);
        final RecyclerView rcSounds = view.findViewById(R.id.rc_sound_items);
        final int[] soundIndex = new int[1];

        tabLayout.addTab(tabLayout.newTab().setText("음계"));
        tabLayout.addTab(tabLayout.newTab().setText("동물"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                soundIndex[0] = soundEffect.getStartIndex(tab.getPosition());
                soundAdapter.setSoundItems(soundEffect.getSoundList(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set Recycler Division Line
        DividerItemDecoration decoration
                = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.rc_item_division_line));
        rcSounds.addItemDecoration(decoration);
        // Set Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetupActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setItemPrefetchEnabled(true);
        rcSounds.setLayoutManager(linearLayoutManager);
        // Set Adapter
        soundAdapter = new SoundAdapter();
        rcSounds.setAdapter(soundAdapter);
        soundAdapter.setSoundItems(soundEffect.getSoundList(0));

        soundAdapter.setOnSelectedSoundItem(new SoundAdapter.OnSelectedSoundItem() {
            @Override
            public void onSoundItem(int position) {
                setTouchSound(touchTag, soundIndex[0] + position);
                menuDialog.dismiss();
            }
        });

        menuDialog = new MenuDialog();
        menuDialog.show(SetupActivity.this, null, view);
    }

    /*
            Helper Method
     */
    private void outputSound(String data){
        try{
            if(data.charAt(0) == '1'){
                soundEffect.output(TOUCH1_SOUND);
                sevTouch1.showBlink(blinkColor);
            }
            if(data.charAt(1) == '1'){
                soundEffect.output(TOUCH2_SOUND);
                sevTouch2.showBlink(blinkColor);
            }
            if(data.charAt(2) == '1'){
                soundEffect.output(TOUCH3_SOUND);
                sevTouch3.showBlink(blinkColor);
            }
            if(data.charAt(3) == '1'){
                soundEffect.output(TOUCH4_SOUND);
                sevTouch4.showBlink(blinkColor);
            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    private void setTouchSound(int touchTag, int soundIndex){
        Log.e(TAG, "# Set Touch Sound : " + touchTag + " = " + soundIndex);
        switch (touchTag){
            case 0:
                sevTouch1.setSoundInfo(soundEffect.getSoundImage(soundIndex), soundEffect.getSoundName(soundIndex));
                TOUCH1_SOUND = soundIndex;
                break;
            case 1:
                sevTouch2.setSoundInfo(soundEffect.getSoundImage(soundIndex), soundEffect.getSoundName(soundIndex));
                TOUCH2_SOUND = soundIndex;
                break;
            case 2:
                sevTouch3.setSoundInfo(soundEffect.getSoundImage(soundIndex), soundEffect.getSoundName(soundIndex));
                TOUCH3_SOUND = soundIndex;
                break;
            case 3:
                sevTouch4.setSoundInfo(soundEffect.getSoundImage(soundIndex), soundEffect.getSoundName(soundIndex));
                TOUCH4_SOUND = soundIndex;
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setConnectedResult(int state){
        // set button
        if(isConnected = state == CONNECTED){
            btnConnect.setText("Disconnect");
        }else{
            btnConnect.setText("Connect");
        }
        // show toast
        String toastMsg;
        if(state == CONNECTED){
            toastMsg = "Physi Kit와 연결되었습니다.";
        }else if(state == DISCONNECTED){
            toastMsg = "Physi Kit 연결이 실패/종료되었습니다.";
        }else{
            toastMsg = "연결할 Physi Kit가 존재하지 않습니다.";
        }
        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        preferences = new PHYSIsPreferences(getApplicationContext());
        serialNumber = preferences.getPhysisSerialNumber();

        soundEffect = new SoundEffect(getApplicationContext());

        snvSetup = findViewById(R.id.snv_setup);
        snvSetup.setSerialNumber(serialNumber);
        snvSetup.showEditView(serialNumber == null);
        snvSetup.setOnSetSerialNumberListener(onSetSerialNumberListener);

        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(onClickListener);

        sevTouch1 = findViewById(R.id.sev_touch1);
        sevTouch2 = findViewById(R.id.sev_touch2);
        sevTouch3 = findViewById(R.id.sev_touch3);
        sevTouch4 = findViewById(R.id.sev_touch4);
        sevTouch1.setButtonOnClickListener(onClickListener);
        sevTouch2.setButtonOnClickListener(onClickListener);
        sevTouch3.setButtonOnClickListener(onClickListener);
        sevTouch4.setButtonOnClickListener(onClickListener);

        setTouchSound(Integer.valueOf(sevTouch1.getButtonTag()), 0);
        setTouchSound(Integer.valueOf(sevTouch2.getButtonTag()), 0);
        setTouchSound(Integer.valueOf(sevTouch3.getButtonTag()), 0);
        setTouchSound(Integer.valueOf(sevTouch4.getButtonTag()), 0);
    }
}
