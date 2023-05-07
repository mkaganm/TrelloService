package com.trello.services;

import com.trello.data.GetDataAuthorization;
import core.utility.Logging;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;

import java.util.HashMap;

public class DeleteBoardService extends TrelloCommonService {

    @Step("Delete Board Service")
    public DeleteBoardService deleteBoardService() {

        String url = baseUrl + "/boards/" + getId();
        core.deleteWithParams(url, paramBuilder(), "", "Delete Board Service");
        core.checkStatusCode(HttpStatus.SC_OK, core.response.getStatusCode());

        return this;
    }

    private HashMap<String, String> paramBuilder() {

        HashMap<String, String> params = new HashMap<>();

        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }

    private String getId() {
        return variables.getCreateBoardResponse().getId();
    }

    public DeleteBoardService getDeleteBoardService() {
        return new DeleteBoardService();
    }
}
