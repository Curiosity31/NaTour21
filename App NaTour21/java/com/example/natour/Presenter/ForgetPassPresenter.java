package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.services.cognitoidentityprovider.model.CodeMismatchException;
import com.amazonaws.services.cognitoidentityprovider.model.ExpiredCodeException;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidParameterException;
import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.LoginActivity;
import com.example.natour.Fragment.ForgetPassFragment;
import com.example.natour.R;

import timber.log.Timber;

public class ForgetPassPresenter {
    
    private final ForgetPassFragment forgetPassFragment;
    private final Context appContext;
    private ForgotPasswordContinuation resultContinuation;
    private final Resources res;

    public ForgetPassPresenter (ForgetPassFragment forgetPassFragment) {

        this.forgetPassFragment = forgetPassFragment;
        appContext = forgetPassFragment.getContext();
        res = forgetPassFragment.getResources();
    }

    private boolean isValidEmail (String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onBtnRecAccountClicked(LoginActivity loginActivity) {

        String userId = forgetPassFragment.getEmail();

        if (userId.length() == 0) {
            forgetPassFragment.setTextInputEmailError(res.getString(R.string.Error_rec_email));
        } else if (!isValidEmail(userId)) {
            forgetPassFragment.setTextInputEmailError(res.getString(R.string.Error_invalid_email_rec));
        } else {

            Cognito recover = Cognito.getCognitoInstance(appContext);
            recover.getUserPool().getUser(userId).forgotPasswordInBackground(new ForgotPasswordHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(appContext, R.string.pass_changed, Toast.LENGTH_LONG).show();
                    Timber.d(res.getString(R.string.pass_changed));

                    forgetPassFragment.startActivity(new Intent(appContext, LoginActivity.class));
                }

                @Override
                public void getResetCode(ForgotPasswordContinuation continuation) {
                    loginActivity.findViewById(R.id.etEmail_fragment_forget).setEnabled(false);
                    loginActivity.findViewById(R.id.btnRecuperaAccount).setEnabled(false);
                    loginActivity.findViewById(R.id.etNuova_pass).setEnabled(true);
                    loginActivity.findViewById(R.id.etRip_nuova_pass).setEnabled(true);
                    loginActivity.findViewById(R.id.etCodiceRecuperoPass).setEnabled(true);
                    loginActivity.findViewById(R.id.btn_conferma_recupero).setEnabled(true);

                    CognitoUserCodeDeliveryDetails codeSentHere = continuation.getParameters();
                    Timber.d("Il codice è stato inviato qui: %s", codeSentHere.getDestination());
                    resultContinuation = continuation;
                }

                @Override
                public void onFailure(Exception exception) {

                    if (exception.getClass() == CodeMismatchException.class) {
                        forgetPassFragment.setTextInputCodeError (res.getString (R.string.Error_code_verify));
                        Toast.makeText(appContext, R.string.Error_code_rec, Toast.LENGTH_LONG).show();
                    } else if (exception.getClass() == ExpiredCodeException.class){
                        forgetPassFragment.setTextInputCodeError (res.getString (R.string.Expired_code_verify));
                        Toast.makeText(appContext, R.string.Expired_code_rec, Toast.LENGTH_LONG).show();
                    } else if (exception.getClass() == InvalidParameterException.class) {
                        forgetPassFragment.setTextInputPasswordError (res.getString (R.string.Error_invalid_password));
                        Toast.makeText(appContext, R.string.Error_invalid_password, Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(appContext, R.string.Unknow_Error_rec, Toast.LENGTH_LONG).show();
                    }

                    Timber.d("Invio codice fallito: %s", exception.getLocalizedMessage());
                }
            });
        }
    }

    public void onBtnConfermaClicked() {

        if (!forgetPassFragment.getPassword().equals(forgetPassFragment.getRepeatPassword())) {
            forgetPassFragment.setTextInputRepeatPasswordError(res.getString(R.string.Error_repeat_pass_rec));
        } else if (forgetPassFragment.getPassword().length() == 0) {
            forgetPassFragment.setTextInputPasswordError(res.getString(R.string.Error_password_rec));
        } else {

            resultContinuation.setPassword(forgetPassFragment.getPassword());
            resultContinuation.setVerificationCode(forgetPassFragment.getCode());
            Timber.d("Conferma cliccato");
            resultContinuation.continueTask();
        }
    }
}
