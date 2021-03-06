package com.matejdro.pebblenotificationcenter.appsetting;

import android.content.Context;
import android.content.SharedPreferences;
import com.matejdro.pebblecommons.util.PreferencesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Matej on 16.9.2014.
 */
public class SharedPreferencesAppStorage implements AppSettingStorage
{
    private DefaultAppSettingsStorage defaultConfig;
    private SharedPreferences appConfig;
    private SharedPreferences.Editor editor;
    private String appPackage;
    private boolean obeyDefaultSettingsOption;

    public SharedPreferencesAppStorage(Context context, String appPackage, DefaultAppSettingsStorage defaultConfig)
    {
        this.defaultConfig = defaultConfig;
        appConfig = context.getSharedPreferences(getSharedPreferencesName(appPackage), Context.MODE_PRIVATE);
        editor = appConfig.edit();
        this.appPackage = appPackage;
    }

    @Override
    public String getString(AppSetting setting)
    {
        if (!appConfig.contains(setting.getKey()))
            return defaultConfig.getString(setting);

        return appConfig.getString(setting.getKey(), null);
    }

    @Override
    public boolean getBoolean(AppSetting setting)
    {
        if (!appConfig.contains(setting.getKey()))
            return defaultConfig.getBoolean(setting);

        return appConfig.getBoolean(setting.getKey(), false);
    }

    @Override
    public int getInt(AppSetting setting)
    {
        if (!appConfig.contains(setting.getKey()))
            return defaultConfig.getInt(setting);

        return appConfig.getInt(setting.getKey(), 0);
    }

    @Override
    public List<String> getStringList(AppSetting setting)
    {
        if (!appConfig.contains(setting.getKey()))
            return defaultConfig.getStringList(setting);

        List<String> list = new ArrayList<String>();
        PreferencesUtil.loadCollection(appConfig, list, setting.getKey());

        return list;
    }

    @Override
    public void setString(AppSetting setting, String val)
    {
        editor.putString(setting.getKey(), val);
        editor.apply();
    }

    @Override
    public void setBoolean(AppSetting setting, boolean val)
    {
        editor.putBoolean(setting.getKey(), val);
        editor.apply();
    }

    @Override
    public void setInt(AppSetting setting, int val)
    {
        editor.putInt(setting.getKey(), val);
        editor.apply();
    }

    @Override
    public void setStringList(AppSetting setting, Collection<String> val)
    {
        PreferencesUtil.saveCollection(editor, val, setting.getKey());
    }

    @Override
    public boolean isAppChecked()
    {
        return defaultConfig.isAppChecked(appPackage);
    }

    @Override
    public void setAppChecked(boolean checked)
    {
        defaultConfig.setAppChecked(appPackage, checked);
    }

    @Override
    public boolean canAppSendNotifications()
    {
        return defaultConfig.canAppSendNotifications(appPackage);
    }

    public static String getSharedPreferencesName(String pkg)
    {
        return "app_".concat(filterAppName(pkg));
    }

    public static String filterAppName(String name)
    {
        return name.replaceAll("[^0-9a-zA-Z ]", "_");
    }

    public String getAppPackage()
    {
        return appPackage;
    }
}
