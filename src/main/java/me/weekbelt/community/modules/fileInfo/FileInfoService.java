package me.weekbelt.community.modules.fileInfo;

import lombok.RequiredArgsConstructor;
import me.weekbelt.community.infra.fileUtils.ImageFile;
import me.weekbelt.community.modules.board.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FileInfoService {

    @Value("${property.image.url}")
    private String PATH;
    private static final String BOARD_IMAGE = "board_img/";

    private final FileInfoRepository fileInfoRepository;

    public FileInfo saveImage(MultipartFile image) {

        String fileName = ImageFile.makeImageFileName(image.getOriginalFilename());
        String saveFileName = BOARD_IMAGE + fileName;

        // 로컬에 이미지 저장
        ImageFile.saveImageFileToLocal(image, PATH + saveFileName);

        // DB에 이미지 저장
        FileInfo saveFileImage = FileInfo.builder()
                .deleteFlag(false)
                .contentType(image.getContentType())
                .fileName(image.getOriginalFilename())
                .saveFileName(saveFileName)
                .createdDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .build();

        return fileInfoRepository.save(saveFileImage);
    }


    public List<FileInfo> findImagesByBoardId(Long boardId) {
        return fileInfoRepository.findAllByBoardId(boardId);
    }

}
