package com.example.demo.ui;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

public class ScreenshotOnFailure implements TestExecutionExceptionHandler {

    private final Supplier<WebDriver> driver;

    public ScreenshotOnFailure(Supplier<WebDriver> driver) {
        this.driver = driver;
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        WebDriver d = driver.get();
        if (d instanceof TakesScreenshot ts) {
            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);
            String name = context.getRequiredTestMethod().getName() + "-" + System.currentTimeMillis() + ".png";
            Files.copy(ts.getScreenshotAs(OutputType.FILE).toPath(), dir.resolve(name),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        throw throwable;
    }
}