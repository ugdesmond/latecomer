package com.app.latecomer.businesslogic;

import com.app.latecomer.model.Attendance;
import com.app.latecomer.model.Employee;
import com.app.latecomer.model.Settings;
import com.app.latecomer.repository.AbstractJpaDao;
import com.app.latecomer.repository.AttendanceRepository;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class AttendanceLogic extends AbstractJpaDao<Attendance> {

    private AttendanceRepository repository;

    Logger logger = Logger.getLogger(Attendance.class);
    public static Double unitValue = 0.2;

    public AttendanceLogic(AttendanceRepository repository) {
        this.repository = repository;
    }

    public List<Attendance> getByColumnName(String columnName, Object value) {
        List<Attendance> attendances = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Attendance> cr = cb.createQuery(Attendance.class);
            Root<Attendance> root = cr.from(Attendance.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Attendance> query = getCurrentSession().createQuery(cr);
            attendances = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return attendances;
    }

    public Double calculateAmountOwing(Settings settings, Timestamp date) {
        long diff = date.getTime() - settings.getMeetingDateTime().getTime();
        if (diff <= 0)
            return 0.0;
        int diffmin = (int) (diff / (60 * 1000));
        System.out.println(("=====================attendance====" + diff));
        return unitValue * diffmin;
    }

    public List<Attendance> findPaginated(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Attendance> pagedResult = repository.findAll(paging);
        return pagedResult.getContent().stream().sorted(comparing(Attendance::getOweAmount).reversed()).collect(Collectors.toList());
    }

    public List<Attendance> searchAttendance(String searchValue) {
        List<Attendance> attendances = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Attendance> cr = cb.createQuery(Attendance.class);
            Root<Attendance> root = cr.from(Attendance.class);
            Join<Attendance, Employee> employeeJoin = root.join("employee");
            Predicate name = cb.like(employeeJoin.get("name"), "%" + searchValue + "%");
            Predicate email = cb.like(employeeJoin.get("email"), "%" + searchValue + "%");
            Predicate address = cb.like(employeeJoin.get("address"), "%" + searchValue + "%");
            Predicate predicateOr = cb.or(name, email, address);
            cr.select(root).where(predicateOr).orderBy(cb.asc(root.get("id")));
            Query<Attendance> query = getCurrentSession().createQuery(cr);
            attendances = query.getResultList();
        } catch (Exception e) {

        }
        return attendances;
    }
}
