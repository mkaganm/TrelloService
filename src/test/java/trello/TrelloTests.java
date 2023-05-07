package trello;

import com.trello.base.TrelloServiceController;
import com.trello.data.dataprovider.TestData;
import com.trello.data.dataprovider.TrelloModal;
import com.trello.services.CreateBoardService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.annotations.Test;


public class TrelloTests extends TrelloServiceController {

    @Epic("Trello Tests")
    @Description("Trello Tests")
    @Test(dataProvider = "testDataProvider", dataProviderClass = TestData.class)
    public void testCase(TrelloModal modal) {

        startTest(new CreateBoardService(modal))
                .postCreateBoardService()
                .getListsService()
                .getIdListService()
                .getCreateCardService()
                .postCreateCardService()
                .getUpdateCardService()
                .putUpdateCardService()
                .getDeleteCardService()
                .deleteCardService()
                .getDeleteBoardService()
                .deleteBoardService()
        ;

    }
}
