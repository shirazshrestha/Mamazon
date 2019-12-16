package com.project.onlinestore.utils.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public void writeFile(String path, String text) {
        try(FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(text);
        } catch (IOException e) {
            // exception handling
            e.printStackTrace();
        }
    }

    @Override
    public String readFile(String path) {
        StringBuilder ret = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line = bufferedReader.readLine();
            while(line != null) {
                ret.append(line);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            // exception handling
        } catch (IOException e) {
            // exception handling
        }
        return ret.toString();
    }

    @Override
    public void transferImage(String path, MultipartFile image,String extension) {
        try {
            image.transferTo(
                    new File(path+"."+extension));
        } catch (Exception e) {
            System.out.println(e.getCause());
            throw new RuntimeException("Image saving failed");
        }
    }
}
