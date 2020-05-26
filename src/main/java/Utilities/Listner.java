package Utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Listner implements ITestListener{
	
	//Report Constants  Declarations
		private static final String FILE_NAME = "AutomationReport.html";
		private static final String DOCUMENT_TITLE="Automation Report";
		public static final String REPORT_PATH=System.getProperty("user.dir")+"/TestAutomationReport/";
		
		//Extent Report Declarations
	    public  static ExtentSparkReporter sparkReporter;
	    public static ExtentReports extent=createInstance();
	    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
		
	    public static ExtentTest extentTest;
	    public static String destination;
	    
	    public static WebDriver driver;
	    
	    //Create an extent report instance
	    public static ExtentReports createInstance() 
	    {
	        sparkReporter=new ExtentSparkReporter(REPORT_PATH+FILE_NAME);
	    	sparkReporter.config().setDocumentTitle(DOCUMENT_TITLE);
	    	sparkReporter.config().setReportName(FILE_NAME);
	    	sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.STANDARD);
	    	sparkReporter.config().setCSS(".r-img { width: 70%; }");
	    	extent = new ExtentReports();
	    	
	    	extent.attachReporter(sparkReporter);
	    	extent.setSystemInfo("Application Name","Test");
	    	extent.setSystemInfo("Platform",System.getProperty("os.name"));
	    	extent.setSystemInfo("Environment","QA");
	 
	        return extent;
	    }

		
	   //ITestListener overriden methods
	    @Override
	    public synchronized void onStart(ITestContext context) {
	        System.out.println("Extent Reports Version 4.1.3 Test Suite started!");
	    }
	 
	    @Override
	    public synchronized void onFinish(ITestContext context) {
	        System.out.println(("Extent Reports Version 4.1.3 Test Suite is ending!"));
	        extent.flush();
	    }
	 
	    @Override
	    public synchronized void onTestStart(ITestResult result) {
	        System.out.println((result.getMethod().getMethodName() + " started!"));
	        extentTest = extent.createTest(result.getMethod().getMethodName(),result.getMethod().getDescription());
	        test.set(extentTest);
	    }
	 
	    @Override
	    public synchronized void onTestSuccess(ITestResult result) {
	        System.out.println((result.getMethod().getMethodName() + " passed!"));
	        test.get().pass("Test passed");
	    }
	 
	    @Override
	    public synchronized void onTestFailure(ITestResult result) {
	        System.out.println((result.getMethod().getMethodName() + " failed!"));
	        test.get().fail(result.getThrowable());
	        
	        
	        try {
	        	String screenshotPath = Listner.getScreenhot(driver,result.getName());
				//extentTest.addScreenCaptureFromPath(screenshotPath);
				extentTest.addScreenCaptureFromPath(screenshotPath);
				//extentTest.addScreenCaptureFromBase64String(screenshotPath);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        driver.quit();
	        
	    }
	 
	    @Override
	    public synchronized void onTestSkipped(ITestResult result) {
	        System.out.println((result.getMethod().getMethodName() + " skipped!"));
	        test.get().skip(result.getThrowable());
	    }
	 
	    @Override
	    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	        System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
	    }
	   
	    public static String getScreenhot(WebDriver driver,String screenshotName) throws Exception {
	    	
	    	
	    	 String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	    	 File source  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    	 //after execution, you could see a folder "FailedTestsScreenshots" under src folder
	    	 destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/"+screenshotName+dateName+".png";
	    	 File finalDestination = new File(destination);
	    	 FileUtils.copyFile(source, finalDestination);
	    	 
	    	 
	    	 //This is used to send to report other user then we need to conver that screenshot to base64
	    	  //InputStream is = new FileInputStream(destination);
	    	    // byte[] imageBytes = IOUtils.toByteArray(is);
	    	     //String base64 = Base64.getEncoder().encodeToString(imageBytes);
	    	//extentTest.log( "Snapshot below: " + extent.addBase64ScreenShot("data:image/png;base64,"+base64));   
	    	    // return base64;
	    	     return destination;
	    	
	    	 }
	    
	   

}
