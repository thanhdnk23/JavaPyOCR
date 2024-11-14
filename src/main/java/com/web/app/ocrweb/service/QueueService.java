package com.web.app.ocrweb.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.web.app.ocrweb.payload.CitizenIdCard;
import com.web.app.ocrweb.payload.ReturnObject;
import com.web.app.ocrweb.payload.request.Base64ImageRequest;
import com.web.app.ocrweb.payload.request.FileImageRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QueueService {
    private final BlockingQueue<Base64ImageRequest> base64Queue = new LinkedBlockingQueue<>();
    private final BlockingQueue<FileImageRequest> fileQueue = new LinkedBlockingQueue<>();
    private final Map<Base64ImageRequest, CompletableFuture<ReturnObject<CitizenIdCard>>> base64FutureMap = new ConcurrentHashMap<>();
    private final Map<FileImageRequest, CompletableFuture<ReturnObject<CitizenIdCard>>> fileFutureMap = new ConcurrentHashMap<>();
    @Async("taskExecutor") 
    public CompletableFuture<ReturnObject<CitizenIdCard>> addToQueue(Base64ImageRequest request) {
        CompletableFuture<ReturnObject<CitizenIdCard>> futureResult = new CompletableFuture<>();
        base64Queue.offer(request);
        base64FutureMap.put(request, futureResult); // Lưu future của Base64 request
        return futureResult;
    }
    @Async("taskExecutor") 
    public CompletableFuture<ReturnObject<CitizenIdCard>> addToQueue(FileImageRequest request) {
        CompletableFuture<ReturnObject<CitizenIdCard>> futureResult = new CompletableFuture<>();
        fileQueue.offer(request);
        fileFutureMap.put(request, futureResult); // Lưu future của File request
        return futureResult;
    }

    public Base64ImageRequest getNextBase64Request() {
        return base64Queue.poll();
    }

    public FileImageRequest getNextFileRequest() {
        return fileQueue.poll();
    }

    public CompletableFuture<ReturnObject<CitizenIdCard>> getBase64Future(Base64ImageRequest request) {
        return base64FutureMap.remove(request); // Lấy và xóa future của Base64 request
    }

    public CompletableFuture<ReturnObject<CitizenIdCard>> getFileFuture(FileImageRequest request) {
        return fileFutureMap.remove(request); // Lấy và xóa future của File request
    }
}
