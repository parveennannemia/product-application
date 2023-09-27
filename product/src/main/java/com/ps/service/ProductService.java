package com.ps.service;

import com.ps.exception.PriceLimitExceeded;
import com.ps.exception.ProductNotFoundException;
import com.ps.repo.Product;
import com.ps.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;


    public void update(Integer productId, Product product) throws ProductNotFoundException {
        Optional<Product> optionalExistingProduct = productRepository.findById(productId);
        if (optionalExistingProduct.isPresent()) {
            Product existingProduct = optionalExistingProduct.get();
            if (product.getPrice() > existingProduct.getPrice() * 150 / 100) {
                product.setState("pending");
            } else {
                product.setState("approved");
            }
            product.setStatus("active");
            Calendar cal = Calendar.getInstance();
            long now = cal.getTimeInMillis();
            product.setLastUpdated(now);
            existingProduct.setId(product.getId());
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStatus(product.getStatus());
            existingProduct.setLastUpdated(product.getLastUpdated());
            existingProduct.setState(product.getState());
            productRepository.save(existingProduct);
        } else {
            throw new ProductNotFoundException();
        }
    }

    public List<Product> getProducts() {
        return productRepository.getActiveProducts();
    }

    public List<Product> searchProducts(String name, Integer minPrice, Integer maxPrice, Long minPostedDate, Long maxPostedDate) {
        return productRepository.findProductByPredicates(name, minPrice, maxPrice, minPostedDate, maxPostedDate);
    }

    public void createProduct(Product product) throws PriceLimitExceeded {
        product.setStatus("active");
        product.setState("approved");

        if (product.getPrice() > 10000) {
            throw new PriceLimitExceeded();
        } else if (product.getPrice() > 5000) {
            product.setState("pending");
        }
        Calendar cal = Calendar.getInstance();
        Long now = cal.getTimeInMillis();

        product.setPostedDate(now);
        product.setLastUpdated(now);
        productRepository.save(product);
    }

    public void deleteProduct(Integer productId) throws ProductNotFoundException {
        Optional<Product> oldProduct = productRepository.findById(productId);
        if (oldProduct.isPresent()) {
            Product product = oldProduct.get();
            product.setStatus("deleted");
            product.setState("pending");
            Calendar cal = Calendar.getInstance();
            long now = cal.getTimeInMillis();
            product.setLastUpdated(now);
            productRepository.save(product);
        } else
            throw new ProductNotFoundException();
    }

    public Iterable<Product> getPendingProducts() {
        return productRepository.findPendingQueue();
    }

    public void approvalQueue(Integer productId, String state) throws ProductNotFoundException {
        Optional<Product> oldProduct = productRepository.findById(productId);
        if (oldProduct.isPresent()) {
            Product product = oldProduct.get();
            product.setState(state);
            Calendar cal = Calendar.getInstance();
            long now = cal.getTimeInMillis();
            product.setLastUpdated(now);
            productRepository.save(product);
        } else
            throw new ProductNotFoundException();
    }
}
