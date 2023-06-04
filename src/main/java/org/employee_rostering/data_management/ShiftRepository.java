package org.employee_rostering.data_management;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.employee_rostering.model.Shift;

@ApplicationScoped
public class ShiftRepository implements PanacheRepository<Shift> {

}

