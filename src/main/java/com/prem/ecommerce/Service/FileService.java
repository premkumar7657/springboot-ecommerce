package com.prem.ecommerce.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String uploadImage(String path, MultipartFile image) throws IOException;


}
