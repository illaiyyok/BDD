package com.boi.ccr.mb.testware.reporting;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import ru.yandex.qatools.allure.Allure;

import ru.yandex.qatools.allure.annotations.Step;

import ru.yandex.qatools.allure.events.MakeAttachmentEvent;

import ru.yandex.qatools.allure.events.StepFinishedEvent;

import ru.yandex.qatools.allure.events.StepStartedEvent;

import ru.yandex.qatools.allure.model.Description;

import ru.yandex.qatools.allure.model.DescriptionType;

public final class LogUtil {

	private static final Logger LOG = LoggerFactory

			.getLogger(LogUtil.class);

	private LogUtil() {

	}

	@Step("{0}")

	public static void log(final String message)

	{

		LOG.info("Logger : " + message);

		StepStartedEvent event = new StepStartedEvent(message);

		Description description = new Description();

		description.setValue(message);

		description.setType(DescriptionType.MARKDOWN);

		event.withTitle(message);

		Allure.LIFECYCLE.fire(event);

		Allure.LIFECYCLE.fire(new StepFinishedEvent());

	}

	public static void logAttachment(String fileName, String text) {

		LOG.info("Logger : " + text);

		Allure.LIFECYCLE.fire(new MakeAttachmentEvent(text.getBytes(), fileName, "text/plain"));

		// AllureReporter.addTextAttachment(fileName, text);

	}

	public static void logAttachmentHTML(String fileName, String text) {

		LOG.info("Logger : " + text);

		Allure.LIFECYCLE.fire(new MakeAttachmentEvent(text.getBytes(), fileName, "html/plain"));

		// AllureReporter.addTextAttachment(fileName, text);

	}

	public static void logCSVAttachment(String sql, String csvFormat) {

		LOG.info("Logger : " + sql);

		Allure.LIFECYCLE.fire(new MakeAttachmentEvent(csvFormat.getBytes(), sql, "text/csv"));

	}

}