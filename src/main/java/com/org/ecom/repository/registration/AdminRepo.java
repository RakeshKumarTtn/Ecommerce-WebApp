package com.org.ecom.repository.registration;

import com.org.ecom.entity.registration.Admin;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdminRepo extends PagingAndSortingRepository<Admin,Long> {

}
