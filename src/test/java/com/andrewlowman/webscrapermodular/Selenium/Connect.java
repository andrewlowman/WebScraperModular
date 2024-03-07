package com.andrewlowman.webscrapermodular.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class Connect {
    WebDriver driver;
    WebDriverWait wait;
    public Connect(WebDriver driver){
        this.driver = driver;
       wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void connectToSite(String site){
        driver.get(site);
    }

    public void connectDepartmentPage(String deptName){

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit']")));
        //this is the submit button
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));

        //department seach box
        WebElement deptInputBox = driver.findElement(By.id("faculty_department_name"));
        //need to click something to close drop down
        WebElement mailCodeBox = driver.findElement(By.id("faculty_mail_code"));

        mailCodeBox.click();

        deptInputBox.sendKeys(deptName);

        submitButton.click();
    }

    public ArrayList<String> readFromEmployeeDetailPage() throws IOException {

        ArrayList<String> info = new ArrayList();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("empName")));

        //employee name
        WebElement empName = driver.findElement(By.id("empName"));
        info.add(empName.getText());

        //employee title
        WebElement empTitle = driver.findElement(By.xpath("//label[text()='Title']/following-sibling::div[1]"));
        info.add(empTitle.getText());

        //employee department
        WebElement empDepartment = driver.findElement(By.xpath("//label[text()='Department']/following-sibling::div[1]"));
        //trim out chars that aren't allowed
        String deptNew = empDepartment.getText().replace("/","");

        //trim to 31 characters
        if(deptNew.length() > 31){
            deptNew = deptNew.substring(0,30);
        }
        info.add(deptNew);

        //employee location
        WebElement empLoc = driver.findElement(By.id("empLoc"));
        info.add(empLoc.getText());

        //employee mail code
        WebElement empMailCode = driver.findElement(By.xpath("//label[text()='Mail Code']/following-sibling::div[1]"));
        info.add(empMailCode.getText());


        return info;
    }

}
