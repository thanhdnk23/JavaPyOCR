package com.web.app.ocrweb.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.web.app.ocrweb.payload.CitizenIdCard;
import com.web.app.ocrweb.payload.ReturnObject;
import com.web.app.ocrweb.payload.request.Base64ImageRequest;
import com.web.app.ocrweb.payload.request.FileImageRequest;
import com.web.app.ocrweb.plugins.QrCodeProcessor_v1;
import com.web.app.ocrweb.service.QueueService;
import java.util.concurrent.CompletableFuture;

@Component
public class QrCodeWorker {

    @Autowired
    private QueueService queueService;

    @Scheduled(fixedDelay = 50) // Chạy worker 50 ms giây
    public void processQueue() {
       // System.out.println("Worker is running...");
        processBase64Request();
        processFileRequest();
    }

    private void processBase64Request() {
        Base64ImageRequest base64Request = queueService.getNextBase64Request();
        if (base64Request != null) {
            CompletableFuture<ReturnObject<CitizenIdCard>> futureResult = queueService.getBase64Future(base64Request);
            try {
                CitizenIdCard citizenIdCard = processBase64Image(base64Request);
                String  mess="No Data";
                boolean status=false;
                if(citizenIdCard!=null){
                    mess="Citizen ID scanned successfully from Base64";
                    status=true;
                }
                ReturnObject<CitizenIdCard> result = new ReturnObject<>(
                        HttpStatus.OK.value(),
                        mess,
                        status,
                        citizenIdCard);
                futureResult.complete(result);
            } catch (Exception e) {
                futureResult.completeExceptionally(e);
            }
        }
    }

    private void processFileRequest() {
        //System.out.println("processFileRequest");
        FileImageRequest fileRequest = queueService.getNextFileRequest();
        if (fileRequest != null) {
            CompletableFuture<ReturnObject<CitizenIdCard>> futureResult = queueService.getFileFuture(fileRequest);
            try {
                CitizenIdCard citizenIdCard = processFileImage(fileRequest);
                String  mess="No Data";
                boolean status=false;
                if(citizenIdCard!=null){
                    mess="Citizen ID scanned successfully from File";
                    status=true;
                }
                ReturnObject<CitizenIdCard> result = new ReturnObject<>(
                        HttpStatus.OK.value(),
                        mess,
                        status,
                        citizenIdCard);
                futureResult.complete(result);
                
            } catch (Exception e) {
                futureResult.completeExceptionally(e);
            }
        }
    }

    // Xử lý ảnh base64
    private CitizenIdCard processBase64Image(Base64ImageRequest base64Image) throws Exception {

        return new QrCodeProcessor_v1().detectQrCodeBase64(base64Image);
    }

    // Xử lý ảnh từ file
    private CitizenIdCard processFileImage(FileImageRequest fileImage) throws Exception {
        return new QrCodeProcessor_v1().detectQrCodeFileImage(fileImage);
    }
}
