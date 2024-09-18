package com.poc.controller;

import com.poc.entity.Base64Request;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private static final String DIRECTORY_PATH = "D:\\test";

    @PostMapping(value = "/decode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> decodePdf(@RequestBody Map<String, String> requestBody) {
        try {
            // Retrieve the Base64 string from the JSON payload
            String base64Pdf = requestBody.get("base64Pdf");
            if (base64Pdf == null || base64Pdf.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Missing 'base64Pdf' field in request body.");
            }

            // Clean the Base64 string by removing whitespace and newline characters
            base64Pdf = base64Pdf.replaceAll("\\s+", "");

            // Decode the Base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(base64Pdf);

            // Ensure the directory exists
            File directory = new File(DIRECTORY_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique file name
            String fileName = "decoded_" + System.currentTimeMillis() + ".pdf";
            File outputFile = new File(directory, fileName);

            // Write the decoded bytes to the file
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(decodedBytes);
            }

            // Return a success response with the file path
            String successMessage = "File saved successfully at " + outputFile.getAbsolutePath();
            return ResponseEntity.ok(successMessage);

        } catch (IllegalArgumentException e) {
            // Handle invalid Base64 input
            return ResponseEntity.badRequest().body("Invalid Base64 input: " + e.getMessage());
        } catch (IOException e) {
            // Handle file I/O errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving file: " + e.getMessage());
        }
    }


    @PostMapping("/encode")
    public String encodePdf(@RequestParam("file") MultipartFile file) throws IOException {
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new IllegalArgumentException("File must be a PDF");
        }
        InputStream inputStream = file.getInputStream();
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStream base64OutputStream = encoder.wrap(outputStream)) {
            inputStream.transferTo(base64OutputStream);
            encodedString = outputStream.toString(StandardCharsets.UTF_8);
        }
        return encodedString;
    }
}
//
//    @PostMapping("/decode")
//    public ResponseEntity<byte[]> decodePdfw(@RequestBody String base64Pdf) throws IOException {
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decodedBytes;
//
//        try (InputStream base64InputStream = new ByteArrayInputStream(base64Pdf.getBytes(StandardCharsets.UTF_8));
//             InputStream decoderInputStream = decoder.wrap(base64InputStream);
//             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            decoderInputStream.transferTo(outputStream);
//            decodedBytes = outputStream.toByteArray();
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "decoded.pdf");
//        headers.setContentLength(decodedBytes.length);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(decodedBytes);
//    }
//}
//
//    @PostMapping("/decodePdfLocal")
//    public ResponseEntity<String> decodePdfLocal(@RequestBody String base64Pdf) throws IOException {
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decodedBytes;
//
//        try (InputStream base64InputStream = new ByteArrayInputStream(base64Pdf.getBytes(StandardCharsets.UTF_8));
//             InputStream decoderInputStream = decoder.wrap(base64InputStream);
//             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            decoderInputStream.transferTo(outputStream);
//            decodedBytes = outputStream.toByteArray();
//        }
//
//        // Get directory path from application properties or use default
//        String directoryPath = System.getProperty("file.save.directory", "D:\\test");
//        // Generate a unique file name
//        String fileName = "decoded_" + System.currentTimeMillis() + ".pdf";
//
//        // Ensure the directory exists
//        File directory = new File(directoryPath);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        // Write the decoded bytes to a file
//        File outputFile = new File(directory, fileName);
//        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
//            fileOutputStream.write(decodedBytes);
//        }
//
//        // Return a success response with the file path
//        return ResponseEntity.ok("File saved successfully at " + outputFile.getAbsolutePath());
//    }


