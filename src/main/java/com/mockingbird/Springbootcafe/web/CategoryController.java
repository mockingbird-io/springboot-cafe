package com.mockingbird.Springbootcafe.web;

import com.mockingbird.Springbootcafe.pojo.Category;
import com.mockingbird.Springbootcafe.service.CategoryService;
import com.mockingbird.Springbootcafe.util.ImageUtil;
import com.mockingbird.Springbootcafe.util.Page4Navigator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        return categoryService.list(start, size, 5);
    }

    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
        categoryService.add(bean);
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, bean.getId() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable(name = "id") int id, HttpServletRequest request) throws Exception {
        categoryService.delete(id);
        File fileFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(fileFolder, id + ".jpg");
        file.delete();
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception {
        return categoryService.get(id);
    }

    @PutMapping("/categories/{id}")
    public Object update(Category category, MultipartFile file, HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        category.setName(name);
        categoryService.update(category);

        if (file != null) {
            saveOrUpdateImageFile(category, file, request);
        }
        return category;
    }
}

