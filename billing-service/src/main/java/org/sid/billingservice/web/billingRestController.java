package org.sid.billingservice.web;

import jakarta.ws.rs.QueryParam;
import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductRestClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repository.BillRepository;
import org.sid.billingservice.repository.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class billingRestController {
    private ProductItemRepository productItemRepository;
    private BillRepository billRepository;
    private ProductRestClient productRestClient;
    private CustomerRestClient customerRestClient;

    public billingRestController(ProductItemRepository productItemRepository, BillRepository billRepository, ProductRestClient productRestClient, CustomerRestClient customerRestClient) {
        this.productItemRepository = productItemRepository;
        this.billRepository = billRepository;
        this.productRestClient = productRestClient;
        this.customerRestClient = customerRestClient;
    }

    @GetMapping("/bill/{id}")
    public Bill getBillBYId(@PathVariable(name = "id") Long id){
    Bill bill=billRepository.findById(id).get();
        Customer customer=customerRestClient.getCustomerById(bill.getCustomerID() );
        bill.setCustomer(customer);
        bill.getProductItems().forEach(productItem -> {
            Product product=productRestClient.getProductById(productItem.getProductID());
//        productItem.setProduct(product);
            productItem.setProductName(product.getName());
        });
    return bill;
    }
}
