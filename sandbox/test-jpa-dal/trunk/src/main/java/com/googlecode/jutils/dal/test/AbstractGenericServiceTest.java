package com.googlecode.jutils.dal.test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;

public abstract class AbstractGenericServiceTest<PK extends Serializable, E extends Dto<PK>, S extends GenericService<PK, E>> extends AbstractGenericReadServiceTest<PK, E, S> {

	@Test
	public void testCreate() {
		final E entity = createEntity();

		final PK pk = service.create(entity);
		Assert.assertNotNull(pk);

		final E result = service.get(pk);
		Assert.assertNotNull(result);
		assertEntity(entity, result);

		final int count = service.count();
		Assert.assertEquals(primaryKeys.size() + 1, count);
	}

	@Test
	public void testUpdate() {
		final PK primaryKey = primaryKeys.get(0);
		final E entity = service.get(primaryKey);
		updateEntity(entity);

		service.update(entity);

		final E result = service.get(primaryKey);
		Assert.assertNotNull(result);
		assertEntity(entity, result);
	}

	@Test
	public void testDelete() {
		final PK primaryKey = primaryKeys.get(0);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		E entity = service.get(primaryKey);

		Integer result = null;
		result = service.delete(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity = null;
		result = service.delete(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);

		entity = instantiateEntity();
		entity.setPrimaryKey(fakePrimaryKey);
		result = service.delete(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteWithList() {
		final PK primaryKey = primaryKeys.get(0);
		final PK primaryKey2 = primaryKeys.get(1);
		final PK primaryKey3 = primaryKeys.get(2);

		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		final PK fakePrimaryKey2 = fakePrimaryKeys.get(1);
		E entity = service.get(primaryKey);
		E entity2 = service.get(primaryKey2);

		Integer result = null;
		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 2, result);

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity2 = service.get(primaryKey2);
		Assert.assertNull(entity);

		entity = service.get(primaryKey);
		entity2 = service.get(primaryKey3);

		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		entity = null;
		entity2 = null;
		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);

		entity = instantiateEntity();
		entity.setPrimaryKey(fakePrimaryKey);

		entity2 = instantiateEntity();
		entity2.setPrimaryKey(fakePrimaryKey2);
		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	@Test
	public void testDeleteByPrimaryKey() {
		final PK primaryKey = primaryKeys.get(0);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		E entity = null;

		Integer result = null;
		result = service.deleteByPrimaryKey(primaryKey);
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

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
		E entity = null;
		E entity2 = null;

		Integer result = null;
		result = service.deleteByPrimaryKeys(Arrays.asList(primaryKey, primaryKey2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 2, result);

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity2 = service.get(primaryKey2);
		Assert.assertNull(entity2);

		result = service.deleteByPrimaryKeys(Arrays.asList(primaryKey, primaryKey3));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 1, result);

		entity = service.get(primaryKey3);
		Assert.assertNull(entity);

		result = service.deleteByPrimaryKeys(Arrays.asList((PK) null, (PK) null));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);

		result = service.deleteByPrimaryKeys(Arrays.asList(fakePrimaryKey, fakePrimaryKey2));
		Assert.assertNotNull(result);
		Assert.assertEquals((Integer) 0, result);
	}

	protected abstract E createEntity();

	protected abstract void updateEntity(E entity);

	protected abstract void assertEntity(E entity, E entity2);

	protected E instantiateEntity() {
		E entity = null;
		try {
			entity = ClassUtil.instantiateClass(entityClass);
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
		return entity;
	}
}
