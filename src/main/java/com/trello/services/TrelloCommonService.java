package com.trello.services;

import com.trello.data.GetDataApplication;
import com.trello.data.TrelloVariables;
import core.base.ServiceCommons;
import core.base.ServiceController;

public class TrelloCommonService extends ServiceController {

    public final ServiceCommons core = ServiceCommons.getCore();

    public final TrelloVariables variables = TrelloVariables.getInstance();

    public String baseUrl;

    public void setBaseUrl(){
        baseUrl = GetDataApplication.getParameters().getBaseUrl();
    }


}
