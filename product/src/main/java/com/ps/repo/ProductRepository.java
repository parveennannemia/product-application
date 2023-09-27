package com.ps.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer>, ProductRepositoryCustom {
    @Query(value = "select p from Product p WHERE p.status = 'active' AND p.state != 'pending' order by p.lastUpdated desc")
    List<Product> getActiveProducts();

    /** Handling this using in ProductRepositoryCustom to make a dynamic query based on the given predicates in the request. */
    List<Product> findProductByPredicates(String name,
                                          Integer minPrice,
                                          Integer maxPrice,
                                          Long minPostedDate,
                                          Long maxPostedDate);

    @Query(value = "select p from Product p where p.state = 'pending' order by p.lastUpdated desc")
    List<Product> findPendingQueue();
}
