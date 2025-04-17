package com.aebong.store.domain.entity.product;

import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag")
@Entity
public class TagEntity extends AuditingEntity {

    @Comment("태그순번 PK")
    @Column(name = "tag_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("태그명")
    @Column(name = "tag_name", nullable = false, length = 50)
    private String tagName;

    @Comment("버전")
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @OneToMany(mappedBy = "tag")
    private List<ProductTagEntity> productTags = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity that = (TagEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private TagEntity(String tagName, Integer version, List<ProductTagEntity> productTags) {
        this.tagName = tagName;
        this.version = version;
        this.productTags = productTags;
    }

}
