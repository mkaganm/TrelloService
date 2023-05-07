package com.trello.services;

import com.trello.data.GetDataAuthorization;
import core.utility.Logging;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;

import java.util.HashMap;

public class UpdateCardService extends TrelloCommonService {

    @Step("Put Update Card Service")
    public UpdateCardService putUpdateCardService() {

        String url = baseUrl + "/cards/" + variables.getCreateCardResponseList().get(0).getId();

        core.putWithParams(url, paramBuilder(), "Put Update Card Service");
        core.checkStatusCode(HttpStatus.SC_OK, core.response.getStatusCode());

        return this;
    }

    private HashMap<String, String> paramBuilder() {

        HashMap<String, String> params = new HashMap<>();

        params.put("name", variables.getTrelloModal().getUpdatedName());
        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }


    public DeleteCardService getDeleteCardService() {
        return new DeleteCardService();
    }

    public DeleteBoardService getDeleteBoardService() {
        return new DeleteBoardService();
    }
}
