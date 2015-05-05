package at.ac.uibk.igwee.webapp.metadata.mdmapper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import at.ac.uibk.igwee.metadata.metaquery.QueryQueue;
import at.ac.uibk.igwee.metadata.metaquery.QueryQueueFormat;

@Controller
@RequestMapping("/app/upload")
@Scope("session")
public class UploadController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UploadController.class);

	@Autowired
	private SessionHolder session;

	@Autowired
	private VocMapperSerializer serializer;

	@RequestMapping(value = "/xml", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void uploadXml(@RequestParam("file") MultipartFile file)
			throws Exception {

		LOGGER.debug("Upload XML called.");

		if (!file.isEmpty()) {
			try {
				QueryQueue qq = serializer.getQueryQueueFactory()
						.loadQueryQueue(file.getInputStream());
				session.setQueryQueue(qq);
				LOGGER.debug("New QueueQueue set to sessionHolder.");
				return;
			} catch (Exception e) {
				LOGGER.error("Cannot deserialize XML to QueryQueue.", e);
				throw e;
			}

		} else {
			throw new Exception("No file content.");
		}

	}

	@RequestMapping(value = "/excelcsv", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void uploadExcelCSV(@RequestParam("file") MultipartFile file,
			@RequestParam("encoding") String encoding) throws Exception {

		LOGGER.debug("Upload Excel CSV called.");

		if (!file.isEmpty()) {
			try {
				QueryQueue qq = serializer.getQueryQueueFactory()
						.loadQueryQueue(file.getInputStream(),
								QueryQueueFormat.EXCEL_CSV, encoding);
				session.setQueryQueue(qq);
				LOGGER.debug("New QueryQueue set to sessionHolder.");
				return;
			} catch (Exception e) {
				LOGGER.error("Cannot deserialize Excel CSV.", e);
				throw e;
			}
		}

		throw new Exception("No file content.");
	}
	
	@RequestMapping(value="/clear", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void clearProgress() throws Exception {
		session.clear();
	}

}
