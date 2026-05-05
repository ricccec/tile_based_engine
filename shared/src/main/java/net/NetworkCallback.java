package net;

public interface NetworkCallback {

    enum FailureType {
        /** Soft failure (e.g. wrong credentials): increment failedAttempts, disconnect after threshold */
        SOFT,
        /** Hard failure (e.g. internal error): disconnect client immediately */
        HARD
    }

    void onSuccess();

    void onFailure(FailureType type);
}
