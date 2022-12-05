package com.org.ecom.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResource {
    private String fileName;
    private String message;

    public FileResource(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }
}
