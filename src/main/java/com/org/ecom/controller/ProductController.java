package com.org.ecom.controller;

import com.org.ecom.dto.product.ProductDto;
import com.org.ecom.dto.product.ViewProductDto;
import com.org.ecom.dto.product.ViewProductDtoCustomer;
import com.org.ecom.entity.product.Product;
import com.org.ecom.entity.product.ProductVariation;
import com.org.ecom.exception.NotFoundException;
import com.org.ecom.repository.ProductRepository;
import com.org.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    //Product Home API
    @GetMapping("/home")
    public ResponseEntity<String> adminHome() {
        String msg = "Product Home";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    /**** PRODUCT API'S FOR SELLER ****/

    //API for adding the product only Accessible by the Sellers
    @PostMapping("/seller/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody Product product, @RequestParam("category") Long category) {
        return productService.addProduct(product, category);
    }

    //API for adding the product variations only Accessible by the Sellers
    @PostMapping("/seller/addProductVariations/{productId}")
    public void addProductVariation(@RequestBody ProductVariation productVariation, @PathVariable Long productId) {
        productService.addProductVariation(productVariation, productId);
    }

    //API for getting the product only Accessible by the Sellers
    @GetMapping("/seller/product/{id}")
    public ProductDto getProduct(@PathVariable Long id) throws NotFoundException {
        return productService.getProduct(id);
    }

    //API for Soft Deleting the product only Accessible by the Sellers
    @DeleteMapping("/seller/product/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    //API for update the product information only Accessible by the Sellers
    @PatchMapping("/seller/product/{productId}")
    public ResponseEntity<String> updateProductById(@PathVariable Long productId, @RequestBody Product product) throws NotFoundException {
        return productService.updateProductByProductId(productId, product);
    }

    //API for getting all product information
    @GetMapping("/seller/allProducts")
    public List<ViewProductDto> allProducts(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        return productService.getProductDetails(pageNo, pageSize, sortBy);
    }


    /**** PRODUCT API'S FOR ADMIN ****/

    //API for getting the single product information only Accessible by the Admin
    @GetMapping("/admin/product/{productId}")
    public ViewProductDto getOneProductForAdmin(@PathVariable Long productId) {
        return productService.viewSingleProductForAdmin(productId);
    }

    //API for getting all the product information only Accessible by the Admin
    @GetMapping("/admin/allProducts")
    public List<ViewProductDtoCustomer> allProductsForAdmin(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy) {
        return productService.getProductDetailsForAdmin(pageNo, pageSize, sortBy);
    }

    //API for Activating the product only Accessible by the Admin
    @GetMapping("/admin/product/activate/{id}")
    public ResponseEntity<String> activateProduct(@PathVariable Long id) {
        return productService.activateProduct(id);
    }

    //API for De_Activating the product only Accessible by the Admin
    @GetMapping("/admin/product/deactivate/{id}")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long id) {
        return productService.deactivateProductById(id);
    }
}
