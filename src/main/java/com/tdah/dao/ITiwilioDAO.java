package com.tdah.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdah.model.TiwilioAccout;

public interface ITiwilioDAO extends JpaRepository<TiwilioAccout, Integer>{
}
