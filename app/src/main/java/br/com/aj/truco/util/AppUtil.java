package br.com.aj.truco.util;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.aj.truco.MainActivity;
import br.com.aj.truco.R;


public class AppUtil {
    private static final String TAG = AppUtil.class.getSimpleName();

    //region Convert Numbers

    public static String formatDecimal(double v) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance();//(Constantes.DECIMAL_FORMAT_LOCAL);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return formatDecimal(v, decimalFormat);
    }

    public static String formatDecimal(double v, int digits) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance();//(Constantes.DECIMAL_FORMAT_LOCAL);
        decimalFormat.setMaximumFractionDigits(digits);
        decimalFormat.setMinimumFractionDigits(digits);
        return formatDecimal(v, decimalFormat);
    }

    public static Double parseDecimal(String string) {
        return parseDecimal(string,
                (DecimalFormat) DecimalFormat.getNumberInstance());//(Constantes.DECIMAL_FORMAT_LOCAL));
    }

    public static String formatDecimal(double v, int minDigit, int maxDigit) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance();//(Constantes.DECIMAL_FORMAT_LOCAL);
        decimalFormat.setMaximumFractionDigits(maxDigit);
        decimalFormat.setMinimumFractionDigits(minDigit);
        return formatDecimal(v, decimalFormat);
    }

    private static String formatDecimal(double v, DecimalFormat decimalFormat) {
        return decimalFormat.format(v);
    }

    private static Double parseDecimal(String string, DecimalFormat decimalFormat) {
        try {
            return decimalFormat.parse(string).doubleValue();

        } catch (ParseException e) {
//            LogUtil.w(TAG, "ParseException: " + e.getMessage());
        } catch (NullPointerException e) {
//            LogUtil.w(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //endregion

    //region Convert Dates

    public static Date convertJsonToDateTime(String jsonDatetime) {
        try {
            jsonDatetime = jsonDatetime.replaceAll("[^0-9]", "");
            return new Date(Long.parseLong(jsonDatetime));

        } catch (NumberFormatException e) {
//            LogUtil.w(TAG, "NumberFormatException: " + e.getMessage());
        } catch (NullPointerException e) {
//            LogUtil.w(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar convertDateTimeToCalendar(Date date) {
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar;

        } catch (NullPointerException e) {
            LogUtil.w(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String formatDate(Date date) {
//        return formatDate(date, DateFormat.getDateInstance());
        return formatDateTime(date, getPatternDateShort4YearDigit());
    }

    public static Date parseDate(String string) {
//        return parseDate(string, DateFormat.getDateInstance());
        return parseDateTime(string, getPatternDateShort4YearDigit());
    }

    public static String formatDateTime(Date date) {
//        return formatDate(date, DateFormat.getDateTimeInstance());
        return formatDateTime(date, getPatternDateTimeShort4YearDigit());
    }

    public static String formatDateTime(Date date, String format) {
        return formatDate(date, new SimpleDateFormat(format));
    }

    private static String getPatternDateShort4YearDigit() {
        String pattern = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT)).toPattern();
        return pattern.replaceAll("\\byy\\b", "yyyy");
    }

    private static String getPatternDateTimeShort4YearDigit() {
        String pattern = ((SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)).toPattern();
        return pattern.replaceAll("\\byy\\b", "yyyy");
    }

    public static Date parseDateTime(String string) {
//        return parseDate(string, DateFormat.getDateTimeInstance());
        return parseDateTime(string, getPatternDateTimeShort4YearDigit());
    }

    public static Date parseDateTime(String string, String format) {
        return parseDate(string, new SimpleDateFormat(format));
    }

    private static String formatDate(Date date, DateFormat dateFormat) {
        try {
            return dateFormat.format(date.getTime());

        } catch (NullPointerException e) {
            LogUtil.w(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Date parseDate(String string, DateFormat dateFormat) {
        try {
            return dateFormat.parse(string);

        } catch (ParseException e) {
            LogUtil.w(TAG, "ParseException: " + e.getMessage());
        } catch (NullPointerException e) {
            LogUtil.w(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long timeDifInSeconds(Date d1, Date d2) {
        // d1, d2 are dates
        long diff = d1.getTime() - d2.getTime();

        long diffSeconds = diff / 1000;
        return diffSeconds;
    }

    public static long timeDifInMinutes(Date d1, Date d2) {
        // d1, d2 are dates
        long diff = d1.getTime() - d2.getTime();

        long diffMinutes = diff / (60 * 1000);
        return diffMinutes;
    }

    public static double timeDifInMinutesDouble(Date d1, Date d2) {
        // d1, d2 are dates
        long diff = d1.getTime() - d2.getTime();

        double diffMinutes = (double) diff / ((double) 60 * (double) 1000);
        return diffMinutes;
    }

    public static long timeDifInHours(Date d1, Date d2) {
        // d1, d2 are dates
        long diff = d1.getTime() - d2.getTime();

        long diffHours = diff / (60 * 60 * 1000);
        return diffHours;
    }

    //endregion

    public static boolean isNumberValid(String string) {
        try {
            Double.parseDouble(replaceDecimalPoints(string));
            return true;

        } catch (NumberFormatException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String replaceDecimalPoints(String string) {
        return removeGroupPointsLocal(string).replace(',', '.');
    }

    public static String replaceDecimalPointsToLocal(String string) {
        return string.replace('.', ',');
    }

    public static String removeGroupPointsLocal(String string) {
        return string.replace(".", "");
    }

    public static void openApplication(Context context) {
//        Intent mainIntent = new Intent(context, SplashActivity.class); // A tela de aceitar abre antes da Main se usar splash
        Intent mainIntent = new Intent(context, MainActivity.class);

        PendingIntent mainPIntent = PendingIntent.getActivity(
                context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        try {
            mainPIntent.send();
        } catch (PendingIntent.CanceledException e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startServiceIfNotRunning(Context context, Class<?> serviceClass) {
        if (!isMyServiceRunning(context, serviceClass)) {
            context.startService(new Intent(context, serviceClass));
        }
    }

    public static void openWebPage(Context context, String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getUriForFile(Context context, File file) {
        // Necessário para API 24+ (Nougat)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".br.com.devbase.cluberlibrary.prestador.provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static void showAlertDialog(Context context, int message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .show();
    }

    public static void showAlertDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .show();
    }

    public static void showAlertDialogOK(Context context, int message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlertDialogOK(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlertDialogOK(Context context, int message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", listener)
                .show();
    }

    public static void showAlertDialogOK(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", listener)
                .show();
    }

    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

//    public static int dpToPx(Context context, int dp) {
//        float density = context.getResources()
//                .getDisplayMetrics()
//                .density;
//        return Math.round((float) dp * density);
//    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPx(Context context, float dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPxToDp(Context context, float px) {
        return Math.round(px / context.getResources().getDisplayMetrics().density);
    }

    public static Calendar showDatePicker(final Context context ) {
       return showDatePicker(context, false);
    }

    private static Calendar showDatePicker(final Context context, final boolean flagTime) {

        Calendar calendar = Calendar.getInstance();

        if (calendar == null) {
            calendar = GregorianCalendar.getInstance();
        }

        final Calendar calendarAux = calendar;
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendarAux.set(Calendar.YEAR, year);
                calendarAux.set(Calendar.MONTH, month);
                calendarAux.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (flagTime) {
//                    editText.setText(formatDateTime(calendarAux.getTime()));
//                    showTimePicker(context, editText);
                } else {
//                    editText.setText(formatDate(calendarAux.getTime()));
                }
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
//        dialog.setTitle(context.getString(R.string.pick_date));
        dialog.show();

        return calendarAux;
    }

//    public static void showTimePicker(final Context context, final EditText editText) {
//        Calendar calendar = AppUtil.convertDateTimeToCalendar(
//                AppUtil.parseDateTime(editText.getText().toString()));
//        if (calendar == null) {
//            calendar = GregorianCalendar.getInstance();
//        }
//
//        final Calendar calendarAux = calendar;
//        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
//
//                calendarAux.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                calendarAux.set(Calendar.MINUTE, minute);
//                calendarAux.set(Calendar.SECOND, 0);
//
//                editText.setText(
//                        formatDateTime(calendarAux.getTime()));
//
//            }
//        },
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                true
//        );
////        dialog.setTitle(context.getString(R.string.pick_date));
//        dialog.show();
//    }

//    public static void showDateTimePicker(final Context context, final EditText editText) {
//        AppUtil.showDatePicker(context,  true);
//    }


    public static String getAndroidVersion() {
        return "API " + Build.VERSION.SDK_INT + " - " + Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    /**
     * Compara strings desconsiderando acentos e alguns caracteres especiais.
     */
    public static boolean equalsIgnoreCaseAndSpecial(String string1, String string2) {
        string1 = Normalizer.normalize(string1, Normalizer.Form.NFD);
        string1 = string1.replaceAll("[^\\p{ASCII}]", "");

        string2 = Normalizer.normalize(string2, Normalizer.Form.NFD);
        string2 = string2.replaceAll("[^\\p{ASCII}]", "");

        return string1.equalsIgnoreCase(string2);
    }


    public static void shareText(Context context, String title, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }


    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    // https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
    public static boolean isOnline(int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53); // Google

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isOnline() {
        return isOnline(3000);
    }

    //endregion

    public static String getTextException(@NotNull Exception e) {
        String text = e.toString();

        StackTraceElement[] stack = e.getStackTrace();
        if (stack != null && stack.length > 0) {
            for (int i = 0; i < stack.length; i++) {
                text += "\n" + stack[i].toString();
            }
        }

        return text;
    }


    public static boolean openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
            return true;
        }

        return false;
    }

    public static boolean openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
            return true;
        }

        return openSettings(activity);
    }

    public static boolean openDevAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
            return true;
        }

        return openAppSettings(activity);
    }

    public static void setVisibleTextView(TextView textView) {
        textView.setVisibility(TextUtils.isEmpty(textView.getText()) ? View.GONE : View.VISIBLE);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    // This version of getScreenHeight excludes all bars (eg: Navigation Bar)
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static <T> List<T> asList(SparseArray<T> sparseArray) {
        if (sparseArray == null) return null;
        List<T> arrayList = new ArrayList<T>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    public static class Payload implements Serializable {
        private static int currentVersion = 1;

        private boolean flagG;
        private int version = 1;
        private String serializedData;


        public boolean isFlagG() {
            return flagG;
        }

        public int getVersion() {
            return version;
        }

        public String getSerializedData() {
            return serializedData;
        }


    }


    public static String nativeToUpperTrim(String str) {
        return nativeToUpperTrim(str, false);
    }

    public static String nativeToUpperTrim(String str, boolean flag) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        Charset utf8charset = Charset.forName("UTF-8");
        byte[] utf8bytes = str.getBytes(utf8charset);
        String strUTF8 = new String(utf8bytes, utf8charset);

        return toUpperTrimNative(strUTF8, flag ? 1 : 0);
    }


    static {
        System.loadLibrary("operations");
    }

    private static native String toUpperTrimNative(String str, int flag);

    private static native String toLowerTrimNative(String str, int flag);

    //endregion

}

