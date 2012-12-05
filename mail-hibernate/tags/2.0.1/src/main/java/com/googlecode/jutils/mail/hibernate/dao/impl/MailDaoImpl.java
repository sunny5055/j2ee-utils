package com.googlecode.jutils.mail.hibernate.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.dao.AbstractGenericDaoHibernate;
import com.googlecode.jutils.dal.util.QueryUtil;
import com.googlecode.jutils.mail.hibernate.dao.MailDao;
import com.googlecode.jutils.mail.hibernate.model.Mail;

/**
 * The Class MailDaoImpl.
 */
@Repository
public class MailDaoImpl extends AbstractGenericDaoHibernate<Integer, Mail> implements MailDao {

	/**
	 * Instantiates a new mail dao impl.
	 */
	public MailDaoImpl() {
		super(Mail.class);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countByName(String name) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), MailDao.COUNT_BY_NAME, new String[] { "name" }, name);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Mail findByName(String name) {
		return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), MailDao.FIND_BY_NAME, new String[] { "name" }, name);
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
			buffer.append("from Mail m ");
			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("name")) {
							buffer.append("upper(m.name) like upper(:name) ");
							search.addStringParameter("name", entry.getValue());
						} else if (entry.getKey().equals("from")) {
							buffer.append("upper(m.from) like upper(:from) ");
							search.addStringParameter("from", entry.getValue());
						} else if (entry.getKey().equals("replyTo")) {
							buffer.append("upper(m.replyTo) like upper(:replyTo) ");
							search.addStringParameter("replyTo", entry.getValue());
						} else if (entry.getKey().equals("subject")) {
							buffer.append("upper(m.subject) like upper(:subject) ");
							search.addStringParameter("subject", entry.getValue());
						} else if (entry.getKey().equals("text")) {
							buffer.append("upper(m.text) like upper(:text) ");
							search.addStringParameter("text", entry.getValue());
						} else if (entry.getKey().equals("htmlMessage")) {
							buffer.append("m.htmlMessage = :htmlMessage ");
							search.addBooleanParameter("htmlMessage", entry.getValue());
						} else if (entry.getKey().equals("template")) {
							buffer.append("m.template = :template ");
							search.addBooleanParameter("template", entry.getValue());
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
					if (entry.getKey().equals("name")) {
						buffer.append("m.name ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("from")) {
						buffer.append("m.from ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("replyTo")) {
						buffer.append("m.replyTo ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("subject")) {
						buffer.append("m.subject ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("text")) {
						buffer.append("m.text ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("htmlMessage")) {
						buffer.append("m.htmlMessage ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("template")) {
						buffer.append("m.template ");
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
