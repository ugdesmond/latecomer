package com.app.latecomer.businesslogic;

import com.app.latecomer.model.Employee;
import com.app.latecomer.model.Settings;
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
public class SettingsLogic extends AbstractJpaDao<Settings> {
    Logger logger = Logger.getLogger(Employee.class);

    public List<Settings> getByColumnName(String columnName, Object value) {
        List<Settings> settingsList = new ArrayList<>();
        try {
            CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<Settings> cr = cb.createQuery(Settings.class);
            Root<Settings> root = cr.from(Settings.class);
            Predicate restriction = cb.equal(root.get(columnName), value);
            cr.select(root).where(restriction).orderBy(cb.asc(root.get("id")));
            Query<Settings> query = getCurrentSession().createQuery(cr);
            settingsList = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return settingsList;
    }

    public void deactActivateAllSettings(List<Settings> settings) {
        for (Settings set : settings) {
            set.setActivated(false);
            update(set);
        }
    }


}
