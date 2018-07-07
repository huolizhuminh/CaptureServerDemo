package com.minhui.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet2 extends HttpServlet {
	public static final String VPN_START_TIME = "VPN_Start_Time";
	public static final String APPLICATION = "App_Name";
	public static final String IP_AND_PORT = "IPAndPort";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			
			boolean isQualified = ServletFileUpload.isMultipartContent(request);
			
			if(!isQualified){
				
				request.setAttribute("message", "invalid format");
				response.setStatus(400);
				return;
			}
			
			String[] extensions = {".txt",".jpg","png",".avi",".rmvb",".doc",".zip"};
			

			DiskFileItemFactory factory = new DiskFileItemFactory();

			factory.setSizeThreshold(1024*1024*2);  
			
			String tmpPath = getServletContext().getRealPath("/tmp");
			
			factory.setRepository(new File(tmpPath));
			
			ServletFileUpload uploader = new ServletFileUpload(factory);

			uploader.setHeaderEncoding("UTF-8");
			
			uploader.setFileSizeMax(1024*1024*2000);  
			
			uploader.setSizeMax(1024*1024*2000); 
			
			
			uploader.setProgressListener(new ProgressListener() {
				
				public void update(long pBytesRead, long pContentLength, int pItems) {
					
					System.out.println("pItems:" + pItems+"sum:" + pContentLength+",reade"+pBytesRead+"byte");
					
				}
			});
			
			List<FileItem> list = uploader.parseRequest(request);
             int fileNum=0;
			for (FileItem fileItem : list) {
				
				if(fileItem.isFormField()){
					
					String fieldName = fileItem.getFieldName();
					String fieldValue = fileItem.getString("UTF-8");
					
					System.out.println(fieldName+" = " +fieldValue);
					
				}else{
					
					String fileName = fileItem.getName();
					
					
					
					int lastIndexOf = fileName.lastIndexOf("\\");
					
					if(lastIndexOf!=-1){
						
						fileName = fileName.substring(lastIndexOf+1);
					}
					
					System.out.println("filename:" + fileName);
					
					int index = fileName.lastIndexOf(".");
					
					String ext = fileName.substring(index);  
					
					String exts = Arrays.toString(extensions); //  [".txt",".jpg","png",".avi",".rmvb",".doc"]
					
					if(!exts.contains(ext)){
						
						request.setAttribute("message", "invalid file format");
						request.getRequestDispatcher("/upload.jsp").forward(request, response);
						return;
					}
					
					InputStream in = fileItem.getInputStream();
					
					//String uniqueFileName = generateUUIDName(fileName);
					
					
					
					String	savepath = generateSavePath(request,index);
					File pathFile = new File(savepath);
					if(!pathFile.exists()){
						pathFile.mkdirs();
					}
					OutputStream out = new FileOutputStream(new File(pathFile,fileName));
					
					int len=0;
					byte[] buf = new byte[1024];
					while((len=in.read(buf))>0){
						out.write(buf, 0, len);
					}
					
					in.close();
					out.close();
					
					fileItem.delete();
					fileNum++;
					
				}
			}
		} catch (FileUploadBase.FileSizeLimitExceededException e) {  
			
			request.setAttribute("message", "no bigger than 100M");
			response.setStatus(400);
			return;
			
		}catch (FileUploadBase.SizeLimitExceededException e) {  
		
			request.setAttribute("message", "no bigger than 100M ");
			response.setStatus(400);
			return;
			
		}catch (FileUploadException e) {
			e.printStackTrace();
		}
	}

	

	private String generateSavePath(HttpServletRequest request,int index) {
		String savepath = getServletContext().getRealPath("/upload");
		String vpnStart=request.getHeader(VPN_START_TIME);
		String appName=request.getHeader(APPLICATION);
		String ipAndPort=request.getHeader(IP_AND_PORT);
		String path=savepath+"/"+vpnStart+"/"+appName+"/"+ipAndPort+"/"+index;
		return path;
	}
	




	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
