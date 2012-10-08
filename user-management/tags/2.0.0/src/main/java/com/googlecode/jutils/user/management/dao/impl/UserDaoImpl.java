package com.googlecode.jutils.user.management.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.dao.AbstractGenericDaoHibernate;
import com.googlecode.jutils.dal.util.QueryUtil;
import com.googlecode.jutils.user.management.dao.UserDao;
import com.googlecode.jutils.user.management.model.User;

/**
 * The Class UserDaoImpl.
 */
@Repository
public class UserDaoImpl extends AbstractGenericDaoHibernate<Integer, User> implements UserDao {

	/**
	 * Instantiates a new user dao impl.
	 */
	public UserDaoImpl() {
		super(User.class);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countByLogin(String login) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), UserDao.COUNT_BY_LOGIN, new String[] { "login" }, login);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public User findByLogin(String login) {
		return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), UserDao.FIND_BY_LOGIN, new String[] { "login" }, login);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countByRoleId(Integer roleId) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), UserDao.COUNT_BY_ROLE_ID, new String[] { "roleId" }, roleId);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	protected Search getSearch(SearchCriteria searchCriteria) {
		Search search = null;
		if (searchCriteria != null) {
			search = new Search();

			final StringBuilder buffer = new StringBuilder();
			buffer.append("from User u ");
			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("firstName")) {
							buffer.append("upper(u.firstName) like upper(:firstName) ");
							search.addStringParameter("firstName", entry.getValue());
						} else if (entry.getKey().equals("lastName")) {
							buffer.append("upper(u.lastName) like upper(:lastName) ");
							search.addStringParameter("lastName", entry.getValue());
						} else if (entry.getKey().equals("login")) {
							buffer.append("upper(u.login) like upper(:login) ");
							search.addStringParameter("login", entry.getValue());
						} else if (entry.getKey().equals("mail")) {
							buffer.append("upper(u.mail) like upper(:mail) ");
							search.addStringParameter("mail", entry.getValue());
						} else if (entry.getKey().equals("active")) {
							buffer.append("upper(u.active) like upper(:active) ");
							search.addBooleanParameter("active", entry.getValue());
						}
						index++;
					}
				}
			}

			search.setCountQuery("select count(*) " + buffer.toString());

			if (searchCriteria.hasSorts()) {
				buffer.append("order by ");
				int index = 0;
				for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
					if (index != 0) {
						buffer.append(", ");
					}
					if (entry.getKey().equals("firstName")) {
						buffer.append("u.firstName ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("lastName")) {
						buffer.append("u.lastName ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("login")) {
						buffer.append("u.login ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("mail")) {
						buffer.append("u.mail ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("active")) {
						buffer.append("u.active ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					}
					index++;
				}
			}
			search.setQuery(buffer.toString());
		}
		return search;
	}

}