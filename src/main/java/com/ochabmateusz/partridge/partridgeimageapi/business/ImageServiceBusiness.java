package com.ochabmateusz.partridge.partridgeimageapi.business;

import com.ochabmateusz.partridge.partridgeimageapi.service.ImageService;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

@Service
public class ImageServiceBusiness implements ImageService {


    @Override
    public Mat grayScale(Mat image) {


        Mat imgGray = new Mat();

        Imgproc.cvtColor(image, imgGray, Imgproc.COLOR_BGR2GRAY);

        return imgGray;
    }

    @Override
    public Mat gaussianBlur(Mat image, List<Integer> values) {


        Mat gaussianBlur = new Mat();
        Imgproc.GaussianBlur(image, gaussianBlur, new Size(values.get(0), values.get(1)), values.get(2));

        return gaussianBlur;

    }
    @Override
    public Mat gaussianBlur(Mat image) {


        Mat gaussianBlur = new Mat();
        Imgproc.GaussianBlur(image, gaussianBlur, new Size(5,5), 0);

        return gaussianBlur;

    }

    @Override
    public Mat scaleUPImage(Mat image, int width, int height) {

        Mat resizedimage = new Mat();
        Size scaleSize = new Size(width,height);
        resize(image, resizedimage, scaleSize , 0, 0, INTER_CUBIC);
    return resizedimage;
    }

    @Override
    public Mat adaptiveThreshold(Mat image) {

        Mat imgAdaptiveThreshold = new Mat();
        Imgproc.adaptiveThreshold(image, imgAdaptiveThreshold, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 51, 4);
        return imgAdaptiveThreshold;
    }

    @Override
    public Mat sobelFilter(Mat image) {
        Mat sobel = new Mat();
        Imgproc.Sobel(image,sobel, CvType.CV_8S,1,0);
        return sobel;
    }
}
