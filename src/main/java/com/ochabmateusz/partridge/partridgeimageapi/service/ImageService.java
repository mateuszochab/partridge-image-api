package com.ochabmateusz.partridge.partridgeimageapi.service;

import org.opencv.core.Mat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ImageService {


    Mat grayScale(Mat image);


    Mat gaussianBlur(Mat image);



    Mat scaleUPImage(Mat image, int width, int height);

    Mat adaptiveThreshold(Mat image);
    Mat sobelFilter(Mat image);

    Mat gaussianBlur(Mat image, List<Integer> values);
}
