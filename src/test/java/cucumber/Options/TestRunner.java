package cucumber.Options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="C:\\Users\\Admin\\Documents\\APIFrameworkver1.1\\src\\test\\java\\features", glue = {"stepDefinitions"})
public class TestRunner {
}
