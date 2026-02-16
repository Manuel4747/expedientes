/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.facades;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import py.gov.jem.expedientes.controllers.DateItem;
import py.gov.jem.expedientes.controllers.DateStringItem;

/**
 *
 * @author eduardo
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;
            
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> findRange(int first, int pageSize, String sortField, String sortOrder, Map<String, Object> filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root<T> entityRoot = cq.from(entityClass);
        cq.select(entityRoot);
        List<javax.persistence.criteria.Predicate> predicates = getPredicates(cb, entityRoot, filters);
        if (predicates.size() > 0) {
            cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        }
        if (sortField != null && sortField.length() > 0) {
            if (entityRoot.get(sortField) != null) {
                if (sortOrder.startsWith("ASC")) {
                    cq.orderBy(cb.asc(entityRoot.get(sortField)));
                }
                if (sortOrder.startsWith("DESC")) {
                    cq.orderBy(cb.desc(entityRoot.get(sortField)));
                }
            }
        }
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);
        return q.getResultList();
    }

    public List<T> findRange(int first, int pageSize, Map<String, String> sortFields, Map<String, Object> filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root<T> entityRoot = cq.from(entityClass);
        cq.select(entityRoot);
        List<javax.persistence.criteria.Predicate> predicates = getPredicates(cb, entityRoot, filters);
        if (predicates.size() > 0) {
            cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        }
        if (sortFields != null && !sortFields.isEmpty()) {
            for (String sortField : sortFields.keySet()) {
                if (entityRoot.get(sortField) != null) {
                    String sortOrder = sortFields.get(sortField);
                    if (sortOrder.startsWith("ASC")) {
                        cq.orderBy(cb.asc(entityRoot.get(sortField)));
                    }
                    if (sortOrder.startsWith("DESC")) {
                        cq.orderBy(cb.desc(entityRoot.get(sortField)));
                    }
                }
            }
        }
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);
        return q.getResultList();
    }

    public int count(Map<String, Object> filters) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root<T> entityRoot = cq.from(entityClass);
        cq.select(cb.count(entityRoot));
        List<javax.persistence.criteria.Predicate> predicates = getPredicates(cb, entityRoot, filters);
        if (predicates.size() > 0) {
            cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));
        }
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    private List<Predicate> getPredicates(CriteriaBuilder cb, Root<T> entityRoot, Map<String, Object> filters) {
        javax.persistence.metamodel.Metamodel entityModel = this.getEntityManager().getMetamodel();
        javax.persistence.metamodel.ManagedType<T> entityType = entityModel.managedType(entityClass);
        java.util.Set<javax.persistence.metamodel.EmbeddableType<?>> embeddables = entityModel.getEmbeddables();
        String fieldTypeName = null;
        // Add predicates (WHERE clauses) based on filters map
        List<javax.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
        for (String s : filters.keySet()) {
            javax.persistence.criteria.Path<Object> pkFieldPath = null;
            if (s.contains(".")) {
                String embeddedIdField = s.split("\\.")[0];
                String embeddedIdMember = s.split("\\.")[1];
                pkFieldPath = entityRoot.get(embeddedIdField).get(embeddedIdMember);
                javax.persistence.metamodel.EmbeddableType<?> embeddableType = entityModel.embeddable(entityType.getAttribute(embeddedIdField).getJavaType());
                fieldTypeName = embeddableType.getAttribute(embeddedIdMember).getJavaType().getName();
            } else {
                pkFieldPath = entityRoot.get(s);
                fieldTypeName = entityType.getAttribute(s).getJavaType().getName();
            }
            if (pkFieldPath != null && fieldTypeName != null) {
                if (fieldTypeName.contains("String")) {
                    predicates.add(cb.like((javax.persistence.criteria.Expression) pkFieldPath, filters.get(s) + "%"));
                } else {
                    javax.persistence.criteria.Expression<?> filterExpression = getCastExpression((String) filters.get(s), fieldTypeName, cb);
                    if (filterExpression != null) {
                        predicates.add(cb.equal((javax.persistence.criteria.Expression<?>) pkFieldPath, filterExpression));
                    } else {
                        predicates.add(cb.equal((javax.persistence.criteria.Expression<?>) pkFieldPath, filters.get(s)));
                    }
                }
            }
        }
        return predicates;
    }

    private Expression<?> getCastExpression(String searchValue, String typeName, CriteriaBuilder cb) {
        javax.persistence.criteria.Expression<?> expression = null;
        switch (typeName) {
            case "short":
                expression = cb.literal(Short.parseShort(searchValue));
                break;
            case "byte":
                expression = cb.literal(Byte.parseByte(searchValue));
                break;
            case "int":
                expression = cb.literal(Integer.parseInt(searchValue));
                break;
            case "long":
                expression = cb.literal(Long.parseLong(searchValue));
                break;
            case "float":
                expression = cb.literal(Float.parseFloat(searchValue));
                break;
            case "double":
                expression = cb.literal(Double.parseDouble(searchValue));
                break;
            case "boolean":
                expression = cb.literal(Boolean.parseBoolean(searchValue));
                break;
            default:
                break;
        }
        return expression;
    }
 
    
    public Date getSystemDate() {
        return getSystemDate(0);
    }
    
    public Date getSystemDate(Integer cantDias) {
        Query query = getEntityManager().createNativeQuery(
                "SELECT DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + cantDias + " DAY) AS DATE_VALUE FROM DUAL"
                , DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }
    
    public Date getSystemDateOnly() {
        return getSystemDateOnly(0);
    }
    
    public Date getSystemDateOnly(Integer cantDias) {
        Query query = getEntityManager().createNativeQuery(
                "SELECT cast(DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + cantDias + " DAY) AS DATE) AS DATE_VALUE FROM DUAL"
                , DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }
    
    public Date getSystemDateLastSecond() {
        return getSystemDateLastSecond(0);
    }
    
    public Date getSystemDateLastSecond(Integer cantDias) {
        Query query = getEntityManager().createNativeQuery(
                "SELECT DATE_ADD(cast(DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + (cantDias + 1) + " DAY) AS DATE), INTERVAL -1 SECOND) AS DATE_VALUE FROM DUAL"
                , DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }
    
    public Date getPrimerDiaMes(Date fecha) {
        Query query = getEntityManager().createNativeQuery(
                "SELECT DATE_ADD(DATE_ADD(LAST_DAY('" + format.format(fecha) + "'), INTERVAL 1 DAY), INTERVAL - 1 MONTH) AS DATE_VALUE FROM DUAL"
                , DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }
    
    public Date getUltimoDiaMes(Date fecha) {
        Query query = getEntityManager().createNativeQuery(
                "SELECT LAST_DAY('" + format.format(fecha) + "') AS DATE_VALUE FROM DUAL"
                , DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }
    
    public Date getSystemDateMinutes(Integer cantMinutos) {
        Query query = getEntityManager().createNativeQuery(
                "SELECT DATE_ADD(CURRENT_TIMESTAMP, INTERVAL " + cantMinutos + " MINUTE) AS DATE_VALUE FROM DUAL"
                , DateItem.class);
        DateItem dateItem = (DateItem) query.getSingleResult();
        return dateItem.getDate();
    }
    
    public String getSystemDateString(Date fecha, Integer cantDias) {
        /*Query query = getEntityManager().createNativeQuery(
                "SET lc_time_names = 'es_PY'");
        query.getSingleResult();*/
        Query query = getEntityManager().createNativeQuery(
                "SELECT date_format(cast(DATE_ADD(str_to_date('" + format.format(fecha) + "','%Y-%m-%d'), INTERVAL " + cantDias + " DAY) AS DATE),'%d de %M de %Y') AS DATE_VALUE FROM DUAL"
                , DateStringItem.class);
        DateStringItem dateItem = (DateStringItem) query.getSingleResult();
        return dateItem.getDate();
    }
}
