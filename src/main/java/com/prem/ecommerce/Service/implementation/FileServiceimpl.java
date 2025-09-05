package com.prem.ecommerce.Service.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prem.ecommerce.Service.FileService;

@Service
public class FileServiceimpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        
        // file names of current file or original file
        String originalName = image.getOriginalFilename();

        // generate a unique file name to avoid the overridden using UID
        String randomId = UUID.randomUUID().toString();

        //originalfilename animal.png --> 1dksjdhskd1213k313j.png
        String newFileName = randomId.concat(originalName.substring(originalName.lastIndexOf('.')));

        //creating original path
        String fileAbsPath = path + File.separator + newFileName;  // File.seperator = "/"

        //Check if the path is exist or create a new folder

        File folder = new File(path);
        if(!folder.exists())
        folder.mkdir();

        //upload to the server 
        Files.copy(image.getInputStream(),Paths.get(fileAbsPath));

        //returning file name
        return newFileName;
        
    }

}
