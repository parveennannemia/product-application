package com.ps.repo;

import java.util.List;

public interface ProductRepositoryCustom {
    public List<Product> findProductByPredicates(String name,
                                                 Integer minPrice,
                                                 Integer maxPrice,
                                                 Long minPostedDate,
                                                 Long maxPostedDate);
}
