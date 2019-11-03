package com.tdah.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdah.model.Estudiante;

public interface IEstudianteDAO extends JpaRepository<Estudiante, Integer> {

}
