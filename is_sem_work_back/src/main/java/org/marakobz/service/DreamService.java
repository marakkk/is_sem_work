package org.marakobz.service;

import org.marakobz.model.*;
import org.marakobz.repository.DreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DreamService {

    @Autowired
    private DreamRepository dreamRepository;

    public Dream createOwnDream(Dream dream) {
        return dreamRepository.save(dream);
    }

    public Dream createFromTemplate(Long templateId, Dream dream) {
        var template = dreamRepository.findById(templateId).orElseThrow(() -> new RuntimeException("Template not found"));

        dream.setName(template.getName());
        dream.setTimeEra(template.getTimeEra());
        dream.setVirtualEnvironment(template.getVirtualEnvironment());

        return dreamRepository.save(dream);
    }


}
