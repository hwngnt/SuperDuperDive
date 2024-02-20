package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int addFile(MultipartFile multipartFile, User user) throws IOException {
        InputStream fis = multipartFile.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = fis.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] fileData = buffer.toByteArray();

        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());
        Integer userId = user.getUserid();
        File file = new File();
        file.setFilename(fileName);
        file.setContenttype(contentType);
        file.setFiledata(fileData);
        file.setFilesize(fileSize);
        file.setUserid(userId);
        return fileMapper.insert(file);
    }

    public List<String> getFiles(User user){
        return fileMapper.getFiles(user.getUserid());
    }

    public File getFile(String filename) {
        return fileMapper.getFileByName(filename);
    }

    public void deleteFile(String filename) {
        fileMapper.deleteFile(fileMapper.getFileByName(filename).getFilename());
    }
}
