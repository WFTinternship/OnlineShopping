package com.workfront.internship.controller;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.business.SizeManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.Size;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        request.getSession().setAttribute("categories", categories);
        request.getSession().setAttribute("categoryMap", categoryMap);
        request.getSession().setAttribute("sizeMap", sizeMap);
        request.getSession().setAttribute("option", option);
        return "addProduct";
    }

    @RequestMapping("/edit")
    public String getEditProductPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        String[] checkedValues = (request.getParameterValues("product"));
        String option = (String) request.getParameter("option");
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
        } else {

            Product product = productManager.getProduct(Integer.parseInt(checkedValues[0]));
            Map<String, Integer> sizeOptionQuantity = productManager.getSizeOptionQuantityMap(product.getProductID());
            request.getSession().setAttribute("product", product);
            request.getSession().setAttribute("sizeOptionQuantity", sizeOptionQuantity);
            request.getSession().setAttribute("categories", categories);
            request.getSession().setAttribute("categoryMap", categoryMap);
            request.getSession().setAttribute("sizeMap", sizeMap);
            request.getSession().setAttribute("option", option);
            return "addProduct";
        }
    }

    @RequestMapping("/saveProduct")
    public String saveProduct(HttpServletRequest request,
                              @RequestParam(value = "file", required = false) MultipartFile image) throws IOException {

        String filePath = request.getSession().getServletContext().getRealPath("/resources/image");


        String imagePath = "";
        Product product = (Product) request.getSession().getAttribute("product");
        if(product == null) {
            product = new Product();
        }
        if (!image.isEmpty()) {
            try {


                saveFile(filePath, image);
                imagePath = "/resources/image/" + image.getOriginalFilename();

            } catch (IOException e) {

            } catch (Exception e) {

            }
        }
        //set request parameters to user
        setRequestParametersToProduct(product, request);
        String option = (String) request.getSession().getAttribute("option");
        if (option.equals("edit")) {
            formSubmissionEditMode(request, product);

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


    private void formSubmissionEditMode(HttpServletRequest request, Product product) {
        Product oldProduct = (Product) request.getSession().getAttribute("product");
        oldProduct.setName(product.getName()).setPrice(product.getPrice()).
                setShippingPrice(product.getShippingPrice()).
                setDescription(product.getDescription()).
                setCategory(product.getCategory()).
                setSizeOptionQuantity(product.getSizeOptionQuantity());
        productManager.updateProduct(product);

    }

    private void formSubmissionAddMode(Product product, String filePath) {
        int id = productManager.createNewProduct(product);
        Set<Map.Entry<String, Integer>> set = product.getSizeOptionQuantity().entrySet();
        for (Map.Entry<String, Integer> entry : set) {
            productManager.setSizes(product.getProductID(), entry.getKey(), entry.getValue());
        }
        Media media;
        // for(int i=0; i<fileNames.size(); i++) {
        media = new Media();
        media.setProductID(id).setMediaPath(filePath);
        mediaManager.insertMedia(media);

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

}

