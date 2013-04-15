package com.googlecode.jutils.dal.test;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.Table;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.AnnotationUtil;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.service.GenericReadService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TestExecutionListeners({ TransactionalTestExecutionListener.class, DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public abstract class AbstractGenericReadServiceTest<PK extends Serializable, E extends Dto<PK>, S extends GenericReadService<PK, E>> extends
		AbstractTransactionalJUnit4SpringContextTests {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenericReadServiceTest.class);

	protected Class<PK> pkClass;
	protected Class<E> entityClass;

	protected S service;
	protected Document document;
	protected List<PK> primaryKeys;
	protected List<PK> fakePrimaryKeys;

	public AbstractGenericReadServiceTest(Class<PK> pkClass, Class<E> entityClass) {
		this.pkClass = pkClass;
		this.entityClass = entityClass;

		this.primaryKeys = new ArrayList<PK>();
		this.fakePrimaryKeys = new ArrayList<PK>();
	}

	public abstract void setService(S service);

	@PostConstruct
	@SuppressWarnings("unchecked")
	protected void init() {
		this.document = getDocument();
		if (document != null) {
			final List<Node> nodes = document.selectNodes("//" + getTableName());
			if (!CollectionUtil.isEmpty(nodes)) {
				primaryKeys = new ArrayList<PK>();
				for (final Node node : nodes) {
					final PK id = getPrimaryKey(node);
					if (id != null) {
						primaryKeys.add(id);
					}
				}
			}

			if (!CollectionUtil.isEmpty(primaryKeys)) {
				fakePrimaryKeys = new ArrayList<PK>();

				for (int i = 0; i < primaryKeys.size(); i++) {
					PK fakeId = null;
					do {
						fakeId = getFakePrimaryKey();
					} while (primaryKeys.contains(fakeId) || fakePrimaryKeys.contains(fakeId));

					fakePrimaryKeys.add(fakeId);
				}
			}
		}
	}

	@Test
	public void testGet() {
		E entity = null;

		for (final PK id : primaryKeys) {
			entity = service.get(id);
			Assert.assertNotNull(entity);
			Assert.assertEquals(id, entity.getPrimaryKey());
		}

		for (final PK id : fakePrimaryKeys) {
			entity = service.get(id);
			Assert.assertNull(entity);
		}
	}

	@Test
	public void testGetObjects() {
		final List<PK> allPrimaryKeys = getAllPrimaryKeys();
		List<E> entities = null;

		entities = service.getObjects(primaryKeys);
		Assert.assertNotNull(entities);
		Assert.assertEquals(primaryKeys.size(), entities.size());

		entities = service.getObjects(fakePrimaryKeys);
		Assert.assertNotNull(entities);
		Assert.assertEquals(0, entities.size());

		entities = service.getObjects(allPrimaryKeys);
		Assert.assertNotNull(entities);
		Assert.assertEquals(primaryKeys.size(), entities.size());
	}

	@Test
	public void testCount() {
		final int count = service.count();
		Assert.assertEquals(primaryKeys.size(), count);
	}

	@Test
	public void testFindAll() {
		final List<E> entities = service.findAll();
		Assert.assertNotNull(entities);
		Assert.assertEquals(primaryKeys.size(), entities.size());
	}

	@Test
	public abstract void testCountWithSearchCriteria();

	@Test
	public abstract void testFindAllWithSearchCriteria();

	@Test
	public void testExistPk() {
		boolean exist = false;

		for (final PK id : primaryKeys) {
			exist = service.existPk(id);
			Assert.assertEquals(true, exist);
		}

		for (final PK id : fakePrimaryKeys) {
			exist = service.existPk(id);
			Assert.assertEquals(false, exist);
		}
	}

	protected String getTableName() {
		String tableName = null;
		if (entityClass != null) {
			final Table table = AnnotationUtil.getAnnotation(Table.class, entityClass);
			if (table != null) {
				tableName = table.name();
			}
		}
		return tableName;
	}

	protected abstract PK getPrimaryKey(Node node);

	protected abstract PK getFakePrimaryKey();

	protected List<PK> getAllPrimaryKeys() {
		final List<PK> allPrimaryKeys = new ArrayList<PK>();
		if (!CollectionUtil.isEmpty(primaryKeys) && !CollectionUtil.isEmpty(fakePrimaryKeys)) {
			allPrimaryKeys.addAll(primaryKeys);
			allPrimaryKeys.addAll(fakePrimaryKeys);
		}
		return allPrimaryKeys;
	}

	private Document getDocument() {
		Document document = null;

		final DatabaseSetup databaseSetup = AnnotationUtil.getAnnotation(DatabaseSetup.class, this.getClass());
		if (databaseSetup != null) {
			final String documentLocation = databaseSetup.value()[0];
			if (!StringUtil.isBlank(documentLocation)) {
				final ResourceLoader resourceLoader = new ClassRelativeResourceLoader(this.getClass());
				final Resource resource = resourceLoader.getResource(documentLocation);
				if (resource != null) {
					final SAXReader saxReader = new SAXReader();
					try {
						document = saxReader.read(resource.getFile());
					} catch (final DocumentException e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(e.getMessage(), e);
						}
					} catch (final IOException e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(e.getMessage(), e);
						}
					}
				}
			}
		}
		return document;
	}

}
