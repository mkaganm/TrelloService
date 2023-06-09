package com.trello.services;

import com.trello.data.GetDataApplication;
import com.trello.data.TrelloVariables;
import core.common.ServiceCommons;
import core.controller.ServiceController;

public class TrelloCommonService extends ServiceController {

    public final ServiceCommons core = ServiceCommons.getCore();

    public final TrelloVariables variables = TrelloVariables.getInstance();

    public static final String baseUrl = GetDataApplication.getParameters().getBaseUrl();

}
