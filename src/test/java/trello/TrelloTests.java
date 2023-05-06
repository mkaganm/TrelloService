package trello;

import com.trello.base.TrelloServiceController;
import com.trello.data.dataprovider.TestData;
import com.trello.data.dataprovider.TrelloModal;
import com.trello.services.board.TrelloBoardService;
import org.testng.annotations.Test;


public class TrelloTests extends TrelloServiceController {

    @Test(dataProvider = "testDataProvider", dataProviderClass = TestData.class)
    public void testCase(TrelloModal modal){

        System.out.println(modal);

        startTest(new TrelloBoardService(modal))
                .postBoardService();

    }
}
