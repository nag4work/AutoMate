package com.AutomationFramework.BDD.Runner;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin={"pretty","html:target/cucumberHTMLReport"}
		         ,features = "classpath:lithia/AutomationFramework/BDD/Features"
		         ,glue={"lithia.BDD.StepDefinations"})

public class RunTest {

}