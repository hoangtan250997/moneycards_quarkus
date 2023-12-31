package com.axonactive.agileskills.position.requiredskill.dao;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

@RequestScoped
public class RequiredSkillDAO {

    @Inject
    private EntityManager em;

    public void deleteInList(List<Long> idList) {
        Query query = em.createQuery("DELETE FROM RequiredSkillEntity rs WHERE rs.id IN :idList")
                .setParameter("idList", idList);
        query.executeUpdate();
    }
}
