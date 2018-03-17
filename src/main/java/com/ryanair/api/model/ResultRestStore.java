package com.ryanair.api.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ResultRestStore<T> implements Serializable {
    private T result;
    private String errorMessage = "";

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


