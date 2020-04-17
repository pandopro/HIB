package com.project.dao.abstraction;

import com.project.model.Welcome;
import com.project.model.WelcomeLocaleDto;

public interface WelcomeDao extends GenericDao<Long, Welcome> {
    WelcomeLocaleDto getWelcomeLocaleDTOByLocale(String locale);
}


