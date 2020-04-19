package com.boi.ccr.mb.testware.reporting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.Lifecycle;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.OutcomesTable;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;
import org.jbehave.core.reporters.StoryReporter;

import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ru.yandex.qatools.allure.events.StepCanceledEvent;
import ru.yandex.qatools.allure.events.StepFailureEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCasePendingEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Label;

public class AllureReporter implements StoryReporter{
	
	
	private static Allure allure = Allure.LIFECYCLE;
	private final Map<String,String> suites = new HashMap<String,String>();
	private String uid;
	
	private Label[] constructLabels(Story story){
		String path = story.getPath();
		String[] dirs = path.split("/");
		if(dirs.length > 2){
			Label featureLabel = AllureModelUtils.createFeatureLabel(normalizeDir(dirs[dirs.length - 3]));
			Label storyLabel = AllureModelUtils.createStoryLabel(normalizeDir(dirs[dirs.length -2]));
			return new Label[] {featureLabel, storyLabel };
		}else if (dirs.length > 1){
			Label featureLabel = AllureModelUtils.createFeatureLabel(normalizeDir(dirs[dirs.length -2]));
			Label storyLabel = AllureModelUtils.createStoryLabel(normalizeDir(story.getName().replace(".story", "")));
			return new Label[] {featureLabel, storyLabel };
		}
		return new Label[] {};
		
	}
	
	public void afterExamples() {
		// TODO Auto-generated method stub
		
	}

	public void afterGivenStories() {
		// TODO Auto-generated method stub
		
	}

	public void afterScenario() {
		// TODO Auto-generated method stub
		
		allure.fire(new TestCaseFinishedEvent());
		
	}

	public void afterStory(boolean givenStory) {
		// TODO Auto-generated method stub
		allure.fire(new TestSuiteFinishedEvent(uid));
		
	}
	
	private String normalizeDir(String dir){
		return dir.replace("_", " ");
		
	}

	public void beforeExamples(List<String> arg0, ExamplesTable arg1) {
		// TODO Auto-generated method stub
		
	}

	public void beforeGivenStories() {
		// TODO Auto-generated method stub
		
	}

	public void beforeScenario(Scenario arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeScenario(String scenarioTitle) {
		// TODO Auto-generated method stub
		allure.fire(new TestCaseStartedEvent(uid, scenarioTitle));
		allure.fire(new ClearStepStorageEvent());
		
	}

	public void beforeStep(String step) {
		// TODO Auto-generated method stub
		allure.fire(new StepStartedEvent(step).withTitle(step));
		
	}

	public void beforeStory(Story story, boolean givenStory) {
		// TODO Auto-generated method stub	
		uid = generateSuiteUid(story);
		TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, story.getName());
		Label[] labels = constructLabels(story);
		event.withLabels(AllureModelUtils.createTestFrameworkLabel("JBehave"), labels);		
	}
	
		
	public static void attachScreenshot(byte[] screenshot){
		String screenhotName = "screenhot_" +System.currentTimeMillis()+".png";
		allure.fire(new MakeAttachmentEvent(screenshot, screenhotName, "image/png"));
	}

	public void comment(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void dryRun() {
		// TODO Auto-generated method stub
		
	}

	public void example(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void example(Map<String, String> arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void failed(String step, Throwable cause) {
		// TODO Auto-generated method stub
		
		allure.fire(new StepFinishedEvent());
		allure.fire(new StepFailureEvent().withThrowable(cause.getCause()));
			
	}

	public void failedOutcomes(String arg0, OutcomesTable arg1) {
		// TODO Auto-generated method stub
		
	}

	public void givenStories(GivenStories arg0) {
		// TODO Auto-generated method stub
		
	}

	public void givenStories(List<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void ignorable(String step) {
		// TODO Auto-generated method stub
		allure.fire(new StepCanceledEvent());
		
	}

	public void lifecyle(Lifecycle arg0) {
		// TODO Auto-generated method stub
		
	}

	public void narrative(Narrative arg0) {
		// TODO Auto-generated method stub
		
	}

	public void notPerformed(String step) {
		// TODO Auto-generated method stub
		allure.fire(new StepCanceledEvent());
		
	}

	public void pending(String step) {
		// TODO Auto-generated method stub
		allure.fire(new StepCanceledEvent());		
		allure.fire(new TestCasePendingEvent().withMessage("PENDING"));
		
	}

	public void pendingMethods(List<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void restarted(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	public void restartedStory(Story arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}

	public void scenarioMeta(Meta arg0) {
		// TODO Auto-generated method stub
		
	}

	public void scenarioNotAllowed(Scenario arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void storyCancelled(Story story, StoryDuration storyDuration) {
		// TODO Auto-generated method stub
		
	}

	public void storyNotAllowed(Story arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void successful(String step) {
		// TODO Auto-generated method stub
		allure.fire(new StepFinishedEvent());
		
	}
	
	public String generateSuiteUid(Story story){
		String uId = UUID.randomUUID().toString();
		synchronized (getSuites()) {
			getSuites().put(story.getPath(),uId);
			
		}
		return uId;
	}

	public Map<String,String> getSuites(){
		return suites;
	}
	
}
