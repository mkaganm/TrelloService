package com.trello.data.dataprovider;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrelloModal {

    @Builder.Default
    private String boardName = "test";

}
