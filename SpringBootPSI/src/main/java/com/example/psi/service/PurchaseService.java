package com.example.psi.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.psi.model.dto.EmployeeDto;
import com.example.psi.model.dto.PurchaseDto;
import com.example.psi.model.dto.PurchaseItemDto;
import com.example.psi.model.dto.SupplierDto;
import com.example.psi.model.po.Employee;
import com.example.psi.model.po.Purchase;
import com.example.psi.model.po.PurchaseItem;
import com.example.psi.model.po.Supplier;
import com.example.psi.repository.PurchaseItemRepository;
import com.example.psi.repository.PurchaseRepository;

import jakarta.transaction.Transactional;

@Service
public class PurchaseService {
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private PurchaseItemRepository purchaseItemRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	// 新增
	@Transactional
	public Long add(PurchaseDto purchaseDto) {
	    Purchase purchase = modelMapper.map(purchaseDto, Purchase.class);
	    Purchase savedPurchase = purchaseRepository.saveAndFlush(purchase);
	    return savedPurchase.getId(); // 返回保存後的 ID
	}

	
	// 修改
	public void update(PurchaseDto purchaseDto, Long id) {
		Optional<Purchase> purchaseOpt = purchaseRepository.findById(id);
		if(purchaseOpt.isPresent()) {
			Purchase purchase = purchaseOpt.get();
			purchase.setDate(purchaseDto.getDate());
			
			EmployeeDto employeeDto = purchaseDto.getEmployee();
			Employee employee = modelMapper.map(employeeDto, Employee.class);
			purchase.setEmployee(employee);
			
			SupplierDto supplierDto = purchaseDto.getSupplier();
			Supplier supplier = modelMapper.map(supplierDto, Supplier.class);
			purchase.setSupplier(supplier);
			
			purchaseRepository.save(purchase);
		} 
	}
	
	// 刪除
	public void delete(Long id) {
		Optional<Purchase> purchaseOpt = purchaseRepository.findById(id);
		if(purchaseOpt.isPresent()) {
			purchaseRepository.delete(purchaseOpt.get());
		} 
	}
		
	// 查詢單筆
	public PurchaseDto getPurchaseById(Long id) {
		Optional<Purchase> purchaseOpt = purchaseRepository.findById(id);
		if(purchaseOpt.isPresent()) {
			Purchase purchase = purchaseOpt.get();
			PurchaseDto purchaseDto = modelMapper.map(purchase, PurchaseDto.class);
			return purchaseDto;
		}
		return null;
	}
	
	// 全部查詢
	public List<PurchaseDto> findAll() {
		List<Purchase> purchases = purchaseRepository.findAll();
		return purchases.stream()
						  .map(purchase -> modelMapper.map(purchase, PurchaseDto.class))
						  .toList();
	}
	
	//-------------------------------------------------------------------------------------
	
	// 新增訂單項目
	public void addItem(PurchaseItemDto purchaseItemDto, Long pid) {
		// 訂單明細
		PurchaseItem purchaseItem = modelMapper.map(purchaseItemDto, PurchaseItem.class);
		// 訂單檔(主檔)
		Purchase purchase = purchaseRepository.findById(pid).get();
		// 訂單項目與訂單檔(主檔)建立關聯 (ps:由多的一方建立關聯)
		purchaseItem.setPurchase(purchase);
		purchaseItemRepository.save(purchaseItem);
	}
	
	// 查詢訂單項目單筆
	public PurchaseItemDto getPurchaseItemById(Long id) {
		Optional<PurchaseItem> purchaseItemOpt = purchaseItemRepository.findById(id);
		if(purchaseItemOpt.isPresent()) {
			PurchaseItem purchaseItem = purchaseItemOpt.get();
			PurchaseItemDto purchaseItemDto = modelMapper.map(purchaseItem, PurchaseItemDto.class);
			return purchaseItemDto;
		}
		return null;
	}
	
	// 修改訂單項目
	public void updatePurchaseItem(PurchaseItemDto purchaseItemDto, Long pid) {
		// 訂單明細
		PurchaseItem purchaseItem = modelMapper.map(purchaseItemDto, PurchaseItem.class);
		// 訂單檔(主檔)
		Purchase purchase = purchaseRepository.findById(pid).get();
		// 訂單項目與訂單檔(主檔)建立關聯 (ps:由多的一方建立關聯)
		purchaseItem.setPurchase(purchase);
		purchaseItemRepository.save(purchaseItem);		
	}
	
	// 刪除訂單項目
	public void deletePurchaseItem(Long iid) {
		purchaseItemRepository.deleteById(iid);
	}
	
	
	
}