package com.ps.controller;


import com.ps.exception.PriceLimitExceeded;
import com.ps.exception.ProductNotFoundException;
import com.ps.repo.Product;
import com.ps.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * Get the list of active products sorted by the latest first.
     */
    @GetMapping(path = "/products")
    public @ResponseBody List<Product> getActiveProducts() {
        return productService.getProducts();
    }

    /**
     * Search for products based on the given criteria (product name, price range, and posted date range).
     */
    @GetMapping(path = "/products/search")
    public @ResponseBody List<Product> searchProducts
    (@RequestParam(required = false) String productName, @RequestParam(required = false) Integer minPrice,
     @RequestParam(required = false) Integer maxPrice, @RequestParam(required = false) Long minPostedDate, @RequestParam(required = false) Long maxPostedDate) {
        return productService.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
    }

    /**
     * Create a new product, but the price must not exceed $10,000.
     * If the price is more than $5,000, the product should be pushed to the approval queue.
     */
    @PostMapping(path = "/products")
    public @ResponseBody ResponseEntity<HttpStatus> addProduct(@RequestBody Product product) throws PriceLimitExceeded {
        productService.createProduct(product);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * Update an existing product, but if the price is more than 50% of its previous price,
     * the product should be pushed to the approval queue.
     */
    @PutMapping(path = "/products/{productId}") 
    public @ResponseBody ResponseEntity<HttpStatus> updateProduct(@PathVariable Integer productId, @RequestBody Product product)
            throws ProductNotFoundException {
        productService.update(productId, product);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * Delete a product, and it should be pushed to the approval queue.
     */
    @DeleteMapping(path = "/products/{productId}")
    public @ResponseBody ResponseEntity<HttpStatus> deleteProduct(@PathVariable Integer productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);

    }

    /**
     * Get all the products in the approval queue, sorted by request date (earliest first).
     */
    @GetMapping(path = "/products/approval-queue")
    public @ResponseBody Iterable<Product> getPendingApprovals() {
        return productService.getPendingProducts();
    }

    /**
     * Approve a product from the approval queue. The product state should be updated, and it should no longer appear in the approval queue.
     */
    @PutMapping(path = "/products/approval-queue/{approvalId}/approve")
    public @ResponseBody ResponseEntity<HttpStatus> approveProduct(@PathVariable Integer approvalId)
            throws ProductNotFoundException {
        productService.approvalQueue(approvalId, "approved");
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * Reject a product from the approval queue. The product state should remain the same, and it should no longer appear in the approval queue.
     */
    @PutMapping(path = "/products//approval-queue/{approvalId}/reject")
    public @ResponseBody ResponseEntity<HttpStatus> rejectProduct(@PathVariable Integer approvalId)
            throws ProductNotFoundException {
        productService.approvalQueue(approvalId, "rejected");
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
