package com.physicomtech.kit.physis_beatmaker.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.physicomtech.kit.physis_beatmaker.R;
import com.physicomtech.kit.physis_beatmaker.customize.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {

    private List<String> soundItems = new ArrayList<>();

    private OnSelectedSoundItem onSelectedSoundItem;

    public interface OnSelectedSoundItem {
        void onSoundItem(int position);
    }

    public void setOnSelectedSoundItem(OnSelectedSoundItem listener){
        onSelectedSoundItem = listener;
    }

    @NonNull
    @Override
    public SoundHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.rc_item_sound, viewGroup, false);
        return new SoundHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundHolder soundHolder, final int i) {
        soundHolder.tvSound.setText(soundItems.get(i));
        soundHolder.tvSound.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(onSelectedSoundItem != null)
                    onSelectedSoundItem.onSoundItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundItems.size();
    }

    public void setSoundItems(List<String> items){
        soundItems = items;
        notifyDataSetChanged();
    }
}
