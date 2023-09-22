package org.home;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Scraper
{
    private int times, checkpoint;
    private WebDriver driver;

    protected Scraper(WebDriver driver, int times)
    {
        this.driver = driver;

        this.times = times;
        checkpoint = Integer.parseInt(FileCommunication.read("src/main/resources/checkpoint.txt").get(0));
    }

    public void run()
    {
        for (int i = 0; i < times; i++)
        {
            ArrayList<String> lines = FileCommunication.read("src/main/resources/movies.csv");

            String url = getID();
            getPage(url);

            if (isMovie())
            {
                String name = "null";
                List<WebElement> nameList = driver.findElements(By.xpath("//h1[@data-testid='hero__pageTitle']"));
                if (!nameList.isEmpty()) name = nameList.get(0).getText().replaceAll(",", "&comma");

                List<WebElement> list = driver.findElements(By.xpath("//ul[@role='presentation']"));

                String year = "null";
                String length = "null";
                String director = "null";

                if (!list.isEmpty())
                {
                    String[] pieces = list.get(1).getText().split("\n");

                    year = pieces[0];
                    length = calculate(pieces[pieces.length - 1]);
                    director = list.get(3).getText().split("\n")[0].replaceAll(",", "&comma");
                }

                String description = "null";
                List<WebElement> descriptionList = driver.findElements(By.xpath("//span[@data-testid='plot-xl']"));
                if (!descriptionList.isEmpty()) description = descriptionList.get(0).getText().replaceAll(",", "&comma");

                String score = "null";
                List<WebElement> scoreElements = driver.findElements(By.xpath("//div[@data-testid='hero-rating-bar__aggregate-rating__score']"));
                if (!scoreElements.isEmpty()) score = scoreElements.get(0).getText().split("\n")[0];

                String genres = "null";
                List<WebElement> genresList = driver.findElements(By.xpath("//div[@data-testid='genres']"));
                if (!genresList.isEmpty()) genres = genresList.get(0).getText().replaceAll("\n", ";");

                String line = "https://www.imdb.com/title/tt" + url + "/?ref_=nv_sr_srsg_0_tt_8_nm_0_q_interst" + "," + name + "," + year + "," + director + "," + score + "," + description + "," + genres + "," + length;
                lines.add(line);
                FileCommunication.write(lines, "src/main/resources/movies.csv");

                ArrayList<String> temp = new ArrayList<>();
                temp.add(Integer.toString(checkpoint));
                FileCommunication.write(temp, "src/main/resources/checkpoint.txt");
            }

        }
    }


    private String getID()
    {
        String id = null;

        checkpoint++;
        String strCheckpoint = Integer.toString(checkpoint);

        for (int i = 0; i < 7 - strCheckpoint.length(); i++)
        {
            if (id == null) id = "0";
            else id += "0";
        }

        if (id == null) id = strCheckpoint;
        else id += strCheckpoint;

        return id;
    }

    private void getPage(String id)
    {
        String url = "https://www.imdb.com/title/tt" + id + "/?ref_=nv_sr_srsg_0_tt_8_nm_0_q_interst";
        driver.get(url);
        waitElement("//h1[@data-testid='hero__pageTitle']");
    }

    private void waitElement(String till)
    {
        Duration duration = Duration.ofSeconds(30);
        WebDriverWait wait = new WebDriverWait(driver, duration);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(till)));
    }

    private boolean isMovie()
    {
        List<WebElement> elements = driver.findElements(By.xpath("//ul[@role='presentation']"));
        String info = elements.get(1).getText();

        if (info.contains("TV Series")) return false;
        else return true;
    }

    private String calculate(String length)
    {
        String time;

        if (length.contains("h"))
        {
            String[] pieces = length.split("h");

            int asMinutes = 0;

            pieces[0] = pieces[0].trim();
            asMinutes = Integer.parseInt(pieces[0]) * 60;
            asMinutes += Integer.parseInt(pieces[1].trim().replace("m", ""));

            time = Integer.toString(asMinutes);
        }
        else time = length.replace("m", "");

        return time;
    }

}
