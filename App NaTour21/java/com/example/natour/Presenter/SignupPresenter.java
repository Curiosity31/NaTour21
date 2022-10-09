package com.example.natour.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidentityprovider.model.UsernameExistsException;
import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.SignupActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.Fragment.VerificationFragment;
import com.example.natour.R;
import com.example.natour.Utente.Utente;

import timber.log.Timber;


public class SignupPresenter {

    private final SignupActivity signupActivity;
    private final Context appContext;

    public SignupPresenter (@NonNull SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
        appContext = signupActivity.getApplicationContext();
    }

    public void onBtnSignupClicked () {

        Resources res = signupActivity.getResources();
        String nome = signupActivity.getName();
        String cognome = signupActivity.getSurname();
        String nickname = signupActivity.getNickname();
        String email = signupActivity.getEmail();
        String password = signupActivity.getPassword();
        String repeat_password = signupActivity.getRepeatPassword();


        if (! password.equals(repeat_password)) {
            signupActivity.setTextInputRepeatPasswordError (res.getString (R.string.Error_repeat_pass));
        }else if (nome.length() == 0) {
            signupActivity.setTextInputNameError (res.getString (R.string.Error_signup_name));
        } else if (cognome.length() == 0) {
            signupActivity.setTextInputSurnameError (res.getString (R.string.Error_signup_cognome));
        } else if(nickname.length() == 0) {
            signupActivity.setTextInputNicknameError (res.getString (R.string.Error_signup_nickname));
        } else if(email.length() == 0) {
            signupActivity.setTextInputEmailError (res.getString (R.string.Error_signup_email));
        } else if(!isValidEmail(email)) {
            signupActivity.setTextInputEmailError (res.getString (R.string.Error_invalid_email));
        } else if(password.length() == 0) {
            signupActivity.setTextInputPasswordError (res.getString (R.string.Error_signup_password));
        }
        else {
            CognitoUserAttributes attributes = new CognitoUserAttributes();
            attributes.addAttribute("name", nome);
            attributes.addAttribute("family_name", cognome);
            attributes.addAttribute("nickname", nickname);
            attributes.addAttribute("email", email);
            registrati (attributes, email, password);
        }
    }

    private boolean isValidEmail (String email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private  void openVerificationFragment (String email, boolean userConfirmed) {

        signupActivity.setTitle("Verifica Account");
        VerificationFragment verificationFragment = VerificationFragment.newInstanceVerificationFragment(email, signupActivity, userConfirmed);
        signupActivity.getSupportFragmentManager().beginTransaction().replace(R.id.verification_fragment_signup, verificationFragment).commit();
    }

    private void registrati (CognitoUserAttributes attributes, final String email, final String password) {

        Cognito authorization = Cognito.getCognitoInstance(appContext);
        authorization.getUserPool().signUpInBackground(email, password, attributes, null, new SignUpHandler() {

            @Override
            public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {

                DAOFactory.getDaoFactory().getUtenteDAO().addUtente(new Utente(signupActivity.getName(), signupActivity.getSurname(), signupActivity.getNickname(), email, false));
                openVerificationFragment(signupActivity.getEmail(), userConfirmed);
            }

            @Override
            public void onFailure(Exception exception) {

                Resources res = signupActivity.getResources();

                if (exception.getClass() == InvalidPasswordException.class) {

                    signupActivity.setTextInputPasswordError (res.getString (R.string.Error_invalid_password));
                    Toast.makeText(appContext, R.string.Error_invalid_password, Toast.LENGTH_LONG).show();
                }
                else if (exception.getClass() == UsernameExistsException.class) {

                    signupActivity.setTextInputEmailError (res.getString (R.string.Error_email_exist));
                    Toast.makeText(appContext, R.string.Error_email_exist, Toast.LENGTH_LONG).show();
                }
                else {

                    Toast.makeText(appContext, R.string.Unknow_Error_signup, Toast.LENGTH_LONG).show();
                }

                Timber.d(String.valueOf(exception), R.string.Error_signup_failed);
            }
        });
    }

}
