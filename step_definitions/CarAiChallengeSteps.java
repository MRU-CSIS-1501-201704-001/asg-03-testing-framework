package step_definitions;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.File;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.util.regex.Matcher;

import cucumber.api.java.en.*;
import cucumber.api.java.Before;
import cucumber.api.java.After;
import cucumber.api.Scenario;
import cucumber.api.PendingException;

import java.util.regex.Pattern;


public class CarAiChallengeSteps {
	
    private LinkedList<String> inputQueue = new LinkedList<>();
    private String[] outputLines;
    private int nextLine;


    @Before
    public void beforeCallingScenario() {
    }


    @After
    public void afterRunningScenario(Scenario scenario) {
        this.inputQueue.clear();
        this.outputLines = null;
        this.nextLine = -1;
    }

    public void provideKeyboardInput() {
        String queuedInput = "";
        for (String s : inputQueue) {
            queuedInput += String.format("%s%n",s);
        }
        System.setIn(new ByteArrayInputStream(queuedInput.getBytes()));
    }

    @When("^I run the Car AI Challenge$")
    public void iRunTheCarAIChallenge() throws Throwable {
        ByteArrayOutputStream outContent = null;
        PrintStream testSystemOut = null;
        try {
            outContent = new ByteArrayOutputStream();
            testSystemOut = new PrintStream(outContent, true, "UTF-8");

            PrintStream originalSystemOut = System.out;
            try {
                System.setOut(testSystemOut);

                provideKeyboardInput();
                Class.forName("ProblemSetMain").getMethod("runCarAiChallenge").invoke(null);
            } finally {
                System.setOut(originalSystemOut);
            }

            testSystemOut.flush();
            outputLines = outContent.toString("UTF-8").split("\\R+");
            nextLine = 0;
        } finally {
            testSystemOut.close();
            outContent.close();
        }
    }      

    @Given("^a number of adult passengers (\\d+)$")
    public void aNumberOfAdultPassengers(int numAdultPassengers) throws Throwable {
        inputQueue.offer(String.valueOf(numAdultPassengers));
    }

    @Given("^a number of child passengers (\\d+)$")
    public void aNumberOfChildPassengers(int numChildPassengers) throws Throwable {
        inputQueue.offer(String.valueOf(numChildPassengers));
    }

    @Given("^a number of adult pedestrians (\\d+)$")
    public void aNumberOfAdultPedestrians(int numAdultPedestrians) throws Throwable {
        inputQueue.offer(String.valueOf(numAdultPedestrians));
    }

    @Given("^a number of child pedestrians (\\d+)$")
    public void aNumberOfChildPedestrians(int numChildPedestrians) throws Throwable {
        inputQueue.offer(String.valueOf(numChildPedestrians));
    }

    @Given("^if asked whether passenger's lives are more important than pedestrian's, I say \"([^\"]*)\"$")
    public void ifAskedWhetherPassengerSLivesAreMoreImportantThanPedestrianSISay(String passengerOverPedestrian) throws Throwable {
        inputQueue.offer(passengerOverPedestrian);
    }

    @Then("^I should see a fatality report stating that \"([^\"]*)\" was killed$")
    public void iShouldSeeAFatalityReportStatingThatWasKilled(String fatalityGroup) throws Throwable {
        this.assertRemainingOutputContainsFragment(fatalityGroup);
    }

    @Then("^the number of adult fatalities were (\\d+)$")
    public void theNumberOfAdultFatalitiesWere(String numAdultFatalities) throws Throwable {
        this.assertRemainingOutputContainsFragment(numAdultFatalities);
    }

    @Then("^the number of child fatalities were (\\d+)$")
    public void theNumberOfChildFatalitiesWere(String numChildFatalities) throws Throwable {
        this.assertRemainingOutputContainsFragment(numChildFatalities);
    }

    
    
    public void assertRemainingOutputContainsFragment(String word) throws Throwable {
        this.assertRemainingOutputContains(word, "\"" + word + "\"");
    }

    public void assertRemainingOutputContainsWord(String word) throws Throwable {
        this.assertRemainingOutputContains("\\b" + word + "\\b", "word \"" + word + "\"");
    }

    public void assertRemainingOutputContains(String regex, String niceName) throws Throwable {
        int lineToCheck = this.nextLine;
        boolean found = false;

        while (!found && lineToCheck < this.outputLines.length) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(this.outputLines[lineToCheck]);
            found = m.find();

            lineToCheck++;
        }

        if (found) {
            this.nextLine = lineToCheck;
        } else {
            throw new AssertionError("Could not find " + niceName + " in remaining lines:\n" +
                String.join("\n", Arrays.copyOfRange(this.outputLines, this.nextLine, this.outputLines.length)) +
                (this.nextLine > 0 ? "\nPrevious line was:\n" + this.outputLines[this.nextLine - 1] : ""));
        }
    }

    public void assertRemainingOutputMissingWord(String word) throws Throwable {
        this.assertRemainingOutputMissing("\\b" + word + "\\b", word);
    }

    public void assertRemainingOutputMissing(String regex, String niceName) throws Throwable {
        int lineToCheck = this.nextLine;
        boolean found = false;

        while (!found && lineToCheck < this.outputLines.length) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(this.outputLines[lineToCheck]);
            found = m.find();

            lineToCheck++;
        }

        if (found) {
            throw new AssertionError("Did not expect to find '" + regex + "', but found it in the line:\n" +
                String.join("\n", this.outputLines[lineToCheck - 1]));
        }
    }
}
