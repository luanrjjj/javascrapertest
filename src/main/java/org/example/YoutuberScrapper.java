package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YoutuberScrapper {
    public static void ScrappingYoutube() {
//        String currentUserHomeDir = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");


        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        ArrayList<String> youtubeChannels = new ArrayList<>();
        List<Map<String, String>> videos = new ArrayList<>();

        youtubeChannels.add("https://www.youtube.com/@YasoobKhalid/videos");
        youtubeChannels.add("https://www.youtube.com/@ClickInvest/videos");
        youtubeChannels.add("https://www.youtube.com/@techTFQ/videos");
//        youtubeChannels.add("https://www.youtube.com/@BroCodez/videos");

        for (int i = 0; i < youtubeChannels.size(); i ++) {
            driver.get(youtubeChannels.get(i));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ytd-channel-name")));
            WebElement channelTitleElement = driver.findElement(By.xpath("//yt-formatted-string[contains(@class, 'ytd-channel-name')]"));
            String channelTitle = channelTitleElement.getText();

            WebElement handleElement = driver.findElement(By.xpath("//yt-formatted-string[@id='channel-handle']"));
            String handle = handleElement.getText();

            WebElement subscriberCountElement = driver.findElement(By.xpath("//yt-formatted-string[@id='subscriber-count']"));
            String subscriberCount = subscriberCountElement.getText();
            System.out.println("subscriberCount" + subscriberCount);

            int WAIT_IN_SECONDS = 5;
            long lastHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.documentElement.scrollHeight");

            while (true) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, arguments[0]);", lastHeight);
                try {
                    Thread.sleep(WAIT_IN_SECONDS * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long newHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.documentElement.scrollHeight");
                if (newHeight == lastHeight) {
                    break;
                }
                lastHeight = newHeight;
            }

            List<WebElement> thumbnails = driver.findElements(By.xpath("//a[@id='thumbnail']/yt-image/img"));
            List<WebElement> views = driver.findElements(By.xpath("//div[@id='metadata-line']/span[1]"));
            List<WebElement> titles = driver.findElements(By.id("video-title"));
            List<WebElement> links = driver.findElements(By.id("video-title-link"));
            System.out.println("links " + links);

            for (int j = 0; j < titles.size(); j++) {
                Map<String, String> videoDict = new HashMap<>();
                videoDict.put("title", titles.get(j).getText());
                videoDict.put("views", views.get(j).getText());
                videoDict.put("thumbnail", thumbnails.get(j).getAttribute("src"));
                videoDict.put("link", links.get(j).getAttribute("href"));
                videos.add(videoDict);
            }
            System.out.println("videos " + videos.size());
        }
        System.out.println("FINISHHHHHHHHHHHHHHHHHH");
        driver.quit();

        for (Map<String, String> video : videos) {
            System.out.println("Title: " + video.get("title"));
            System.out.println("Views: " + video.get("views"));
            System.out.println("Thumbnail: " + video.get("thumbnail"));
            System.out.println("Link: " + video.get("link"));
            System.out.println();
        }
    }
}
