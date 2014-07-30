package com.acidspacecompany.epicwallpaperfight.Configs.Preference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.R;

import java.io.FileNotFoundException;

public class ChooseScaleOpacity extends Activity {
    private static final float seekBarMaxValue = 100;

    Bitmap rawPicture;
    ImageView image;
    SeekBar opacitySeekBar,scaleSeekBar;
    Button applyButton;

    Bitmap resultBitmap;
    Canvas drawings;



    float scaleMin,scaleMax,currentScale,currentOpacity;

    int backgroundColor;

    private int getNormalizedValue(float min, float max, float current) {
        return (int)((current-min)*seekBarMaxValue/(max - min));
    }
    private float getValue(float min, float max, int current) {
        return current*(max-min)/seekBarMaxValue+min;
    }

    View.OnClickListener applyButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LocalConfigs.setBackgroundOpacity(currentOpacity);
            LocalConfigs.setBackgroundScale(currentScale);
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    };

    SeekBar.OnSeekBarChangeListener opacitySeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentOpacity = getValue(0,1,progress);
            updateCanvas();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    SeekBar.OnSeekBarChangeListener scaleSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            currentScale=getValue(scaleMin,scaleMax,progress);
            updateCanvas();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    private void createCanvas() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        resultBitmap = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels,
                Bitmap.Config.ARGB_8888);

        drawings = new Canvas(resultBitmap);
    }

    private void updateCanvas() {
        drawings.drawColor(backgroundColor);

        Paint paint = new Paint();
        paint.setAlpha((int)(currentOpacity*255));

        for (float x=0; x<drawings.getWidth(); x+=currentScale) {
            for (float y = 0; y < drawings.getHeight(); y+=currentScale) {
                drawings.drawBitmap(rawPicture, null,
                        new RectF(x, y, x + currentScale, y + currentScale), paint);
            }
        }

        image.setImageDrawable(new BitmapDrawable(getResources(), resultBitmap));

    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Intent parcel = getIntent();

        if (parcel.getBooleanExtra(ChooseTextureActivity.EXTRA_TYPE, false)) {
            Uri data = parcel.getData();
            try {
                rawPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(data));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
            rawPicture = BitmapFactory.decodeResource(getResources(),
                    parcel.getIntExtra(ChooseTextureActivity.EXTRA_RESOURCE, -1));

        setContentView(R.layout.choose_scale_opacity);

        float[] background = LocalConfigs.getWorldBGColor();
        backgroundColor = Color.argb((int)(background[3]*255),
                (int)(background[0]*255),
                (int)(background[1]*255),
                (int)(background[2]*255));

        scaleMin = LocalConfigs.getBackgroundScaleMin();
        scaleMax = LocalConfigs.getBackgroundScaleMax();
        currentScale = LocalConfigs.getBackgroundScale();
        currentOpacity = LocalConfigs.getBackgroundOpacity();

        image = (ImageView)findViewById(R.id.tilingImageView);
        opacitySeekBar = (SeekBar)findViewById(R.id.opacitySeekBar);
        scaleSeekBar = (SeekBar)findViewById(R.id.scaleSeekBar);

        scaleSeekBar.setProgress(getNormalizedValue(scaleMin,scaleMax,currentScale));
        opacitySeekBar.setProgress(getNormalizedValue(0,1,currentOpacity));

        opacitySeekBar.setOnSeekBarChangeListener(opacitySeekBarListener);
        scaleSeekBar.setOnSeekBarChangeListener(scaleSeekBarListener);

        applyButton = (Button)findViewById(R.id.applyButton);
        applyButton.setOnClickListener(applyButtonClickListener);

        createCanvas();
        updateCanvas();

    }
}
