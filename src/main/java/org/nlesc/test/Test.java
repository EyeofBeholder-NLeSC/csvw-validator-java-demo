package org.nlesc.test;

import com.malyvoj3.csvwvalidator.processor.CsvwProcessor;
import com.malyvoj3.csvwvalidator.config.ProcessorConfig;
import com.malyvoj3.csvwvalidator.processor.ProcessingContext;
import com.malyvoj3.csvwvalidator.processor.ProcessingSettings;
import com.malyvoj3.csvwvalidator.processor.result.ProcessingResult;
import com.malyvoj3.csvwvalidator.processor.result.TextResultWriter;
import com.malyvoj3.csvwvalidator.processor.result.RdfResultWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.malyvoj3.csvwvalidator.utils.UriUtils;
import java.io.File;
import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException{
		// tmp directory to download the csv file
		// otherwise the file won't be downloaded by apache-commons
		
		File file = new File("tmp");
		if (file.exists()) {
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		file.mkdir();
	
		// validate the tree-op test case
		// String schemaAbsoluteUrl = "https://w3c.github.io/csvw/tests/test011/tree-ops.csv-metadata.json";
		String schemaAbsoluteUrl = "https://github.com/EyeofBeholder-NLeSC/assessments-ontology/blob/fix-metadata/metadata.json";
		String result = Test.validate(null, schemaAbsoluteUrl, true);
		System.out.println(result);
	}

	private static String validate(String fileUrl, String schemaUrl, boolean isNotStrict) throws IOException {

		// Initialize csvw processor using Spring
		ApplicationContext ctx = new AnnotationConfigApplicationContext(ProcessorConfig.class);
		CsvwProcessor csvwProcessor = ctx.getBean(CsvwProcessor.class);
		TextResultWriter textWriter = ctx.getBean(TextResultWriter.class);
		RdfResultWriter rdfWriter = ctx.getBean(RdfResultWriter.class);
		
		// Call different processors depending on different input
		ProcessingResult processingResult = null;
		String fileAbsoluteUrl = getAbsoluteUrl(fileUrl);
		String schemaAbsoluteUrl = getAbsoluteUrl(schemaUrl);
		ProcessingSettings settings = new ProcessingSettings();
		settings.setStrictMode(!isNotStrict);
		ProcessingContext context = new ProcessingContext(settings);
		if (fileAbsoluteUrl != null && schemaAbsoluteUrl != null) {
			processingResult = csvwProcessor.process(context, fileAbsoluteUrl, schemaAbsoluteUrl);
		} else if (fileAbsoluteUrl != null) {
			processingResult = csvwProcessor.processTabularData(context, fileAbsoluteUrl);
		} else if (schemaAbsoluteUrl != null) {
			processingResult = csvwProcessor.processMetadata(context, schemaAbsoluteUrl);
		}
		
		// return string result
		byte[] textResult = textWriter.writeResult(processingResult);
		byte[] rdfResult = rdfWriter.writeResult(processingResult);
		FileUtils.writeByteArrayToFile(new File("rdf_result.ttl"), rdfResult);
		String result = new String(textResult);
		return result;
	}

	private static String getAbsoluteUrl(String url) {
		String absoluteUrl = null;
		if (url != null) {
			if (UriUtils.isValidUri(url)) {
				absoluteUrl = url;
			} else {
				File file = new File(url);
				if (file.exists() && !file.isDirectory()) {
					absoluteUrl = file.toURI().toString();
				}
			}
		}
		return absoluteUrl;
	}
}