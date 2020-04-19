package com.project.dao;

import com.project.model.GenreDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class GenreDtoDaoImpl implements GenreDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GenreDto getGenreDtoById(long id, String locale) throws NoResultException {
        String temp = "Select new com.project.model.GenreDto(g.id, g.number, g.genreLocale.lang) FROM Genre g WHERE g.id = :genreId".replaceAll("lang", locale);
        return entityManager.createQuery(temp, GenreDto.class).setParameter("genreId", id).getSingleResult();
    }

    @Override
    public List<GenreDto> getAllGenresDto(String locale) {
        String temp = "Select new com.project.model.GenreDto(g.id, g.number, g.genreLocale.lang) FROM Genre g".replaceAll("lang", locale);
        return entityManager.createQuery(temp, GenreDto.class).getResultList();
    }

    @Override
    public Long getMaxNumber() throws NoResultException {
        String temp = "Select new com.project.model.GenreDto(g.id, g.number, g.genreLocale.en) FROM Genre g WHERE g.number = (select max(number) from Genre)";
        return entityManager.createQuery(temp, GenreDto.class).getSingleResult().getNumber();
    }
}
