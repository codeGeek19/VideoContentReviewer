package com.example.demo.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortalUiTest extends BaseUiTest {

    @Test
    @DisplayName("J1: create a record and see it as SUBMITTED")
    void createRecord_showsInTable() {
        String title = uniqueTitle("Create");
        createRecord(title, "http://example.com/v1");
        assertEquals("SUBMITTED", statusOf(title));
        assertEquals("Saved", driver.findElement(By.id("msg")).getText());
    }

    @Test
    @DisplayName("J2: saving an empty form shows a validation error")
    void emptyForm_showsError() {
        driver.findElement(By.id("saveBtn")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("msg"), "Error"));
        assertTrue(driver.findElement(By.id("msg")).getText().contains("required"));
    }

    @Test
    @DisplayName("J3: search by title filters the table")
    void search_filtersByTitle() {
        String wanted = uniqueTitle("Alpha");
        String other = uniqueTitle("Beta");
        createRecord(wanted, "http://example.com/a");
        createRecord(other, "http://example.com/b");

        driver.findElement(By.id("q")).sendKeys(wanted);
        driver.findElement(By.id("searchBtn")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(rowOf(other)));
        assertEquals(1, driver.findElements(rowOf(wanted)).size());
        assertEquals(0, driver.findElements(rowOf(other)).size());
    }

    @Test
    @DisplayName("J4: role-based workflow: submitter blocked, reviewer allowed")
    void workflow_respectsRoles() {
        String title = uniqueTitle("Flow");
        createRecord(title, "http://example.com/f");
        By moveToReview = By.xpath("//tr[td[2]='" + title + "']//button[@class='move']");

        selectRole("SUBMITTER");
        driver.findElement(moveToReview).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("msg"), "Not allowed"));
        assertEquals("SUBMITTED", statusOf(title));

        selectRole("REVIEWER");
        wait.until(ExpectedConditions.visibilityOfElementLocated(moveToReview));
        driver.findElement(moveToReview).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//tr[td[2]='" + title + "']/td[@class='status']"), "IN_REVIEW"));
        assertEquals("IN_REVIEW", statusOf(title));
    }

    @Test
    @DisplayName("J5: dashboard total increases after creating a record")
    void dashboard_countsNewRecord() {
        int before = Integer.parseInt(driver.findElement(By.id("count-TOTAL")).getText());
        createRecord(uniqueTitle("Count"), "http://example.com/c");
        wait.until(d -> Integer.parseInt(d.findElement(By.id("count-TOTAL")).getText()) == before + 1);
        assertEquals(before + 1, Integer.parseInt(driver.findElement(By.id("count-TOTAL")).getText()));
    }
}