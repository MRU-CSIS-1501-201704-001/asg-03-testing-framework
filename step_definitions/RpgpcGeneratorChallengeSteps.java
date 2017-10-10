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


public class RpgpcGeneratorChallengeSteps {
	
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

    @When("^I run the RPG PC Generator Challenge$")
    public void iRunTheRpgPcGeneratorChallenge() throws Throwable {
        ByteArrayOutputStream outContent = null;
        PrintStream testSystemOut = null;
        try {
            outContent = new ByteArrayOutputStream();
            testSystemOut = new PrintStream(outContent, true, "UTF-8");

            PrintStream originalSystemOut = System.out;
            try {
                System.setOut(testSystemOut);

                provideKeyboardInput();
                Class.forName("ProblemSetMain").getMethod("runRpgpcGeneratorChallenge").invoke(null);
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

    @Given("^a race of \"([^\"]*)\"$")
    public void aRaceOf(String race) throws Throwable {      
        inputQueue.offer(race);
    }

    @Given("^rolls of (\\d+), (\\d+), (\\d+), and (\\d+)$")
    public void rollsOfAnd(int rollOne, int rollTwo, int rollThree, int rollFour) throws Throwable {     
        inputQueue.offer(String.valueOf(rollOne));
        inputQueue.offer(String.valueOf(rollTwo));
        inputQueue.offer(String.valueOf(rollThree));
        inputQueue.offer(String.valueOf(rollFour));
    }

    @Then("^I should see the race displayed as \"([^\"]*)\"$")
    public void iShouldSeeTheRaceDisplayedAs(String raceDisplay) throws Throwable {
        assertRemainingOutputContainsFragment(raceDisplay);
    }

    @Then("^the dexterity score displayed as \"([^\"]*)\"$")
    public void theDexterityScoreDisplayedAs(String dexScore) throws Throwable {
        assertRemainingOutputContainsFragment(dexScore);
    }

    @Then("^the dexterity modifier displayed as \"([^\"]*)\"$")
    public void theDexterityModifierDisplayedAs(String dexModifier) throws Throwable {
        assertRemainingOutputContainsFragment(dexModifier);
    }

    @Then("^the note should be \"([^\"]*)\"$")
    public void theNoteShouldBe(String note) throws Throwable {
        if (note.isEmpty()) {
            assertRemainingOutputIsEmpty();
        } else {
            assertRemainingOutputContainsFragment(note);
        }
        
    }

    public void assertRemainingOutputIsEmpty() throws Throwable {
        if (this.nextLine != this.outputLines.length) {
            throw new AssertionError("Expected no output, but there was output.");
        }
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


        System.err.println(regex);

        regex = regex.replaceAll("[-.\\+*?\\[^\\]$(){}=!<>|:\\\\]", "\\\\$0");
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

        regex = regex.replaceAll("[-.\\+*?\\[^\\]$(){}=!<>|:\\\\]", "\\\\$0");
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
