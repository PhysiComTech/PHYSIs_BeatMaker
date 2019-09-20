package com.physicomtech.kit.physis_beatmaker.customize;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.physicomtech.kit.physis_beatmaker.R;

public class SoundEffectView extends LinearLayout {

    ImageView ivSound;
    Button btnSetSound;
    TextView tvSound;
    LinearLayout llSound;

    OnClickListener clickListener = null;

    private String btnText;
    private String btnTag;

    public SoundEffectView(Context context) {
        super(context);
    }

    @SuppressLint({"Recycle", "CustomViewStyleable"})
    public SoundEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        btnText = (String) context.obtainStyledAttributes(attrs, R.styleable.SoundEffectAttrs)
                .getText(R.styleable.SoundEffectAttrs_setButtonText);
        btnTag = (String) context.obtainStyledAttributes(attrs, R.styleable.SoundEffectAttrs)
                .getText(R.styleable.SoundEffectAttrs_setButtonTag);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.view_touch_sound, this, false);
        addView(view);

        ivSound = view.findViewById(R.id.img_touch);
        btnSetSound = view.findViewById(R.id.btn_touch);
        btnSetSound.setText(btnText);
        btnSetSound.setTag(btnTag);
        tvSound = view.findViewById(R.id.tv_touch);
        llSound = view.findViewById(R.id.ll_touch);
    }

    public void setButtonOnClickListener(OnClickListener clickListener){
        this.clickListener = clickListener;
        btnSetSound.setOnClickListener(clickListener);
    }

    public void setSoundInfo(Drawable img, String sound){
        ivSound.setImageDrawable(img);
        tvSound.setText(sound);
    }

    public void setButtonTag(String tag){
        btnSetSound.setTag(tag);
    }


    public String getButtonTag(){
        return btnSetSound.getTag().toString();
    }


    @SuppressLint("WrongConstant")
    public void showBlink(int blinkColor){
        ObjectAnimator anim = ObjectAnimator.ofInt(llSound, "backgroundColor", Color.WHITE, blinkColor, Color.WHITE);
        anim.setDuration(300);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(2);
        anim.start();
    }
}
