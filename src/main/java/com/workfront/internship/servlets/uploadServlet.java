package com.workfront.internship.servlets;

import com.workfront.internship.common.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * Created by annaasmangulyan on 9/28/16.
 */
public class uploadServlet {
	public void saveProduct(HttpServletResponse response, HttpServletRequest request) throws IOException {

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
		List<String> fileName = new ArrayList<>();
		int j =0;
		String fName=null;


		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()) {
					fName=fi.getName();
					fileName.add(fName);
					if (fName.lastIndexOf("\\") >= 0) {
						file = new File(filePath +
							fName.substring(fName.lastIndexOf("\\")));
					} else {
						file = new File(filePath +
							fName.substring(fName.lastIndexOf("\\") + 1));
					}
					fi.write(file);
					out.println("Uploaded Filename: " + fName + "<br>");
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



	}
}
