package com.example.natour.Client;

import androidx.annotation.NonNull;

public interface Retrieve {

    void onSuccessBoolean(@NonNull Boolean value);

    void onError(@NonNull Throwable throwable);
}
