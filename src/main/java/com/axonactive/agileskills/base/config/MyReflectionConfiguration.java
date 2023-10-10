package com.axonactive.agileskills.base.config;

import com.axonactive.agileskills.base.exception.ResponseBody;
import com.axonactive.agileskills.base.model.PagedDataResponse;
import com.axonactive.agileskills.base.model.PaginationData;
import com.axonactive.agileskills.base.security.rest.model.JwtRequest;
import com.axonactive.agileskills.base.security.service.dto.JwtResponse;
import com.axonactive.agileskills.department.service.model.Department;
import com.axonactive.agileskills.department.team.service.model.Team;
import com.axonactive.agileskills.position.service.model.Position;
import com.axonactive.agileskills.report.service.model.YearlyStatistics;
import com.axonactive.agileskills.skill.service.model.Skill;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import com.axonactive.agileskills.user.service.model.User;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
        ResponseBody.class,
        PagedDataResponse.class,
        PaginationData.class,
        JwtResponse.class,
        JwtRequest.class,
        Department.class,
        Team.class,
        Position.class,
        YearlyStatistics.class,
        Skill.class,
        Topic.class,
        User.class})
public class MyReflectionConfiguration {
}
