package uz.lb.fxchatserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)

public class Attachment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String fileName;

    String hashId;

    String contentType;

    String extension;

    String uploadPath;

    String link;

    Float fileSize;

    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
    @JoinColumn(name = "user_id")
    private User user;


    @JsonIgnore
    @CreationTimestamp
    Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    Date updatedAt;

}
