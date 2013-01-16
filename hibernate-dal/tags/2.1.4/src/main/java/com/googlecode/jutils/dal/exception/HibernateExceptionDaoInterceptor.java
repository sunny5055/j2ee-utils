package com.googlecode.jutils.dal.exception;

import org.hibernate.HibernateException;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

/**
 * Class HibernateExceptionDaoInterceptor.
 */
public class HibernateExceptionDaoInterceptor implements ThrowsAdvice {

    /**
     * Encapsulate Hibernate exceptions into Spring data exceptions.
     * 
     * @param ex
     * @throws DataAccessException
     */
    public void afterThrowing(HibernateException ex) throws DataAccessException {
        throw SessionFactoryUtils.convertHibernateAccessException(ex);
    }
}
