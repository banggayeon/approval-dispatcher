package com.banggayeon.workflow_approval_dispatcher.domain.template;

import com.banggayeon.workflow_approval_dispatcher.domain.template.dto.TemplateCreateRequest;
import com.banggayeon.workflow_approval_dispatcher.domain.template.dto.TemplateResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
public class TemplateController { 
    private final TemplateService service;

    public TemplateController(TemplateService service){
        this.service = service;
    }

    @PostMapping
    public TemplateResponse create(@Valid @RequestBody TemplateCreateRequest req){
        Long id = service.create(req);
        return TemplateResponse.from(service.get(id));
    }

    @GetMapping("/{id}")
    public TemplateResponse get(@PathVariable Long id){
        return TemplateResponse.from(service.get(id));
    }

    @GetMapping
    public List<TemplateResponse> list() {
        return service.list().stream().map(TemplateResponse::from).toList();
    }
}
