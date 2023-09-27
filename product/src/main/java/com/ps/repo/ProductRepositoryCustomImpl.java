package com.ps.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Adding all the predicates given in the request to the predicates(list) and passing it to the query's where clause using entityManager.
 * and returning the list of products
 */
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findProductByPredicates(String name,
                                                 Integer minPrice,
                                                 Integer maxPrice,
                                                 Long minPostedDate,
                                                 Long maxPostedDate) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();
        Path<Integer> price = product.get("price");
        Path<Long> postedDate = product.get("postedDate");

        if (name != null) {
            Path<String> namePath = product.get("name");
            predicates.add(cb.like(namePath, '%' + name + '%'));
        }

        if (minPrice != null && maxPrice != null) {
            predicates.add(cb.between(price, minPrice, maxPrice));
        } else if (minPrice != null) {
            predicates.add(cb.ge(price, minPrice));
        } else if (maxPrice != null) {
            predicates.add(cb.le(price, maxPrice));
        }

        if (minPostedDate != null && maxPostedDate != null) {
            predicates.add(cb.between(postedDate, minPostedDate, maxPostedDate));
        } else if (minPostedDate != null) {
            predicates.add(cb.ge(postedDate, minPostedDate));
        } else if (maxPostedDate != null) {
            predicates.add(cb.le(postedDate, maxPostedDate));
        }

        query.select(product).where(predicates.toArray(new Predicate[predicates.size()]));
        return entityManager.createQuery(query).getResultList();
    }
}
