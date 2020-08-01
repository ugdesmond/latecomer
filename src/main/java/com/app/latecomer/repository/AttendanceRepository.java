package com.app.latecomer.repository;

import com.app.latecomer.model.Attendance;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends PagingAndSortingRepository<Attendance,Integer> {
}
