package com.trello.data;

import com.trello.data.dataprovider.TrelloModal;
import com.trello.models.response.CreateBoardResponse;
import com.trello.models.response.CreateCardResponse;
import com.trello.models.response.IdListResponse;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class TrelloVariables {

    private static ThreadLocal<TrelloVariables> tl = new ThreadLocal<>();

    public void unload() {
        tl.remove();
    }

    private TrelloVariables() {
    }

    public static TrelloVariables getInstance() {
        return tl.get();
    }

    public static void setUp() {
        tl.set(new TrelloVariables());
    }

    private TrelloModal trelloModal;
    private CreateBoardResponse createBoardResponse;
    private List<CreateCardResponse> createCardResponseList = new ArrayList<>();
    private List<IdListResponse> idListResponseList = new ArrayList<>();
    private String idList;
}
