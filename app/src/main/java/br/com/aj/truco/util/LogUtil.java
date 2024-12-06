package br.com.aj.truco.util;


import android.util.Log;

import br.com.aj.truco.AppConfig;

public class LogUtil {
    public static void v(String tag, String msg) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (AppConfig.AppBuild.DEBUG) {
            Log.e(tag, msg, tr);
        }
    }

}

