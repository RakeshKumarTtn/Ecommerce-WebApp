package com.org.ecom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.ecom.dto.product.ProductDto;
import com.org.ecom.dto.product.ProductVariationDto;
import com.org.ecom.dto.product.ViewProductDto;
import com.org.ecom.dto.product.ViewProductDtoCustomer;
import com.org.ecom.entity.category.Category;
import com.org.ecom.entity.category.CategoryMetadataField;
import com.org.ecom.entity.product.Product;
import com.org.ecom.entity.product.ProductVariation;
import com.org.ecom.entity.registration.Seller;
import com.org.ecom.exception.NotFoundException;
import com.org.ecom.exception.NullException;
import com.org.ecom.repository.ProductRepository;
import com.org.ecom.repository.ProductVariationRepository;
import com.org.ecom.repository.category.CategoryMetadataFieldRepo;
import com.org.ecom.repository.category.CategoryMetadataFieldValuesRepo;
import com.org.ecom.repository.category.CategoryRepository;
import com.org.ecom.repository.registration.SellerRepository;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.security.CustomizedAuditorAware;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CategoryMetadataFieldValuesRepo categoryMetadataFieldValuesRepository;

    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private Long[] l = {};

    @Autowired
    CustomizedAuditorAware customizedAuditorAware;

    //PRODUCT API'S FOR SELLER

    /*
        Method for adding a product
        Firstly check product if added only in the leaf node
    */
    public ResponseEntity<String> addProduct(Product product, Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent() && categoryRepository.checkIfLeaf(categoryId) == 0) {

            if (product.getName().equals(categoryRepository.findById(categoryId).get().getName()) || product.getName().equals(product.getBrand())) {
                throw new NullException(messageSource.getMessage("message86.txt", l, LocaleContextHolder.getLocale()));
            }
            Product product1 = new Product();
            product1 = modelMapper.map(product, Product.class);
            String sellerUsername = userService.getLoggedInSeller().getUsername();
            Seller seller = sellerRepository.findByUsername(sellerUsername).get();

            Set<Product> productSet = new HashSet<>();
            product1.setBrand(product.getBrand());
            product1.setActive(false);
            product1.setIsCancellable(product.isCancellable());
            product1.setDescription(product.getDescription());
            product1.setIsReturnable(product.getIsReturnable());
            product1.setName(product.getName());
            product1.setSeller(seller);
            product1.setCreatedBy(seller.getFirstName() + " " + seller.getLastName());
            product1.setCreatedDate(LocalDateTime.now());
            product1.setLastModifiedBy(seller.getFirstName() + " " + seller.getLastName());
            product1.setLastModifiedDate(LocalDateTime.now());
            customizedAuditorAware.setName(seller.getFirstName());
            product1.setCategory(categoryRepository.findById(categoryId).get());
            productSet.add(product1);
            productRepository.save(product1);
        } else {
            try {
                throw new NotFoundException(messageSource.getMessage("message87.txt", l, LocaleContextHolder.getLocale()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(messageSource.getMessage("message88.txt", l, LocaleContextHolder.getLocale()), HttpStatus.OK);
    }

    /*
        Method for adding the Product Variation
    */
    public void addProductVariation(ProductVariation productVariation, Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product product1 = product.get();
            if (product1.isActive()) {
                Map<String, String> stringMap = new HashMap<>();

                String username = userService.getLoggedInSeller().getUsername();
                Seller seller1 = sellerRepository.findByUsername(username).get();

                Map<String, Object> map = productVariation.getInfoAttributes();

                if ((product1.getSeller().getUsername()).equals(seller1.getUsername())) {
                    productVariation.setProduct(product1);
                    Long categoryId = productRepository.getCategoryId(product1.getID());
                    List<Long> metadataIds = categoryMetadataFieldValuesRepository.getMetadataId(categoryId);

                    for (long l : metadataIds) {
                        String metadata = categoryMetadataFieldRepository.getNameOfMetadata(l);
                        String metadataValues = categoryMetadataFieldValuesRepository.getFieldValuesForCompositeKey(categoryId, l);
                        stringMap.put(metadata, metadataValues);
                    }
                    int count = 0;
                    for (String key : map.keySet()) {
                        if (stringMap.containsKey(key)) {
                            String str = stringMap.get(key);
                            Object obj = map.get(key);
                            String[] strings = str.split(",");
                            List<String> list = Arrays.asList(strings);
                            if (list.contains(obj)) {
                                count++;
                            }
                        }
                    }

                    if (count == map.size()) {
                        String info = null;
                        try {
                            info = objectMapper.writeValueAsString(productVariation.getInfoAttributes());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        productVariation.setInfoJson(info);
                        productVariation.setActive(true);
                        productVariationRepository.save(productVariation);
                    } else {
                        try {
                            throw new NotFoundException("Field values are not provided correctly");
                        } catch (NotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else {
                    throw new NullException("you can't add any product variation to this product");
                }

            } else {
                throw new NullException("product is not active");
            }
        } else {
            try {
                throw new NotFoundException("The product name provided is wrong");
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /*
        Method for getting the product details
    */
    public ProductDto getProduct(Long productId) {

        String username = userService.getLoggedInSeller().getUsername();
        Seller seller = sellerRepository.findByUsername(username).get();

        Optional<Product> product = productRepository.findById(productId);
        Long[] l = {};
        if (product.isPresent()) {
            if ((product.get().getSeller().getEmail()).equals(seller.getEmail())) {
                ProductDto viewProductDTO = new ProductDto();
                viewProductDTO.setBrand(product.get().getBrand());
                viewProductDTO.setActive(product.get().isActive());
                viewProductDTO.setIsCancellable(product.get().isCancellable());
                viewProductDTO.setDescription(product.get().getDescription());
                viewProductDTO.setName(product.get().getName());

                Optional<Category> category = categoryRepository.findById(productRepository.getCategoryId(productId));
                viewProductDTO.setCategoryName(category.get().getName());
                viewProductDTO.setCategoryId(category.get().getId());

                List<String> fields = new ArrayList<>();
                List<String> values = new ArrayList<>();
                List<Long> longList1 = categoryMetadataFieldValuesRepository.getMetadataId(category.get().getId());
                for (Long l1 : longList1) {
                    Optional<CategoryMetadataField> categoryMetadataField = categoryMetadataFieldRepository.findById(l1);
                    fields.add(categoryMetadataField.get().getName());
                    values.add(categoryMetadataFieldValuesRepository.getFieldValuesForCompositeKey(category.get().getId(), l1));
                }
                System.out.println(fields);
                viewProductDTO.setMetadataFieldName(fields);
                viewProductDTO.setMetadataFieldValues(values);
                return viewProductDTO;
            } else {
                try {
                    throw new NotFoundException(messageSource.getMessage("message110.txt", l, LocaleContextHolder.getLocale()));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                throw new NotFoundException(messageSource.getMessage("notfound.txt", l, LocaleContextHolder.getLocale()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
        Method for Deleting the Product
    */
    public ResponseEntity<String> deleteProductById(Long id) {
        String username = userService.getLoggedInSeller().getUsername();
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            return ResponseEntity.ok(messageSource.getMessage("message89.txt", l, LocaleContextHolder.getLocale()));
        }
        Product product = productOptional.get();
        if (!product.getSeller().getUsername().equalsIgnoreCase(username)) {
            return ResponseEntity.ok(messageSource.getMessage("message111.txt", l, LocaleContextHolder.getLocale()));
        }
        if (product.isDeleted()) {
            return ResponseEntity.ok(messageSource.getMessage("message112.txt", l, LocaleContextHolder.getLocale()));
        }
        product.setDeleted(true);
        customizedAuditorAware.setName(userService.getLoggedInSeller().getLastName());
        productRepository.deleteProduct(product.getID());
        return ResponseEntity.ok(product.getName() + " " + messageSource.getMessage("message113.txt", l, LocaleContextHolder.getLocale()));
    }

    /*
        Method for updating the product
    */
    public ResponseEntity<String> updateProductByProductId(Long id, Product product) {

        Seller seller = userService.getLoggedInSeller();
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product1 = productOptional.get();
            if ((product1.getSeller().getUsername()).equals(seller.getUsername())) {
                if (product.getName().equals(product1.getBrand()) || product.getName().equals(product1.getCategory().getName()) || product.getName().equals(seller.getFirstName()) || product.getName().equals(product.getBrand())) {
                    throw new NullException(messageSource.getMessage("message114.txt", l, LocaleContextHolder.getLocale()));
                }
                if (product.getName() != null) {
                    product1.setName(product.getName());
                }
                if (product.getBrand() != null) {
                    product1.setBrand(product.getBrand());
                }
                if (product.isCancellable() != null) {
                    product1.setIsCancellable(product.isCancellable());
                }
                if (product.getIsReturnable() != null) {
                    product1.setIsReturnable(product.getIsReturnable());
                }
                if (product.getDescription() != null) {
                    product1.setDescription(product.getDescription());
                }
                if (product.isActive() != null) {
                    product1.setActive(product.isActive());
                }
                customizedAuditorAware.setName(seller.getFirstName());
                productRepository.save(product1);
            } else {
                throw new NullException(messageSource.getMessage("message20.txt", l, LocaleContextHolder.getLocale()));
            }
        } else {
            try {
                throw new NotFoundException(messageSource.getMessage("notfound.txt", l, LocaleContextHolder.getLocale()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok(messageSource.getMessage("message115.txt", l, LocaleContextHolder.getLocale()));
    }

    /*
        Method for getting all the product
    */
    public List<ViewProductDto> getProductDetails(Integer pageNo, Integer pageSize, String sortBy) {
        PageRequest paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.asc(sortBy)));
        String username = userService.getLoggedInSeller().getUsername();

        Seller seller = sellerRepository.findByUsername(username).get();

        List<Long> longList = productRepository.getProductIdOfSeller(seller.getUserId(), paging);
        List<ViewProductDto> list = new ArrayList<>();
        for (Long l : longList) {
            list.add(viewSingleProductForSeller(productRepository.findById(l).get().getID()));
        }
        return list;
    }

    /*
        Method for viewing a Single product for seller
    */
    public ViewProductDto viewSingleProductForSeller(Long productId) {
        String username = userService.getLoggedInSeller().getUsername();
        Seller seller1 = sellerRepository.findByUsername(username).get();

        Optional<Product> product = productRepository.findById(productId);
        Long[] l = {};
        if (product.isPresent()) {
            if ((product.get().getSeller().getEmail()).equals(seller1.getEmail())) {
                ViewProductDto viewProductDTO = new ViewProductDto();
                viewProductDTO.setId(product.get().getID());
                viewProductDTO.setBrand(product.get().getBrand());
                viewProductDTO.setActive(product.get().isActive());
                viewProductDTO.setIsCancellable(product.get().getIsCancellable());
                viewProductDTO.setDescription(product.get().getDescription());
                viewProductDTO.setProductName(product.get().getName());
                viewProductDTO.setCreatedBy(product.get().getCreatedBy());
                viewProductDTO.setCreatedDate(product.get().getCreatedDate());
                viewProductDTO.setLastModifiedBy(product.get().getLastModifiedBy());
                viewProductDTO.setLastModifiedDate(product.get().getLastModifiedDate());
                Optional<Category> category = categoryRepository.findById(productRepository.getCategoryId(productId));
                viewProductDTO.setProductName(category.get().getName());

                return viewProductDTO;
            } else {
                try {
                    throw new NotFoundException(messageSource.getMessage("Category not found", l, LocaleContextHolder.getLocale()));
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                throw new NotFoundException(messageSource.getMessage("product not found", l, LocaleContextHolder.getLocale()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //PRODUCT API'S FOR ADMIN

    /*
        Method for viewing the Product
    */
    public ViewProductDtoCustomer viewSingleProductForAdmin(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            ViewProductDtoCustomer viewProductDTO = new ViewProductDtoCustomer();
            viewProductDTO.setBrand(product.get().getBrand());
            viewProductDTO.setActive(product.get().isActive());
            viewProductDTO.setIsCancellable(product.get().isCancellable());
            viewProductDTO.setDescription(product.get().getDescription());
            viewProductDTO.setProductName(product.get().getName());

            Optional<Category> category = categoryRepository.findById(productRepository.getCategoryId(productId));
            viewProductDTO.setName(category.get().getName());

            List<String> fields = new ArrayList<>();
            List<String> values = new ArrayList<>();
            List<Long> longList1 = categoryMetadataFieldValuesRepository.getMetadataId(category.get().getId());
            for (Long l1 : longList1) {
                Optional<CategoryMetadataField> categoryMetadataField = categoryMetadataFieldRepository.findById(l1);
                fields.add(categoryMetadataField.get().getName());
                values.add(categoryMetadataFieldValuesRepository.getFieldValuesForCompositeKey(category.get().getId(), l1));
            }
            viewProductDTO.setFieldName(fields);
            viewProductDTO.setValues(values);
            List<String> list = new ArrayList<>();
            Set<ProductVariation> productVariations = product.get().getProductVariations();
            String firstPath = System.getProperty("user.dir");
            String fileBasePath = firstPath + "Ecommerce-WebApp-master/Ecommerce-Website/src/main/resources/productVariation/";
            for (ProductVariation productVariation : productVariations) {
                File dir = new File(fileBasePath);
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    for (File file1 : files) {
                        String value = productVariation.getId() + "_0";
                        if (file1.getName().startsWith(value)) {
                            list.add("http://localhost:8080/viewProductVariationImage/" + file1.getName());
                        }
                    }
                }
            }
            viewProductDTO.setLinks(list);
            return viewProductDTO;
        } else {
            Long[] l = {};
            try {
                throw new NotFoundException(messageSource.getMessage("notfound.txt", l, LocaleContextHolder.getLocale()));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
        Method for getting all product details along with the category
    */
    public List<ViewProductDtoCustomer> getProductDetailsForAdmin(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.asc(sortBy)));
        List<Long> longList = productRepository.getAllId(paging);
        System.out.println(longList);
        List<ViewProductDtoCustomer> list = new ArrayList<>();
        for (Long l : longList) {
            System.out.println(productRepository.findById(l).get().getName());
            list.add(viewSingleProductForAdmin(productRepository.findById(l).get().getID()));
        }
        return list;
    }

    /*
        Method for activate a product
    */
    public ResponseEntity<String> activateProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            return ResponseEntity.ok(messageSource.getMessage("message89.txt", l, LocaleContextHolder.getLocale()));
        }
        Product product1 = product.get();
        if (product1.isActive()) {
            return ResponseEntity.ok(messageSource.getMessage("message90.txt", l, LocaleContextHolder.getLocale()));
        }
        productRepository.activateProduct(product1.getID());
        String email = product1.getSeller().getEmail();

        emailSenderService.sendEmail(email, messageSource.getMessage("message91.txt", l, LocaleContextHolder.getLocale()),
                "Product with Details " + "name: " + product1.getName() + "\nbrand: " + product1.getBrand() + " is Activated Successfully");
        return ResponseEntity.ok(messageSource.getMessage("message92.txt", l, LocaleContextHolder.getLocale()));
    }

    /*
          Method for de activate a product
    */
    public ResponseEntity<String> deactivateProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            return ResponseEntity.ok(messageSource.getMessage("message89.txt", l, LocaleContextHolder.getLocale()));
        }
        Product product1 = product.get();
        if (!product1.isActive()) {
            return ResponseEntity.ok(messageSource.getMessage("message93.txt", l, LocaleContextHolder.getLocale()));
        }
        productRepository.deActivateProduct(product1.getID());
        String email = product1.getSeller().getEmail();

        emailSenderService.sendEmail(email, messageSource.getMessage("message95.txt", l, LocaleContextHolder.getLocale()),
                "Product with Details " + "name: " + product1.getName() + "\nbrand: " + product1.getBrand() + " is De Activated Successfully");

        return ResponseEntity.ok(messageSource.getMessage("message94.txt", l, LocaleContextHolder.getLocale()));
    }
}
