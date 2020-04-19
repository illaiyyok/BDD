package com.boi.ccr.mb.testware;

import java.net.MalformedURLException;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import org.jbehave.core.ConfigurableEmbedder;

import org.jbehave.core.configuration.Configuration;

import org.jbehave.core.embedder.Embedder;

import org.jbehave.core.embedder.EmbedderControls;

import org.jbehave.core.embedder.StoryControls;

import org.jbehave.core.failures.FailureStrategy;

import org.jbehave.core.failures.PassingUponPendingStep;

import org.jbehave.core.i18n.LocalizedKeywords;

import org.jbehave.core.io.AbsolutePathCalculator;

import org.jbehave.core.io.CodeLocations;

import org.jbehave.core.io.LoadFromClasspath;

import org.jbehave.core.io.StoryFinder;

import org.jbehave.core.io.UnderscoredCamelCaseResolver;

import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;

import org.jbehave.core.parsers.RegexStoryParser;

import org.jbehave.core.reporters.ConsoleOutput;

import org.jbehave.core.reporters.FreemarkerViewGenerator;

import org.jbehave.core.reporters.PrintStreamStepdocReporter;

import org.jbehave.core.reporters.StoryReporterBuilder;

import org.jbehave.core.steps.InjectableStepsFactory;

import org.jbehave.core.steps.InstanceStepsFactory;

import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;

import org.jbehave.core.steps.ParameterControls;

import org.jbehave.core.steps.ParameterConverters;

import org.jbehave.core.steps.SilentStepMonitor;

import org.jbehave.core.steps.StepFinder;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.boi.ccr.mb.testware.config.ApplicationProperties;

import com.boi.ccr.mb.testware.reporting.AllureReporter;

import com.boi.ccr.mb.testware.steps.Steps;

import com.thoughtworks.paranamer.NullParanamer;

public class Main extends ConfigurableEmbedder {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	private static final int STORY_TIMEOUT = 8 * 60 * 60;

	private Configuration configuration;

	public Main() throws MalformedURLException {

		config();

	}

	private void config() {

		configuration = new Configuration() {

		};

		configuration.useStoryReporterBuilder(new StoryReporterBuilder().withReporters(new AllureReporter(),

				new ConsoleOutput()));

		configuration.useFailureStrategy(new FailureStrategy() {

			@Override

			public void handleFailure(Throwable throwable) throws Throwable {

				LOG.error("Story failure!", throwable);

			}

		});

		configuration.useKeywords(new LocalizedKeywords(Locale.ENGLISH));

		configuration.usePathCalculator(new AbsolutePathCalculator());

		configuration.useParameterControls(new ParameterControls());

		configuration.useParameterConverters(new ParameterConverters());

		configuration.useParanamer(new NullParanamer());

		configuration.usePendingStepStrategy(new PassingUponPendingStep());

		configuration.useStepCollector(new MarkUnmatchedStepsAsPending());

		configuration.useStepdocReporter(new PrintStreamStepdocReporter());

		configuration.useStepFinder(new StepFinder());

		configuration.useStepMonitor(new SilentStepMonitor());

		configuration.useStepPatternParser(new RegexPrefixCapturingPatternParser());

		configuration.useStoryControls(new StoryControls());

		configuration.useStoryParser(new RegexStoryParser(configuration.keywords()));

		configuration.useStoryPathResolver(new UnderscoredCamelCaseResolver());

		configuration.useViewGenerator(new FreemarkerViewGenerator());

		configuration.useStoryLoader(new LoadFromClasspath());

		EmbedderControls embedderControls = configuredEmbedder().embedderControls();

		embedderControls.doBatch(false);

		embedderControls.doGenerateViewAfterStories(false);

		embedderControls.doIgnoreFailureInStories(true);

		embedderControls.doIgnoreFailureInView(true);

		embedderControls.doSkip(false);

		embedderControls.doVerboseFailures(true);

		embedderControls.doVerboseFiltering(false);

		embedderControls.useStoryTimeouts(Long.toString(STORY_TIMEOUT));

		embedderControls.useThreads(1);

	}

	@Override

	public Configuration configuration() {

		return configuration;

	}

	@Override

	public InjectableStepsFactory stepsFactory() {

		return new InstanceStepsFactory(configuration(), new Steps().getSteps());

	}

	@Override

	public void run() {

		Embedder embedder = configuredEmbedder();

		try {

			List<String> stories = findStories();

			LOG.info("Found stories: " + stories);

			embedder.runStoriesAsPaths(stories);

		} catch (Throwable t) {

			LOG.error("Error running story!", t);

		}

	}

	
	private List<String> findStories(){
		String storiesPath = "";
		String baseStory = "stories/**/";
		String storyListFromProperty = ApplicationProperties.instance.getStoriesFilter();
		String[] filterAry = storyListFromProperty.split(",");
		for (String filter : filterAry) {
			if(StringUtils.isBlank(filter)){
				storiesPath +=baseStory+ "*.story";
			}else
			{
				if(filter.startsWith("/")){
					filter = filter.substring(1);
				}
				
				if(filter.endsWith(".story")){
					storiesPath += baseStory + filter;
				}else
				{
					if(!filter.endsWith("/"))
					{
						filter +="/";
					}
					storiesPath += baseStory +"*.story";
				}
			}
			
			storiesPath +=",";
		}

		URL baseDir = CodeLocations.codeLocationFromClass(this.getClass());
		List<String> stories = new StoryFinder().findPaths(baseDir, storiesPath, "");
		stories.size();
		return stories;		
	}	
	
	

	public static void main(String[] args) throws MalformedURLException, Exception  {

		new Main().run();

	}

}