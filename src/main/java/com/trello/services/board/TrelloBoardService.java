package com.trello.services.board;

import com.trello.data.GetDataAuthorization;
import com.trello.data.dataprovider.TrelloModal;
import com.trello.services.TrelloCommonService;
import org.apache.http.HttpStatus;

import java.util.HashMap;

public class TrelloBoardService extends TrelloCommonService {

    public TrelloBoardService(TrelloModal modal) {
        variables.setTrelloModal(modal);
        setBaseUrl();
    }

    public void postBoardService(){

        String boardName = variables.getTrelloModal().getBoardName();
        core.postWithParams(baseUrl, paramBuilder(boardName), "Create Board Service");
        core.checkStatusCode(HttpStatus.SC_OK);

    }

    private HashMap<String, String> paramBuilder(String boardName){

        HashMap<String, String> params = new HashMap<>();

        params.put("name", boardName);
        params.put("key", GetDataAuthorization.getParameters().getApiKey());
        params.put("token", GetDataAuthorization.getParameters().getToken());

        return params;
    }
}
