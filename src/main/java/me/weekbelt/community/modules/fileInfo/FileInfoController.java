package me.weekbelt.community.modules.fileInfo;

import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class FileInfoController {

    @Value("${property.image.url}")
    private String CLASS_PATH;

    private final FileInfoService fileInfoService;
    private final FileInfoRepository fileInfoRepository;

    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(MultipartFile file) {
        try {
            FileInfo fileInfo = fileInfoService.saveImage(file);
            return ResponseEntity.ok().body("/image/" + fileInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<?> serveImages(@PathVariable Long imageId) {
        try {
            FileInfo findImage = fileInfoRepository.findById(imageId).orElseThrow(
                    () -> new IllegalArgumentException("해당하는 이미지를 찾지 못했습니다. imageId=" + imageId));

            FileSystemResource fileSystemResource = new FileSystemResource(CLASS_PATH + findImage.getSaveFileName());
            return ResponseEntity.ok().body(fileSystemResource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
