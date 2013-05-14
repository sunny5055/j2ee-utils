package com.googlecode.jutils.pp.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.pp.action.Action;
import com.googlecode.jutils.pp.action.ActionCall;
import com.googlecode.jutils.pp.action.exception.ActionException;
import com.googlecode.jutils.pp.service.TemplateProcessorService;
import com.googlecode.jutils.pp.settings.Settings;

@Service
public class TemplateProcessorServiceImpl implements TemplateProcessorService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateProcessorServiceImpl.class);

	@Autowired
	private List<Action> actions;

	public TemplateProcessorServiceImpl() {
		super();
		this.actions = new ArrayList<Action>();
	}

	@Override
	public void process(Settings settings) throws ActionException {
		if (settings != null) {
			final File sourceDirectory = settings.getSourceDirectory();
			final List<ActionCall> actionCalls = settings.getActions();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Process directory : " + sourceDirectory);
			}
			processDir(sourceDirectory, actionCalls, settings);
		}
	}

	private void processDir(File file, List<ActionCall> actionCalls, Settings settings) throws ActionException {
		if (file != null && !CollectionUtil.isEmpty(actionCalls) && settings != null) {
			if (file.isDirectory()) {
				for (final File child : file.listFiles()) {
					processDir(child, actionCalls, settings);
				}
			} else {
				final Map<String, ActionCall> filteredActions = filterAction(file, actionCalls);
				if (!MapUtil.isEmpty(filteredActions)) {
					for (final ActionCall actionCall : filteredActions.values()) {
						final Action action = getAction(actionCall.getName());
						if (action != null) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Execute action[" + action.getName() + "] on " + file);
							}
							action.execute(settings, file, actionCall.getParameters());
						}
					}
				}
			}
		}
	}

	private Map<String, ActionCall> filterAction(File file, List<ActionCall> actionCalls) {
		Map<String, ActionCall> actions = null;
		if (!CollectionUtil.isEmpty(actionCalls)) {
			actions = new HashMap<String, ActionCall>();

			for (final ActionCall actionCall : actionCalls) {
				if (IoUtil.matches(file, actionCall.getApplyTo())) {
					if (!actions.containsKey(actionCall.getName())) {
						actions.put(actionCall.getName(), actionCall);
					} else {
						if (actionCall.isFileRegex()) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Action[" + actionCall.getName() + "] has been replaced for file " + file);
								LOGGER.debug(">>> Old : " + actions.get(actionCall.getName()));
								LOGGER.debug(">>> New " + actionCall);
							}
							actions.put(actionCall.getName(), actionCall);
						} else {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Action[" + actionCall.getName() + "] already registered for file " + file);
								LOGGER.debug(">>> Registered : " + actions.get(actionCall.getName()));
								LOGGER.debug(">>> Current " + actionCall);
							}
						}
					}
				}
			}

			if (!MapUtil.isEmpty(actions) && actions.containsKey("ignore")) {
				actions = MapUtil.keepOnly(actions, "ignore");
			}
		}
		return actions;
	}

	private Action getAction(String name) {
		Action action = null;
		if (!StringUtil.isBlank(name)) {
			if (!CollectionUtil.isEmpty(actions)) {
				for (final Action a : actions) {
					if (a.getName().equalsIgnoreCase(name)) {
						action = a;
						break;
					}
				}
			}
		}
		return action;
	}
}
