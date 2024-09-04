package com.umc.ttg.domain.coupon.utils;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class BitMatrixToMultipartFileConverter {
    public static MultipartFile convertToMultipartFile(BitMatrix bitMatrix) throws IOException {
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return convertBufferedImageToMultipartFile(bufferedImage);
    }

    private static MultipartFile convertBufferedImageToMultipartFile(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", out);
        } catch (IOException e) {
            log.error("IO Error", e);
        }
        byte[] bytes = out.toByteArray();
        return new CustomMultipartFile(bytes, "image", "image.jpeg", "jpeg", bytes.length);
    }

}
