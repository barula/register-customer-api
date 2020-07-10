package com.huawei.controllers;

import com.huawei.models.CustomerModel;
import com.huawei.services.CustomerService;
import com.paypertic.filter.util.models.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/customers")
/**
 * Controller rest de ejemplo. Recursos en plural y en español separando palabras por guiones (-).
 */
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("")
    /**
     * Anotacion de model access, esta anotación indica que el modelo retornado es un pojo.
     */
    public CustomerModel create(@RequestBody CustomerModel model) {
        return customerService.create(model);
    }

    @GetMapping("/{id}")
    /**
     * Anotacion de model access, esta anotación indica que el modelo retornado es un pojo.
     */
    public CustomerModel get(@PathVariable("id") String exampleId) {
        return customerService.get(exampleId);
    }

    @GetMapping("")
    /**
     * Anotacion de model access, esta anotación indica que el modelo retornado es una pagina.
     */
    public Page<CustomerModel> search(HttpServletRequest request) {
        return customerService.search(request.getQueryString());
    }

}