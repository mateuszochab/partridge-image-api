package com.ochabmateusz.partridge.partridgeimageapi.service;

import org.opencv.core.Mat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public interface ConversionService {
     String getOnlyStrings(String s);

    BufferedImage multipartToBufferedImage(MultipartFile multipartFile) throws IOException;

    Mat bufferedImageToMat(BufferedImage bi);

    BufferedImage convertMatToBufferedImage(Mat image);



    void saveBufferedImageOnDisk(BufferedImage bufferedImage, String basePath) throws IOException;

    void saveMatImageOnDisk(Mat image, String basePath);

}
