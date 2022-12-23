package com.mballem.curso.security.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

	@Autowired
	private EspecialidadeRepository repository;

	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(Especialidade especialidade) {
		repository.save(especialidade);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {

		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<?> page;

		if (datatables.getSearch().isEmpty()) {
			page = repository.findAll(datatables.getPageable());
		} else {
			page = repository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());
		}

		return datatables.getResponse(page);

	}

}
