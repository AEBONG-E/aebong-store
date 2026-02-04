package com.aebong.store.domain.entity.product;

import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.domain.entity.AuditingEntity;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
@Entity
public class ImageEntity extends AuditingEntity {

    @Comment("이미지순번 순번 PK")
    @Column(name = "image_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("상품순번")
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @Comment("이미지파일 관리 이름")
    @Column(name = "admin_image_file_name", nullable = false, length = 1000)
    private String adminImageFileName;

    @Comment("이미지파일 원본 이름")
    @Column(name = "original_image_file_name", nullable = false, length = 1000)
    private String originalImageFileName;

    @Comment("이미지파일 이름")
    @Column(name = "image_file_name", nullable = false, length = 1000)
    private String imageFileName;

    @Comment("이미지파일 url")
    @Lob @Column(name = "image_file_url", nullable = false)
    private String imageFileUrl;

    @Comment("이미지유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false, length = 20)
    private ImageType imageType;

    @Comment("컨텐츠유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, length = 20)
    private ContentType contentType;

    @Comment("너비")
    @Column(name = "width", nullable = false)
    private Integer width;

    @Comment("높이")
    @Column(name = "height", nullable = false)
    private Integer height;

    @Comment("파일크기")
    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private ImageEntity(ProductEntity product,
                        String adminImageFileName,
                        String originalImageFileName,
                        String imageFileName,
                        String imageFileUrl,
                        ImageType imageType,
                        ContentType contentType,
                        Integer width,
                        Integer height,
                        Integer fileSize) {
        this.product = product;
        this.adminImageFileName = adminImageFileName;
        this.originalImageFileName = originalImageFileName;
        this.imageFileName = imageFileName;
        this.imageFileUrl = imageFileUrl;
        this.imageType = imageType;
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
    }

    public static List<ImageEntity> create(ProductEntity product, ProductRegisterInfo registerInfo) {
        if (Objects.isNull(product) || Objects.isNull(registerInfo)) return Collections.emptyList();

        return registerInfo.getImageList().stream()
                .map(image -> ImageEntity.builder()
                        .product(product)
                        .adminImageFileName(image.getAdminImageFileName())
                        .originalImageFileName(image.getOriginalImageFileName())
                        .imageFileName(image.getImageFileName())
                        .imageFileUrl(image.getImageFileUrl())
                        .imageType(image.getImageType())
                        .contentType(image.getContentType())
                        .width(image.getWidth())
                        .height(image.getHeight())
                        .fileSize(image.getFileSize())
                        .build())
                .toList();
    }

}
