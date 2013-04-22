package com.googlecode.jutils.user.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.dao.AbstractGenericJpaDao;
import com.googlecode.jutils.dal.util.QueryUtil;
import com.googlecode.jutils.user.management.dao.RightDao;
import com.googlecode.jutils.user.management.model.Right;

/**
 * The Class RightDaoImpl.
 */
@Repository
public class RightDaoImpl extends AbstractGenericJpaDao<Integer, Right> implements RightDao {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countByCode(String code) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, RightDao.COUNT_BY_CODE, new String[] { "code" }, code);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Right findByCode(String code) {
		return QueryUtil.getByNamedQueryAndNamedParam(entityManager, RightDao.FIND_BY_CODE, new String[] { "code" }, code);

	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countByRoleId(Integer roleId) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, RightDao.COUNT_BY_ROLE_ID, new String[] { "roleId" }, roleId);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<Right> findAllByRoleId(Integer roleId) {
		return QueryUtil.findByNamedQueryAndNamedParam(entityManager, RightDao.FIND_ALL_BY_ROLE_ID, new String[] { "roleId" }, roleId);
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
			buffer.append("from Right rig ");
			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("code")) {
							buffer.append("upper(rig.code) like upper(:code) ");
							search.addStringParameter("code", entry.getValue());
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
					if (entry.getKey().equals("code")) {
						buffer.append("rig.code ");
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
