package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException;
import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.SettingsActivity;
import com.example.natour.Fragment.ModificaPasswordFragment;
import com.example.natour.R;

import timber.log.Timber;

public class ModificaPasswordPresenter {

    private final ModificaPasswordFragment modificaPasswordFragment;

    public ModificaPasswordPresenter (ModificaPasswordFragment modificaPasswordFragment) {
        this.modificaPasswordFragment = modificaPasswordFragment;
    }

    private boolean isValidEmail (String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onBtnConfModificaClicked() {

        Resources res = modificaPasswordFragment.getResources();
        Context appContext = modificaPasswordFragment.getContext();

        String email = modificaPasswordFragment.getEmail();
        String password = modificaPasswordFragment.getPassword();
        String repeatPassword = modificaPasswordFragment.getRepeatPassword();
        String oldPassword = modificaPasswordFragment.getOldPassword();

        if (! password.equals(repeatPassword)) {
            modificaPasswordFragment.setTextInputRepeatPasswordError(res.getString (R.string.Error_repeat_pass_mod));
        } else if (oldPassword.length() == 0) {
            modificaPasswordFragment.setTextInputOldPassError (res.getString (R.string.old_pass_mod));
        } else if(!isValidEmail(email)) {
            modificaPasswordFragment.setTextInputEmailError (res.getString (R.string.Error_invalid_email_mod));
        } else if(password.length() == 0) {
            modificaPasswordFragment.setTextInputPasswordError (res.getString (R.string.Error_modifica_password));
        } else if (email.length() == 0){
            modificaPasswordFragment.setTextInputEmailError (res.getString (R.string.Error_email_modifica));
        } else {

            Cognito.getCognitoInstance(appContext).getUserPool().getUser(email)
                    .changePasswordInBackground(oldPassword, password, new GenericHandler() {
                        @Override
                        public void onSuccess() {
                            Timber.d("Password con successo.");
                            Toast.makeText(appContext, R.string.pass_changed_mod, Toast.LENGTH_LONG).show();
                            modificaPasswordFragment.startActivity(new Intent(appContext, SettingsActivity.class));
                        }

                        @Override
                        public void onFailure(Exception exception) {

                            if (exception.getClass() == InvalidPasswordException.class) {

                                modificaPasswordFragment.setTextInputPasswordError (res.getString (R.string.Error_invalid_password));
                                Toast.makeText(appContext, R.string.Error_invalid_password, Toast.LENGTH_LONG).show();
                            } else if (exception.getClass() == NotAuthorizedException.class) {

                                modificaPasswordFragment.setTextInputOldPassError (res.getString (R.string.Err_pass));
                                Toast.makeText(appContext, R.string.Err_pass, Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(appContext, R.string.Unknow_Error_verification, Toast.LENGTH_LONG).show();
                                Timber.d(exception.getMessage(), "Modifica password fallita :%s");
                            }
                        }
                    });
        }

    }



}
