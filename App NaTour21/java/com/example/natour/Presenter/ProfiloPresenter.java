package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.ProfiloActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.ItinerarioDAO;
import com.example.natour.R;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import timber.log.Timber;


public class ProfiloPresenter {

    private final ProfiloActivity profiloActivity;
    private final Context appContext;
    private final TextView tv_nickname_profilo;
    private final Bundle bundle;
    private final RecyclerView recycle_itinerari_profilo;
    private final ItinerarioDAO itinerarioDAO;

    public ProfiloPresenter(ProfiloActivity profiloActivity, Bundle savedInstanceState) {
        this.profiloActivity = profiloActivity;
        itinerarioDAO = DAOFactory.getDaoFactory().getItinerarioDAO();
        appContext = profiloActivity.getApplicationContext();
        bundle = savedInstanceState;
        tv_nickname_profilo = profiloActivity.findViewById(R.id.tv_nickname_profilo);
        recycle_itinerari_profilo = profiloActivity.findViewById(R.id.recycle_itinerari_profilo);
        recycle_itinerari_profilo.setLayoutManager(new LinearLayoutManager(appContext));
        recycle_itinerari_profilo.setHasFixedSize(true);
        Intent intent = profiloActivity.getIntent();
        if (intent.getBooleanExtra("Profilo", false)){
            ownProfile();
        }else {
            String email = intent.getStringExtra("emailUtente");
            itinerarioDAO.getListaItinerariByUtenteId(email, tv_nickname_profilo, appContext, bundle, recycle_itinerari_profilo, profiloActivity);
            Timber.d(String.valueOf(R.string.lista_itinerari_utente));
        }
    }

    private void ownProfile() {
        String email = null;
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(appContext);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (acct != null) {
            email = acct.getEmail();
        }
        else if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, (jsonObject, graphResponse) -> {

                assert graphResponse != null;
                FacebookRequestError facebookRequestError = graphResponse.getError();
                if (facebookRequestError != null) {
                    Timber.d("Facebook: GraphResponse Error%s", facebookRequestError);
                } else {
                    String jsonresult = String.valueOf(jsonObject);
                    Timber.d("Facebook: JSON Result: %s", jsonresult);

                    assert jsonObject != null;
                    itinerarioDAO.getListaItinerariByUtenteId(jsonObject.optString("email"), tv_nickname_profilo, appContext, bundle, recycle_itinerari_profilo, profiloActivity);

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "first_name,email,last_name");
            request.setParameters(parameters);
            request.executeAsync();
            Timber.d(String.valueOf(R.string.lista_itinerari_utente));
        }
        else {
            email = Cognito.getCognitoInstance(appContext).getUserPool().getCurrentUser().getUserId();
        }

        itinerarioDAO.getListaItinerariByUtenteId(email, tv_nickname_profilo, appContext, bundle, recycle_itinerari_profilo, profiloActivity);
        Timber.d(String.valueOf(R.string.lista_itinerari_utente));
    }


    public void onBtnTornaHomeClicked() {
        profiloActivity.startActivity(new Intent(appContext, HomeActivity.class));
    }
}
