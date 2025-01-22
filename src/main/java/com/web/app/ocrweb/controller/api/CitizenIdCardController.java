package com.web.app.ocrweb.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.app.ocrweb.payload.ReturnObject;
import com.web.app.ocrweb.payload.CitizenIdCard;
import com.web.app.ocrweb.payload.request.Base64ImageRequest;
import com.web.app.ocrweb.payload.request.FileImageRequest;
import com.web.app.ocrweb.service.QueueService;

import java.util.concurrent.CompletableFuture;

/**
 * API controller to handle requests related to Citizen ID card scanning and OCR
 * processing.
 */
@RestController
@RequestMapping("/api/v1")
public class CitizenIdCardController {

        @Autowired
        private QueueService queueService;

        /**
         * Scan Citizen ID Card from an image file.
         *
         * @param request The request containing the image file.
         * @return A JSON response with the parsed Citizen ID card details.
         */
        @Operation(summary = "Scan Citizen ID Card from an image file", description = "Scans a Citizen ID card from an uploaded image file and returns the parsed details.")
        // @ApiResponses(value = {
        // @ApiResponse(responseCode = "200", description = "Successfully scanned
        // Citizen ID card",
        // content = @Content(mediaType = "application/json")),
        // @ApiResponse(responseCode = "500", description = "Error occurred while
        // scanning the Citizen ID card",
        // content = @Content(mediaType = "application/json"))
        // })

        @PostMapping(value = "/citizen-id-card/scan-from-file", consumes = "multipart/form-data")
        public CompletableFuture<ResponseEntity<ReturnObject<CitizenIdCard>>> scanCitizenIdFromFile(
                        @RequestParam("fileImage") MultipartFile fileImage,
                        @RequestParam("cardType") int cardType) {
                FileImageRequest request = new FileImageRequest(fileImage, cardType);
                return queueService.addToQueue(request)
                                .thenApply(result -> ResponseEntity.ok(result))
                                .exceptionally(e -> {
                                        ReturnObject<CitizenIdCard> errorResponse = new ReturnObject<>(
                                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                        "Error scanning Citizen ID: " + e.getMessage(),
                                                        false,
                                                        null);
                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body(errorResponse);
                                });
        }

        /**
         * Scan Citizen ID Card from a Base64 encoded image string.
         *
         * @param request The request containing the Base64 encoded image string.
         * @return A JSON response with the parsed Citizen ID card details.
         */
        @Operation(summary = "Scan Citizen ID Card from Base64 image string", description = "Scans a Citizen ID card from a Base64 encoded image string and returns the parsed details.")
        // @ApiResponses(value = {
        // @ApiResponse(responseCode = "200", description = "Successfully scanned
        // Citizen ID card",
        // content = @Content(mediaType = "application/json")),
        // @ApiResponse(responseCode = "500", description = "Error occurred while
        // scanning the Citizen ID card",
        // content = @Content(mediaType = "application/json"))
        // })
        @PostMapping("/citizen-id-card/scan-from-base64")
        public CompletableFuture<ResponseEntity<ReturnObject<CitizenIdCard>>> scanCitizenIdFromBase64(
                        @RequestBody Base64ImageRequest request) {

                return queueService.addToQueue(request)
                                .thenApply(result -> ResponseEntity.ok(result))
                                .exceptionally(e -> {
                                        ReturnObject<CitizenIdCard> errorResponse = new ReturnObject<>(
                                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                        "Error scanning Citizen ID: " + e.getMessage(),
                                                        false,
                                                        null);
                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body(errorResponse);
                                });
        }

        /**
         * Scan Citizen ID Card from an image file using OCR (Optical Character
         * Recognition).
         *
         * @param request The request containing the image file.
         * @return A JSON response with the parsed Citizen ID card details using OCR.
         */
        @Operation(summary = "Scan Citizen ID Card using OCR from image file", description = "Uses Optical Character Recognition (OCR) to scan a Citizen ID card from an uploaded image file and return the parsed details.")
        // @ApiResponses(value = {
        // @ApiResponse(responseCode = "200", description = "Successfully scanned
        // Citizen ID card using OCR",
        // content = @Content(mediaType = "application/json")),
        // @ApiResponse(responseCode = "500", description = "Error occurred while
        // scanning the Citizen ID card using OCR",
        // content = @Content(mediaType = "application/json"))
        // })
        @PostMapping("/citizen-id-card/ocr-scan-from-file")
        public CompletableFuture<ResponseEntity<ReturnObject<CitizenIdCard>>> scanCitizenIdUsingOCRFromFile(
                        @RequestBody FileImageRequest request) {

                return queueService.addToQueue(request)
                                .thenApply(result -> ResponseEntity.ok(result))
                                .exceptionally(e -> {
                                        ReturnObject<CitizenIdCard> errorResponse = new ReturnObject<>(
                                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                        "Error scanning Citizen ID using OCR: " + e.getMessage(),
                                                        false,
                                                        null);
                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body(errorResponse);
                                });
        }

        /**
         * Scan Citizen ID Card from a Base64 encoded image string using OCR.
         *
         * @param request The request containing the Base64 encoded image string.
         * @return A JSON response with the parsed Citizen ID card details using OCR.
         */
        @Operation(summary = "Scan Citizen ID Card using OCR from Base64 image string", description = "Uses Optical Character Recognition (OCR) to scan a Citizen ID card from a Base64 encoded image string and return the parsed details.")
        // @ApiResponses(value = {
        // @ApiResponse(responseCode = "200", description = "Successfully scanned
        // Citizen ID card using OCR",
        // content = @Content(mediaType = "application/json")),
        // @ApiResponse(responseCode = "500", description = "Error occurred while
        // scanning the Citizen ID card using OCR",
        // content = @Content(mediaType = "application/json"))
        // })
        @PostMapping("/citizen-id-card/ocr-scan-from-base64")
        public CompletableFuture<ResponseEntity<ReturnObject<CitizenIdCard>>> scanCitizenIdUsingOCRFromBase64(
                        @RequestBody Base64ImageRequest request) {

                return queueService.addToQueue(request)
                                .thenApply(result -> ResponseEntity.ok(result))
                                .exceptionally(e -> {
                                        ReturnObject<CitizenIdCard> errorResponse = new ReturnObject<>(
                                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                        "Error scanning Citizen ID using OCR: " + e.getMessage(),
                                                        false,
                                                        null);
                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body(errorResponse);
                                });
        }
        @GetMapping("Test")
        public ReturnObject returnText() {
            ReturnObject returnObj = new ReturnObject(); 
            returnObj.setData("ok");
            // Thiết lập giá trị cho returnObj nếu cần
            return returnObj;  // Spring sẽ tự động chuyển đổi đối tượng ReturnObject thành JSON
        }
}
