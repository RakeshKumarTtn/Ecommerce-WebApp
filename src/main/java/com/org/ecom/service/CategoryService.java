package com.org.ecom.service;

import com.org.ecom.dto.category.CategoryDto;
import com.org.ecom.entity.category.Category;
import com.org.ecom.entity.category.CategoryMetadataField;
import com.org.ecom.entity.category.CategoryMetadataFieldValues;
import com.org.ecom.repository.category.CategoryMetadataFieldRepo;
import com.org.ecom.repository.category.CategoryMetadataFieldValuesRepo;
import com.org.ecom.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMetadataFieldRepo categoryMetadataFieldRepository;

    @Autowired
    private CategoryMetadataFieldValuesRepo categoryMetadataFieldValuesRepository;

    @Autowired
    MessageSource messageSource;

    private final Long[] l = {};

    /*
        Method for add a new Category
        In this method we will firstly check it is the root category or the child category
        of an existing category.
        If it is root category then the parentID of that category will be null.
        If it is the child category of an existing category then its parentID will be the id
        of Existing parent Category.
        Also check the name of parent category and child category cannot be same.
        If category is already present then return otherwise add that category
    */
    public ResponseEntity<?> addCategory(String categoryName, Long parentCategoryId) {
        if (parentCategoryId != null) {
            if (categoryRepository.existsById(parentCategoryId)) {
                Category parent = categoryRepository.findById(parentCategoryId).get();
                if (parent.getName().equals(categoryName)) {
                    return new ResponseEntity<>(messageSource.getMessage("message96.txt", l, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
                } else {
                    Optional<Category> category2 = categoryRepository.findByCategoryName(categoryName);
                    if (category2.isPresent()) {
                        return new ResponseEntity<>(messageSource.getMessage("message97.txt", l, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
                    } else {
                        Category category = new Category();
                        category.setName(categoryName);
                        category.setCategory(parent);
                        categoryRepository.save(category);

                        return new ResponseEntity<>(String.format(messageSource.getMessage("message98.txt", l, LocaleContextHolder.getLocale()) + parent.getName()), HttpStatus.CREATED);
                    }
                }
            } else {
                return new ResponseEntity<>(String.format(messageSource.getMessage("message99.txt", l, LocaleContextHolder.getLocale()) + parentCategoryId), HttpStatus.NOT_FOUND);
            }
        } else {
            Optional<Category> category2 = categoryRepository.findByCategoryName(categoryName);
            if (category2.isPresent()) {
                return new ResponseEntity<>(messageSource.getMessage("message97.txt", l, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
            } else {
                Category category = new Category();
                category.setName(categoryName);
                category = categoryRepository.save(category);
                return new ResponseEntity<>(String.format(messageSource.getMessage("message100.txt", l, LocaleContextHolder.getLocale()) + category.getId()), HttpStatus.CREATED);
            }
        }
    }

    /*
        Method for view a category according to the category ID
        Firstly we check if the category is present or not if not present then
        return the failure message otherwise return that category information
    */
    public ResponseEntity<?> viewCategory(Long categoryId) {
        Optional<Category> currCategory = categoryRepository.findById(categoryId);

        if (!currCategory.isPresent()) {
            return new ResponseEntity<>(messageSource.getMessage("message101.txt", l, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
        }
        CategoryDto category = new CategoryDto();
        category.setCurrentCategory(currCategory.get());

        List<Category> childCategories = categoryRepository.findChildCategories(categoryId);
        category.setChildCategories(childCategories);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /*
        Method for adding metadata field of a category
        like Shirt having color,size
    */
    public ResponseEntity<?> addMetadataField(String fieldName) {
        Optional<CategoryMetadataField> categoryMetadataField = categoryMetadataFieldRepository.findByCategoryMetadataFieldName(fieldName);
        if (categoryMetadataField.isPresent()) {
            return new ResponseEntity<>(messageSource.getMessage("message102.txt", l, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
        } else {
            CategoryMetadataField field = new CategoryMetadataField();
            field.setName(fieldName);
            field = categoryMetadataFieldRepository.save(field);
            return new ResponseEntity<>(String.format(messageSource.getMessage("message103.txt", l, LocaleContextHolder.getLocale()) + field.getId()), HttpStatus.CREATED);
        }
    }

    /*
        Method for view metadata field of a category like a category shirt having metadata like size, color
        then it will return the list of them
    */
    public ResponseEntity<?> viewMetadataField() {
        Iterable<CategoryMetadataField> metadataFields = categoryMetadataFieldRepository.findAll();
        return new ResponseEntity<>(metadataFields, HttpStatus.OK);
    }

    /*
        Method for viewing all the category present in the database
        it will consist all information from parent category to the child category
    */
    public ResponseEntity<List<Category>> viewAllCategory(Integer pageNo, Integer pageSize,
                                                          String sortBy, String orderBy) {

        PageRequest pageable;

        if (orderBy.equals("category_id") && !sortBy.equals("category_id")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        } else if (!orderBy.equals("category_id") && sortBy.equals("category_id")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.Order.asc(orderBy).getDirection(), orderBy);
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.Order.asc(orderBy).getDirection(), orderBy);
        }

        List<Category> categoriesList = categoryRepository.findAll(pageable).toList();
        return ResponseEntity.ok(categoriesList);

    }


    /*
        Method for updating the Category
        firstly we check the category is present or not if present then update the category
        otherwise return the failure message
    */
    public ResponseEntity<?> updateCategory(Long categoryId, String categoryName) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.findById(categoryId).get();
            Optional<Category> categoryDuplicate = categoryRepository.findByCategoryName(categoryName);
            if (!categoryDuplicate.isPresent()) {
                category.setName(categoryName);
                categoryRepository.save(category);
                return new ResponseEntity<>(messageSource.getMessage("message104.txt", l, LocaleContextHolder.getLocale()) + category.getName(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(messageSource.getMessage("message97.txt", l, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(messageSource.getMessage("message104.txt", l, LocaleContextHolder.getLocale()) + categoryName, HttpStatus.NOT_FOUND);
        }
    }

    /*
        Method for adding the values of metadata field
        A metadata field like Color then we add its values like RED,WHITE,PINK,BLACK
    */
    public ResponseEntity<?> addCategoryMetadataFieldValues(Long categoryId, Long metadataFieldId, Set<String> values) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.findById(categoryId).get();

            if (category.getCategory() == null) {
                return new ResponseEntity<>(messageSource.getMessage("message105.txt", l, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
            } else {
                if (categoryMetadataFieldRepository.existsById(metadataFieldId)) {

                    CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findById(metadataFieldId).get();
                    CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();

                    categoryMetadataFieldValues.setCategory(category);
                    categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);

                    categoryMetadataFieldValues.setValue(values.toString());

                    categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);

                    return new ResponseEntity<>(messageSource.getMessage("message106.txt", l, LocaleContextHolder.getLocale()) + categoryMetadataField.getName(), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(messageSource.getMessage("message107.txt", l, LocaleContextHolder.getLocale()) + metadataFieldId, HttpStatus.NOT_FOUND);
                }
            }
        } else {
            return new ResponseEntity<>(messageSource.getMessage("message108.txt", l, LocaleContextHolder.getLocale()) + categoryId, HttpStatus.NOT_FOUND);
        }
    }

    /*
        Method for updating the metadata field values like changing the
        color deleting the color value or adding the color value
    */
    public ResponseEntity<?> updateCategoryMetadataFieldValues(Long categoryId, Long metadataFieldId, Set<String> valueList) {

        if (!categoryRepository.existsById(categoryId)) {
            return new ResponseEntity<>(messageSource.getMessage("message108.txt", l, LocaleContextHolder.getLocale()) + categoryId, HttpStatus.NOT_FOUND);
        }

        if (!categoryMetadataFieldRepository.existsById(metadataFieldId)) {
            return new ResponseEntity<>(messageSource.getMessage("message107.txt", l, LocaleContextHolder.getLocale()) + metadataFieldId, HttpStatus.NOT_FOUND);
        }

        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository
                .findById(metadataFieldId).get();

        Optional<CategoryMetadataFieldValues> categoryMetadataFieldValues = categoryMetadataFieldValuesRepository
                .findByCategoryMetadataFieldIdAndCategoryId(metadataFieldId, categoryId);
        if (categoryMetadataFieldValues.isPresent()) {
            categoryMetadataFieldValues.get().setValue(valueList.toString());
            categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues.get());
            return new ResponseEntity<>(messageSource.getMessage("message109.txt", l, LocaleContextHolder.getLocale()) + categoryMetadataField.getName(), HttpStatus.CREATED);
        } else {
            return addCategoryMetadataFieldValues(categoryId, metadataFieldId, valueList);
        }
    }
}