package com.googlecode.jutils.mail.hibernate.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.dao.AbstractGenericDaoHibernate;
import com.googlecode.jutils.dal.util.QueryUtil;
import com.googlecode.jutils.mail.hibernate.dao.MailAttachmentDao;
import com.googlecode.jutils.mail.hibernate.model.MailAttachment;

/**
 * The Class MailAttachmentDaoImpl.
 */
@Repository
public class MailAttachmentDaoImpl extends AbstractGenericDaoHibernate<Integer, MailAttachment> implements MailAttachmentDao {

	/**
	 * Instantiates a new mail attachment dao impl.
	 */
	public MailAttachmentDaoImpl() {
		super(MailAttachment.class);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countByName(String name) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), MailAttachmentDao.COUNT_BY_NAME, new String[] { "name" }, name);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public MailAttachment findByName(String name) {
		return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), MailAttachmentDao.FIND_BY_NAME, new String[] { "name" }, name);

	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countForMailId(Integer mailId) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), MailAttachmentDao.COUNT_FOR_MAIL_ID, new String[] { "mailId" }, mailId);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<MailAttachment> findAllByMailId(Integer mailId) {
		return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), MailAttachmentDao.FIND_ALL_BY_MAIL_ID, new String[] { "mailId" }, mailId);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer countForMailIdAndName(Integer mailId, String attachmentName) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), MailAttachmentDao.COUNT_FOR_MAIL_ID_AND_NAME, new String[] { "mailId", "attachmentName" }, mailId,
				attachmentName);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public MailAttachment findByMailIdAndName(Integer mailId, String attachmentName) {
		return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), MailAttachmentDao.FIND_BY_MAIL_ID_AND_NAME, new String[] { "mailId", "attachmentName" }, mailId,
				attachmentName);
	}

	/**
	 * Gets the search.
	 * 
	 * @param mailId
	 *            the mail id
	 * @param searchCriteria
	 *            the search criteria
	 * @return the search
	 */
	protected Search getSearch(Integer mailId, SearchCriteria searchCriteria) {
		Search search = null;
		if (mailId != null && searchCriteria != null) {
			search = new Search();
			final StringBuilder buffer = new StringBuilder();
			buffer.append("from Mail m ");
			buffer.append("left join mai.attachments as attachment ");
			buffer.append("where m.id = :mailId ");
			search.addIntegerParameter("mailId", mailId);

			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("name")) {
							buffer.append("upper(attachment.name) like upper(:name) ");
							search.addStringParameter("name", entry.getValue());
						}
						index++;
					}
				}
			}

			search.setCountQuery("select count(attachment) " + buffer.toString());

			if (searchCriteria.hasSorts()) {
				buffer.append("order by ");
				int index = 0;
				for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
					if (index != 0) {
						buffer.append(", ");
					}
					if (entry.getKey().equals("name")) {
						buffer.append("attachment.name ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					}
					index++;
				}
			}

			search.setQuery("select attachment " + buffer.toString());
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
			buffer.append("from MailAttachment maa ");
			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("name")) {
							buffer.append("upper(maa.name) like upper(:name) ");
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
						buffer.append("maa.name ");
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
