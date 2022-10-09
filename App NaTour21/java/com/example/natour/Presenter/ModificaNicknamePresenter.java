package com.example.natour.Presenter;

import android.content.Intent;
import android.widget.Toast;

import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.SettingsActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.Fragment.ModificaNicknameFragment;
import com.example.natour.R;

import timber.log.Timber;


public class ModificaNicknamePresenter {

    ModificaNicknameFragment modificaNicknameFragment;

    public ModificaNicknamePresenter(ModificaNicknameFragment modificaNicknameFragment) {
        this.modificaNicknameFragment = modificaNicknameFragment;
    }


    public void onBtnModificaNickNameClicked() {

        String newNickname = modificaNicknameFragment.getNickname();
        if (newNickname == null || newNickname.length() < 1) {
            modificaNicknameFragment.setTextInputNicknameError(modificaNicknameFragment.getResources().getString(R.string.Error_newNickname));
        } else {
            DAOFactory.getDaoFactory().getUtenteDAO().updateNickname(Cognito.getCognitoInstance(modificaNicknameFragment.getContext()).getUserPool().getCurrentUser().getUserId(), newNickname);
            Timber.d(modificaNicknameFragment.getResources().getString(R.string.change_nick_ok));
            Toast.makeText(modificaNicknameFragment.getContext(), R.string.change_nick_ok, Toast.LENGTH_LONG).show();
            modificaNicknameFragment.startActivity(new Intent(modificaNicknameFragment.getContext(), SettingsActivity.class));
        }
    }
}
