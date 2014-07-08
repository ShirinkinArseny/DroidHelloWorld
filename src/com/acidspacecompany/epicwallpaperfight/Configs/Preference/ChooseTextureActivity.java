package com.acidspacecompany.epicwallpaperfight.Configs.Preference;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.R;

public class ChooseTextureActivity extends Activity {
    //Массив id картинок
    private static final int[] images = new int[] {
            R.drawable.grid, R.drawable.tex2,
            R.drawable.blood, R.drawable.blue,
            R.drawable.bottom, R.drawable.bluetower
    };

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        setContentView(R.layout.choose_texture_preference);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        final float[] background = LocalConfigs.getWorldBGColor();
        gridview.setBackgroundColor(Color.argb((int)(background[3]*255), (int)(background[0]*255), (int)(background[1]*255), (int)(background[2]*255)));

        gridview.setAdapter(new ImageAdapter(this));

    }

    private class ImageAdapter extends BaseAdapter {
        private int getLandDiv2() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            final int width = displayMetrics.widthPixels;
            final int height = displayMetrics.heightPixels;
            return (width<height) ? width / 2 : width / 3;
        }

        private Context mContext;

        protected ImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView==null) {
                imageView = new ImageView(mContext);
                int size = getLandDiv2();
                imageView.setLayoutParams(new GridView.LayoutParams(size, size));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                final float[] borders = LocalConfigs.getWorldBoardersColor();
                imageView.setBackgroundColor(Color.argb((int)(borders[3]*255), (int)(borders[0]*255), (int)(borders[1]*255), (int)(borders[2]*255)));
            }
            else {
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(images[position]);
            return imageView;
        }
    }
}
