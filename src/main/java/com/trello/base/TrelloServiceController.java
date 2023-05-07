package com.trello.base;

import com.trello.data.TrelloVariables;
import core.controller.ServiceController;
import org.testng.annotations.BeforeMethod;

public class TrelloServiceController extends ServiceController {

    @BeforeMethod
    public void initialize() {
        TrelloVariables.setUp();
    }
}
