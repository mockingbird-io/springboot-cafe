package com.mockingbird.Springbootcafe.web;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.ProductImage;
import com.mockingbird.Springbootcafe.service.ProductImageService;
import com.mockingbird.Springbootcafe.service.ProductService;
import com.mockingbird.Springbootcafe.util.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ProductImageController {
    @Resource
    ProductImageService productImageService;
    @Resource
    ProductService productService;

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") int pid) throws Exception{
        Product product = productService.get(pid);
        if(ProductImageService.type_single.equals(type)){
            return productImageService.listSingleProductImages(product);
        }else if(ProductImageService.type_detail.equals(type)){
            return productImageService.listDetailProductImages(product);
        }else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") int pid, @RequestParam("type") String type, MultipartFile image, HttpServletRequest request) throws Exception{
        ProductImage productImage = new ProductImage();
        Product product = productService.get(pid);
        productImage.setProduct(product);
        productImage.setType(type);

        productImageService.add(productImage);
        String folder = "img/";
        if(ProductImageService.type_single.equals(productImage.getType())){
            folder +="productSingle";
        }
        else{
            folder +="productDetail";
        }
        File imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,productImage.getId()+".jpg");
        String fileName = file.getName();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ProductImageService.type_single.equals(productImage.getType())){
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }

        return productImage;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception{
        ProductImage bean = productImageService.get(id);
        productImageService.delete(id);

        String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType()))
            folder +="productSingle";
        else
            folder +="productDetail";

        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        file.delete();
        if(ProductImageService.type_single.equals(bean.getType())){
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.delete();
            f_middle.delete();
        }

        return null;
    }

    @PostMapping("/excelUpload")
    public Boolean excelUpload(@RequestParam("file") MultipartFile file) throws IOException{
        if(file == null){
            return ResultVO.error(500,"文件不能为空");
        }
        InputStream inputStream = file.getInputStream();
        ExcelReader excelReader = ExcelUtil.getReader(inputStream);
        List<List<Object>> readAll = excelReader.read();
        int i = -1;
        for (List<Object> execList : readAll){
            i++;
            if (i<=5 || i>=106){
                continue;
            }

            //录入master主表
            ProjectMasterRef projectMasterRef = new ProjectMasterRef();
            projectMasterRef.setNumber(projectMasterRefService.selectMaxNumber() + 1);
            projectMasterRef.setType("省级重点项目");
            projectMasterRef.setProjectName(Convert.toStr(execList.get(1)).trim());
            projectMasterRef.setJsdd(Convert.toStr(execList.get(17)).trim());
            projectMasterRef.setZtz(Convert.toBigDecimal(execList.get(2)).divide(new BigDecimal(10000),6, RoundingMode.UNNECESSARY));
            projectMasterRef.setDnjhtz(Convert.toBigDecimal(execList.get(6)).divide(new BigDecimal(10000),6, RoundingMode.UNNECESSARY));
            projectMasterRef.setJhKgTime(Convert.toLong(execList.get(3)));
            projectMasterRefService.save(projectMasterRef);

            //录入info扩展表
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setMasterId(projectMasterRef.getId());
            projectInfo.setZrdw(Convert.toStr(execList.get(16)).trim());
            String[] tempStr;
            tempStr = Convert.toStr(execList.get(29)).trim().split(",");
            projectInfo.setXmllr(tempStr[0].replace(" ",""));
            projectInfo.setXmllrPhone(tempStr[1]);
            projectInfo.setZyjsnr(Convert.toStr(execList.get(15)));
            projectInfo.setZjly(Convert.toStr(execList.get(21)).replace(" ",""));
            projectInfo.setDnjsnrjydjhap(Convert.toStr(execList.get(5)));
            projectInfo.setBuildertype(Convert.toStr(execList.get(19)));
            projectInfo.setSpwj(Convert.toStr(execList.get(18)));
            projectInfo.setTzly(Convert.toStr(execList.get(20)));
            projectInfo.setLand_zyd(Convert.toBigDecimal(execList.get(22)));
            projectInfo.setLand_yyxq(Convert.toBigDecimal(execList.get(23)));
            projectInfo.setLand_yyyd(Convert.toBigDecimal(execList.get(24)));
            projectInfo.setGh(Convert.toStr(execList.get(25)));
            projectInfo.setYd(Convert.toStr(execList.get(26)));
            projectInfo.setHp(Convert.toStr(execList.get(27)));
            projectInfo.setNp(Convert.toStr(execList.get(28)));
            projectInfo.setFocus(Convert.toStr(execList.get(30)));
            projectInfoService.save(projectInfo);

            //录入type_ref表
            ProjectTypeRef projectTypeRef = new ProjectTypeRef();
            projectTypeRef.setMasterId(projectMasterRef.getId());
            projectTypeRef.setType("省级重点项目");
            projectTypeRefService.save(projectTypeRef);

            Object value1 = execList.get(1);
            System.out.println(value1);
        }
        return true;
    }
}
