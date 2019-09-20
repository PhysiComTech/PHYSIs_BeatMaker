package com.physicomtech.kit.physis_beatmaker.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.physicomtech.kit.physis_beatmaker.R;

public class SoundHolder extends ViewHolder {

    TextView tvSound;

    public SoundHolder(@NonNull View itemView) {
        super(itemView);

        tvSound = itemView.findViewById(R.id.tv_sound_item);
    }
}
