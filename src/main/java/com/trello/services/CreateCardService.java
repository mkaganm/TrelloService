package com.trello.services;

import com.trello.data.GetDataAuthorization;
import com.trello.models.response.CreateCardResponse;
import com.trello.models.response.IdListResponse;
import core.utility.Logging;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;

import java.util.HashMap;

public class CreateCardService extends TrelloCommonService {

    @Step("Create Card Service")
    public CreateCardService postCreateCardService() {

        for (String name : variables.getTrelloModal().getCardNames()) {

            core.postWithParams(baseUrl + "/cards", paramBuilder(name), "", "Create Card Service");
            core.checkStatusCode(HttpStatus.SC_OK, core.response.getStatusCode());
            createCardParser();
        }

        return this;
    }

    private HashMap<String, String> paramBuilder(String cardName) {

        HashMap<String, String> params = new HashMap<>();
        String id;

        if (idListFilter() != null) {
            id = idListFilter().getId();
        } else {
            id = variables.getTrelloModal().getIdList();
        }

        params.put("name", cardName);
        params.put("idList", id);
        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }

    private IdListResponse idListFilter() {

        return variables.getIdListResponseList().stream().filter(idListResponse ->
                        idListResponse.getName().equals("To Do"))
                .findAny()
                .orElse(null)
                ;
    }

    private void createCardParser() {

        CreateCardResponse createCardResponse = core.response.jsonPath()
                .getObject("$.", CreateCardResponse.class);

        variables.getCreateCardResponseList().add(createCardResponse);

    }

    public UpdateCardService getUpdateCardService() {
        return new UpdateCardService();
    }

    public DeleteBoardService getDeleteBoardService() {
        return new DeleteBoardService();
    }
}
