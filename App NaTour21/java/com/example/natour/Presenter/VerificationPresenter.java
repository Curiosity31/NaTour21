package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.amazonaws.services.cognitoidentityprovider.model.CodeMismatchException;
import com.amazonaws.services.cognitoidentityprovider.model.ExpiredCodeException;
import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.LoginActivity;
import com.example.natour.Fragment.VerificationFragment;
import com.example.natour.R;

import timber.log.Timber;

public class VerificationPresenter {

    private final VerificationFragment verificationFragment;
    private final Context appContext;

    public VerificationPresenter (VerificationFragment verificationFragment) {

        this.verificationFragment = verificationFragment;
        appContext = verificationFragment.getContext();
    }

    public void onBtnVerifyClicked() {

        String email = verificationFragment.getEmail();
        String confCode = verificationFragment.getConfCode();
        confirmUser(email, confCode);
    }

    private void confirmUser(String userId, String code) {

        Cognito confirm = Cognito.getCognitoInstance(appContext);
        confirm.getUserPool().getUser(userId).confirmSignUpInBackground(code,false, new GenericHandler() {

            @Override
            public void onSuccess() {

                Resources res = verificationFragment.getResources();

                // Account verificato.
                Timber.d("Registrazione avvenuta con successo.");
                Toast.makeText(appContext, R.string.Verify_ok, Toast.LENGTH_LONG).show();

                // Controllo se l'utente ha già verificato l'account.
                if(verificationFragment.getUserConfirmed()) {
                    // L'account è già stato verificato.
                    verificationFragment.setTextInputCodeError(res.getString (R.string.Error_verify));
                    Toast.makeText(appContext, R.string.Error_verify, Toast.LENGTH_LONG).show();
                }

                verificationFragment.startActivity(new Intent(appContext, LoginActivity.class));

            }

            @Override
            public void onFailure(Exception exception) {

                Resources res = verificationFragment.getResources();

                if (exception.getClass() == CodeMismatchException.class) {
                    verificationFragment.setTextInputCodeError (res.getString (R.string.Error_code_verify));
                    Toast.makeText(appContext, R.string.Error_code_verify, Toast.LENGTH_LONG).show();
                } else if (exception.getClass() == ExpiredCodeException.class){
                    verificationFragment.setTextInputCodeError (res.getString (R.string.Expired_code_verify));
                    Toast.makeText(appContext, R.string.Expired_code_verify, Toast.LENGTH_LONG).show();
                }
                Toast.makeText(appContext, R.string.Unknow_Error_verification, Toast.LENGTH_LONG).show();
                Timber.d(exception.getMessage(), "Registrazione fallita :%s");
            }
        });
    }


    public void onBtnResendClicked() {

        String email = verificationFragment.getEmail();
        CognitoUser cognitoUser = Cognito.getCognitoInstance(appContext).getUserPool().getUser(email);
        new ResendConfirmationCodeAsyncTask().execute(cognitoUser);
    }

    private static class ResendConfirmationCodeAsyncTask extends AsyncTask<CognitoUser, Void, String> {

        @Override
        protected String doInBackground(CognitoUser... cognitoUsers) {

            final String[] result = new String[1];
            VerificationHandler resendCodeHandler = new VerificationHandler() {

                @Override
                public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                    result[0] = "Codice di conferma inviato correttamente " + cognitoUserCodeDeliveryDetails.getDestination();
                }

                @Override
                public void onFailure(Exception exception) {
                    result[0] = exception.getLocalizedMessage();
                }

            };

            cognitoUsers[0].resendConfirmationCode(resendCodeHandler);
            return result[0];
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Timber.d("Risultato rinvio codice :%s", result);
        }
    }

}
