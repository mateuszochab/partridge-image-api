package com.ochabmateusz.partridge.partridgeimageapi.controller;

import com.ochabmateusz.partridge.partridgeimageapi.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class DetectionController {


    private final DetectionService detectionService;

    @Autowired
    public DetectionController(DetectionService detectionService) {
        this.detectionService = detectionService;
    }

    @GetMapping(value = "/detectFace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void detectFace(@RequestParam("image") MultipartFile imageMultipart) throws IOException {


        this.detectionService.faceDetection(imageMultipart);
    }



    @GetMapping(value = "/detectEyes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void detectEyes(@RequestParam("image") MultipartFile imageMultipart) throws IOException{


        this.detectionService.eyesDetection(imageMultipart);
    }
    @GetMapping(value = "/detectSmile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void detectSmile(@RequestParam("image") MultipartFile imageMultipart) throws IOException{


        this.detectionService.smileDetection(imageMultipart);
    }


    @GetMapping(value = "/detectUpperbody", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void detectUpperbody(@RequestParam("image") MultipartFile imageMultipart) throws IOException{


        this.detectionService.upperbodyDetection(imageMultipart);
    }

    @GetMapping(value = "/detectFullbody", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void detectFullbody(@RequestParam("image") MultipartFile imageMultipart) throws IOException{


        this.detectionService.fullbodyDetection(imageMultipart);
    }

}
