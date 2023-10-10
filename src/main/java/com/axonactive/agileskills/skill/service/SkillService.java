package com.axonactive.agileskills.skill.service;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.base.exception.ErrorMessage;
import com.axonactive.agileskills.base.exception.InputValidationException;
import com.axonactive.agileskills.base.exception.ResourceNotFoundException;
import com.axonactive.agileskills.base.model.PaginationData;
import com.axonactive.agileskills.skill.dao.SkillDAO;
import com.axonactive.agileskills.skill.entity.SkillEntity;
import com.axonactive.agileskills.skill.service.mapper.SkillMapper;
import com.axonactive.agileskills.skill.service.model.Skill;
import com.axonactive.agileskills.skill.topic.dao.TopicDAO;
import com.axonactive.agileskills.skill.topic.entity.TopicEntity;
import com.axonactive.agileskills.skill.topic.service.TopicService;
import com.axonactive.agileskills.skill.topic.service.mapper.TopicMapper;
import com.axonactive.agileskills.skill.topic.service.model.Topic;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
@RegisterForReflection(targets = {InputValidationException.class})
public class SkillService {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Inject
    private SkillDAO skillDAO;

    @Inject
    private TopicDAO topicDAO;

    @Inject
    private SkillMapper skillMapper;

    @Inject
    private TopicMapper topicMapper;

    @Inject
    private TopicService topicService;

    @Inject
    private SkillListCache skillListCache;

    public List<SkillEntity> getByStatus(StatusEnum status) {
        List<SkillEntity> cachedResult = skillListCache.getCache().getIfPresent(status.name());

        if (cachedResult != null) {
            return cachedResult;
        }

        List<SkillEntity> skillEntityList = skillDAO.findByStatus(status);

        skillListCache.getCache().put(status.name(), skillEntityList);

        return skillEntityList;
    }

    public Skill getByIdAndStatus(Long id, StatusEnum status) throws ResourceNotFoundException {
        SkillEntity skillEntity = skillDAO.findByIdAndStatus(id, status)
                .orElseThrow(()
                        -> new ResourceNotFoundException(ErrorMessage.KEY_SKILL_NOT_FOUND, ErrorMessage.SKILL_NOT_FOUND));
        return skillMapper.toDTO(skillEntity);
    }

    @Transactional
    public Skill create(Skill skill) throws InputValidationException {

        verifySkill(skill);

        if (CollectionUtils.isNotEmpty(skill.getTopicList())) {
            verifyTopicList(skill.getTopicList());
        }

        SkillEntity skillEntity = SkillEntity.builder()
                .name(skill.getName().trim())
                .description(skill.getDescription() == null ? null : skill.getDescription().trim())
                .status(StatusEnum.ACTIVE)
                .build();

        SkillEntity referencedSkill = skillDAO.create(skillEntity);

        List<TopicEntity> topicEntityList = createTopicEntityList(skill.getTopicList(), referencedSkill);

        Skill returnSkill = skillMapper.toDTO(referencedSkill);

        if (CollectionUtils.isNotEmpty(topicEntityList)) {
            returnSkill.setTopicList(topicMapper.toDTOList(topicEntityList));
        } else {
            returnSkill.setTopicList(new ArrayList<>());
        }

        invalidateCache();

        return returnSkill;
    }

