package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Scope("session")
@RequestMapping("/app/export")
public class ExportController {

	
	@Autowired
	private SessionHolder sessionHolder;
	
	@Autowired
	private VocMapperSerializer serializer;
	
	@RequestMapping("/csv")
	public void exportResult(
			@RequestParam(value="delimiter", required=false, defaultValue=",") String delim,
			@RequestParam(value="encoding", required=false, defaultValue="utf-8") String encoding,
			HttpServletResponse resp) throws Exception {
		if (sessionHolder.getQueryQueue()==null)
			throw new Exception("No query queue in session.");
		
		char delimiter = delim.charAt(0);
		
		String csv = serializer.getQueryQueueFactory()
				.exportResult(sessionHolder.getQueryQueue(), delimiter);

		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/csv");
		resp.setCharacterEncoding(encoding);
		resp.setHeader("Content-Disposition", "attachment;filename=result.csv");
		Writer writer = null;
		try {
			writer = resp.getWriter();
			writer.write(csv);
		} finally {
			if (writer!=null) {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
		}
		
	}
	
}
