package org.ssh.pm.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity(name="B2C_COMMODITY")
@Indexed(index = "indexes/employee")
public class Employee implements java.io.Serializable {
    private static final long serialVersionUID = 7794235365739814541L;
    private Integer empId;
    private String empName;

    private String empNo;
    private Double empSalary;

    // Constructors

    /** */
    /** default constructor */
    public Employee() {
    }

    /** */
    /** minimal constructor */
    public Employee(Integer empId) {
        this.empId = empId;
    }

    /** */
    /** full constructor */
    public Employee(Integer empId, String empName, String empNo, Double empSalary) {
        this.empId = empId;
        this.empName = empName;
        this.empNo = empNo;
        this.empSalary = empSalary;
    }

    // Property accessors
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "emp_id", unique = true, nullable = false, insertable = true, updatable = true)
    @DocumentId
    public Integer getEmpId() {
        return this.empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    @Column(name = "emp_name", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
    @Field(name = "name", index = Index.TOKENIZED, store = Store.YES)
    public String getEmpName() {
        return this.empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    @Column(name = "emp_no", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
    @Field(index = Index.UN_TOKENIZED)
    public String getEmpNo() {
        return this.empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    @Column(name = "emp_salary", unique = false, nullable = true, insertable = true, updatable = true, precision = 7)
    public Double getEmpSalary() {
        return this.empSalary;
    }

    public void setEmpSalary(Double empSalary) {
        this.empSalary = empSalary;
    }
}