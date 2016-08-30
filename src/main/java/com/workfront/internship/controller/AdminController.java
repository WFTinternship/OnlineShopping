package com.workfront.internship.controller;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.MediaManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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

    @RequestMapping("/admin")
    public String getAdminPage() {
        return "admin";
    }

    @RequestMapping("/add")
    public String getaddProductPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        Product product = new Product();
        Category category = new Category();
        String option = (String)request.getParameter("option");
        product.setName(" ").setPrice(0.0).setShippingPrice(0.0).setDescription(" ").setCategory(category);
        request.getSession().setAttribute("product", product);
        request.getSession().setAttribute("categories", categories);
        request.getSession().setAttribute("option", option);
        return "addProduct";
    }
    @RequestMapping("/edit")
    public String getEditProductPage(HttpServletRequest request) {
        List<Category> categories = categoryManager.getAllCategories();
        int productId = Integer.parseInt(request.getParameter("product"));
        String option = (String)request.getParameter("option");
        Product product = productManager.getProduct(productId);

        if(option.equals("delete")){
            productManager.deleteProduct(productId);

            List<Product> products = (List<Product>)request.getSession().getAttribute("products");

            for (int i=0; i<products.size(); i++) {
                if (products.get(i).getProductID() == productId) {
                    products.remove(products.get(i));

                }
            }
            /* List<Product> products = productManager.getAllProducts();
            Category category=null;
            for(Product product1 : products){
                category = product1.getCategory();
                category.setName((categoryManager.getCategoryByID(category.getParentID())).getName() + ": " + category.getName());
                product.setCategory(category);
            }*/
            request.getSession().setAttribute("products", products);
            return "products";
        }

        request.getSession().setAttribute("product", product);
        request.getSession().setAttribute("categories", categories);
        request.getSession().setAttribute("option", option);
        return "addProduct";
    }
    @RequestMapping("/saveProduct")
    public String saveProduct(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String filePath = "C:\\Users\\Workfront\\IdeaProjects\\OnlineShop\\OnlineShop\\src\\main\\webapp\\resources\\image";
        int maxFileSize = 140 * 1024;

        File file;

        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);
        String name="";
        Double price=0.0;
        Double shippingPrice=0.0;
        String color="";
        int categoryId=0;
        String[] fileName = new String[3];
        int j =0;


        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
                    fileName[j] = fi.getName();
                    // Write the file

                    System.out.println(fileName[j].lastIndexOf("\\"));
                    if (fileName[j].lastIndexOf("\\") >= 0) {
                        file = new File(filePath +
                                fileName[j].substring(fileName[j].lastIndexOf("\\")));
                    } else {
                        file = new File(filePath +
                                fileName[j].substring(fileName[j].lastIndexOf("\\") + 1));
                    }
                    fi.write(file);
                    out.println("Uploaded Filename: " + fileName[j] + "<br>");
                    j++;

                }
                else{
                    String paramName=fi.getFieldName();
                    String value=fi.getString();
                    if(paramName.equals("productName"))
                        name=value;
                    if(paramName.equals("price"))
                        price= Double.parseDouble(value);
                    if(paramName.equals("shippingPrice"))
                        shippingPrice=Double.parseDouble(value);
                    if(paramName.equals("color"))
                        color=value;
                    if(paramName.equals("category"))
                        categoryId=Integer.parseInt(value);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        Category category = categoryManager.getCategoryByID(categoryId);
        if(request.getSession().getAttribute("option").equals("edit")){
            Product product = (Product)request.getSession().getAttribute("product");
            product.setName(name).setPrice(price).setShippingPrice(shippingPrice).setDescription(color).setCategory(category);
            productManager.updateProduct(product);
            return "admin";
        }
        Product product = new Product();
        product.setName(name).setPrice(price).setShippingPrice(shippingPrice).setDescription(color).setCategory(category);

        int id = productManager.createNewProduct(product);
        Media media=null;
        for(int i=0; i<2;i++) {
            media = new Media();
            media.setProductID(id).setMediaPath("/resources/image/" + fileName[i]);
            mediaManager.insertMedia(media);
        }
        return "admin";


    }
    @RequestMapping("/products")
    public String getAllProducts(HttpServletRequest request){
        List<Product> products = productManager.getAllProducts();
        Category category=null;
        for(Product product : products){
            category = product.getCategory();
            category.setName((categoryManager.getCategoryByID(category.getParentID())).getName() + ": " + category.getName());
            product.setCategory(category);
        }
        request.getSession().setAttribute("products", products);
        return "products";
    }
    @RequestMapping("/categories")
    public String getAllCategories(HttpServletRequest request){
        List<Category> categories = categoryManager.getAllCategories();
        request.setAttribute("categories", categories);
        return "categories";
    }

}

