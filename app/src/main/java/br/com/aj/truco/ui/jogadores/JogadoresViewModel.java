package br.com.aj.truco.ui.jogadores;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JogadoresViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JogadoresViewModel() {
        mText = new MutableLiveData<>();
       // mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}