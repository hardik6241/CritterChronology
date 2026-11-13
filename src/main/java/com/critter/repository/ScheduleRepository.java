package com.critter.repository;

import com.critter.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByPetsId(Long petId);
    List<Schedule> findByEmployeesId(Long employeeId);
    List<Schedule> findByPetsCustomerId(Long customerId);
}
