package com.zwallet.zwalletapi.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String storeFile(MultipartFile file, Integer id);

    Resource loadFileAsResource(String filename);
}