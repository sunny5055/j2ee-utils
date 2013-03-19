package com.google.code.jee.utils.user.management.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.dal.util.QueryUtil;
import com.google.code.jee.utils.user.management.dao.RoleDao;
import com.google.code.jee.utils.user.management.model.Role;

/**
 * The Class RoleDaoImpl.
 */
@Repository
public class RoleDaoImpl  extends AbstractGenericDaoHibernate<Integer, Role> implements
		RoleDao {

	/**
     * Instantiates a new role dao impl.
     */
    public RoleDaoImpl() {
        super(Role.class);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByCode(String code) {
        return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), RoleDao.COUNT_BY_CODE,
                new String[] { "code" }, code);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Role findByCode(String code) {
        return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), RoleDao.FIND_BY_CODE,
                new String[] { "code" }, code);

    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByUserId(Integer userId) {
        return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), RoleDao.COUNT_BY_USER_ID,
                new String[] { "userId" }, userId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Role> findAllByUserId(Integer userId) {
        return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), RoleDao.FIND_ALL_BY_USER_ID,
                new String[] { "userId" }, userId);
    }
    
    /**
     * {@inheritedDoc}
     */
	@Override
	public Integer countByRightId(Integer rightId) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), RoleDao.COUNT_BY_RIGHT_ID,
                new String[] { "rightId" }, rightId);
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
            buffer.append("from Role r ");
            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("code")) {
                            buffer.append("upper(r.code) like upper(:code) ");
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
                        buffer.append("r.code ");
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
