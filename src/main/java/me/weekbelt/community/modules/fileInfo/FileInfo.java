package me.weekbelt.community.modules.fileInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.weekbelt.community.modules.board.Board;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @NoArgsConstructor @AllArgsConstructor
@Entity
public class FileInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String saveFileName;

    private String contentType;

    private boolean deleteFlag;

    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    @Builder
    public FileInfo(String fileName, String saveFileName, String contentType, boolean deleteFlag, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.fileName = fileName;
        this.saveFileName = saveFileName;
        this.contentType = contentType;
        this.deleteFlag = deleteFlag;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }
}
