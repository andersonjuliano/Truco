package br.com.aj.truco.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import br.com.aj.truco.AppConfig;
import br.com.aj.truco.R;
import br.com.aj.truco.databinding.FragmentSobreBinding;


public class SobreFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        br.com.aj.truco.databinding.FragmentSobreBinding objBinding = FragmentSobreBinding.inflate(inflater, container, false);

        objBinding.aboutTextVersao.setText(getString(R.string.versao,
                AppConfig.AppBuild.VERSION_NAME, AppConfig.AppBuild.VERSION_BANCO));


        return objBinding.getRoot();

    }
}