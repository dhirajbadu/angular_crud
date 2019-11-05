package com.user.us.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtls {

    private static final int heightMain = 100;
    private static final int widthMain = 100;

    private static final BufferedImage cropImageSquare(byte[] image) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == heightMain && width == widthMain) {
            return originalImage;
        }

        /*// Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2), // x coordinate of the upper-left corner
                yc - (squareSize / 2), // y coordinate of the upper-left corner
                squareSize,            // widht
                squareSize             // height
        );*/

        int squareSize = (height > width ? width : height);
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2), // x coordinate of the upper-left corner
                yc - (squareSize / 2), // y coordinate of the upper-left corner
                widthMain,            // widht
                heightMain             // height
        );

        return croppedImage;
    }

    public static void upload(MultipartFile uploadfile, String filename, String filepath) throws IOException {
        // Crop the image (uploadfile is an object of type MultipartFile)
        BufferedImage croppedImage = cropImageSquare(uploadfile.getBytes());


// Get the file extension
        String ext = FilenameUtils.getExtension(filename);

// Save the file locally
        File outputfile = new File(filepath);
        ImageIO.write(croppedImage, ext, outputfile);
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(widthMain, heightMain, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, widthMain, heightMain, null);
        g.dispose();

        return resizedImage;
    }

}
