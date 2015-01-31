package common;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.KeyStore.Entry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.rendersnake.HtmlCanvas;
import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

import static org.rendersnake.HtmlAttributesFactory.*;

public class CustomReport implements Constants {
	private static SimpleDateFormat sdftime = new SimpleDateFormat();
	private static String timeZone = "GMT-7";
	static HtmlCanvas html = new HtmlCanvas();

	public void startReport() throws IOException {

		html.html().head();
		writeStyleSheet();
		html._head().title().write("Selenword Report")._title();
		html.body().h1().write("Test Report")._h1();
		html.div(class_("metadata")).table(width("420")).tbody().tr()
				.td(class_("em")).write("Report Title")._td().td()
				.write(REPORT_TITLE)._td()._tr().tr().td(class_("em"))
				.write("Date")._td().td().write(getDateAsString())._td()._tr()
				.tr().td(class_("em")).write("Host")._td().td()
				.write("Vish Machine")._td()._tr()._tbody()._table()._div();

		html.div(class_("summary")).h2().write("Execution Summary")._h2();
		html.table().tbody().tr(class_("tableHeader")).th().write("Test Suite")
				._th().th().write("Total")._th().th().write("Passed")._th()
				.th().write("Skipped")._th().th().write("Failed")._th().th()
				.write("Passed %")._th()._tr();
	}

	private String getDateAsString() {
		Date date = new Date();
		sdftime.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdftime.format(date);
	}

	public void finishReport() throws IOException {
		Writer write = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("filename.html"), "utf-8"));
		write.write(html.toHtml());
		write.close();
	}

	public void writeExecutionSummary(ITestContext testContext)
			throws IOException {
		int passed = testContext.getPassedTests().size()
				+ testContext.getFailedButWithinSuccessPercentageTests().size();
		int skipped = testContext.getSkippedTests().size();
		int failed = testContext.getFailedTests().size();
		int total = passed + skipped + failed;
		html.tr(class_("tableHeader")).td().a(href("#Detail"))
				.write(REPORT_TITLE)._a()._td().td().write(total)._td()
				.td(class_("passed number")).write(passed)._td()
				.td(class_("skipped number")).write(skipped)._td()
				.td(class_("failed number")).write(failed)._td();
		html.td().write(passedPercentage(passed, total))._td()._tr()._tbody()
				._table()._div();
	}

	public void writeExecutionDetail(LinkedHashMap<String, String> testPassed,
			LinkedHashMap<String, Object[]> testFailed,
			LinkedHashMap<String, Object[]> testSkipped,LinkedHashMap<String,String> testLogs) throws IOException {
		int count = 1;
		LinkedHashMap<String, String> passed = testPassed;
		LinkedHashMap<String,Object[]> failed = testFailed;
		LinkedHashMap<String, Object[]> skipped = testSkipped;
		html.div(class_("summary")).h2().write("Execution Detail")._h2();
		html.table(border("1")).tbody().tr(class_("tableHeader")).th()
				.a(name("Detail")).write("#")._a()._th().th()
				.write("Test Name")._th().th().write("Description")._th().th()
				.write("Result")._th()._tr();
		for (Map.Entry<String, String> entry : passed.entrySet()) {
			html.tr(class_("passed number")).td().write(count++)._td()
					.td(class_("methodn")).a(href("#"+entry.getKey())).write(entry.getKey())._a()._td().td()
					.write(entry.getValue())._td().td()
					.write("Passed")._td()._tr();
		}
		Set<String> keyset_s = skipped.keySet();
		for (String key : keyset_s) {
			Object[] o = failed.get(key);
			html.tr(class_("skipped number")).td().write(count++)._td()
					.td(class_("methodn")).a(href("#"+key)).write(key)._a()._td().td()
					.write(o[0].toString())._td().td()
					.write(o[1].toString())._td()._tr();
		}
		 Set<String> keyset_f = failed.keySet();
		for (String key : keyset_f) {
			Object[] o = failed.get(key);
			html.tr(class_("failed number")).td().write(count++)._td()
					.td(class_("methodn")).a(href("#"+key)).write(key)._a()._td().td()
					.write(o[0].toString())._td().td()
					.write(o[1].toString())._td()._tr();
		}

		html._tbody()._table()._div();
		messageCollector(testLogs);
		html._body()._html();
	}
	
	public void messageCollector(LinkedHashMap<String,String> testLogs) throws IOException {
		LinkedHashMap<String, String> messages = testLogs;
		html.div(class_("summary")).h2().write("Test Logs")._h2();
		Set<java.util.Map.Entry<String,String>> keyset = messages.entrySet();
		for(java.util.Map.Entry<String, String> key : keyset){
			html.table(border("1")).tbody().tr(class_("tableHeader")).th().a(name(key.getKey())).write(key.getKey())._a()._th()._tr().tr().td().write(key.getValue())._td();
			html._tr()._tbody()._table();	
		}
		html._div();
	}

	public String passedPercentage(Integer passed, Integer total) {
		String s = null;
		try {
			s = Float.toString((float) (passed * 100 / total));
		} catch (Exception e) {
			s = e.toString();
		}
		return s;
	}

	public void writeStyleSheet() throws IOException {
		html.style(type("text/css"))
				.write("body {font-family: Arial,sans-serif;margin: 20px 20px 20px 30px;background-color: #b0c4de;}h1, h2, h3 {font-weight: bold;}h1 { background-color: #557799; border-radius: 10px; box-shadow: 3px 3px 4px #aaa;color: white;"
						+ "padding: 10px;text-align: center;text-shadow: 2px 2px 2px black;width: 400px;align : center;}h2 {border-top: 5px solid lightgray; font-size: 150%;margin-top: 40px;padding-top: 5px;}h3 {margin-left: 10px;margin-top: 30px;}"
						+ ".tableHeader {font-weight: bold;}.number {text-align: center;}.priority1, .priority2, .priority3 {color: #990000;font-weight: bold;text-align: center;}table {border: 2px solid gray;border-collapse: collapse;box-shadow: 3px 3px 4px #aaa;"
						+ "}td, th {border: 1px solid #d3d3d3;padding: 4px 20px;}th {text-shadow: 2px 2px 2px white;}th {background-color: #ddddff;border-bottom: 1px solid gray;}em, .em {font-weight: bold;}.number {text-align: right;}.passed {background-color: #44aa44;}"
						+ ".skipped {background-color: #ffaa00;}.failed {background-color: #ff4444;}.methodn{color: #006699;}")
				._style();
	}
}
