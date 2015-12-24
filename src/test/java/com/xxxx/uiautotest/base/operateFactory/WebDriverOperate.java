package com.xxxx.uiautotest.base.operateFactory;

import org.openqa.selenium.*;

import java.util.List;

/**
 * Created by yuyilong on 15/9/24.
 */
public class WebDriverOperate {

    private By tabBy = null;
    private WebElement table = null;
    private List<WebElement> tabRows = null;
    private List<WebElement> tables = null;

    /**
     * construct with parameters initialize.
     *
     * @param driver
     *            the WebDriver instance.
     * @param tabFinder
     *            the By locator of the table.
     * @param bodyOrHead
     *            choice of table body and head to operate.
     */
    public WebDriverOperate(WebDriver driver, By tabFinder, String bodyOrHead) {
        this.tabBy = tabFinder;
        this.tables = driver.findElements(tabBy);
        if (null == tables || tables.size() == 0) {
            throw new NoSuchElementException("the table " + tabFinder.toString() + "was not found!");
        }
        this.table = tables.get(0);
        this.tabRows = table.findElements(By.tagName(bodyOrHead)).get(0).findElements(By.tagName("tr"));
        if (null == tabRows || tabRows.size() == 0) {
            throw new InvalidElementStateException("the table " + tabFinder.toString() + "is empty!");
        }
    }

    /**
     * construct with parameters initialize.
     *
     * @param driver
     *            the WebDriver instance.
     * @param tabFinder
     *            the By locator of the table.
     */
    public WebDriverOperate(WebDriver driver, By tabFinder) {
        this.tabBy = tabFinder;
        this.tables = driver.findElements(tabBy);
        if (null == tables || tables.size() == 0) {
            throw new NoSuchElementException("the table " + tabFinder.toString() + "was not found!");
        }
        this.table = tables.get(0);
        this.tabRows = table.findElements(By.xpath("*/tr"));
        if (null == tabRows || tabRows.size() == 0) {
            throw new InvalidElementStateException("the table " + tabFinder.toString() + "is empty!");
        }
    }

    /**
     * to get the whole web table element.
     *
     * @return the table element.
     */
    public WebElement tableElement() {
        return this.table;
    }

    /**
     * to get the web table head element.
     *
     * @return the first table head element.
     */
    public WebElement tableHeader() {
        return this.table.findElements(By.tagName("thead")).get(0);
    }

    /**
     * to get the web table body element.
     *
     * @return the first table body element.
     */
    public WebElement tableBody() {
        return this.table.findElements(By.tagName("tbody")).get(0);
    }

    /**
     * get row count of a webtable.
     *
     * @return the row count of the table.
     */
    public int rowCount() {
        return tabRows.size();
    }

    /**
     * get column count of a specified webtable row.
     *
     * @param rowNum
     *            row index of your table to count.
     * @return the column count of the row in table.
     */
    public int colCount(int rowNum) {
        return tabRows.get(rowNum - 1).findElements(By.xpath("td")).size();
    }

    /**
     * get the element in the table cell by row and col index.
     *
     * @param row
     *            row index of the table.
     * @param col
     *            column index of the table.
     * @param type
     *            the element type, such as "img"/"a"/"input"...
     * @param index
     *            element index in the specified cell, begins with 1.
     * @return the table cell WebElement.
     */
    public WebElement childItem(int row, int col, String type, int index) {
        List<WebElement> cells = tabRows.get(row - 1).findElements(By.xpath("td"));
        return (type.contains("cell")) ? cells.get(col - 1) : childsGetter(cells.get(col - 1), type).get(index - 1);
    }

    /**
     * get the cell text of the table on specified row and column.
     *
     * @param row
     *            row index of the table.
     * @param col
     *            column index of the table.
     * @return the cell text.
     */
    public String cellText(int row, int col) {
        return childItem(row, col, "cell", 0).getText();
    }

    /**
     * button/edit/checkbox are using the same html tag "input", others may be
     * the same.</BR> this method will get the WebElements List accord the user
     * element classes.
     *
     * @param father
     *            the father element to get childs.
     * @param elementClass
     *            link/button/edit/checkbox/image/list and so on.
     * @return the WebElements List.
     */
    private List<WebElement> childsGetter(WebElement father, String elementClass) {
        return father.findElements(By.tagName(elementTagGetter(elementClass)));
    }

    /**
     * get the tag of element by webelement type.
     *
     * @param elementType
     *            link/button/edit/checkbox/image/list and so on.
     */
    private String elementTagGetter(String elementType) {
        if (elementType.toLowerCase().trim().contains("link")) {
            return "a";
        } else if (elementType.toLowerCase().trim().contains("button")) {
            return "input";
        } else if (elementType.toLowerCase().trim().contains("edit")) {
            return "input";
        } else if (elementType.toLowerCase().trim().contains("checkbox")) {
            return "input";
        } else if (elementType.toLowerCase().trim().contains("image")) {
            return "img";
        } else if (elementType.toLowerCase().trim().contains("list")) {
            return "select";
        } else if (elementType.toLowerCase().trim().contains("text")) {
            return "textarea";
        } else {
            return elementType.toLowerCase();
        }
    }
}