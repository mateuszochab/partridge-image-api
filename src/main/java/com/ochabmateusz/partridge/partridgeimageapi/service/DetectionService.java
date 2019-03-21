package com.ochabmateusz.partridge.partridgeimageapi.service;


import com.drew.imaging.ImageProcessingException;
import com.ochabmateusz.partridge.partridgeimageapi.entity.Tags;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

@Component
public interface DetectionService {


    BufferedInputStream multiPartFileToBIS(MultipartFile multipartFile) throws IOException;

    List<Tags> getListOfTagsMetadata(BufferedInputStream bis) throws ImageProcessingException, IOException;


    BufferedImage faceDetection(MultipartFile multipartFile) throws IOException;

   BufferedImage eyesDetection(MultipartFile multipartFile) throws IOException;


    BufferedImage smileDetection(MultipartFile multipartFile) throws IOException;

    BufferedImage upperbodyDetection(MultipartFile multipartFile) throws IOException;

    BufferedImage fullbodyDetection(MultipartFile imageMultipart) throws IOException;

    String textFromImage(MultipartFile imageMultipart) throws IOException, TesseractException;



    String textFromBufferedImage(BufferedImage bufferedImage, String country) throws IOException, TesseractException;
}
