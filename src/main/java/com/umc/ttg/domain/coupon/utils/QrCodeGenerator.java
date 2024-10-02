package com.umc.ttg.domain.coupon.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umc.ttg.domain.store.entity.StoreEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;



public class QrCodeGenerator {

    public static MultipartFile generateQrCode(StoreEntity storeEntity) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(
                "ID:  " + storeEntity.getId() + "\n " +
                        "CouponName: " + storeEntity.getName() + "\n ", BarcodeFormat.QR_CODE, 400, 400);

        return BitMatrixToMultipartFileConverter.convertToMultipartFile(bitMatrix);

     }

}
