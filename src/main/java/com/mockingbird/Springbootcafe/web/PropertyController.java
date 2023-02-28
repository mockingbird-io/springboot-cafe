package com.mockingbird.Springbootcafe.web;

import com.mockingbird.Springbootcafe.pojo.Property;
import com.mockingbird.Springbootcafe.service.PropertyService;
import com.mockingbird.Springbootcafe.util.Page4Navigator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class PropertyController {
    @Resource
    PropertyService propertyService;

    @GetMapping("/categories/{cid}/properties")
    public Page4Navigator<Property> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        return propertyService.list(cid, start, size,5);
    }

    @GetMapping("/properties/{id}")
    public Property get(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
        return propertyService.get(id);
    }

    @PostMapping("/properties")
    public Object add(@RequestBody Property bean) throws Exception{
        propertyService.add(bean);
        return bean;
    }

    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
        propertyService.delete(id);
        return null;
    }

    @PutMapping("/properties")
    public Object update(@RequestBody Property bean) throws Exception{
        propertyService.update(bean);
        return bean;
    }


}
