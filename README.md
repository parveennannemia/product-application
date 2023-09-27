# product-application
Product Application

default port : 8081

End Points Can be tested in postman:
1. GetProducts -  Get the list of active products sorted by the latest first.

    **URL**:  http://localhost:8080/api/products
2. Search -      Search for products based on the given criteria (product name, price range, and posted date range).
   Its Get http method so input has to pass as a queryString.
   **URL**:  http://localhost:8080/api/products/search?productName=iphone20

3. Add product -  Create a new product, but the price must not exceed $10,000.
      If the price is more than $5,000, the product should be pushed to the approval queue.
   Its a Post http method, request has to give in body
   **URL**:  http://localhost:8080/api/products
   Example request body, It accepts Json format:
    {
    "id" : 1,
    "name" : "iphone20",
    "price": 5001
}

4. update Product -  Update an existing product, but if the price is more than 50% of its previous price,
   the product should be pushed to the approval queue.
      Its a PUT http method, request has to give in body
   **URL**:  http://localhost:8080/api/products/1
   Example: It accepts Json format:
    {
    "id" : 1,
    "name" : "watch",
    "price": 500
     }

 5. Delete Product -  Delete a product, and it should be pushed to the approval queue.
        ITs Delete http method, Ex **URL**: http://localhost:8080/api/products/5

 6. Get ALl Approval_Queue -     Get all the products in the approval queue, sorted by request date (earliest first).
     Its a Get http method, **URL**: http://localhost:8080/api/products/approval-queue
 7. Approve Product:    Approve a product from the approval queue. The product state should be updated, and it should no longer appear in the approval queue.
       Its PUT Http method, Ex **URL**:  http://localhost:8080/api/products/approval-queue/103/approve
 
 8. Reject Product -   Reject a product from the approval queue. The product state should remain the same, and it should no longer appear in the approval queue.
           Its PUT http method, **URL** http://localhost:8080/api/products//approval-queue/100/reject")



