Selenword : Keydriven framework using selenium
----------------------------------------------

Selenword is divided into three parts :
1. Test Scripts: Test scripts are stored in excel sheet in .xls format.
2. Test Driver: Test scripts are triggered using Java and TestNG class.
3. Report : Test report is generated after the execution of all tests. 

#### **Features** : #

 - Create an automated test scripts with the knowledge of keywords and HTML locators.
 - Supported browsers : Firefox, GoogleChrome, Internet Explorer and Safari.
 - Supports Multi-Threaded execution over remote machines or local.
 - HTML report with timestamp, execution time, host name and steps to debug for any failed tests.
 - Screenshots for failed tests.

#### **Pre - Requisites** ##

 - JDK 1.6 or higher
 - Maven 3.3.1 or higher
 - xls format editor.

#### **Preparation Test Suite and Test Cases** ##

Sample excel can be found [here](https://github.com/Wehardcoderz/Selenword/blob/master/TestCase.xls).

 - First sheet is the test suite where all details of the test cases are listed.
 - We call first sheet as "TestSuite" and name should remain unchanged.
 - Test cases listed under "TestName" column should have a sheet name in the same name for easier mapping of test suite with test cases.

TestSuite Headers :

 - **SlNo** : Count in ascending order.
 - **TestName** : Test case name which has to be included in the test suite.
 - **Test Description** : Description of the test case.
 - **Enabled** : Exclude test case as part of regression if set to "False". By default it is taken as "True". 

TestCase Headers :

 - **SlNo** : Count in ascending order.
 - **Description** : Description of the test step.
 - **Command** : Keywords for the action you want to perform. For eg. click, type etc. Refer the list of keywords here.
 - **Target** : Key for the Locator where action to be performed. keys are stored as Object Repository in key = Locator>value notation. For Eg. LoginButton = ID>vuforiaLogin. Object repository file is found [here](https://github.com/Wehardcoderz/Selenword/blob/master/src/main/java/resources/objectrepository.properties).
 - **Value** : Value to be passed on to the locator. For Eg. Enter text to text box, File location to upload files etc
 - **Index** : Index of an element. Sometimes page has more than one elements with the same locator and index of that element can be provided. By default index is "0" which means first returned element in the page.

#### **Setting up test suite run properties** ##

Make sure config.properties file is filled with all the details required to execute regression. config.properties file is found [here](https://github.com/Wehardcoderz/Selenword/blob/master/src/main/java/resources/config.properties).

You need to put the chrome and internet explorer driver into resource folder. 

 - For any information on Internet Explorer Driver, click [here](https://code.google.com/p/selenium/wiki/InternetExplorerDriver)
 - For any information on Chrome Driver, click [here](https://code.google.com/p/selenium/wiki/ChromeDriver).
 - For any information on Safari Driver, click [here](https://code.google.com/p/selenium/wiki/SafariDriver).
 - For any information on Opera Driver, click [here](https://code.google.com/p/selenium/wiki/OperaDriver).

#### **Running your Selenword Test** ##

Once all the configurations and test scripts are done, you can kick start your test suite run. Here is how

 - Open cmd/terminal and navigate to the folder where Selenword is downloaded.
 - Run this command "mvn exec:java-P"SelenwordTest".
 - Report will be generated under "SelenwordReports" folder with the time stamp, host name, total passed, failed and skipped, total time taken and steps to debug if any test failed. kool right!
 - Still dint get where the failure is? you can see the screenshots at "screenshot" folder.


----------
#### **Contributing** ##

See [CONTRIBUTING.md](https://github.com/Wehardcoderz/Selenword/blob/master/CONTRIBUTING.md) for info on working on Selenword and sending patches.

Contact 
E : vish.shady@gmail.com

