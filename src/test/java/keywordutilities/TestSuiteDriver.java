package keywordutilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import common.Constants;

public class TestSuiteDriver implements Constants{
	//private static Logger log = Logger.getLogger(TestSuiteDriver.class);

	public static void main(String[] args) throws IOException {

		TestNG tng = new TestNG();
		XmlSuite suite = new XmlSuite();
		suite.setName("Default Suite");
		suite.setParallel("methods");
		suite.setDataProviderThreadCount(THREAD_COUNT);
		XmlTest test = new XmlTest(suite);
		test.setName("Default Test");
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass("keywordutilities.TestDriver"));
		test.setXmlClasses(classes);
		List<XmlTest> tests = new ArrayList<XmlTest>();
		 tests.add(test);
		 suite.setTests(tests);
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		TestListenerAdapter tla = new TestListenerAdapter();
		tng.setXmlSuites(suites);
		tng.addListener(tla);
		tng.run(); ;

	}

	
}
