package br.com.aj.truco;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.aj.truco.network.ErrorMessage;
import tr.xip.errorview.ErrorView;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String STATE_IS_ERROR_VISIBLE = "STATE_IS_ERROR_VISIBLE";

    private boolean mOverrideHomeAsUp = false;
    private boolean onPause_onDestroy = false;

    private boolean isTransactionSafe;
    private boolean isTransactionPending;
    private boolean isStateErrorVisible;
    private Bundle transactionPendingBundle;

    private ViewGroup mViewData;
    private ErrorView mErrorView;
    private ProgressDialog progressDialog;

    protected Typeface tfRegular;
    protected Typeface tfLight;
    protected Typeface tfBold;
    protected Typeface tfExtraBold;

    //region Activity - Eventos

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mOverrideHomeAsUp) {
                    onBackPressed();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /*
    onPostResume is called only when the activity's state is completely restored. In this we will
    set our boolean variable to true. Indicating that transaction is safe now
    */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        isTransactionSafe = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isStateErrorVisible = savedInstanceState.getBoolean(STATE_IS_ERROR_VISIBLE);
        }
        isTransactionSafe = true;

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        tfBold = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");
        tfExtraBold = Typeface.createFromAsset(getAssets(), "OpenSans-ExtraBold.ttf");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTransactionSafe = false;

        if (isFinishing()) {
            onPause_onDestroy = true;
            onPause_onDestroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isFinishing() || !onPause_onDestroy) {
            onPause_onDestroy = true;
            onPause_onDestroy();
        }
    }

    /**
     * Método necessário para finalizar tarefas antes de executar o onCreate.
     * Em alguns casos o onStop e onDestroy é executado após o onCreate, podendo impedir determinada tasks necessárias.
       */
    protected void onPause_onDestroy() {
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        boolean isErrorVisible = mErrorView != null && mErrorView.getVisibility() == View.VISIBLE;
        outState.putBoolean(STATE_IS_ERROR_VISIBLE, isErrorVisible);
        super.onSaveInstanceState(outState);
    }

    //endregion

    //region Métodos

    public void setDisplayHomeAsUpEnabled(boolean b) {
        setDisplayHomeAsUpEnabled(b, false);
    }

    public void setDisplayHomeAsUpEnabled(boolean b, boolean overrideHomeAsUp) {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(b);
            mOverrideHomeAsUp = overrideHomeAsUp;
        }
    }

    public void setCustomToolbar(int toolbarId) {
        setCustomToolbar(toolbarId, false);
    }

    public void setCustomToolbar(int toolbarId, boolean displayShowTitleEnabled) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(displayShowTitleEnabled);
    }

    //region Save Instance

    public boolean isTransactionSafe() {
        return isTransactionSafe;
    }

    public boolean isTransactionPending() {
        return isTransactionPending;
    }

    public boolean isStateErrorVisible() {
        return isStateErrorVisible;
    }

    public void setTransactionPending(boolean transactionPending, Bundle transactionPendingBundle) {
        isTransactionPending = transactionPending;
        this.transactionPendingBundle = transactionPendingBundle;
    }

    public void clearTransactionPending() {
        isTransactionPending = false;
        transactionPendingBundle = null;
    }

    public Bundle getTransactionPendingBundle() {
        return transactionPendingBundle == null ? new Bundle() : transactionPendingBundle;
    }

    //endregion

    //region Progress Dialog

    public void showProgressDialog(Context context, CharSequence title,
                                   CharSequence message) {
        showProgressDialog(context, title, message, false);
    }

    public void showProgressDialog(Context context, CharSequence title,
                                   CharSequence message, boolean indeterminate) {
        showProgressDialog(context, title, message, indeterminate, false, null);
    }

    public void showProgressDialog(Context context, CharSequence title,
                                   CharSequence message, boolean indeterminate, boolean cancelable) {
        showProgressDialog(context, title, message, indeterminate, cancelable, null);
    }

    public void showProgressDialog(Context context, CharSequence title,
                                   CharSequence message, boolean indeterminate,
                                   boolean cancelable, DialogInterface.OnCancelListener cancelListener) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(context, title, message, indeterminate, cancelable, cancelListener);
        }
    }

    public void dismissProgressDialog() {
        if (isShowingProgressDialog()) {
            progressDialog.dismiss();
        }
    }

    public boolean isShowingProgressDialog() {
        return (progressDialog != null && progressDialog.isShowing());
    }

    //endregion

    //region ViewDate / ErrorView

    public void setupErrorView(int errorViewId) {
        mErrorView = findViewById(errorViewId);
    }

    public void setupErrorView(int errorViewId, int viewDataId) {
        mErrorView = findViewById(errorViewId);
        mViewData = findViewById(viewDataId);
    }

    public void hideErrorView() {
        if (mViewData != null) {
            mViewData.setVisibility(View.VISIBLE);
        }
        mErrorView.setVisibility(View.GONE);
        mErrorView.setSubtitle(null);
        mErrorView.setRetryText(null);
    }

    public void showErrorView(String subtitle, ErrorView.RetryListener listener) {
        isStateErrorVisible = false;
        if (mViewData != null) {
            mViewData.setVisibility(View.GONE);
        }
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setSubtitle(subtitle);
        mErrorView.setRetryText(R.string.app_error_view_retry);
        mErrorView.setRetryListener(listener);
    }

    public void showEmptyView(String subtitle) {
        showEmptyView(subtitle, null, null);
    }

    public void showEmptyView(String subtitle, String retryText, ErrorView.RetryListener listener) {
        isStateErrorVisible = false;
        if (mViewData != null) {
            mViewData.setVisibility(View.GONE);
        }
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setSubtitle(subtitle);
        mErrorView.setRetryText(retryText);
        mErrorView.setRetryListener(listener);

        mErrorView.setRetryVisible(listener != null);
    }

    public void setViewDataVisible(boolean visible) {
        mViewData.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    //endregion

    //region Show ErrorMessage

    public void showErrorView(ErrorMessage errorMessage, String defaultMessage, ErrorView.RetryListener listener) {

        showErrorView(
                errorMessage.getMessageOrDefault(defaultMessage),
                listener);
    }

    public void showErrorToast(Context context, ErrorMessage errorMessage, String defaultMessage) {

        Toast.makeText(context,
                errorMessage.getMessageOrDefault(defaultMessage),
                Toast.LENGTH_LONG).show();
    }

    public void showErrorAlert(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.app_error_view_retry, listener)
                .setNegativeButton(R.string.app_error_view_retry_no, null)
                .show();
    }

    //endregion

    //endregion

}
