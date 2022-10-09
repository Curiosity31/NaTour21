package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.LoginActivity;

import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.UtenteDAO;
import com.example.natour.Fragment.ForgetPassFragment;
import com.example.natour.Fragment.VerificationFragment;
import com.example.natour.R;

import android.content.res.Resources;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.SignupActivity;
import com.example.natour.Utente.Utente;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import java.util.Arrays;
import java.util.Objects;

import timber.log.Timber;

public class LoginPresenter {

    private final LoginActivity loginActivity;
    private final Context appContext;
    private String userPassword;
    private String userEmail;
    private final UtenteDAO utenteDAO;
    private final Resources res;
    private CallbackManager callbackManager;
    private final int RC_SIGN_IN = 0;

    public LoginPresenter(LoginActivity loginActivity) {

        this.loginActivity = loginActivity;
        appContext = loginActivity.getApplicationContext();
        res = loginActivity.getResources();
        utenteDAO = DAOFactory.getDaoFactory().getUtenteDAO();
    }


    public void onBtnLoginClicked() {

        EditText etEmail = loginActivity.findViewById(R.id.etEmail);
        EditText etPassword = loginActivity.findViewById(R.id.etPassword);
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.length() == 0) {

            loginActivity.setTextInputEmailError(res.getString(R.string.Error_email_login));
        } else if (password.length() == 0) {

            loginActivity.setTextInputPasswordError(res.getString(R.string.Error_pass_login));
        }

        userLogin(email.replace(" ", ""), password);
    }


    private void userLogin(String userId, String password) {

        Cognito authentication = Cognito.getCognitoInstance(appContext);
        CognitoUserPool userPool = authentication.getUserPool();
        CognitoUser cognitoUser = userPool.getUser(userId);
        userEmail = userId;
        userPassword = password;
        cognitoUser.getSessionInBackground(new AuthenticationHandler() {

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

                throw new UnsupportedOperationException();
            }

            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {

                utenteDAO.updateLoggedUtente(userEmail, true);
                Toast.makeText(appContext, R.string.ValidAuthentication, Toast.LENGTH_LONG).show();
                loginActivity.startActivity(new Intent(appContext, HomeActivity.class));
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {

                throw new UnsupportedOperationException();
            }

            @Override
            public void onFailure(Exception exception) {

                if (exception.getClass() == NotAuthorizedException.class) {
                    Toast.makeText(appContext, R.string.AuthenticationFailed, Toast.LENGTH_LONG).show();
                    loginActivity.setTextInputEmailError(res.getString(R.string.InvalidEmailOrPass));
                } else if (exception.getClass() == UserNotConfirmedException.class) {
                    Toast.makeText(appContext, R.string.Verification_account, Toast.LENGTH_LONG).show();
                    openVerificationFragment();
                } else {
                    Timber.d(exception, res.getString(R.string.Unknow_Error_Login));
                }
            }
        });
    }

    private void openVerificationFragment() {

        loginActivity.setTitle("Verifica Account");
        VerificationFragment verificationFragment = VerificationFragment.newInstanceVerificationFragment(userEmail, loginActivity, false);
        loginActivity.getSupportFragmentManager().beginTransaction().replace(R.id.verification_fragment_login, verificationFragment).commit();
    }

    public void onBtnNotAccountClicked() {

        loginActivity.startActivity(new Intent(appContext, SignupActivity.class));
    }


    public void onBtnGoogleClicked() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        Intent signInIntent = GoogleSignIn.getClient(appContext, gso).getSignInIntent();
        loginActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Risultato tornato dal lancio dell' Intent da GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // Il Task tornato da questa chiamata è sempre completato, non è necessario collegare un listener.
            try {
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                // Log in effettuato con successo.
                initGoogle (Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(appContext)));
                loginActivity.startActivity(new Intent(appContext, HomeActivity.class));
            } catch (ApiException e) {
                Timber.d("Login fallito: code = %s", e.getStatusCode());
            }
        }
        else {
            // Risultato tornato dal lancio dell' Intent da Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void onBtnForgetPassClicked() {

        loginActivity.setTitle("Recupera Account");
        ForgetPassFragment forgetPassFragment = ForgetPassFragment.newInstanceForgetPass(loginActivity);
        loginActivity.getSupportFragmentManager().beginTransaction().replace(R.id.ForgetPass_fragment, forgetPassFragment).commit();
    }

    public void onBtnFacebookClicked() {

        FacebookSdk.sdkInitialize(loginActivity);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(loginActivity, Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                initFacebook();
                loginActivity.startActivity(new Intent(appContext, HomeActivity.class));
            }

            @Override
            public void onCancel() {
                loginActivity.startActivity(new Intent(appContext, LoginActivity.class));
                Timber.d("Log In con facebook Cancellato :");
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                Timber.d(exception.getMessage(), "Log In con facebook fallito :%s");
            }
        });
    }


    public void initGoogle(GoogleSignInAccount acct) {

        String personGivenName = acct.getGivenName();
        String personFamilyName = acct.getFamilyName();
        String personEmail = acct.getEmail();
        //  Uri personPhoto = acct.getPhotoUrl(); Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        utenteDAO.addUtente(new Utente(personGivenName, personFamilyName, personGivenName, personEmail, false));
    }


    public void initFacebook() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, (jsonObject, graphResponse) -> {

            assert graphResponse != null;
            FacebookRequestError facebookRequestError = graphResponse.getError();
            if (facebookRequestError != null) {
                Timber.d("Facebook: GraphResponse Error%s", facebookRequestError);
            } else {
                String jsonresult = String.valueOf(jsonObject);
                Timber.d("Facebook: JSON Result: %s", jsonresult);

                assert jsonObject != null;
                //String fbUserId = jsonObject.optString("id");
                String fbUserFirstName = jsonObject.optString("first_name");
                String fbUserEmail = jsonObject.optString("email");
                String fbUserSurname = jsonObject.optString("last_name");
                //String fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";
                utenteDAO.addUtente(new Utente(fbUserFirstName, fbUserSurname, fbUserFirstName, fbUserEmail, false));
            }
            Timber.d(graphResponse.toString());
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,email,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

}