package live.wallpaper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import live.wallpaper.Ads.AdBuilder;
import live.wallpaper.Configs.ConfigField;
import live.wallpaper.Configs.LocalConfigs;
import live.wallpaper.Configs.StringField;

import java.util.ArrayList;
import java.util.Map;

public class Settings extends PreferenceActivity {
    public Settings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Preference preference = new Preference(this);
        getFragmentManager().beginTransaction().replace(android.R.id.content, preference).commit();
    }


    @Override
    public void onPause()
    {
        super.onPause();
        //Configs.init(this);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //Configs.init(this);
    }

    public static class Preference extends PreferenceFragment
    {

        //Копия настроек для сброса
        private ArrayList<ConfigField> backupFields;

        private Button applyButton, cancelButton, restoreButton;

        private void destroySettings() {
            PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
            PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
            getActivity().finish();

        }

        private Button.OnClickListener applyButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Завершаем работу Activity
                getActivity().finish();
            }
        };
        private Button.OnClickListener cancelButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Для обработки исключений -- отключаем применение настроек
                LocalConfigs.setListening(false);

                //Нужно теперь сбросить все настройки и вернуть к стандартным значениям из бэкапа
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                //Очищаем
                editor.clear();
                //Добавляем все значения из бэкапа
                for (ConfigField field : backupFields)
                    switch (field.getType()) {
                        //Так как у нас все значения задаются на самом деле строками, а потом конвертируются --
                        //то мы помещаем именно строку
                        //А для Boolean хранится именно boolean
                        case Float:
                            editor.putFloat(field.getName(), (float)field.getValue());
                            break;
                        case Integer:
                            editor.putInt(field.getName(), (int)field.getValue());
                            break;
                        case String:
                            editor.putString(field.getName(), (String)field.getValue());
                            break;
                        case Boolean:
                            editor.putBoolean(field.getName(), (boolean)field.getValue());
                            break;
                    }

                editor.apply();

                LocalConfigs.setFields(backupFields);

                LocalConfigs.setListening(true);

                getActivity().finish();

            }
        };
        private Button.OnClickListener restoreButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Вызываем AlertDialog с предупреждением о потере данных
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(R.string.setDefaultAlertTitle);
                alertDialogBuilder.setMessage(R.string.setDefaultMessage);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setNegativeButton(R.string.cancelSettings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.setPositiveButton(R.string.setDefault, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        destroySettings();
                    }
                });
                alertDialogBuilder.create().show();
            }
        };

        private static Context context;
        private Preference(Context con) {
            context = con;
        }

        public Preference() {

        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            backupFields = LocalConfigs.getFields();

            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundleSavedInstance) {
            LinearLayout linearLayout = (LinearLayout) super.onCreateView(inflater, container, bundleSavedInstance);

            View buttons = inflater.inflate(R.layout.buttons_settings, null);

            applyButton = (Button)buttons.findViewById(R.id.applyButton);
            applyButton.setOnClickListener(applyButtonListener);
            cancelButton = (Button)buttons.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(cancelButtonListener);
            restoreButton = (Button)buttons.findViewById(R.id.defaultButton);
            restoreButton.setOnClickListener(restoreButtonListener);

            linearLayout.addView(buttons);

            //Создание диалога ожидания
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(R.string.loading);
            progressDialog.setMessage(context.getResources().getString(R.string.loadingProgress));
            progressDialog.show();


            linearLayout.addView(AdBuilder.createAdView(context, AdBuilder.bannerAtSettingsID));
            progressDialog.dismiss();
            return linearLayout;
        }
    }
}