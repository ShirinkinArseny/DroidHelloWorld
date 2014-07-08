package com.acidspacecompany.epicwallpaperfight.Configs.Preference;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.R;

public class ChooseTextureActivity extends Activity implements AdapterView.OnItemClickListener {
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
        colorBackground =  Color.argb((int)(background[3]*255), (int)(background[0]*255), (int)(background[1]*255), (int)(background[2]*255));

        final float[] borders = LocalConfigs.getWorldBoardersColor();
        colorBorder = Color.argb((int)(borders[3]*255), (int)(borders[0]*255), (int)(borders[1]*255), (int)(borders[2]*255));

        gridview.setBackgroundColor(colorBackground);

        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(this);

    }

    int colorBackground;
    int colorBorder;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView==null) {
                imageView = new ImageView(mContext);
                int size = getLandDiv2();
                imageView.setLayoutParams(new GridView.LayoutParams(size, size));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setBackgroundColor(colorBorder);
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(final View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
                            valueAnimator.setDuration(100);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    final int aB = Color.alpha(colorBackground);
                                    final int rB = Color.red(colorBackground);
                                    final int gB = Color.green(colorBackground);
                                    final int bB = Color.blue(colorBackground);

                                    final int aE = Color.alpha(colorBorder);
                                    final int rE = Color.red(colorBorder);
                                    final int gE = Color.green(colorBorder);
                                    final int bE = Color.blue(colorBorder);

                                    final float part = (float)animation.getAnimatedValue();

                                    final int aD = (int) (aB * part + aE * (1 - part));
                                    final int rD = (int) (rB * part + rE * (1 - part));
                                    final int gD = (int) (gB * part + gE * (1 - part));
                                    final int bD = (int) (bB * part + bE * (1 - part));

                                    v.setBackgroundColor(Color.argb(aD, rD, gD, bD));
                                }
                            });
                            valueAnimator.start();
                        }
                        else if (event.getAction() == MotionEvent.ACTION_UP) {
                            Toast.makeText(ChooseTextureActivity.this, "Texture " + position, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
            else {
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(images[position]);
            return imageView;
        }
    }
}
