package com.lin.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.customwidget.views.chatting.CustomChatBubbleContentFileLeft;
import com.customwidget.views.chatting.CustomChatBubbleContentFileRight;
import com.customwidget.views.loading.CustomLoadingWidget;
import com.customwidget.views.threepointloading.ThreePointLoadingView;

import relation.vankyshare.com.androidstudiocustomwidget.R;

/**
 * Created by Administrator on 2016/5/4.
 */
public class TestActivity extends Activity{
    CustomLoadingWidget customLoadingWidget;
    private CustomChatBubbleContentFileLeft customChatBubbleContentFileLeft;
    private CustomChatBubbleContentFileRight customChatBubbleContentFileRight;
    private ThreePointLoadingView threePointLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        threePointLoadingView = (ThreePointLoadingView) findViewById(R.id.threepointview);
        FrameLayout frameLayoutLoadingContainor = (FrameLayout) findViewById(R.id.framelayout);
        frameLayoutLoadingContainor.addView(customLoadingWidget = new CustomLoadingWidget(this), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        customChatBubbleContentFileLeft = (CustomChatBubbleContentFileLeft) findViewById(R.id.bubbleleft);
        customChatBubbleContentFileRight = (CustomChatBubbleContentFileRight) findViewById(R.id.bubbleright);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                customChatBubbleContentFileLeft.setProgress(progress);
                customChatBubbleContentFileRight.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                threePointLoadingView.startAnim();
            }
        }, 100);
    }
}
