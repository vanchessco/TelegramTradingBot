package com.mi.api_tinkoff.config.tinkoff_api_config;

public class TinkoffApiParameters {


    private final String token;
    private final boolean sandBoxMode;


    public TinkoffApiParameters(String token, boolean sandBoxMode) {
        this.token = token;
        this.sandBoxMode = sandBoxMode;
    }


    public String getToken() {
        return this.token;
    }

    public boolean isSandBoxMode() {
        return this.sandBoxMode;
    }


}
