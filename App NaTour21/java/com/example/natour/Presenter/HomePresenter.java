package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.AddNewItinerarioActivity;
import com.example.natour.Activity.CompilationActivity;
import com.example.natour.Activity.FiltriActivity;
import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.InserisciCompilationActivity;
import com.example.natour.Activity.LoginActivity;
import com.example.natour.Activity.SettingsActivity;
import com.example.natour.Activity.ProfiloActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.ItinerarioDAO;
import com.example.natour.DAO.UtenteDAO;
import com.example.natour.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import timber.log.Timber;


public class HomePresenter {

    private final HomeActivity homeActivity;
    private final Context appContext;
    private final RecyclerView recycle_itinerari;
    private final Bundle savedInstanceState;
    private final UtenteDAO utenteDAO;
    private final ItinerarioDAO itinerarioDAO;

    public HomePresenter(HomeActivity homeActivity, RecyclerView recycle_itinerari, Bundle savedInstanceState, Context appContext, UtenteDAO utenteDAO) {
        this.homeActivity = homeActivity;
        homeActivity.setTitle("");
        this.utenteDAO = utenteDAO;
        itinerarioDAO = DAOFactory.getDaoFactory().getItinerarioDAO();
        this.appContext = appContext;
        this.savedInstanceState = savedInstanceState;
        this.recycle_itinerari = recycle_itinerari;
        Intent intent = homeActivity.getIntent();
        if (intent.getBooleanExtra("filtri", false)) {
            double latitudine = intent.getDoubleExtra("latitudine", -1000);
            double longitudine = intent.getDoubleExtra("longitudine", -1000);
            int min_durata = intent.getIntExtra("min_durata", -1000);
            int max_durata = intent.getIntExtra("max_durata", -1000);
            boolean [] difficolta = intent.getBooleanArrayExtra("difficolta");
            boolean accessibilita = intent.getBooleanExtra("accessibilita", false);
            itinerarioDAO.applicaFiltri(latitudine, longitudine, min_durata, max_durata, difficolta, accessibilita, recycle_itinerari, savedInstanceState, appContext, homeActivity);
        } else {
            itinerarioDAO.getListaItinerari(recycle_itinerari, appContext, savedInstanceState, homeActivity);
        }
    }

    public void refresh(SwipeRefreshLayout refreshLayout) {
        itinerarioDAO.getListaItinerari(recycle_itinerari, appContext, savedInstanceState, homeActivity);
        refreshLayout.setRefreshing(false);
    }

    public void onBtnSignOutClicked() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(appContext, gso);

        if (GoogleSignIn.getLastSignedInAccount(appContext) != null) {

            mGoogleSignInClient.signOut().addOnCompleteListener(homeActivity, task -> { });
            Timber.d(homeActivity.getResources().getString(R.string.LogOut_success));
        }
        else if (AccessToken.getCurrentAccessToken() != null) {

            LoginManager.getInstance().logOut();
        }
        else {
            CognitoUser cognitoUser = Cognito.getCognitoInstance(appContext).getUserPool().getCurrentUser();
            utenteDAO.updateLoggedUtente (cognitoUser.getUserId(), false);
            cognitoUser.globalSignOutInBackground(new GenericHandler() {
                @Override
                public void onSuccess() {
                    Timber.d(homeActivity.getResources().getString(R.string.LogOut_success));
                }

                @Override
                public void onFailure(Exception exception) {
                    Timber.d(homeActivity.getResources().getString(R.string.Error_LogOut));
                }
            });
        }
        Toast.makeText(appContext, R.string.LogOut_success, Toast.LENGTH_LONG).show();
        homeActivity.startActivity(new Intent(appContext, LoginActivity.class));
    }

    public void onBtnAddNewItinerarioClicked() {
        homeActivity.startActivity(new Intent(appContext, AddNewItinerarioActivity.class));

    }

    public void onBtnSettingClicked() {
        homeActivity.startActivity(new Intent(appContext, SettingsActivity.class));
    }

    public void onBtnFiltriClicked() {
        homeActivity.startActivity(new Intent(appContext, FiltriActivity.class));
    }


    public void onBtnAddNewCompilationClicked() {
        homeActivity.startActivity(new Intent(appContext, InserisciCompilationActivity.class));
    }

    public void onBtnVisualizzaCompilationClicked() {
        homeActivity.startActivity((new Intent(appContext, CompilationActivity.class)));
    }

    public void onBtnProfiloClicked() {
        Intent intent = new Intent(appContext, ProfiloActivity.class);
        intent.putExtra("Profilo", true);
        homeActivity.startActivity(intent);
    }

}

