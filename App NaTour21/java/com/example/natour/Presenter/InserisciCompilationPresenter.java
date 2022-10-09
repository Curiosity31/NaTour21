package com.example.natour.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.InserisciCompilationActivity;
import com.example.natour.Compilation.Compilation;
import com.example.natour.Compilation.CompilationAdapter;
import com.example.natour.DAO.CompilationDAO;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.Itinerario.Itinerario;
import com.example.natour.R;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import timber.log.Timber;

public class InserisciCompilationPresenter {
    private final InserisciCompilationActivity inserisciCompilationActivity;
    private final Context appContext;

    public InserisciCompilationPresenter(InserisciCompilationActivity inserisciCompilationActivity) {
        this.inserisciCompilationActivity = inserisciCompilationActivity;
        appContext = inserisciCompilationActivity.getApplicationContext();
    }

    public void onBtnSalvaCompilationClicked(RecyclerView recycle_itinerari_compilation) {
        CompilationDAO compilationDAO = DAOFactory.getDaoFactory().getCompilationDAO();
        CompilationAdapter compilationAdapter = (CompilationAdapter) recycle_itinerari_compilation.getAdapter();
        assert compilationAdapter != null;
        List<Itinerario> itinerarioList = compilationAdapter.getLista_itinerari_checked();
        String nomeCompilation = inserisciCompilationActivity.getNomeCompilation();
        String descrizioneCompilation = inserisciCompilationActivity.getDescrizioneCompilation();
        if (itinerarioList.size() == 0) {
            Toast.makeText(appContext, R.string.Error_list_empty, Toast.LENGTH_LONG).show();
        } else if (nomeCompilation.equals("")) {
            inserisciCompilationActivity.setErrorNomeCompilation("Inserire il nome della compilation");
        }else if (descrizioneCompilation.equals("")) {
            inserisciCompilationActivity.setErrorDescrizioneCompilation("Inserire la descrizione della compilation");
        } else {
            Compilation compilation = new Compilation(nomeCompilation, descrizioneCompilation, itinerarioList);
            final String email;
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
                        compilationDAO.addCompilation(compilation, jsonObject.optString("email"));
                        Toast.makeText(appContext, R.string.Compilation_salvata, Toast.LENGTH_LONG).show();
                        inserisciCompilationActivity.startActivity(new Intent(appContext, HomeActivity.class));
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,email,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                return;
            }
            else {
                email = Cognito.getCognitoInstance(appContext).getUserPool().getCurrentUser().getUserId();
            }

            compilationDAO.addCompilation(compilation, email);
            Toast.makeText(appContext, R.string.Compilation_salvata, Toast.LENGTH_LONG).show();
            inserisciCompilationActivity.startActivity(new Intent(appContext, HomeActivity.class));
        }
    }
}
