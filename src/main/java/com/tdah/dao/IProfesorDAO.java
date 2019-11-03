package com.tdah.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdah.model.Profesor;

public interface IProfesorDAO extends JpaRepository<Profesor, Integer> {

}
