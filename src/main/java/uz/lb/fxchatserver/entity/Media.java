//package uz.lb.fxchatserver.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import uz.lb.fxchatserver.enums.GeneralStatus;
//
//import java.util.Date;
//import java.util.List;
//
//@Data
//@Builder
//@Entity(name = "Media")
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
//public class Media {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    Long id;
//
//    @Column(unique = true, nullable = false, name = "content")
//    String content;
//
//    @ManyToMany
//    @JoinColumn(name = "user_id", nullable = false)
//    private List<User> user;
//
//    @Column(name = "status")
//    @JsonIgnore
//    @Enumerated(EnumType.STRING)
//    @Builder.Default
//    GeneralStatus status = GeneralStatus.ACTIVE;
//
//    @JsonIgnore
//    @Column(name = "visible")
//    @Builder.Default
//    Boolean visible = Boolean.TRUE;
//
//    @JsonIgnore
//    @CreationTimestamp
//    Date createdAt;
//
//    @JsonIgnore
//    @UpdateTimestamp
//    Date updatedAt;
//}
