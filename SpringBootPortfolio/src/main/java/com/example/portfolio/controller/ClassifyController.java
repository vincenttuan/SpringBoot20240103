package com.example.portfolio.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.portfolio.model.po.Classify;
import com.example.portfolio.service.PortfolioService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/classify")
public class ClassifyController {

    @Autowired
    private PortfolioService service;

    @GetMapping(value = {"/", "/query"})
    public Iterable<Classify> query() {
        return service.getClassifyRepository().findAll();
    }

    @GetMapping(value = {"/{id}", "/get/{id}"})
    @Transactional
    public Classify get(@PathVariable("id") Integer id) {
        Classify classify = service.getClassifyRepository().findById(id).get();
        //!!!!!因為是延遲加載，通過執行size()這種方式取得所有子項
        if (classify != null && classify.getTStocks() != null) {
            classify.getTStocks().size();
        }
        return classify;
    }

    @PostMapping(value = {"/", "/add"})
    @Transactional
    public Classify add(@RequestBody Map<String, String> map) {
        Classify classify = new Classify();
        classify.setName(map.get("name"));
        System.out.println("map: " + map);
        if (map.get("tx") == null) {
            classify.setTx(false);
        } else {
            classify.setTx(true);
        }
        service.getClassifyRepository().save(classify);
        return classify;
    }

    @PutMapping(value = {"/{id}", "/update/{id}"})
    @Transactional
    public Boolean update(@PathVariable("id") Integer id, @RequestBody Map<String, String> map) {
        Classify o_Classify = get(id);
        if (o_Classify == null) {
            return false;
        }
        o_Classify.setName(map.get("name"));
        if (map.get("transaction") == null) {
            o_Classify.setTx(false);
        } else {
            o_Classify.setTx(true);
        }
        //service.getClassifyRepository().update(id, o_Classify.getName(), o_Classify.getTx());
        System.out.println(o_Classify);
        service.getClassifyRepository().saveAndFlush(o_Classify);
        return true;
    }

    @DeleteMapping(value = {"/{id}", "/delete/{id}"})
    @Transactional
    public Boolean delete(@PathVariable("id") Integer id) {
        service.getClassifyRepository().deleteById(id);
        return true;
    }

}