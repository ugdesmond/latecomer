package com.app.latecomer.businesslogic;

import com.app.latecomer.model.Employee;
import com.app.latecomer.repository.AbstractJpaDao;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeLogic extends AbstractJpaDao<Employee> {
    Logger logger = Logger.getLogger(Employee.class);


    public List<Employee> getByColumnName(String columnName, Object value) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
            Root<Employee> root = cr.from(Employee.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Employee> query = getCurrentSession().createQuery(cr);
            employeeList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return employeeList;
    }
}
