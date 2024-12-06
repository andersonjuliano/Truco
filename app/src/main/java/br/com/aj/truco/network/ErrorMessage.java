package br.com.aj.truco.network;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.aj.truco.util.LogUtil;

public class ErrorMessage {

    private int code;
    private String message;
    private String obj;

    public ErrorMessage() {
        this(0, "", "");
    }

    public ErrorMessage(String message) {
        this(0, message, "");
    }

    public ErrorMessage(int code, String message, String obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getObj() {
        return obj;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", obj='" + obj + '\'' +
                '}';
    }

    //region Auxiliares

    public static ErrorMessage parseJSON(JSONObject jsonObject) {
        try {
            JSONObject jsonError = jsonObject.optJSONObject("error");
            if (jsonError != null) {
                return new ErrorMessage(jsonError.optInt("code"), jsonError.optString("message"), jsonError.optString("obj"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return null;
        return new ErrorMessage();
    }

    public static JSONObject getJSON(int code, String message) {
        try {
            JSONObject jsonError = new JSONObject();
            jsonError.put("error", new JSONObject().put("code", code).put("message", message));
            LogUtil.e("ErrorMessage", jsonError.toString());
            return jsonError;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getMessageOrDefault(String defaultValue) {
        return TextUtils.isEmpty(message) ? defaultValue : message;
    }

    //endregion

}
