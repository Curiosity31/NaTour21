package com.example.natour.Presenter;

import android.view.View;

import com.example.natour.Activity.SettingsActivity;
import com.example.natour.Fragment.InformazioniFragment;
import com.example.natour.Fragment.ModificaNicknameFragment;
import com.example.natour.Fragment.ModificaPasswordFragment;
import com.example.natour.R;

public class SettingsPresenter {

    private final SettingsActivity settingsActivity;

    public SettingsPresenter (SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
        settingsActivity.getResources();
    }

    public void onBtnModificaPassClicked() {
        settingsActivity.setTitle(R.string.Modifica_Password);
        clearActivity();
        ModificaPasswordFragment modificaPasswordFragment = new ModificaPasswordFragment();
        settingsActivity.getSupportFragmentManager().beginTransaction().replace(R.id.modifica_password_fragment_settings, modificaPasswordFragment).commit();
    }

    public void onBtnModificaNicknameClicked() {
        settingsActivity.setTitle(R.string.Modifica_Nickname);
        clearActivity();
        ModificaNicknameFragment modificaNicknameFragment = new ModificaNicknameFragment();
        settingsActivity.getSupportFragmentManager().beginTransaction().replace(R.id.modifica_nickname_fragment_settings, modificaNicknameFragment).commit();
    }

    public void onBtnInfoClicked() {
        settingsActivity.setTitle(R.string.Informazioni);
        clearActivity();
        InformazioniFragment informazioniFragment = new InformazioniFragment();
        settingsActivity.getSupportFragmentManager().beginTransaction().replace(R.id.informazioni_fragment_settings, informazioniFragment).commit();
    }

    private void clearActivity () {

        settingsActivity.findViewById(R.id.btn_info).setVisibility(View.GONE);
        settingsActivity.findViewById(R.id.btn_Modifica_pass).setVisibility(View.GONE);
        settingsActivity.findViewById(R.id.btn_Modifica_nickname).setVisibility(View.GONE);
        settingsActivity.findViewById(R.id.imageView3).setVisibility(View.GONE);
    }

}
