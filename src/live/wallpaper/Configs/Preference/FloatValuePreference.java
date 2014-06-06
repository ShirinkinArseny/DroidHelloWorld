package live.wallpaper.Configs.Preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import live.wallpaper.Configs.BicycleDebugger;
import live.wallpaper.R;

import java.util.Objects;

public class FloatValuePreference extends DialogPreference {
    private float maxValue = 100;
    private float minValue = 1;
    private float value = 50;
    //TextView для того чтобы записывать туда значение
    TextView textValue;
    //SeekBar для выбора значения
    SeekBar progress;
    //Listener для изменения TextView
    SeekBar.OnSeekBarChangeListener progressListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textValue.setText(getTextValue(getValueByProgress(progress)));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private int getProgressByValue(float value)
    {
        return (int)(100*(value-minValue)/(maxValue-minValue));
    }
    private float getValueByProgress(int progress) {
        return progress*(maxValue-minValue)/100f + minValue;
    }
    private float getValueByProgress() {
        return getValueByProgress(progress.getProgress());
    }
    private String getTextValue(float value) {
        return String.format("%.2f", value);
    }

    public FloatValuePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static String TAG;

    public FloatValuePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(R.string.applySettings);
        setNegativeButtonText(R.string.cancelSettings);

        minValue = attrs.getAttributeFloatValue(null, "min", minValue);
        maxValue = attrs.getAttributeFloatValue(null, "max", maxValue);
        TAG = attrs.getAttributeValue("android", "tag");

    }

    @Override
    protected View onCreateDialogView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.real_value_preference, null);
        textValue = (TextView)view.findViewById(R.id.textView);
        textValue.setText("Hello!");
        progress = (SeekBar)view.findViewById(R.id.seekBar);
        progress.setProgress(50);
        progress.setOnSeekBarChangeListener(progressListener);
        return view;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        textValue.setText(Float.toString(value));
        progress.setProgress(getProgressByValue(value));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            value = getValueByProgress();
            if (callChangeListener(value)) {
                persistFloat(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray array, int index)
    {
        return array.getFloat(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            if (defaultValue == null) {
                BicycleDebugger.i("Float", "Set default from null. Initial value null name: " + TAG);
                value = getPersistedFloat(value);
            } else
            {
                BicycleDebugger.i("Float", "Set default from param. Initial value " + defaultValue.toString() + " name: " + TAG);
                value = getPersistedFloat((float) defaultValue);
            }
        } else {
            BicycleDebugger.i("Float", "Set default from not restore. Initial value " + defaultValue.toString() + " name: " + TAG);
            value = (float)defaultValue;
            persistFloat(value);
        }

    }
}
