package jp.co.example.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.co.example.common.AppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "section")
public class Section extends AppBaseEntity implements Serializable {

    @NotNull
    @Size(min = 1, max = 50)
    private String sectionName;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="section", targetEntity=EmployeeSection.class)
    private List<EmployeeSection> employeeSectionz;
}
