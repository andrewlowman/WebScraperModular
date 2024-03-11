package com.andrewlowman.webscrapermodular;

import com.andrewlowman.webscrapermodular.Selenium.Connect;
import com.andrewlowman.webscrapermodular.Selenium.DepartmentList;
import com.andrewlowman.webscrapermodular.Utility.Excel;
import com.andrewlowman.webscrapermodular.Utility.FileIn;
import com.andrewlowman.webscrapermodular.Utility.FileOut;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame {
    private JPanel mainPanel;
    private JButton scrapeButton;
    private JButton excelButton;
    private JTextField departmentTextField;
    private JButton loadPreviousSessionButton;
    private JComboBox deptComboBox;
    private JButton exitButton;
    private JButton oneDepartmentButton;

    private File excelFile = null;
    private ArrayList<String> departments;

    //need this because combobox listener is triggering on load
    private boolean ifComboxBoxActive = false;

    private boolean ifExcelLoaded = false;
    WebDriver driver = new FirefoxDriver();

    Excel excel = new Excel();
    Connect connect = new Connect(driver);

    DepartmentList departmentList = new DepartmentList(driver);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    FileIn fileIn = new FileIn();
    FileOut fileOut = new FileOut();


    public Main(){
        departments = new ArrayList<>();

        //jframe stuff
        JFrame frame = new JFrame();
        frame.setContentPane(mainPanel);
        frame.setTitle("Web Scraper");
        frame.setSize(750,600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        connect.connectToSite("https://itsweb.ucsd.edu/directory/search");

        departments.addAll(departmentList.returnDepartmentList());

        for(String s: departments){
            deptComboBox.addItem(s);
        }

        ifComboxBoxActive = true;

        scrapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(ifExcelLoaded){

                    for(int i = 0; i < departments.size(); i++){
                        //connect to the department
                        connect.connectDepartmentPage(departments.get(i));

                        //counter for what page of results for depts is on
                        int ctr = 0;

                        //for loop
                        boolean good = true;

                        //loop through all the pages of the department
                        do{

                            //loop through employees on one page
                            try {
                                loopThroughEmployees(0,ctr,departments.get(i));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //check if the arrows for another page of results are present
                            boolean isPresent = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

                            if(isPresent){
                                WebElement nextPageArrow =  driver.findElement(By.id("DataTables_Table_0_next"));

                                boolean nextPageIsNotPresent = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

                                if(nextPageIsNotPresent == false){
                                    nextPageArrow.click();
                                    ctr++;
                                }else{
                                    good = false;
                                }

                            }else{
                                //driver.get("https://itsweb.ucsd.edu/directory/search");
                                good =false;
                            }

                        }while(good);

                        driver.navigate().back();

                        //need to clear form
                        WebElement clearButton = driver.findElement(By.id("resetme"));

                        clearButton.click();
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Pick an excel file");
                }

            }
        });

        deptComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(ifExcelLoaded && ifComboxBoxActive){

                    deptComboBox = (JComboBox) actionEvent.getSource();

                    int j = 0;

                    j = deptComboBox.getSelectedIndex();

                    for(int i = j; i < departments.size(); i++){
                        //connect to the department
                        connect.connectDepartmentPage(departments.get(i));

                        //counter for what page of results for depts is on
                        int ctr = 0;

                        //for loop
                        boolean good = true;

                        //loop through all the pages of the department
                        do{

                            //loop through employees on one page
                            try {
                                loopThroughEmployees(0,ctr, departments.get(i));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //check if the arrows for another page of results are present
                            boolean isPresent = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

                            if(isPresent){
                                WebElement nextPageArrow =  driver.findElement(By.id("DataTables_Table_0_next"));

                                boolean nextPageIsNotPresent = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

                                if(!nextPageIsNotPresent){
                                    nextPageArrow.click();
                                    ctr++;
                                }else{
                                    good = false;
                                }

                            }else{
                                //driver.get("https://itsweb.ucsd.edu/directory/search");
                                good =false;
                            }

                        }while(good);

                        driver.navigate().back();

                        //need to clear form
                        WebElement clearButton = driver.findElement(By.id("resetme"));

                        clearButton.click();
                    }

                }

            }
        });

        excelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //open file chooser
                JFileChooser fileChooser = new JFileChooser("C:\\Users\\low85\\Desktop");
                //filter for excel files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx", "xlsm");
                fileChooser.setFileFilter(filter);

                int file = fileChooser.showOpenDialog(Main.this);
                if (file == 0) {
                    Main.this.excelFile = fileChooser.getSelectedFile();
                    excel.setExcelFile(excelFile);
                    ifExcelLoaded = true;
                }
            }
        });

        loadPreviousSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ArrayList <Integer> intCounter = fileIn.load();

                int counter = intCounter.get(0);
                int employeeNumber = intCounter.get(1);
                int deptNumber = 0;

                if(ifExcelLoaded) {

                    boolean goodFirstTime = true;

                    String deptName = fileIn.loadDepartmentName();

                    deptNumber = departments.indexOf(deptName);

                    connect.connectDepartmentPage(deptName);


                    //if there is more employees after the saved one/ do a normal loop
                    if(checkIfAnotherEmployee(employeeNumber, counter) == 0){

                        do {

                            try {
                                loopThroughEmployees(employeeNumber + 1, counter, deptName);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //check if the arrows for another page of results are present
                            boolean isPresent = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

                            if (isPresent) {
                                WebElement nextPageArrow = driver.findElement(By.id("DataTables_Table_0_next"));

                                boolean nextPageIsNotPresent = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

                                if (nextPageIsNotPresent == false) {
                                    nextPageArrow.click();
                                    counter++;
                                } else {
                                    goodFirstTime = false;
                                }

                            } else {
                                //driver.get("https://itsweb.ucsd.edu/directory/search");
                                goodFirstTime = false;
                            }

                        } while (goodFirstTime);

                        //if there is another page of results the employee would be on
                    } else if (checkIfAnotherEmployee(employeeNumber,counter) == 1) {

                        WebElement nextPage = driver.findElement(By.id("DataTables_Table_0_next"));
                        nextPage.click();
                        counter++;
                        do {

                            try {
                                loopThroughEmployees(0, counter, deptName);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //check if the arrows for another page of results are present
                            boolean isPresent = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

                            if (isPresent) {
                                WebElement nextPageArrow = driver.findElement(By.id("DataTables_Table_0_next"));

                                boolean nextPageIsNotPresent = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

                                if (nextPageIsNotPresent == false) {
                                    nextPageArrow.click();
                                    counter++;
                                } else {
                                    goodFirstTime = false;
                                }

                            } else {
                                //driver.get("https://itsweb.ucsd.edu/directory/search");
                                goodFirstTime = false;
                            }

                        } while (goodFirstTime);
                    }


                    driver.navigate().back();

                    //need to clear form
                    WebElement clearButton = driver.findElement(By.id("resetme"));

                    clearButton.click();

                    for (int i = deptNumber + 1; i < departments.size(); i++) {
                        //connect to the department
                        connect.connectDepartmentPage(departments.get(i));

                        //counter for what page of results for depts is on
                        int ctr = 0;

                        //for loop
                        boolean good = true;

                        //loop through all the pages of the department
                        do {

                            //loop through employees on one page
                            try {
                                loopThroughEmployees(0, ctr, departments.get(i));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //check if the arrows for another page of results are present
                            boolean isPresent = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

                            if (isPresent) {
                                WebElement nextPageArrow = driver.findElement(By.id("DataTables_Table_0_next"));

                                boolean nextPageIsNotPresent = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

                                if (nextPageIsNotPresent == false) {
                                    nextPageArrow.click();
                                    ctr++;
                                } else {
                                    good = false;
                                }

                            } else {
                                //driver.get("https://itsweb.ucsd.edu/directory/search");
                                good = false;
                            }

                        } while (good);

                        driver.navigate().back();

                        clearButton = driver.findElement(By.id("resetme"));
                        //need to clear form
                        clearButton.click();
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Please select an Excel file");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        oneDepartmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(ifExcelLoaded){

                        //counter for what page of results for depts is on
                        int ctr = 0;

                        //for loop
                        boolean good = true;

                        //loop through all the pages of the department
                        do{

                            //loop through employees on one page
                            try {
                                loopThroughEmployees(0,ctr, "N/A");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            //check if the arrows for another page of results are present
                            boolean isPresent = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

                            if(isPresent){
                                WebElement nextPageArrow =  driver.findElement(By.id("DataTables_Table_0_next"));

                                boolean nextPageIsNotPresent = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

                                if(!nextPageIsNotPresent){
                                    nextPageArrow.click();
                                    ctr++;
                                }else{
                                    good = false;
                                }

                            }else{
                                //driver.get("https://itsweb.ucsd.edu/directory/search");
                                good =false;
                            }

                        }while(good);

                        driver.navigate().back();

                        //need to clear form
                        WebElement clearButton = driver.findElement(By.id("resetme"));

                        clearButton.click();
                    }
                }
        });
    }


    public void loopThroughEmployees(int k, int ctr, String originalDepartmentName) throws IOException {
        //get list of employee links
        List<WebElement> employeeLinks = driver.findElements(By.xpath("//td[@id='empNames']/a[1]"));

        //loop thru employees on page
        for(int i = k; i < employeeLinks.size(); i++ ){

            //have to refresh links bc they expire
            employeeLinks = driver.findElements(By.xpath("//td[@id='empNames']/a[1]"));

            wait.until(ExpectedConditions.elementToBeClickable(employeeLinks.get(i)));

            try{
                employeeLinks.get(i).click();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Boop, boop, ERROR");
            }

            //get details from empl detail page
            ArrayList<String> employeeDetails = connect.readFromEmployeeDetailPage();

            String departmentName = employeeDetails.get(2);

            if(!excel.checkIfSheetCreatedAlready(departmentName)){
                excel.creatSheet(departmentName);
            }

            departmentTextField.setText(employeeDetails.get(0));
            this.revalidate();

            System.out.println(employeeDetails.get(0));

            excel.writeToBook(employeeDetails.get(0), employeeDetails.get(1), employeeDetails.get(2), employeeDetails.get(3), employeeDetails.get(4));

            fileOut.save(ctr,i);

            //need to save the department name from the dropdown bc it differs from the employee name
            fileOut.saveDepartmentName(originalDepartmentName);

            //go back to the main empl page
            driver.navigate().back();

            //find the page you should be on bc the page gets reset
            for(int j = 0; j < ctr; j++){

                WebElement page = driver.findElement(By.id("DataTables_Table_0_next"));
                page.click();
            }

            //wait so the directory doesn't time us out
            try{
                TimeUnit.SECONDS.sleep(60);
            }catch (InterruptedException e){

            }

        }
    }

    public int checkIfAnotherEmployee(int i, int ctr){
        boolean nextPageArrowsExist = driver.findElements(By.id("DataTables_Table_0_next")).size() > 0;

        boolean nextPageDoesNotExist = driver.findElements(By.xpath("//a[@class='fg-button ui-button ui-state-default next ui-state-disabled']")).size() > 0;

        int number = i + 1;

        //get to the correct page
        for(int j = 0; j < ctr; j++){

            WebElement page = driver.findElement(By.id("DataTables_Table_0_next"));
            page.click();
        }

        //check if +1 is still on the page
        List<WebElement> links = driver.findElements(By.xpath("//td[@id='empNames']/a[1]"));
        if(number < links.size()){
            return 0;
            //if +1 is beyond the page and another page exists
        }else if(nextPageArrowsExist && !nextPageDoesNotExist){
            return 1;
        }

        return -1;
    }


    public static void main(String[] args) {
        new Main();
    }
}
