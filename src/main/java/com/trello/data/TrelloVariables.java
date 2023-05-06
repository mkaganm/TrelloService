package com.trello.data;

import com.trello.data.dataprovider.TrelloModal;
import lombok.Data;

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
}
