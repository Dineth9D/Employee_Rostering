package org.employee_rostering.data_management;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.employee_rostering.model.ScheduleState;

@ApplicationScoped
public class ScheduleStateRepository implements PanacheRepository<ScheduleState> {

}

