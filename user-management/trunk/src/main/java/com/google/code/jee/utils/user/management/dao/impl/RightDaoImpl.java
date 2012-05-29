package com.google.code.jee.utils.user.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.dal.util.QueryUtil;
import com.google.code.jee.utils.user.management.dao.RightDao;
import com.google.code.jee.utils.user.management.model.Right;

/**
 * The Class RightDaoImpl.
 */
@Repository
public class RightDaoImpl extends AbstractGenericDaoHibernate<Integer, Right> implements
		RightDao {

	/**
     * Instantiates a new right right dao impl.
     */
    public RightDaoImpl() {
        super(Right.class);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByName(String name) {
        return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), RightDao.COUNT_BY_NAME,
                new String[] { "name" }, name);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Right findByName(String name) {
        return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), RightDao.FIND_BY_NAME,
                new String[] { "name" }, name);

    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForRoleId(Integer roleId) {
        return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), RightDao.COUNT_FOR_ROLE_ID,
                new String[] { "roleId" }, roleId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Right> findAllByRoleId(Integer roleId) {
        return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), RightDao.FIND_ALL_BY_ROLE_ID,
                new String[] { "roleId" }, roleId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForRoleIdAndName(Integer roleId, String rightName) {
        return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(),
                RightDao.COUNT_FOR_ROLE_ID_AND_NAME, new String[] { "roleId", "rightName" }, roleId,
                rightName);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Right findByRoleIdAndName(Integer roleId, String rightName) {
        return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), RightDao.FIND_BY_ROLE_ID_AND_NAME,
                new String[] { "roleId", "rightName" }, roleId, rightName);
    }

    /**
     * Gets the search.
     * 
     * @param roleId the mail id
     * @param searchCriteria the search criteria
     * @return the search
     */
    protected Search getSearch(Integer roleId, SearchCriteria searchCriteria) {
        Search search = null;
        if (roleId != null && searchCriteria != null) {
            search = new Search();
            final StringBuilder buffer = new StringBuilder();
            buffer.append("from Role r ");
            buffer.append("left join r.rights as right ");
            buffer.append("where r.id = :roleId ");
            search.addIntegerParameter("roleId", roleId);

            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("name")) {
                            buffer.append("upper(right.name) like upper(:name) ");
                            search.addStringParameter("name", entry.getValue());
                        }
                        index++;
                    }
                }
            }

            search.setCountQuery("select count(right) " + buffer.toString());

            if (searchCriteria.hasSorts()) {
                buffer.append("order by ");
                int index = 0;
                for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
                    if (index != 0) {
                        buffer.append(", ");
                    }
                    if (entry.getKey().equals("name")) {
                        buffer.append("rig.name ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    }
                    index++;
                }
            }

            search.setQuery("select right " + buffer.toString());
        }
        return search;
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
            buffer.append("from Right maa ");
            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("name")) {
                            buffer.append("upper(rig.name) like upper(:name) ");
                            search.addStringParameter("name", entry.getValue());
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
                        buffer.append("rig.name ");
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
