package com.googlecode.jutils.bundle.hibernate.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.bundle.hibernate.dao.LabelDao;
import com.googlecode.jutils.bundle.hibernate.model.Label;
import com.googlecode.jutils.bundle.hibernate.model.LabelId;
import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.dao.AbstractGenericDaoHibernate;
import com.googlecode.jutils.dal.util.QueryUtil;

/**
 * The Class LabelDaoImpl.
 */
@Repository
public class LabelDaoImpl extends AbstractGenericDaoHibernate<LabelId, Label> implements LabelDao {

	/**
	 * Instantiates a new label dao impl.
	 */
	public LabelDaoImpl() {
		super(Label.class);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countForKey(String key) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), LabelDao.COUNT_FOR_KEY, new String[] { "key" }, key);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<Label> findAllByKey(String key) {
		return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), LabelDao.FIND_ALL_BY_KEY, new String[] { "key" }, key);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countForLanguage(String language) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), LabelDao.COUNT_FOR_LANGUAGE, new String[] { "language" }, language);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<Label> findAllByLanguage(String language) {
		return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), LabelDao.FIND_ALL_BY_LANGUAGE, new String[] { "language" }, language);
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
			buffer.append("from Label l ");
			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("key")) {
							buffer.append("upper(l.primaryKey.key) like upper(:key) ");
							search.addStringParameter("key", entry.getValue());
						} else if (entry.getKey().equals("language")) {
							buffer.append("upper(l.primaryKey.language) like upper(:language) ");
							search.addStringParameter("language", entry.getValue());
						} else if (entry.getKey().equals("value")) {
							buffer.append("upper(l.value) like upper(:value) ");
							search.addStringParameter("value", entry.getValue());
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
					if (entry.getKey().equals("key")) {
						buffer.append("l.primaryKey.key ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("language")) {
						buffer.append("l.primaryKey.language ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("value")) {
						buffer.append("l.value ");
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
