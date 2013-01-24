package com.agipi.test.jboss.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agipi.test.jboss.model.User;
import com.agipi.test.jboss.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "view.action")
	@ResponseBody
	public Map<String, Object> view() {
		final Map<String, Object> model = new HashMap<String, Object>();

		List<User> users = null;
		try {
			users = userService.findAll();
			model.put("total", users.size());
			model.put("data", users);
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to retrieve users : " + e.getMessage());
		}

		return model;
	}

	@RequestMapping(value = "create.action", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> create(@RequestBody User user) {
		final Map<String, Object> model = new HashMap<String, Object>();

		try {
			userService.create(user);
			model.put("data", user);
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to create the user : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "update.action", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> update(@RequestBody User user) {
		final Map<String, Object> model = new HashMap<String, Object>();

		try {
			userService.update(user);
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to update the user : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "delete.action", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> delete(@RequestBody User user) {
		final Map<String, Object> model = new HashMap<String, Object>();

		try {
			userService.delete(user);
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to delete the user : " + e.getMessage());
		}
		return model;
	}

}
