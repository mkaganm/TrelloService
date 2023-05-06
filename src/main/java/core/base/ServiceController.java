package core.base;

import core.utility.Command;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class ServiceController {

    public <T> T startTest(T t){
        ThreadLocal<T> tl = new ThreadLocal<>();
        tl.set(t);
        return tl.get();
    }

    @BeforeSuite
    public void removeReportHistory() {
        Command.runCommand("allure generate --clean --output allure-results");
    }

    @AfterSuite(alwaysRun = true)
    public void openAllureReport() {
        Command.runCommand("allure serve -h localhost");
    }
}
