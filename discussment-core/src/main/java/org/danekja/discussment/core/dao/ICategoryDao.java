package org.danekja.discussment.core.dao;

import org.danekja.discussment.core.domain.Category;

import java.util.List;

/**
 * Created by Martin Bláha on 13.05.17.
 */
public interface ICategoryDao extends IGenericDao<Category> {
    List<Category> getCategories();
}