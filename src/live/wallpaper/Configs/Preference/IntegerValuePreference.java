package live.wallpaper.Configs.Preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import live.wallpaper.R;

public class IntegerValuePreference extends DialogPreference {


    private int maxValue = 100;
    private int minValue = 1;
    private int value = 50;
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
    private int getValueByProgress(int progress) {
        return progress*(maxValue-minValue)/100 + minValue;
    }
    private int getValueByProgress() {
        return getValueByProgress(progress.getProgress());
    }
    private String getTextValue(float value) {
        return Float.toString(value);
    }

    public IntegerValuePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IntegerValuePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(R.string.applySettings);
        setNegativeButtonText(R.string.cancelSettings);

        minValue = attrs.getAttributeIntValue(null, "min", minValue);
        maxValue = attrs.getAttributeIntValue(null, "max", maxValue);
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
                persistInt(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray array, int index)
    {
        return array.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            if (defaultValue == null) {
                value = getPersistedInt(value);
            } else
            {
                value = getPersistedInt((int) defaultValue);
            }
        } else {
            value = (int)defaultValue;
        }

    }

}
