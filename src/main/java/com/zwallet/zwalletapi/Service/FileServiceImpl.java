package com.zwallet.zwalletapi.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.zwallet.zwalletapi.Model.Entity.FileEntity;
import com.zwallet.zwalletapi.Property.FileStorageProperties;
import com.zwallet.zwalletapi.Repository.FileRepository;
import com.zwallet.zwalletapi.Utils.FileException.FileStorageException;
import com.zwallet.zwalletapi.Utils.FileException.MyFileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    private Path fileStorageLocation;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    public void FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            // TODO: handle exception
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        // TODO Auto-generated method stub
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (filename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileEntity fileEntity = new FileEntity(filename, file.getContentType());
            fileRepository.save(fileEntity);

            return filename;
        } catch (Exception e) {
            // TODO: handle exception
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
        }
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        // TODO Auto-generated method stub
        try {
            Path filepPath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filepPath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + filename);
            }
        } catch (MalformedURLException e) {
            // TODO: handle exception
            throw new MyFileNotFoundException("File not found", e);
        }
    }

}
