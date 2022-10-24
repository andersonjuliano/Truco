package br.com.aj.truco.ui.jogo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JogoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JogoViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}