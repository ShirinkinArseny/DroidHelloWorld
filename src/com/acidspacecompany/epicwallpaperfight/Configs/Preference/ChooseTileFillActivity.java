package com.acidspacecompany.epicwallpaperfight.Configs.Preference;

import java.io.*;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.R;

public class ChooseTileFillActivity extends Activity implements View.OnClickListener {

    ImageView fillImage, tileImage;
    Drawable fillDrawable, tileDrawable;
    int backgroundColor;


    private int previousOrientation = -1;

    private Bitmap createTiling(Bitmap pictureToTile) {
        int tileCount = 5;
        int size = pictureToTile.getHeight();
        float step = (float)size / tileCount;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Bitmap result = Bitmap.createBitmap(size,size, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(result);
        for (float x=0; x<size; x+=step) {
            for (float y = 0; y < size; y+=step) {
                canvas.drawBitmap(pictureToTile, null, new RectF(x, y, x + step, y + step), new Paint());
            }
        }

        return result;
    }

    private void recreateLayout() {
        setContentView(R.layout.choose_tile_fill);
        findViewById(R.id.choose_tile_fill_layout).setBackgroundColor(backgroundColor);
        fillImage = (ImageView)findViewById(R.id.fill_example);
        tileImage = (ImageView)findViewById(R.id.tile_example);
        fillImage.setImageDrawable(fillDrawable);
        tileImage.setImageDrawable(tileDrawable);
        fillImage.setOnClickListener(this);
        tileImage.setOnClickListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);

        if (newConfiguration.orientation != previousOrientation) {
            previousOrientation = newConfiguration.orientation;
            recreateLayout();
        }

    }

    private static final int PICTURE_REQUEST_CODE = 1;
    private Bitmap rawPicture = null;

    private void preparePictures(Bitmap rawPicture) {

        BitmapDrawable picture = new BitmapDrawable(getResources(), rawPicture);
        picture.setAntiAlias(false);
        BitmapDrawable tiledPicture = new BitmapDrawable(getResources(), createTiling(rawPicture));
        tiledPicture.setAntiAlias(false);


        fillDrawable = picture;
        tileDrawable = tiledPicture;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_REQUEST_CODE & resultCode == Activity.RESULT_OK) {
            try {
                InputStream stream = getContentResolver().openInputStream(data.getData());
                rawPicture = BitmapFactory.decodeStream(stream);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            preparePictures(rawPicture);
            recreateLayout();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Intent parcel = getIntent();

        float[] background = LocalConfigs.getWorldBGColor();
        backgroundColor = Color.argb((int) (background[3] * 255), (int) (background[0] * 255), (int) (background[1] * 255), (int) (background[2] * 255));

        if (parcel.getBooleanExtra(ChooseTextureActivity.EXTRA_TYPE, false)) {
            Uri access = parcel.getData();
            try {
                rawPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(access));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
            rawPicture = BitmapFactory.decodeResource(getResources(),
                    parcel.getIntExtra(ChooseTextureActivity.EXTRA_RESOURCE, -1));

            preparePictures(rawPicture);

            recreateLayout();


    }


    public static final String RESULT_EXTRA_NAME = "TILE_FILL_RESULT";
    public static final String FILL_RESULT = "FILL";
    public static final String TILE_RESULT = "TILE";

    @Override
    public void onClick(View v) {
        Intent result = new Intent();
        switch (v.getId()) {
            case R.id.fill_example:
                result.putExtra(RESULT_EXTRA_NAME, FILL_RESULT);
                break;
            case R.id.tile_example:
                result.putExtra(RESULT_EXTRA_NAME, TILE_RESULT);
                break;
        }
        setResult(Activity.RESULT_OK, result);
        rawPicture.recycle();
        finish();
    }
}
