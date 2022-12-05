package com.org.ecom.service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.org.ecom.dto.login.AddressDto;
import com.org.ecom.dto.login.SellerDto;
import com.org.ecom.entity.registration.Address;
import com.org.ecom.entity.registration.Role;
import com.org.ecom.entity.registration.Seller;
import com.org.ecom.entity.security.ActivationToken;
import com.org.ecom.exception.UserNotFoundException;
import com.org.ecom.repository.registration.AddressRepository;
import com.org.ecom.repository.registration.RoleRepository;
import com.org.ecom.repository.registration.SellerRepository;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.security.CustomizedAuditorAware;
import com.org.ecom.utility.CustomJwtUtility;
import com.org.ecom.utility.ObjectConverter;
import com.org.ecom.validation.RegisterValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import static com.org.ecom.enums.RoleLevel.SELLER;

@Service
@Transactional
public class SellerService {

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    private CustomizedAuditorAware customizedAuditorAware;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private RegisterValidations registerValidations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ActivationTokenService activationTokenService;

    @Autowired
    private CustomJwtUtility customJwtUtility;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    MessageSource messageSource;

    @Value("${project.image}")
    public String path;

    @Autowired
    FileService fileService;

    public void registerSeller(SellerDto sellerDto) {

        if (registerValidations.passwordAndConfirmPasswordMatchSeller(sellerDto)) {
            customizedAuditorAware.setName(sellerDto.getFirstName());

            Seller seller = objectConverter.dtoToEntity(sellerDto);

            for (AddressDto addressDto : sellerDto.getAddressDtoSet()) {
                Address address = objectConverter.dtoToEntity(addressDto);
                seller.addAddress(address);
            }

            Optional<Role> byAuthority = roleRepository.findByAuthority(String.valueOf(SELLER));

            byAuthority.ifPresent(seller::addRole);

            seller.setIsExpired(false);
            seller.setPassword(passwordEncoder.encode(seller.getPassword()));


            userRepository.save(seller);

            String resetToken = jwtUtility.generateResetToken(seller);

            ActivationToken activationToken = new ActivationToken();
            activationToken.setToken(resetToken);
            activationToken.setUser(seller);
            activationTokenService.saveData(activationToken);

            emailSenderService.sendEmail(seller.getEmail(),
                    messageSource.getMessage("message53.txt", null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("message41.txt", null, LocaleContextHolder.getLocale()) + "\n" + resetToken);
        }
    }

    public MappingJacksonValue sellerData(String token) {
        String username = customJwtUtility.extractUsername(token);
        Seller seller = sellerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username"));

        Set<String> fileList = fileService.listFilesUsingDirectoryStream();
        seller.setImageFilePath(fileList.contains(username.concat(".png")) ? path + File.separator + username + ".png" : null);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(seller);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId",
                "firstName", "lastName", "isActive", "companyName", "companyContact", "gstin", "userAddress", "imageFilePath");

        FilterProvider filters = new SimpleFilterProvider().addFilter("SellersFilter", filter);

        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    public Optional<Seller> findByUserId(Long id) {
        return sellerRepository.findById(id);
    }

    public ResponseEntity<String> updateSellerProfile(Long id, Seller seller) {

        Seller savedSeller = sellerRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(messageSource.getMessage("message46.txt", null, LocaleContextHolder.getLocale())));

        if (seller.getFirstName() != null)
            savedSeller.setFirstName(seller.getFirstName());

        if (seller.getCompanyContact() != null)
            savedSeller.setCompanyContact(seller.getCompanyContact());

        if (seller.getLastName() != null)
            savedSeller.setLastName(seller.getLastName());

        if (seller.getGstin() != null)
            savedSeller.setGstin(seller.getGstin());

        if (seller.getCompanyName() != null)
            savedSeller.setCompanyName(seller.getCompanyName());

        customizedAuditorAware.setName(savedSeller.getFirstName());
        sellerRepository.save(savedSeller);
        return ResponseEntity.ok(messageSource.getMessage("message42.txt", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<String> updatePassword(Long id, String password, String confirmPassword) {

        Optional<Seller> sellerById = sellerRepository.findById(id);

        if (sellerById.isPresent()) {
            if (!password.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})"))
                return ResponseEntity.ok(messageSource.getMessage("message54.txt", null, LocaleContextHolder.getLocale()));
            if (!confirmPassword.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})"))
                return ResponseEntity.ok(messageSource.getMessage("message55.txt", null, LocaleContextHolder.getLocale()));

            Seller seller = sellerById.get();
            String oldPassword = seller.getPassword();
            String newPassword = passwordEncoder.encode(password);
            if (password.equals(confirmPassword)) {
                if (!oldPassword.equals(newPassword)) {
                    seller.setPassword(newPassword);
                    customizedAuditorAware.setName(seller.getFirstName());
                    sellerRepository.save(seller);

                    emailSenderService.sendEmail(seller.getEmail(), messageSource.getMessage("message43.txt", null, LocaleContextHolder.getLocale()), messageSource.getMessage("message43.txt", null, LocaleContextHolder.getLocale()));
                    return ResponseEntity.ok(messageSource.getMessage("message43.txt", null, LocaleContextHolder.getLocale()));
                } else
                    return ResponseEntity.ok(messageSource.getMessage("message44.txt", null, LocaleContextHolder.getLocale()));
            } else
                return ResponseEntity.ok(messageSource.getMessage("message45.txt", null, LocaleContextHolder.getLocale()));
        } else
            throw new UserNotFoundException(messageSource.getMessage("message46.txt", null, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<String> updateAddress(Long id, Integer address_id, Address address) {
        Optional<Address> byId = addressRepository.findByAddressId(address_id);
        Seller seller = sellerRepository.findById(id).get();
        Optional<Address> addressId = seller
                .getUserAddress()
                .stream()
                .filter(e -> e.getAddressId().equals(address_id))
                .findFirst();
        Integer dbAddressId = null;
        if (addressId.isPresent()) {
            dbAddressId = addressId.get().getAddressId();
        }

        if (byId.isPresent() && dbAddressId == address_id) {
            Address address1 = byId.get();
            if (address.getCityName() != null)
                address1.setCityName(address.getCityName());
            if (address.getAddressLine() != null)
                address1.setAddressLine(address.getAddressLine());
            if (address.getStateName() != null)
                address1.setStateName(address.getStateName());
            if (address.getCountryName() != null)
                address1.setCountryName(address.getCountryName());
            if (address.getZipCode() != null)
                address1.setZipCode(address.getZipCode());
            if (address.getLabel() != null)
                address1.setLabel(address.getLabel());

            addressRepository.save(address1);
            return ResponseEntity.ok(messageSource.getMessage("message33.txt", null, LocaleContextHolder.getLocale()));
        } else
            return ResponseEntity.ok(messageSource.getMessage("message34.txt", null, LocaleContextHolder.getLocale()));
    }
}
