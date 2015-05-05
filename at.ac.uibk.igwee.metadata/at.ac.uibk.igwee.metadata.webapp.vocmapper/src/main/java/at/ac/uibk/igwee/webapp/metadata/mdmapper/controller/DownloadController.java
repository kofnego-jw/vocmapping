package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;

@Controller
@RequestMapping("/app/download")
@Scope("session")
public class DownloadController {

	@Autowired
	private SessionHolder sessionHolder; 
	
	@Autowired
	private VocMapperSerializer serializer;
	
	@RequestMapping("/progress")
	public void downloadProgress(HttpServletResponse response) throws Exception {
		
		
		
		QueryQueue qq = sessionHolder.getQueryQueue();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		serializer.getQueryQueueFactory().writeQueryQueue(qq, baos);
		
		byte[] content = baos.toByteArray();
		int size = content.length;
		
		response.setHeader("Content-Disposition", "attachment; filename=queryQueue.xml");
		response.setHeader("Content-Length", Integer.toString(size));
		
		ServletOutputStream os = null;
		
		try {
			os = response.getOutputStream();
			IOUtils.write(content, os);
		} finally {
			if (os!=null) {
				try {
					os.close();
				} catch (Exception e) {
					// ignored
				}
			}

		}
		
		
	}
	
	
}
