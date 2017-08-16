package jp.co.example.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import jp.co.example.common.AppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "section_employee")
public class EmployeeSection extends AppBaseEntity implements Serializable {

    @NotNull
    private Long sectionId;

    @NotNull
    private Long employeeId;


    @ManyToOne
    @JoinColumn(name="sectionId", referencedColumnName = "id", insertable = false, updatable = false )
    private Section section;

    @ManyToOne
    @JoinColumn(name="employeeId", referencedColumnName = "id", insertable = false, updatable = false )
    private Employee employee;
}
