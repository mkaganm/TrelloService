package com.trello.services;

import com.trello.data.GetDataAuthorization;
import com.trello.models.response.CreateCardResponse;
import core.utility.Logging;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;

import java.util.HashMap;

public class DeleteCardService extends TrelloCommonService {

    @Step("Delete Card Service")
    public DeleteCardService deleteCardService() {

        for (CreateCardResponse card : variables.getCreateCardResponseList()) {

            String url = baseUrl + "/cards/" + card.getId();
            core.deleteWithParams(url, paramBuilder(), "", "Delete Board Service");
            core.checkStatusCode(HttpStatus.SC_OK, core.response.getStatusCode());

        }
        return this;
    }

    private HashMap<String, String> paramBuilder() {

        HashMap<String, String> params = new HashMap<>();

        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }

    public DeleteBoardService getDeleteBoardService() {
        return new DeleteBoardService();
    }


}
