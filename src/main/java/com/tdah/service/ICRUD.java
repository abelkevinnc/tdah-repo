package com.tdah.service;

import java.util.List;

public interface ICRUD <T>{
	List<T> findAll();

	T saveOrUpdate(T obj);

	T findById(int id);


	void delete(int id);
}
