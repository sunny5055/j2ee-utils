package com.googlecode.jutils.dal.test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.dal.entity.BaseEntity;
import com.googlecode.jutils.dal.service.GenericService;

public abstract class AbstractGenericServiceTest<PK extends Serializable, DTO, E extends BaseEntity<PK>, S extends GenericService<PK, DTO>> extends
		AbstractGenericReadServiceTest<PK, DTO, E, S> {

	@Test
	public void testCreate() {
		final DTO dto = createDto();

		final PK pk = service.create(dto);
		Assert.assertNotNull(pk);

		final DTO result = service.get(pk);
		Assert.assertNotNull(result);
		assertDto(dto, result);

		final int count = service.count();
		Assert.assertEquals(primaryKeys.size() + 1, count);
	}

	@Test
	public void testUpdate() {
		final PK primaryKey = primaryKeys.get(0);
		final DTO dto = service.get(primaryKey);
		updateDto(dto);

		service.update(dto);

		final DTO result = service.get(primaryKey);
		Assert.assertNotNull(result);
		assertDto(dto, result);
	}

	@Test
	public void testDelete() {
		final PK primaryKey = primaryKeys.get(0);
		DTO dto = service.get(primaryKey);

		Integer result = null;
		result = service.delete(dto);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		dto = service.get(primaryKey);
		Assert.assertNull(dto);

		dto = null;
		result = service.delete(dto);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteWithList() {
		final PK primaryKey = primaryKeys.get(0);
		final PK primaryKey2 = primaryKeys.get(1);
		final PK primaryKey3 = primaryKeys.get(2);

		DTO dto = service.get(primaryKey);
		DTO dto2 = service.get(primaryKey2);

		Integer result = null;
		result = service.delete(Arrays.asList(dto, dto2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 2, result);

		dto = service.get(primaryKey);
		Assert.assertNull(dto);

		dto2 = service.get(primaryKey2);
		Assert.assertNull(dto);

		dto = service.get(primaryKey);
		dto2 = service.get(primaryKey3);

		result = service.delete(Arrays.asList(dto, dto2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		dto = null;
		dto2 = null;
		result = service.delete(Arrays.asList(dto, dto2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	@Test
	public void testDeleteByPrimaryKey() {
		final PK primaryKey = primaryKeys.get(0);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		DTO dto = null;

		Integer result = null;
		result = service.deleteByPrimaryKey(primaryKey);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		dto = service.get(primaryKey);
		Assert.assertNull(dto);

		result = service.deleteByPrimaryKey((PK) null);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);

		result = service.deleteByPrimaryKey(fakePrimaryKey);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteByPrimaryKeys() {
		final PK primaryKey = primaryKeys.get(0);
		final PK primaryKey2 = primaryKeys.get(1);
		final PK primaryKey3 = primaryKeys.get(2);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		final PK fakePrimaryKey2 = fakePrimaryKeys.get(1);
		DTO dto = null;
		DTO dto2 = null;

		Integer result = null;
		result = service.deleteByPrimaryKeys(Arrays.asList(primaryKey, primaryKey2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 2, result);

		dto = service.get(primaryKey);
		Assert.assertNull(dto);

		dto2 = service.get(primaryKey2);
		Assert.assertNull(dto2);

		result = service.deleteByPrimaryKeys(Arrays.asList(primaryKey, primaryKey3));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		dto = service.get(primaryKey3);
		Assert.assertNull(dto);

		result = service.deleteByPrimaryKeys(Arrays.asList((PK) null, (PK) null));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);

		result = service.deleteByPrimaryKeys(Arrays.asList(fakePrimaryKey, fakePrimaryKey2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	protected abstract DTO createDto();

	protected abstract void updateDto(DTO dto);

	protected abstract void assertDto(DTO dto, DTO dto2);

	protected DTO instantiateDto() {
		DTO dto = null;
		try {
			dto = ClassUtil.instantiateClass(dtoClass);
		} catch (final IllegalArgumentException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		} catch (final InstantiationException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		} catch (final IllegalAccessException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		} catch (final InvocationTargetException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		}
		return dto;
	}
}
