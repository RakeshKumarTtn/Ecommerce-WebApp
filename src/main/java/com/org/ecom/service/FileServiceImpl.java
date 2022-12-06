package com.org.ecom.service;

import com.org.ecom.entity.Image;
import com.org.ecom.repository.ImageRepository;
import com.org.ecom.repository.registration.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageSource messageSource;

    /*
        Method for Uploading the Image of User
    */
    @Override
    public String uploadImage(String userName, String path, MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        String fileName = userName.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

        Image image = new Image();
        image.setImageName(originalFilename);
        image.setRemarks(messageSource.getMessage("message70.txt", null, LocaleContextHolder.getLocale()));
        imageRepository.save(image);

        String filePath = path + File.separator + fileName;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        Files.copy(multipartFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return originalFilename;
    }

    /*
        Method for getting the image of user
    */
    @Override
    public InputStream getUserImage(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }

    /*
        Method for finding all the files Present in the image folder
    */
    public Set<String> listFilesUsingDirectoryStream() {
        String dir = "/home/rakesh/Documents/EcomWeb/Ecommerce-WebApp-master/Ecommerce-Website/src/main/resources/images/";
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileSet;
    }
}
