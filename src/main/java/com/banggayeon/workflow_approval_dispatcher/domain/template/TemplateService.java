package com.banggayeon.workflow_approval_dispatcher.domain.template;

import com.banggayeon.workflow_approval_dispatcher.domain.template.dto.TemplateCreateRequest;
import com.banggayeon.workflow_approval_dispatcher.global.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TemplateService { 
    private final TemplateRepository repo;

    public TemplateService(TemplateRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Long create(TemplateCreateRequest req) {
        Template t = new Template(req.getName(), req.getChannelType(), req.getPromptBody());
        return repo.save(t).getId();
    }

    public Template get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Template not found: " + id));
    }

    public List<Template> list(){
        return repo.findAll();
    }
}
