package com.ochabmateusz.partridge.partridgeimageapi.business;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.ochabmateusz.partridge.partridgeimageapi.entity.Tags;
import com.ochabmateusz.partridge.partridgeimageapi.service.ConversionService;
import com.ochabmateusz.partridge.partridgeimageapi.service.DetectionService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetectionServiceBusiness implements DetectionService {


    private static final String OUTPUTFILE_FACES = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageFaceDetected";
    private static final String OUTPUTFILE_EYES = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageEyesDetected";
    private static final String OUTPUTFILE_SMILE = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageSmileDetected";
    private static final String OUTPUTFILE_UPPERBODY = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageUpperBodyDetected";
    private static final String OUTPUTFILE_FULLBODY = "B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES\\imageFullbodyDetected";
    private static final String CASCADE_FACE_CLASSIFIER = "A:\\OpenCV\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml";
    private static final String CASCADE_EYES_CLASSIFIER = "A:\\OpenCV\\opencv\\build\\etc\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml";
    private static final String CASCADE_SMILE_CLASSIFIER = "A:\\OpenCV\\opencv\\build\\etc\\haarcascades\\haarcascade_smile.xml";
    private static final String CASCADE_UPPERBODY_CLASSIFIER = "A:\\OpenCV\\opencv\\build\\etc\\haarcascades\\haarcascade_upperbody.xml";
    private static final String CASCADE_FULLBODY_CLASSIFIER = "A:\\OpenCV\\opencv\\build\\etc\\haarcascades\\haarcascade_fullbody.xml";

    private CascadeClassifier cascadeClassifier;


    private ConversionService conversionService;

    @Autowired
    public DetectionServiceBusiness(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BufferedInputStream multiPartFileToBIS(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        return bis;
    }

    @Override
    public List<Tags> getListOfTagsMetadata(BufferedInputStream bis) throws ImageProcessingException, IOException {

        List<Tags> listOfTags = new ArrayList<>();

        Metadata metadata = ImageMetadataReader.readMetadata(bis);

        metadata.getDirectories().forEach((g) -> {
            g.getTags().forEach((t) -> listOfTags.add(new Tags(g.getName(), t.getTagName(), t.getDescription())));
        });


        return listOfTags;
    }

    @Override
    public BufferedImage faceDetection(MultipartFile multipartFile) throws IOException {


        this.cascadeClassifier = new CascadeClassifier(CASCADE_FACE_CLASSIFIER);

        return makeDetection(multipartFile, OUTPUTFILE_FACES);
    }


    //TODO detects more points than eyes- have to optimize algorithm
    @Override
    public BufferedImage eyesDetection(MultipartFile multipartFile) throws IOException {
        this.cascadeClassifier = new CascadeClassifier(CASCADE_EYES_CLASSIFIER);


        return makeDetection(multipartFile, OUTPUTFILE_EYES);
    }

    //TODO have to train my own classifier-this one aint work
    @Override
    public BufferedImage smileDetection(MultipartFile multipartFile) throws IOException {
        this.cascadeClassifier = new CascadeClassifier(CASCADE_SMILE_CLASSIFIER);


        return makeDetection(multipartFile, OUTPUTFILE_SMILE);
    }

    @Override
    public BufferedImage upperbodyDetection(MultipartFile multipartFile) throws IOException {
        this.cascadeClassifier = new CascadeClassifier(CASCADE_UPPERBODY_CLASSIFIER);


        return makeDetection(multipartFile, OUTPUTFILE_UPPERBODY);
    }

    //TODO classifier aint work- not recognizing anything- have to train my own
    @Override
    public BufferedImage fullbodyDetection(MultipartFile multipartFile) throws IOException {
        this.cascadeClassifier = new CascadeClassifier(CASCADE_FULLBODY_CLASSIFIER);


        return makeDetection(multipartFile, OUTPUTFILE_FULLBODY);
    }

    @Override
    public String textFromImage(MultipartFile imageMultipart) throws IOException, TesseractException {
        BufferedImage bufferedImage = this.conversionService.multipartToBufferedImage(imageMultipart);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES");
        tesseract.setLanguage("eng");
        String text = tesseract.doOCR(bufferedImage);

        return this.conversionService.getOnlyStrings(text);
    }

    @Override
    public String textFromBufferedImage(BufferedImage bufferedImage,String country) throws IOException, TesseractException {


        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("B:\\INTELLIJ_WORKSPACE\\PARTRIDGE\\TEMPFILES");
        tesseract.setLanguage(country);
        String text = tesseract.doOCR(bufferedImage);

        return this.conversionService.getOnlyStrings(text);
    }

    private BufferedImage makeDetection(MultipartFile multipartFile, String outputFile) throws IOException {

        BufferedImage image = this.conversionService.multipartToBufferedImage(multipartFile);

        Mat imageMat = this.conversionService.bufferedImageToMat(image);

        //detect faces
        MatOfRect smileDetections = new MatOfRect();
        cascadeClassifier.detectMultiScale(imageMat, smileDetections);

        //draw rectangles of detected eyes
        imageMat = drawRectangles(imageMat, smileDetections);

        BufferedImage bufferedImage = this.conversionService.convertMatToBufferedImage(imageMat);

        this.conversionService.saveBufferedImageOnDisk(bufferedImage, outputFile);

        return bufferedImage;


    }

    //private method
    private Mat drawRectangles(Mat imageMat, MatOfRect faceDetections) {

        Mat imageMatDrawRect = imageMat;

        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(imageMatDrawRect, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(100, 100, 25), 3);
        }

        return imageMatDrawRect;
    }


}
