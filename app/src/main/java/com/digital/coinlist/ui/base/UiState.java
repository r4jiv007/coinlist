package com.digital.coinlist.ui.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UiState<T> {

    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    @Nullable
    private Throwable error;

    public UiState() {
        this.status = DataStatus.CREATED;
        this.data = null;
        this.error = null;
    }

    public UiState<T> loading() {
        this.status = DataStatus.LOADING;
        this.data = null;
        this.error = null;
        return this;
    }

    public UiState<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.error = null;
        return this;
    }

    public UiState<T> error(@NonNull Throwable error) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    public UiState<T> complete() {
        this.status = DataStatus.COMPLETE;
        return this;
    }

    @NonNull
    public DataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public enum DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE
    }
}