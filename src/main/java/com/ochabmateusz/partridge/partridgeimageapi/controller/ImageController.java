package com.ochabmateusz.partridge.partridgeimageapi.controller;


import com.drew.imaging.ImageProcessingException;
import com.ochabmateusz.partridge.partridgeimageapi.service.ConversionService;
import com.ochabmateusz.partridge.partridgeimageapi.service.DetectionService;
import com.ochabmateusz.partridge.partridgeimageapi.service.ImageService;
import com.ochabmateusz.partridge.partridgeimageapi.service.TextService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class ImageController {

    private static final String OUTPUTFILE_GRAYSCALE = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageGrayscale";
    private static final String OUTPUTFILE_GAUSSIANBLUR = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageGaussianBlur";
    private static final String OUTPUTFILE_ADAPTIVE_THR = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageAdaptiveThr";
    private static final String OUTPUTFILE_SOBEL ="B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageSobelFilter" ;
    private final DetectionService detectionService;
    private final ImageService imageService;
    private final TextService textService;
    private final ConversionService conversionService;

    @Autowired
    public ImageController(DetectionService detectionService, ImageService imageService, TextService textService, ConversionService conversionService) {
        this.detectionService = detectionService;
        this.imageService = imageService;
        this.textService = textService;
        this.conversionService = conversionService;
    }





    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(@RequestParam("image") MultipartFile imageMultipart) throws IOException, ImageProcessingException {

        BufferedInputStream bis = this.detectionService.multiPartFileToBIS(imageMultipart);

       this.detectionService.getListOfTagsMetadata(bis);


    }

    @GetMapping(value = "/textFromImage/{country}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String readTextFromImage(@RequestParam("image") MultipartFile imageMultipart, @PathVariable("country") String country) throws IOException, TesseractException {

        Mat image=this.conversionService.bufferedImageToMat(this.conversionService.multipartToBufferedImage(imageMultipart));
        Mat scaledUP=this.imageService.scaleUPImage(image,2000,2000);
        Mat grayscale=this.imageService.grayScale(scaledUP);
        this.conversionService.saveMatImageOnDisk(grayscale,OUTPUTFILE_GRAYSCALE);

        Mat gaussianBlur=this.imageService.gaussianBlur(grayscale);
        this.conversionService.saveMatImageOnDisk(gaussianBlur,OUTPUTFILE_GAUSSIANBLUR);
        Mat imageadaptiveThreshold=this.imageService.adaptiveThreshold(gaussianBlur);

        //Mat imageSobel=this.imageService.sobelFilter(imageadaptiveThreshold);
        this.conversionService.saveMatImageOnDisk(imageadaptiveThreshold,OUTPUTFILE_ADAPTIVE_THR);
        BufferedImage bufferedImage=this.conversionService.convertMatToBufferedImage(grayscale);

        return this.detectionService.textFromBufferedImage(bufferedImage,country);
    }
    @GetMapping(value = "/imageGray", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void grayscale(@RequestParam("image") MultipartFile imageMultipart) throws IOException {


        Mat image=this.conversionService.bufferedImageToMat(this.conversionService.multipartToBufferedImage(imageMultipart));

        Mat grayscale=this.imageService.grayScale(image);

        this.conversionService.saveMatImageOnDisk(grayscale,OUTPUTFILE_GRAYSCALE);
    }
    @GetMapping(value = "/imageGaussianBlur", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void gaussianBlur(@RequestParam("image") MultipartFile imageMultipart, @RequestParam("value")List<Integer> value) throws IOException {



        Mat image=this.conversionService.bufferedImageToMat(this.conversionService.multipartToBufferedImage(imageMultipart));

        Mat gaussianBlur=this.imageService.gaussianBlur(image,value);

        this.conversionService.saveMatImageOnDisk(gaussianBlur,OUTPUTFILE_GAUSSIANBLUR);
    }
    @GetMapping(value = "/imageAdaptiveThr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void adaptiveThreshold(@RequestParam("image") MultipartFile imageMultipart) throws IOException {



        Mat image=this.conversionService.bufferedImageToMat(this.conversionService.multipartToBufferedImage(imageMultipart));

        //first wee need to change  grayscale because adaptivethreshold needs 1 channel image and before there were 3 channels
        Mat grayscale=this.imageService.grayScale(image);

        Mat imageadaptiveThreshold=this.imageService.adaptiveThreshold(grayscale);

        this.conversionService.saveMatImageOnDisk(imageadaptiveThreshold,OUTPUTFILE_ADAPTIVE_THR);
    }
    @GetMapping(value = "/sobelFilter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sobelFilter(@RequestParam("image") MultipartFile imageMultipart) throws IOException {



        Mat image=this.conversionService.bufferedImageToMat(this.conversionService.multipartToBufferedImage(imageMultipart));

        //first wee need to change  grayscale because adaptivethreshold needs 1 channel image and before there were 3 channels
        Mat grayscale=this.imageService.grayScale(image);

        Mat imageSobel=this.imageService.sobelFilter(grayscale);

        this.conversionService.saveMatImageOnDisk(imageSobel,OUTPUTFILE_SOBEL);
    }
}
