package com.googlecode.jutils.dal.test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.dal.Result;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericService;

public abstract class AbstractGenericServiceTest<PK extends Serializable, E extends Dto<PK>, S extends GenericService<PK, E>> extends AbstractGenericReadServiceTest<PK, E, S> {

	public AbstractGenericServiceTest(Class<PK> pkClass, Class<E> entityClass) {
		super(pkClass, entityClass);
	}

	@Test
	public void testCreate() {
		final E entity = createEntity();

		final Result<E> result = service.create(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertNotNull(result.getValue());

		final E hibernateEntity = service.get(entity.getPrimaryKey());
		Assert.assertNotNull(hibernateEntity);
		assertEntity(entity, hibernateEntity);

		final int count = service.count();
		Assert.assertEquals(primaryKeys.size() + 1, count);
	}

	@Test
	public void testUpdate() {
		final PK primaryKey = primaryKeys.get(0);
		final E entity = service.get(primaryKey);
		updateEntity(entity);

		final Result<E> result = service.update(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertNotNull(result.getValue());

		final E hibernateEntity = service.get(primaryKey);
		Assert.assertNotNull(entity);
		assertEntity(entity, hibernateEntity);
	}

	@Test
	public void testDelete() {
		final PK primaryKey = primaryKeys.get(0);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		E entity = service.get(primaryKey);

		Result<Integer> result = null;
		result = service.delete(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) 1, result.getValue());

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity = null;
		result = service.delete(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());

		entity = instantiateEntity();
		entity.setPrimaryKey(fakePrimaryKey);
		result = service.delete(entity);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteWithArray() {
		final PK primaryKey = primaryKeys.get(0);
		final PK primaryKey2 = primaryKeys.get(1);
		final PK primaryKey3 = primaryKeys.get(2);

		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		final PK fakePrimaryKey2 = fakePrimaryKeys.get(1);
		E entity = service.get(primaryKey);
		E entity2 = service.get(primaryKey2);

		Result<Integer> result = null;
		result = service.delete(entity, entity2);
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) 2, result.getValue());

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity2 = service.get(primaryKey2);
		Assert.assertNull(entity);

		entity = service.get(primaryKey);
		entity2 = service.get(primaryKey3);

		result = service.delete(entity, entity2);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 1, result.getValue());

		entity = null;
		entity2 = null;
		result = service.delete(entity, entity2);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());

		entity = instantiateEntity();
		entity.setPrimaryKey(fakePrimaryKey);

		entity2 = instantiateEntity();
		entity2.setPrimaryKey(fakePrimaryKey2);
		result = service.delete(entity, entity2);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());
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

		Result<Integer> result = null;
		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) 2, result.getValue());

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity2 = service.get(primaryKey2);
		Assert.assertNull(entity);

		entity = service.get(primaryKey);
		entity2 = service.get(primaryKey3);

		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 1, result.getValue());

		entity = null;
		entity2 = null;
		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());

		entity = instantiateEntity();
		entity.setPrimaryKey(fakePrimaryKey);

		entity2 = instantiateEntity();
		entity2.setPrimaryKey(fakePrimaryKey2);
		result = service.delete(Arrays.asList(entity, entity2));
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());
	}

	@Test
	public void testDeleteByPrimaryKey() {
		final PK primaryKey = primaryKeys.get(0);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		E entity = null;

		Result<Integer> result = null;
		result = service.deleteByPrimaryKey(primaryKey);
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) 1, result.getValue());

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		result = service.deleteByPrimaryKey((PK) null);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());

		result = service.deleteByPrimaryKey(fakePrimaryKey);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());
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

		Result<Integer> result = null;
		result = service.deleteByPrimaryKeys(primaryKey, primaryKey2);
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) 2, result.getValue());

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity2 = service.get(primaryKey2);
		Assert.assertNull(entity2);

		result = service.deleteByPrimaryKeys(primaryKey, primaryKey3);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 1, result.getValue());

		entity = service.get(primaryKey3);
		Assert.assertNull(entity);

		result = service.deleteByPrimaryKeys(null, null);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());

		result = service.deleteByPrimaryKeys(fakePrimaryKey, fakePrimaryKey2);
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeleteByPrimaryKeysWithIdList() {
		final PK primaryKey = primaryKeys.get(0);
		final PK primaryKey2 = primaryKeys.get(1);
		final PK primaryKey3 = primaryKeys.get(2);
		final PK fakePrimaryKey = fakePrimaryKeys.get(0);
		final PK fakePrimaryKey2 = fakePrimaryKeys.get(1);
		E entity = null;
		E entity2 = null;

		Result<Integer> result = null;
		result = service.deleteByPrimaryKeys(Arrays.asList(primaryKey, primaryKey2));
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) 2, result.getValue());

		entity = service.get(primaryKey);
		Assert.assertNull(entity);

		entity2 = service.get(primaryKey2);
		Assert.assertNull(entity2);

		result = service.deleteByPrimaryKeys(Arrays.asList(primaryKey, primaryKey3));
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 1, result.getValue());

		entity = service.get(primaryKey3);
		Assert.assertNull(entity);

		result = service.deleteByPrimaryKeys(Arrays.asList((PK) null, (PK) null));
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());

		result = service.deleteByPrimaryKeys(Arrays.asList(fakePrimaryKey, fakePrimaryKey2));
		Assert.assertNotNull(result);
		Assert.assertEquals(false, result.isValid());
		Assert.assertEquals((Integer) 0, result.getValue());
	}

	@Test
	public void testDeleteAll() {
		Result<Integer> result = null;
		result = service.deleteAll();
		Assert.assertNotNull(result);
		Assert.assertEquals(true, result.isValid());
		Assert.assertEquals((Integer) primaryKeys.size(), result.getValue());

		final int count = service.count();
		Assert.assertEquals(0, count);
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
