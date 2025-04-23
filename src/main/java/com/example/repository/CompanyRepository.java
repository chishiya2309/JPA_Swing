package com.example.repository;

import com.example.entity.Department;
import com.example.entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class CompanyRepository {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public CompanyRepository() {
        emf = Persistence.createEntityManagerFactory("CompanyPU");
        em = emf.createEntityManager();
    }

    public void addDepartment(Department department) {
        em.getTransaction().begin();
        em.persist(department);
        em.getTransaction().commit();
    }

    public void addEmployee(Employee employee) {
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
    }

    public List<Department> getAllDepartments() {
        return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
    }

    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return em.createQuery("SELECT e FROM Employee e WHERE e.department.id = :deptId", Employee.class)
                .setParameter("deptId", departmentId)
                .getResultList();
    }

    // JPQL Example 1: Tìm phòng ban theo tên (sử dụng LIKE)
    public List<Department> findDepartmentsByName(String namePattern) {
        TypedQuery<Department> query = em.createQuery(
                "SELECT d FROM Department d WHERE d.name LIKE :namePattern",
                Department.class
        );
        query.setParameter("namePattern", "%" + namePattern + "%");
        return query.getResultList();
    }

    // JPQL Example 2: Đếm số nhân viên trong một phòng ban
    public Long countEmployeesInDepartment(Long departmentId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Employee e WHERE e.department.id = :deptId",
                Long.class
        );
        query.setParameter("deptId", departmentId);
        return query.getSingleResult();
    }

    // JPQL Example 3: Tìm nhân viên theo tên gần đúng
    public List<Employee> findEmployeesByName(String namePattern) {
        TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.name LIKE :namePattern",
                Employee.class
        );
        query.setParameter("namePattern", "%" + namePattern + "%");
        return query.getResultList();
    }

    public void close() {
        em.close();
        emf.close();
    }
}
