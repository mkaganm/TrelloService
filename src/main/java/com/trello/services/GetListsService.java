package com.trello.services;

import com.trello.data.GetDataAuthorization;
import com.trello.models.response.IdListResponse;
import core.utility.Logging;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

public class GetListsService extends TrelloCommonService {

    @Step("Get Id List Service")
    public GetListsService getIdListService() {

        String url = baseUrl + "/boards/" + getId() + "/lists";
        core.getWithParams(url, paramBuilder(), "Get Id List Service");
        core.checkStatusCode(HttpStatus.SC_OK, core.response.getStatusCode());
        idListResponseParser();

        return this;
    }

    public CreateCardService getCreateCardService() {
        return new CreateCardService();
    }

    private HashMap<String, String> paramBuilder() {

        HashMap<String, String> params = new HashMap<>();

        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }

    private void idListResponseParser() {
        List<IdListResponse> idListResponseList = core.response.jsonPath().getList("$.", IdListResponse.class);
        variables.setIdListResponseList(idListResponseList);;

    }

    private String getId() {
        return variables.getCreateBoardResponse().getId();
    }
}
