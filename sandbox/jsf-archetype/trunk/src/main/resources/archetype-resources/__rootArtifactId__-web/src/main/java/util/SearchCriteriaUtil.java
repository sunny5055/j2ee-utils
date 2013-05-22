#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortMeta;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;

/**
 * The Class SearchCriteriaUtil.
 */
public final class SearchCriteriaUtil {

	/**
	 * Instantiates a new search criteria util.
	 */
	private SearchCriteriaUtil() {
		super();
	}

	/**
	 * To search criteria.
	 *
	 * @param first
	 *            the first
	 * @param pageSize
	 *            the page size
	 * @param sortFields
	 *            the sort fields
	 * @param filters
	 *            the filters
	 * @return the search criteria
	 */
	public static SearchCriteria toSearchCriteria(int first, int pageSize, List<SortMeta> sortFields, Map<String, String> filters) {
		final SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setFirstResult(first);
		searchCriteria.setMaxResults(pageSize);

		final Map<String, SortOrder> sorts = toSortsMap(sortFields);
		searchCriteria.setSorts(sorts);

		final Map<String, Object> objectFilters = toObjectFilters(filters);
		searchCriteria.setFilters(objectFilters);

		return searchCriteria;
	}

	/**
	 * To search criteria.
	 *
	 * @param first
	 *            the first
	 * @param pageSize
	 *            the page size
	 * @param sortField
	 *            the sort field
	 * @param sortOrder
	 *            the sort order
	 * @param filters
	 *            the filters
	 * @return the search criteria
	 */
	public static SearchCriteria toSearchCriteria(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map<String, String> filters) {
		final SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setFirstResult(first);
		searchCriteria.setMaxResults(pageSize);

		final Map<String, SortOrder> sorts = toSortsMap(sortField, sortOrder);
		searchCriteria.setSorts(sorts);

		final Map<String, Object> objectFilters = toObjectFilters(filters);
		searchCriteria.setFilters(objectFilters);

		return searchCriteria;
	}

	/**
	 * To sorts map.
	 *
	 * @param sortFields
	 *            the sort fields
	 * @return the map
	 */
	private static Map<String, SortOrder> toSortsMap(List<SortMeta> sortFields) {
		final Map<String, SortOrder> sortsMap = new HashMap<String, SortOrder>();
		if (!CollectionUtil.isEmpty(sortFields)) {
			for (final SortMeta sortField : sortFields) {
				final SortOrder order = convertFromPrimefaces(sortField.getSortOrder());
				sortsMap.put(sortField.getSortField(), order);
			}
		}
		return sortsMap;
	}

	/**
	 * To sorts map.
	 *
	 * @param sortField
	 *            the sort field
	 * @param sortOrder
	 *            the sort order
	 * @return the map
	 */
	private static Map<String, SortOrder> toSortsMap(String sortField, org.primefaces.model.SortOrder sortOrder) {
		final Map<String, SortOrder> sortsMap = new HashMap<String, SortOrder>();
		if (!StringUtil.isBlank(sortField)) {
			final SortOrder order = convertFromPrimefaces(sortOrder);
			sortsMap.put(sortField, order);
		}
		return sortsMap;
	}

	/**
	 * Convert from primefaces.
	 *
	 * @param order
	 *            the order
	 * @return the sort order
	 */
	private static SortOrder convertFromPrimefaces(org.primefaces.model.SortOrder order) {
		SortOrder sortOrder = SortOrder.UNSORTED;
		if (order == org.primefaces.model.SortOrder.ASCENDING) {
			sortOrder = SortOrder.ASCENDING;
		} else if (order == org.primefaces.model.SortOrder.DESCENDING) {
			sortOrder = SortOrder.DESCENDING;
		}
		return sortOrder;
	}

	/**
	 * To object filters.
	 *
	 * @param filters
	 *            the filters
	 * @return the map
	 */
	private static Map<String, Object> toObjectFilters(Map<String, String> filters) {
		final Map<String, Object> objectFilters = new HashMap<String, Object>();
		if (!MapUtil.isEmpty(filters)) {
			for (final String key : filters.keySet()) {
				objectFilters.put(key, filters.get(key));
			}
		}
		return objectFilters;
	}
}