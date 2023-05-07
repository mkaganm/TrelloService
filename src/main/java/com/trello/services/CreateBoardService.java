package com.trello.services;

import com.trello.data.GetDataAuthorization;
import com.trello.data.dataprovider.TrelloModal;
import com.trello.models.response.CreateBoardResponse;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;
import java.util.HashMap;


public class CreateBoardService extends TrelloCommonService {

    public CreateBoardService(TrelloModal modal) {
        variables.setTrelloModal(modal);
    }

    @Step("Post Create Board Service")
    public CreateBoardService postCreateBoardService() {

        String boardName = variables.getTrelloModal().getBoardName();
        core.postWithParams(baseUrl + "/boards", paramBuilder(boardName), "", "Create Board Service");
        core.checkStatusCode(HttpStatus.SC_OK, core.response.getStatusCode());
        createBoardParser();

        return this;
    }

    private HashMap<String, String> paramBuilder(String boardName) {

        HashMap<String, String> params = new HashMap<>();

        params.put("name", boardName);
        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }

    private void createBoardParser() {

        CreateBoardResponse createBoardResponse = core.response.jsonPath()
                .getObject("$.", CreateBoardResponse.class);

        variables.setCreateBoardResponse(createBoardResponse);
    }

    public DeleteBoardService getDeleteBoardService() {
        return new DeleteBoardService();
    }

    public CreateCardService getCreateCardService() {
        return new CreateCardService();
    }

    public GetListsService getListsService() {
        return new GetListsService();
    }
}
