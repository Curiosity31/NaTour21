package com.example.natour.AWS;


import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class Cognito {

    // Informazioni su Cognito Pool
    private final static String POOL_ID = "";
    private final static String CLIENT_ID = "";
    private final static String CLIENT_SECRET = "";
    private final static Regions AWS_REGION = Regions.DEFAULT_REGION;

    private final CognitoUserPool userPool;

    private Cognito(Context context) {
        userPool = new CognitoUserPool(context, POOL_ID, CLIENT_ID, CLIENT_SECRET, AWS_REGION);
    }

    public static Cognito getCognitoInstance (Context context) {

        return new Cognito(context);
    }

    public CognitoUserPool getUserPool() {

        return userPool;
    }

}