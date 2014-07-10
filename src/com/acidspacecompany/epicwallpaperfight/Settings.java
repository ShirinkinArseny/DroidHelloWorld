package com.acidspacecompany.epicwallpaperfight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.acidspacecompany.epicwallpaperfight.Ads.AdBuilder;
import com.acidspacecompany.epicwallpaperfight.Configs.ConfigField;
import com.acidspacecompany.epicwallpaperfight.Configs.LocalConfigs;
import com.acidspacecompany.epicwallpaperfight.Configs.Preference.ChooseTextureActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Settings extends PreferenceActivity {
    public Settings() {

    }



    /**
     * Создает всплывающее уведомление о просьбе оставить отзыв,
     * если то необходимо. Отзыв создается если:
     * 1) Впервые были запущены настройки
     * 2) отзыв был отложен
     */
    private void popReview() {
        final boolean needReview = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("need_review", true);
        if (needReview) {
            //Создаем всплывающее уведомление
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.googlePlayReviewTitle);
            alertDialogBuilder.setMessage(R.string.googlePlayReviewSummary);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setNegativeButton(R.string.googlePlayGoReview, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Говорим пользователю "спасибо"
                    Toast.makeText(getBaseContext(), R.string.thankyou, Toast.LENGTH_LONG).show();

                    //Значит, всё-таки пользователь оставляет отзыв
                    //Создаем переход на страницу Google Play
                    try {
                        //Если у пользователя стоит Google Play -- то открывем его в приложении
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + LocalConfigs.PACKAGE_NAME)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        //Если не стоит -- то открываем в браузере
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + LocalConfigs.PACKAGE_NAME)));
                    }

                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("need_review", false);
                }
            });
            alertDialogBuilder.setPositiveButton(R.string.googlePlayDeclineReview, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("need_review", false);
                }
            });
            alertDialogBuilder.setNeutralButton(R.string.googlePlayLaterReview, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialogBuilder.create().show();
        }
    }

    //Копия настроек для сброса
    private static ArrayList<ConfigField> backupFields;

    private static Activity me;

    private static void exit() {
        me.finish();
    }


    private static void destroySettings() {
        PreferenceManager.getDefaultSharedPreferences(me).edit().clear().commit();
        PreferenceManager.setDefaultValues(me, R.xml.preferences, true);
        LocalConfigs.applySettings();
        exit();

    }

    private static Button.OnClickListener applyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Завершаем работу Activity
            exit();
        }
    };
    private static Button.OnClickListener cancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Для обработки исключений -- отключаем применение настроек
            LocalConfigs.setListening(false);

            //Нужно теперь сбросить все настройки и вернуть к стандартным значениям из бэкапа
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(me).edit();
            //Очищаем
            editor.clear();
            //Добавляем все значения из бэкапа
            for (ConfigField field : backupFields)
                switch (field.getType()) {
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

            exit();

        }
    };
    private static Button.OnClickListener restoreButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Вызываем AlertDialog с предупреждением о потере данных
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(me);
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

    private static View createButtons(LayoutInflater inflater) {
        View buttons = inflater.inflate(R.layout.buttons_settings, null);

        buttons.findViewById(R.id.applyButton).
                setOnClickListener(applyButtonListener);
        buttons.findViewById(R.id.cancelButton).
                setOnClickListener(cancelButtonListener);
        buttons.findViewById(R.id.defaultButton)
                .setOnClickListener(restoreButtonListener);

        return buttons;
    }

    private static void pauseAd() {
        AdBuilder.getAdView().pause();
    }
    private static void resumeAd() {
        AdBuilder.getAdView().resume();
    }
    private static void removeAd(ViewGroup layoutWithAd) {
        layoutWithAd.removeView(AdBuilder.getAdView());
    }

    private boolean toooooooooOld = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Создаем всплывающее уведомление о просьбе оставить отзыв
        //если то необходимо
        popReview();

        me = this;
        //Если поддерживаются Preferences
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Preference preference = new Preference();
            getFragmentManager().beginTransaction().replace(android.R.id.content, preference).commit();
        }
        else {
            //Сегодня мы выполним трудную задачу:
            //Реализуем древние настройки для древних телефонов
            toooooooooOld = true;
            //Тут мы будем использовать Deprecated методы.
            //Ну и хер с ним.

            //Вот первый
            addPreferencesFromResource(R.xml.preferences);
            //А теперь задаем LinearLayout
            ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.list).getParent().getParent().getParent();


            //Добавляем вьюшки
            //Кнопочки
            viewGroup.addView(createButtons(getLayoutInflater()));

            //Рекламка
            viewGroup.addView(AdBuilder.getAdView());

            //А теперь получем настроечки
            backupFields = LocalConfigs.getFields();

            //На этом всё
            //^_^
            //Вы молодцы ^_^
            //Знали бы вы сколько тут пришлось рефакторить
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toooooooooOld)
            resumeAd();
    }
    @Override
    public void onPause() {
        if (toooooooooOld)
            pauseAd();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        if (toooooooooOld)

            removeAd((ViewGroup)findViewById(android.R.id.list).getParent().getParent().getParent());
        super.onDestroy();
    }


    public static class Preference extends PreferenceFragment
    {
        public Preference() {

        }

        @Override
        public void onResume() {
            super.onResume();
            resumeAd();
        }

        @Override
        public void onPause() {
            super.onPause();
            pauseAd();
        }
        @Override
        public void onDestroyView() {
            removeAd((LinearLayout)getView());
            super.onDestroyView();
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            AdBuilder.getAdView().resume();

            backupFields = LocalConfigs.getFields();

            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundleSavedInstance) {
            LinearLayout linearLayout = (LinearLayout) super.onCreateView(inflater, container, bundleSavedInstance);

            linearLayout.addView(createButtons(inflater));

            linearLayout.addView(AdBuilder.getAdView());
            return linearLayout;
        }
    }
}