package jp.co.example.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.co.example.common.AppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "aseet")
public class Asset extends AppBaseEntity implements Serializable {

    private Long sectionId;


    @NotNull
    @Size(min = 1, max = 50)
    private String assetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true, name = "sectionId",referencedColumnName = "id", insertable = false, updatable = false)
    private Section section;

}
