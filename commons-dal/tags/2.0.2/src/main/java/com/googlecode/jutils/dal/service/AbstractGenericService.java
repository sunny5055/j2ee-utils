package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.googlecode.jutils.dal.Result;
import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractGenericService.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 * @param <DAO>
 *            the generic type
 */
public abstract class AbstractGenericService<PK extends Serializable, E extends Dto<PK>, DAO extends GenericDao<PK, E>> extends AbstractGenericReadService<PK, E, DAO> implements
		GenericService<PK, E> {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<E> create(E dto) {
		final Result<E> result = new Result<E>();
		if (dto != null) {
			final PK pk = this.dao.create(dto);
			if (pk != null) {
				final E entity = this.get(pk);
				if (entity != null) {
					result.setValid(true);
					result.setValue(entity);
				} else {
					result.setValid(false);
					result.addErrorMessage("error_created_entity_not_found");
				}
			} else {
				result.setValid(false);
				result.addErrorMessage("error_creation_failed");
			}
		} else {
			result.setValid(false);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<E> update(E dto) {
		final Result<E> result = new Result<E>();
		if (dto != null) {
			final Integer updated = this.dao.update(dto);
			if (updated != 0) {
				final E entity = this.get(dto.getPrimaryKey());
				if (entity != null) {
					result.setValid(true);
					result.setValue(entity);
				} else {
					result.setValid(false);
					result.addErrorMessage("error_updated_entity_not_found");
				}
			} else {
				result.setValid(false);
				result.addErrorMessage("error_update_failed");
			}
		} else {
			result.setValid(false);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> delete(E dto) {
		final Result<Integer> result = new Result<Integer>();
		if (dto != null) {
			if (isRemovable(dto)) {
				final Integer deleted = this.dao.delete(dto);
				result.setValue(deleted);
				if (deleted == 1) {
					result.setValid(true);
				} else {
					result.setValid(false);
					result.addErrorMessage("error_delete_one_failed");
				}
			} else {
				result.setValid(false);
				result.setValue(0);
				result.addErrorMessage("error_entity_not_removable");
			}
		} else {
			result.setValid(false);
			result.setValue(0);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> delete(E... dtos) {
		Result<Integer> result = new Result<Integer>();
		if (!ArrayUtils.isEmpty(dtos)) {
			result = delete(Arrays.asList(dtos));
		} else {
			result.setValid(false);
			result.setValue(0);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> delete(Collection<E> dtos) {
		final Result<Integer> result = new Result<Integer>();
		if (!CollectionUtils.isEmpty(dtos)) {
			Integer deleted = 0;
			for (final E dto : dtos) {
				if (isRemovable(dto)) {
					deleted += this.dao.delete(dto);
				}
			}
			result.setValue(deleted);
			if (deleted != 0 && deleted == dtos.size()) {
				result.setValid(true);
			} else {
				result.setValid(false);
				result.addErrorMessage("error_delete_multiple_failed");
			}
		} else {
			result.setValid(false);
			result.setValue(0);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> deleteByPrimaryKey(PK pk) {
		Result<Integer> result = new Result<Integer>();
		if (pk != null) {
			final E dto = this.dao.get(pk);
			if (dto != null) {
				result = delete(dto);
			} else {
				result.setValid(false);
				result.setValue(0);
				result.addErrorMessage("error_entity_not_found");
			}
		} else {
			result.setValid(false);
			result.setValue(0);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> deleteByPrimaryKeys(PK... pks) {
		Result<Integer> result = new Result<Integer>();
		if (!ArrayUtils.isEmpty(pks)) {
			result = deleteByPrimaryKeys(Arrays.asList(pks));
		} else {
			result.setValid(false);
			result.setValue(0);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> deleteByPrimaryKeys(Collection<PK> pks) {
		Result<Integer> result = new Result<Integer>();
		if (!CollectionUtils.isEmpty(pks)) {
			final List<E> dtos = this.dao.getObjects(pks);
			result = delete(dtos);
			if (result != null && result.getValue() != pks.size()) {
				result.setValid(false);
				result.addErrorMessage("error_delete_multiple_failed");
			}
		} else {
			result.setValid(false);
			result.setValue(0);
			result.addErrorMessage("error_wrong_parameter");
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Result<Integer> deleteAll() {
		final List<E> dtos = this.dao.findAll();
		return delete(dtos);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean isRemovable(PK pk) {
		return true;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean isRemovable(E dto) {
		boolean removable = true;
		if (dto != null) {
			removable = isRemovable(dto.getPrimaryKey());
		}
		return removable;
	}
}