    private List<TopicEntity> createTopicEntityList(List<Topic> topicList, SkillEntity skill) {
        List<TopicEntity> topicEntityList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(topicList)) {
            for (Topic topic : topicList
            ) {
                TopicEntity createdTopicEntity = TopicEntity.builder()
                        .name(topic.getName().trim())
                        .description(topic.getDescription() == null ? null : topic.getDescription().trim())
                        .status(StatusEnum.ACTIVE)
                        .skill(skill)
                        .build();
                topicEntityList.add(topicDAO.create(createdTopicEntity));
            }
        }
        return topicEntityList;
    }

    public List<Skill> getSkillListIncludingTopicList() throws ResourceNotFoundException {

        List<SkillEntity> skillEntityList = skillDAO.findByStatus(StatusEnum.ACTIVE);

        List<Skill> skillList = skillMapper.toDTOList(skillEntityList);

        for (Skill skill : skillList) {
            List<Topic> topicList = topicService.getBySkillIdAndStatus(skill.getId(), StatusEnum.ACTIVE);
            skill.setTopicList(topicList);
        }
        return skillList;
    }

    public Skill softDelete(Long id) throws ResourceNotFoundException {
        SkillEntity deletedSkill = skillDAO.softDelete(id);
        if (deletedSkill == null) {
            throw new ResourceNotFoundException(ErrorMessage.KEY_SKILL_NOT_FOUND, ErrorMessage.SKILL_NOT_FOUND);
        }
        invalidateCache();
        return skillMapper.toDTO(deletedSkill);
    }

    public List<SkillEntity> getPagedByStatus(Integer page, Integer size, StatusEnum status) throws ResourceNotFoundException, InputValidationException {
        List<SkillEntity> skillEntityList = getByStatus(status);
        if (page < 1 || size < 1) {
            throw new InputValidationException(ErrorMessage.KEY_PAGE_RANGE_NONPOSITIVE, ErrorMessage.PAGE_RANGE_NONPOSITIVE);
        } else if ((page - 1) * size >= skillEntityList.size()) {
            throw new ResourceNotFoundException(ErrorMessage.KEY_PAGE_OUT_OF_RANGE, ErrorMessage.PAGE_OUT_OF_RANGE);
        }
        int toIndex = page * size;
        if (toIndex > skillEntityList.size()) {
            toIndex = skillEntityList.size();
        }
        return skillEntityList.subList((page - 1) * size, toIndex);
    }

    public PaginationData getPaginationData(Integer page, Integer size, StatusEnum status) throws ResourceNotFoundException, InputValidationException {
        int totalRecords = Math.toIntExact(skillDAO.getTotalRecord(status));
        if (page < 1 || size < 1) {
            throw new InputValidationException(ErrorMessage.KEY_PAGE_RANGE_NONPOSITIVE, ErrorMessage.PAGE_RANGE_NONPOSITIVE);
        } else if ((page - 1) * size > totalRecords) {
            throw new ResourceNotFoundException(ErrorMessage.KEY_PAGE_OUT_OF_RANGE, ErrorMessage.PAGE_OUT_OF_RANGE);
        }

        Integer totalPages = (int) Math.ceil(totalRecords / (double) size);

        return PaginationData.builder()
                .totalRecords(totalRecords)
                .currentPage(page)
                .pageSize(size)
                .totalPages(totalPages)
                .build();
    }

    private boolean isExisted(String name) {
        return skillDAO.findByName(name.trim().toLowerCase()).isPresent();
    }

    private void verifySkill(Skill skill) throws InputValidationException {
        Set<ConstraintViolation<Skill>> violations = validator.validate(skill);
        if (CollectionUtils.isNotEmpty(violations)) {
            throw new ConstraintViolationException(violations);
        }
        if (isExisted(skill.getName())) {
            throw new InputValidationException(ErrorMessage.KEY_SKILL_ALREADY_EXISTED,
                    ErrorMessage.SKILL_ALREADY_EXISTED);
        }
    }

    private void verifyTopicList(List<Topic> topicList) throws InputValidationException {

        checkAllTopicsAreValid(topicList);

        checkDuplicatedTopicList(topicList);
    }

    private void checkAllTopicsAreValid(List<Topic> topicList) {
        for (Topic topic : topicList
        ) {
            if (topic.getName() != null) {
                topic.setName(topic.getName().trim());
            }
            Set<ConstraintViolation<Topic>> violations = validator.validate(topic);
            if (CollectionUtils.isNotEmpty(violations)) {
                throw new ConstraintViolationException(violations);
            }
        }
    }

    private void checkDuplicatedTopicList(List<Topic> topicList) throws InputValidationException {
        List<String> topicNameList = topicList.stream()
                .filter(Objects::nonNull)
                .map(topic -> topic.getName().trim().toLowerCase())
                .collect(Collectors.toList());
        Set<String> topicNameSet = new HashSet<>(topicNameList);
        if (topicNameList.size() != topicNameSet.size())
            throw new InputValidationException(ErrorMessage.KEY_DUPLICATED_TOPIC_NAME, ErrorMessage.DUPLICATED_TOPIC_NAME);
    }

    private void invalidateCache() {
        skillListCache.getCache().invalidateAll();
    }
}
