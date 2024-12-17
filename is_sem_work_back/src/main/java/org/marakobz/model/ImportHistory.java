package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DreamUser user;

    private LocalDateTime startTime;

    @Column(columnDefinition="text", length = 500)
    private String status;
    @Column(columnDefinition="text", length = 500)
    private String message;
    @Column(columnDefinition="text", length = 500)
    private String fileUrl;
    private String fileName;

    private int addedObjects;

    public String getCreatorUsername() {
        return user != null ? user.getUsername() : null;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }

}
