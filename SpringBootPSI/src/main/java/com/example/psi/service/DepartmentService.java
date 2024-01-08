package com.example.psi.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.psi.model.dto.DepartmentDTO;
import com.example.psi.model.po.Department;
import com.example.psi.repository.DepartmentRepository;

@Service
public class DepartmentService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	// 新增
	public void add(DepartmentDTO departmentDTO) {
		// 將 departmentDTO 轉 department
		Department department = modelMapper.map(departmentDTO, Department.class);
		departmentRepository.save(department);
	}
	
	// 修改
	public void update(DepartmentDTO departmentDTO, Long id) {
		Optional<Department> departmentOpt = departmentRepository.findById(id);
		if(departmentOpt.isPresent()) {
			Department department = departmentOpt.get();
			department.setName(departmentDTO.getName());
			departmentRepository.save(department);
		} 
	}
	
	// 刪除
	public void delete(Long id) {
		Optional<Department> departmentOpt = departmentRepository.findById(id);
		if(departmentOpt.isPresent()) {
			departmentRepository.delete(departmentOpt.get());
		} 
	}
	
	// 查詢單筆
	public DepartmentDTO getDepartmentById(Long id) {
		Optional<Department> departmentOpt = departmentRepository.findById(id);
		if(departmentOpt.isPresent()) {
			Department department = departmentOpt.get();
			// department 轉 departmentDTO
			DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
			return departmentDTO;
		}
		return null;
	}
	
	// 全部查詢
	public List<DepartmentDTO> findAll() {
		List<Department> departments = departmentRepository.findAll();
		return departments.stream()
						  .map(department -> modelMapper.map(department, DepartmentDTO.class))
						  /*
						  .map(department -> {
							  DepartmentDTO dto = new DepartmentDTO();
							  dto.setId(department.getId());
							  dto.setName(department.getName());
							  
							  Set<EmployeeDTO> empDtos = new LinkedHashSet<>();
							  for(Employee emp : department.getEmployees()) {
								  EmployeeDTO empDto = new EmployeeDTO();
								  empDto.setId(emp.getId());
								  empDto.setName(emp.getName());
								  empDtos.add(empDto);
							  }
							  dto.setEmployees(empDtos);
							  
							  return dto;
						  })
						  */
						  .toList();
	}
	
}
