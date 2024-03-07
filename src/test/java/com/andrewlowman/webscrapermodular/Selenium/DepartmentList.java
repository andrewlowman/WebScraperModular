package com.andrewlowman.webscrapermodular.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DepartmentList {
    WebDriver driver;


    public DepartmentList(WebDriver driver){
        this.driver = driver;
    }

    public List returnDepartmentList(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

        List<String> allDepartments = new ArrayList<>();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Select Some Options']")));

        //This is the department drop down search box
        WebElement deptSearch = driver.findElement(By.xpath("//input[@value='Select Some Options']"));

        //click the box to open list; it's what worked
        deptSearch.click();

        List<WebElement> tempList = driver.findElements(By.className("active-result"));

        for(int i = 0; i < tempList.size(); i++){
            allDepartments.add(tempList.get(i).getText());
        }

        //remove department all departments and all sio because they give an error
        allDepartments.remove(0);

        allDepartments.remove(0);

        return allDepartments;

    }
}
