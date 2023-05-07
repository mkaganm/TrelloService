package com.trello.data.dataprovider;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TrelloModal {

    @Builder.Default
    private String boardName = "test1";

    @Builder.Default
    private List<String> cardNames = List.of("Card1", "Card2");

    @Builder.Default
    private String idList = "6456cfc86301178f43ca74fb";

    @Builder.Default
    private String updatedName = "UpdatedName";

}
