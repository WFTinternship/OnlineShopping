package com.workfront.internship.controller;

import com.sun.xml.internal.ws.encoding.StringDataContentHandler;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import com.workfront.internship.business.*;
import com.workfront.internship.common.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
@Controller
public class AdminController {
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private MediaManager mediaManager;
    @Autowired
    private SizeManager sizeManager;
    @Autowired
    private SalesManager salesManager;

    @RequestMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @RequestMapping("/add")
    public String getaddProductPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        Map categoryMap = new HashMap();
        Map sizeMap = new HashMap();


        for (Category category : categories) {
            if (category.getParentID() == 0) {
                List<Size> sizes = sizeManager.getSizesByCategoryId(category.getCategoryID());
                sizeMap.put(category.getCategoryID(), sizes);
            }

        }
        for (Category category : categories) {
            Category category1;
            if (category.getParentID() == 0) {
                categoryMap.put(category.getCategoryID(), category.getCategoryID());

            } else {
                category1 = categoryManager.getCategoryByID(category.getParentID());

                categoryMap.put(category.getCategoryID(), category1.getParentID());

            }
        }


        Product product = new Product();
        Category category = new Category();
        String option = (String) request.getParameter("option");
        product.setName(" ").setPrice(0.0).setShippingPrice(0.0).setDescription(" ").setCategory(category);
        request.getSession().setAttribute("product", product);
        request.setAttribute("categories", categories);
        request.setAttribute("categoryMap", categoryMap);
        request.setAttribute("sizeMap", sizeMap);
        request.getSession().setAttribute("option", option);
        return "addProduct";
    }

    @RequestMapping("/edit")
    public String getEditProductPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        String[] checkedValues = (request.getParameterValues("product"));
        String option = (String) request.getParameter("option");
        if(option.equals("sale")){
            request.getSession().setAttribute("checkedValues", checkedValues);
            return "discountPage";
        }
        if (option.equals("delete")) {
            List<Product> products = (List<Product>) request.getSession().getAttribute("products");
            for (int j = 0; j < checkedValues.length; j++) {
                productManager.deleteProduct(Integer.parseInt(checkedValues[j]));


                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getProductID() == Integer.parseInt(checkedValues[j])) {
                        products.remove(products.get(i));

                    }
                }
            }

            request.getSession().setAttribute("products", products);
            return "products";
        }
        else{
        Map categoryMap = new HashMap();
        Map sizeMap = new HashMap();


        for (Category category : categories) {
            if (category.getParentID() == 0) {
                List<Size> sizes = sizeManager.getSizesByCategoryId(category.getCategoryID());
                sizeMap.put(category.getCategoryID(), sizes);
            }

        }
        for (Category category : categories) {
            Category category1;
            if (category.getParentID() == 0) {
                categoryMap.put(category.getCategoryID(), category.getCategoryID());

            } else {
                category1 = categoryManager.getCategoryByID(category.getParentID());

                categoryMap.put(category.getCategoryID(), category1.getParentID());

            }
        }


            Product product = productManager.getProduct(Integer.parseInt(checkedValues[0]));
            Map<String, Integer> sizeOptionQuantity = productManager.getSizeOptionQuantityMap(product.getProductID());
            request.getSession().setAttribute("product", product);
            request.setAttribute("sizeOptionQuantity", sizeOptionQuantity);
            request.setAttribute("categories", categories);
            request.setAttribute("categoryMap", categoryMap);
            request.setAttribute("sizeMap", sizeMap);
            request.getSession().setAttribute("option", option);
            return "addProduct";
        }
    }

    @RequestMapping("/saveProduct")
    public String saveProduct(HttpServletRequest request,
                              @RequestParam(value = "file", required = false) MultipartFile[] image) throws IOException {

        String filePath = request.getSession().getServletContext().getRealPath("/resources/image");
List<String> imagePath = new ArrayList<>();


        Product product = (Product) request.getSession().getAttribute("product");
        if(product == null) {
            product = new Product();
        }
        for(MultipartFile multipartFile : image) {
            if (!multipartFile.isEmpty()) {
                try {


                    saveFile(filePath, multipartFile);
                    imagePath.add("/resources/image/" + multipartFile.getOriginalFilename());

                } catch (IOException e) {

                } catch (Exception e) {

                }
            }
        }
        //set request parameters to user
        setRequestParametersToProduct(product, request);
        String option = (String) request.getSession().getAttribute("option");
        if (option.equals("edit")) {
            formSubmissionEditMode(request, product, imagePath);

            List<Product> products = productManager.getAllProducts();
            request.getSession().setAttribute("products", products);
            return "products";

        } else {
            formSubmissionAddMode(product, imagePath);
            List<Product> products = productManager.getAllProducts();
            request.getSession().setAttribute("products", products);
            return "products";
        }
    }


    private String saveFile(String uploadPath, MultipartFile image) throws IOException {


        String fileName = image.getOriginalFilename();
        String filePath = null;


        //create file path
        filePath = uploadPath + File.separator + fileName;
        File storeFile = new File(filePath);

        // saves the file on disk
        FileUtils.writeByteArrayToFile(storeFile, image.getBytes());
        return filePath;
    }

    private void setRequestParametersToProduct(Product product, HttpServletRequest request) {
        String name = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        double shippingPrice = Double.parseDouble(request.getParameter("shippingPrice"));
        String color = request.getParameter("color");
        int categoryId = Integer.parseInt(request.getParameter("category"));

        product.setName(name).setDescription(color).
                setPrice(price).setCategory(categoryManager.getCategoryByID(categoryId)).
                setShippingPrice(shippingPrice);

        //finding the main parrent categoryid of the selected category...
        Category category = categoryManager.getCategoryByID(categoryId);
        category = categoryManager.getCategoryByID(category.getParentID());

        //getting all size options that correspond to the given parent id...
        List<Size> sizes = sizeManager.getSizesByCategoryId(category.getParentID());
        //creating sizeId list for setting to product's size list...
        Map<String, Integer> sizeOptionQuantity = new HashMap<>();
        for (int i = 0; i < sizes.size(); i++) {

            //  String str = i + sizes.get(i).getSizeOption();
            String sizeOption = request.getParameter("sizeoption" + i);
            if (!request.getParameter("quantity" + i).equals("")) {
                int quantity = Integer.parseInt(request.getParameter("quantity" + i));

                sizeOptionQuantity.put(sizeOption, quantity);
                // product.getSizeId().add(Integer.parseInt(sizeOptionId));
                //  int sizeId = sizeManager.getSizeIdBySizeOptionAndQuantity(sizeOption, quantity);

            }
        }
        product.setSizeOptionQuantity(sizeOptionQuantity);
    }


    private void formSubmissionEditMode(HttpServletRequest request, Product product, List<String> filePath) {
        Product oldProduct = (Product) request.getSession().getAttribute("product");
        oldProduct.setName(product.getName()).setPrice(product.getPrice()).
                setShippingPrice(product.getShippingPrice()).
                setDescription(product.getDescription()).
                setCategory(product.getCategory()).
                setSizeOptionQuantity(product.getSizeOptionQuantity());
        productManager.updateProduct(product);
        for(String path : filePath) {
            if (!path.equals("")) {
                Media media;
                // for(int i=0; i<fileNames.size(); i++) {
                media = new Media();
                media.setProductID(oldProduct.getProductID()).setMediaPath(path);
                mediaManager.insertMedia(media);
            }
        }
    }

    private void formSubmissionAddMode(Product product, List<String> filePath) {
        int id = productManager.createNewProduct(product);
        Set<Map.Entry<String, Integer>> set = product.getSizeOptionQuantity().entrySet();
        for (Map.Entry<String, Integer> entry : set) {
            productManager.setSizes(product.getProductID(), entry.getKey(), entry.getValue());
        }
       for(String path : filePath) {
           Media media = new Media();
           media.setProductID(id).setMediaPath(path);
           mediaManager.insertMedia(media);
       }
        //   }
    }

    @RequestMapping("/products")
    public String getAllProducts(HttpServletRequest request) {
        List<Product> products = productManager.getAllProducts();
        Category category;
        for (Product product : products) {
            category = product.getCategory();
            category.setName((categoryManager.getCategoryByID(category.getParentID())).getName() + ": " + category.getName());
            product.setCategory(category);
        }
        request.getSession().setAttribute("products", products);
        return "products";
    }

    @RequestMapping("/categories")
    public String getAllCategories(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        request.setAttribute("categories", categories);
        return "categories";
    }
    @RequestMapping("/addCategory")
    public String getaddCategoryPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();

        Category category = new Category();
        String option = (String) request.getParameter("option");
        category.setName(" ");
        request.getSession().setAttribute("category", category);
        request.setAttribute("categories", categories);

        request.getSession().setAttribute("option", option);
        return "addCategory";
    }
    @RequestMapping("/saveCategory")
    public String saveCategory(HttpServletRequest request){
        Category category = (Category) request.getSession().getAttribute("category");
        String option = (String) request.getSession().getAttribute("option");
        String name = request.getParameter("categoryName");
        int parentId = Integer.parseInt(request.getParameter("category"));
        //add option...
        if(!option.equals("edit")) {
            category = new Category();
            category.setName(name).setParentID(parentId);
            categoryManager.createNewCategory(category);
        }
        //edit option...
        else{

            category.setName(name).setParentID(parentId);
            categoryManager.updateCategory(category);

        }
        List<Category> categories = categoryManager.getAllCategories();
        request.setAttribute("categories", categories);
        return "categories";
    }
    @RequestMapping("/editCategory")
    public String getEditCategoryPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        String[] checkedValues = (request.getParameterValues("category"));
        String option = (String) request.getParameter("option");

        if (option.equals("delete")) {

            for (int j = 0; j < checkedValues.length; j++) {
                categoryManager.deleteCategory(Integer.parseInt(checkedValues[j]));

            }
            List<Category> categories1 = categoryManager.getAllCategories();
            request.setAttribute("categories", categories1);
            return "categories";
        }
        else{

            Category category = categoryManager.getCategoryByID(Integer.parseInt(checkedValues[0]));

            request.getSession().setAttribute("category", category);

            request.setAttribute("categories", categories);

            request.getSession().setAttribute("option", option);

        }
        return "addCategory";
    }
    @RequestMapping("/allOrders")
    public String getAllOrders(HttpServletRequest request){

        List<Sale> orders = salesManager.getAllSales();
        request.setAttribute("orders", orders);

        return "allOrders";
    }
    @RequestMapping("/changeStatus")
    @ResponseBody
    public String changeStatus(HttpServletRequest request){

        int saleId = Integer.parseInt(request.getParameter("saleId"));
        String status = request.getParameter("status");

        salesManager.updateSaleStatus(saleId, status);

        return "status is changed";
    }
    @RequestMapping("/getDiscountPage")
    public String getDiscountPage(HttpServletRequest request){


        String[] checkedValues = (request.getParameterValues("product"));

        request.getSession().setAttribute("checkedValues", checkedValues);
        return "discountPage";

    }
    @RequestMapping("/makeDiscount")
    public String makeDiscount(HttpServletRequest request){

        int discount = Integer.parseInt(request.getParameter("discount"));
        String[] checkedValues = (String[])request.getSession().getAttribute("checkedValues");

        for (int j = 0; j < checkedValues.length; j++) {
            productManager.updateSaleField(Integer.parseInt(checkedValues[j]), discount);
        }

        List<Product> products = productManager.getSaledProducts();

        request.setAttribute("products", products);

            return "saledProducts";

        }
        @RequestMapping("/sale")
    public String sale(HttpServletRequest request){
           List<Product> products = productManager.getSaledProducts();
for(Product product : products){

}
            request.setAttribute("products", products);
            return "saledProducts";
        }

}

