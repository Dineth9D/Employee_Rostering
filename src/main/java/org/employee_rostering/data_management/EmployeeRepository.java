package org.employee_rostering.data_management;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.employee_rostering.model.Employee;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepositoryBase<Employee, Long> {

}
