package com.ochabmateusz.partridge.partridgeimageapi.business;

import com.ochabmateusz.partridge.partridgeimageapi.service.ConversionService;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class ConversionServiceBusiness implements ConversionService {


    @Override
    public String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z 0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    @Override
    public BufferedImage multipartToBufferedImage(MultipartFile multipartFile) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes());
        BufferedImage image = ImageIO.read(inputStream);

        return image;

    }

    @Override
    public Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
    @Override
    public BufferedImage convertMatToBufferedImage(Mat image) {

        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (image.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = image.channels() * image.cols() * image.rows();
        byte[] bytes = new byte[bufferSize];
        image.get(0, 0, bytes);
        BufferedImage imageBuffer = new BufferedImage(image.cols(), image.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) imageBuffer.getRaster().getDataBuffer()).getData();
        System.arraycopy(bytes, 0, targetPixels, 0, bytes.length);
        return imageBuffer;


    }

    @Override
    public void saveBufferedImageOnDisk(BufferedImage bufferedImage, String basePath) throws IOException {


        File outputfile = new File(buildImageName(basePath));
        ImageIO.write(bufferedImage, "jpg", outputfile);

    }
    @Override
    public void saveMatImageOnDisk(Mat image, String basePath){

        Imgcodecs.imwrite(buildImageName(basePath),image);


    }

    public String buildImageName(String base) {

        Random generator = new Random();
        int i = generator.nextInt(1000000000);
        String outputname = base + i + ".jpg";
        return outputname;

    }
}
