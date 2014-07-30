package com.acidspacecompany.epicwallpaperfight.Configs.Preference;



import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.R;

import java.io.*;

public class ChooseTextureActivity extends Activity implements AbsListView.OnScrollListener {
    //Массив id картинок
    private static final int[] images = new int[] {
            R.drawable.grid, R.drawable.tex2,
            R.drawable.blood, R.drawable.blue,
            R.drawable.bottom, R.drawable.bluetower
    };

    private static final int PICTURE_REQUEST_CODE = 1;
    private static final int TILE_FILL_REQUEST_CODE = 2;
    private byte[] picture;
    private int resourceId;

    private void createLayout() {
        setContentView(R.layout.choose_texture_preference);

        GridView gridview = (GridView) findViewById(R.id.gridview);

        gridview.setBackgroundColor(colorBackground);

        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnScrollListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        createLayout();
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private boolean isLoading;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode==PICTURE_REQUEST_CODE) {
                Uri result = null;
                try {
                    result = data.getData();
                InputStream stream = getContentResolver().openInputStream(result);
                Bitmap bmpPicture = BitmapFactory.decodeStream(stream);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bmpPicture.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    picture = outputStream.toByteArray();
                    bmpPicture.recycle();

                stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent chooseTileFillActivityIntent = new Intent(this, ChooseTileFillActivity.class);
                chooseTileFillActivityIntent.putExtra(EXTRA_TYPE, true);
                chooseTileFillActivityIntent.setData(result);
                startActivityForResult(chooseTileFillActivityIntent, TILE_FILL_REQUEST_CODE);
            }

            if (requestCode==TILE_FILL_REQUEST_CODE) {

                String result;
                if (isLoading) {
                    result="FILE:";
                    //Сохраняем в память картинку
                    FileOutputStream file;
                    try {
                        file = openFileOutput(LocalConfigs.BACKGROUND_FILE_NAME, Context.MODE_PRIVATE);
                        file.write(picture);
                    } catch (FileNotFoundException e) {
                        //Не удалось создать файл
                        e.printStackTrace();
                    } catch (IOException e) {
                        //Нет места
                        Toast.makeText(this, R.string.not_enough_free_memory, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    result+=LocalConfigs.BACKGROUND_FILE_NAME;
                }
                else {
                   result="RESOURCE:"+resourceId;
                }
                result+=":"+data.getStringExtra(ChooseTileFillActivity.RESULT_EXTRA_NAME);

                LocalConfigs.getSettings().edit().putString(LocalConfigs.BACKGROUND_PREFERENCE_NAME, result).apply();
                //LocalConfigs.updateBackground();

                //Intent resultIntent = new Intent();
                //resultIntent.putExtra("result", result);
                //setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        picture = null;
        resourceId = -1;

        final float[] background = LocalConfigs.getWorldBGColor();
        colorBackground =  Color.argb((int)(background[3]*255), (int)(background[0]*255), (int)(background[1]*255), (int)(background[2]*255));

        final float[] borders = LocalConfigs.getWorldBoardersColor();
        colorBorder = Color.argb((int)(borders[3]*255), (int)(borders[0]*255), (int)(borders[1]*255), (int)(borders[2]*255));

        createLayout();
    }

    public static final String EXTRA = LocalConfigs.PACKAGE_NAME + ".Configs.Preference.ChooseTextureActivity";
    public static final String EXTRA_TYPE = EXTRA + ".TYPE";
    public static final String EXTRA_RESOURCE = EXTRA + ".RESOURCE";
    private void chooseTexture(int position) {
        //Загруженная картинка не была получена из вне, а представляет из себя ресурс
        if (position!=images.length-1) {
            resourceId=images[position];
            Intent chooseTileFillActivityIntent = new Intent(this, ChooseTileFillActivity.class);
            chooseTileFillActivityIntent.putExtra(EXTRA_TYPE, false);
            chooseTileFillActivityIntent.putExtra(EXTRA_RESOURCE, resourceId);
            startActivityForResult(chooseTileFillActivityIntent, TILE_FILL_REQUEST_CODE);
            isLoading = false;
        }
        else {
            Intent pickImage = new Intent();
            pickImage.setType("image/jpeg");
            pickImage.setAction(Intent.ACTION_GET_CONTENT);
            pickImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pickImage, PICTURE_REQUEST_CODE);
            isLoading = true;
        }
    }

    int colorBackground;
    int colorBorder;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private static boolean isScrolled = false;
    private static void setScrollListen() {
        isScrolled = false;
    }
    private static boolean getIsScrolled() {
        return isScrolled;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isScrolled = true;
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
                imageView.setOnTouchListener(new TouchListener(position));
            }
            else {
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(images[position]);
            return imageView;
        }


         public class TouchListener implements View.OnTouchListener {
            private int position;
            private ValueAnimator animator, valueAnimator;

            public TouchListener(int position) {
                this.position = position;
            }

             private float startX,startY;

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        valueAnimator = ValueAnimator.ofFloat(0,1);
                        valueAnimator.setDuration(100);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                animate(animation, v);
                            }
                        });
                        valueAnimator.start();
                        setScrollListen();
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        float x=event.getX(), y = event.getY();
                        if (getIsScrolled() | Math.abs(x-startX)>10 | Math.abs(y-startY)>10) {
                            animator = ValueAnimator.ofFloat(1,0);
                            animator.setDuration(100);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    animate(animation, v);
                                }
                            });
                            animator.start();
                        }
                        else {
                            valueAnimator = ValueAnimator.ofFloat(1,0);
                            valueAnimator.setDuration(100);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    animate(animation, v);
                                }
                            });
                            valueAnimator.start();
                            chooseTexture(position);
                        }
                        break;
                }
                return true;
            }

            public void animate(ValueAnimator animation, View v) {
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


        }
    }
}
