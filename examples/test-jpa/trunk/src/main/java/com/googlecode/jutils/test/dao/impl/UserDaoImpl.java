package com.googlecode.jutils.test.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.dao.AbstractGenericJpaDao;
import com.googlecode.jutils.test.dao.UserDao;
import com.googlecode.jutils.test.model.User;

@Repository
public class UserDaoImpl extends AbstractGenericJpaDao<Integer, User> implements UserDao {

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
						if (entry.getKey().equals("name")) {
							buffer.append("upper(u.name) like upper(:name) ");
							search.addStringParameter("name", entry.getValue());
						} else if (entry.getKey().equals("lastname")) {
							buffer.append("u.lastname like upper(:lastname) ");
							search.addStringParameter("lastname", entry.getValue());
						}

						index++;
					}
				}
			}

			search.setCountQuery("select count(*) " + buffer.toString());

			buffer.append("order by ");
			if (searchCriteria.hasSorts()) {
				int index = 0;
				for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
					if (index != 0) {
						buffer.append(", ");
					}
					if (entry.getKey().equals("name")) {
						buffer.append("u.name ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("lastname")) {
						buffer.append("u.lastname ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					}

					index++;
				}
			}

			if (searchCriteria.hasSorts()) {
				buffer.append(", ");
			}

			search.setQuery(buffer.toString());
		}
		return search;
	}
}
