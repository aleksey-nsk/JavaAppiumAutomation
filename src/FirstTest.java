import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

public class FirstTest extends CoreTestCase {

  private MainPageObject mainPageObject;

  protected void setUp() throws Exception {
    super.setUp();
    mainPageObject = new MainPageObject(driver);
  }

  @Test
  public void testSearch() {
    System.out.print("\n\n***** Тестовый метод testSearch() *****\n");
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForSearchResult("Object-oriented programming language");
  }

  @Test
  public void testCancelSearch() {
    System.out.print("\n\n***** Тестовый метод testCancelSearch() *****\n");
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clearSearchField();
    searchPageObject.waitForCancelButtonToAppear();
    searchPageObject.clickCancelButton();
    searchPageObject.waitForCancelButtonToDisappear();
  }

  @Test
  public void testCheckWordInSearch() {
    System.out.print("\n\n***** Тестовый метод testCheckWordInSearch() *****\n");

    mainPageObject.waitForElementAndClick(
        By.id("org.wikipedia:id/search_container"),
        "Can not find 'Search Wikipedia' input",
        5
    );

    final String searchWord = "Java";

    mainPageObject.waitForElementAndSendKeys(
        By.xpath("//*[contains(@text, 'Search…')]"),
        searchWord,
        "Can not find 'Search…' input",
        5
    );

    mainPageObject.waitForElementPresent(
        By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']/*[@resource-id='org.wikipedia:id/search_results_list']"),
        "Не дождались элемента, содержащего список результатов поиска"
    );

    mainPageObject.checkWordInSearch(
        By.xpath("//*[@resource-id='org.wikipedia:id/search_results_list']/*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@resource-id='org.wikipedia:id/page_list_item_title']"),
        searchWord
    );
  }

  @Test
  public void testCompareArticleTitle() {
    System.out.print("\n\n***** Тестовый метод testCompareArticleTitle() *****\n");

    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

    ArticlePageObject articlePageObject = new ArticlePageObject(driver);
    String articleTitle = articlePageObject.getArticleTitle();

    String expectedTitle = "Java (programming language)";
    Assert.assertEquals("We see unexpected title", expectedTitle, articleTitle);
  }

  @Test
  public void testSwipeArticle() {
    System.out.print("\n\n***** Тестовый метод testSwipeArticle() *****\n");

    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Appium");
    searchPageObject.clickByArticleWithSubstring("Appium");

    ArticlePageObject articlePageObject = new ArticlePageObject(driver);
    articlePageObject.waitForTitleElement();
    articlePageObject.swipeToFooter();
  }

  @Test
  public void testSaveFirstArticleToMyList() {
    System.out.print("\n\n***** Тестовый метод testSaveFirstArticleToMyList() *****\n");

    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

    ArticlePageObject articlePageObject = new ArticlePageObject(driver);
    articlePageObject.waitForTitleElement();
    final String articleTitle = articlePageObject.getArticleTitle();
    final String nameOfFolder = "Learning programming";
    articlePageObject.addArticleToMyList(nameOfFolder);
    articlePageObject.closeArticle();

    NavigationUI navigationUI = new NavigationUI(driver);
    navigationUI.clickMyList();

    MyListsPageObject listsPageObject = new MyListsPageObject(driver);
    listsPageObject.openFolderByName(nameOfFolder);
    listsPageObject.swipeByArticleToDelete(articleTitle);
  }

  @Test
  public void testAmountOfNotEmptySearch() {
    System.out.print("\n\n***** Тестовый метод testAmountOfNotEmptySearch() *****\n");
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    final String searchLine = "Linkin park discography";
    searchPageObject.typeSearchLine(searchLine);
    int amountOfFoundArticles = searchPageObject.getAmountOfFoundArticles();
    Assert.assertTrue("We found too few results", amountOfFoundArticles > 0);
  }

  @Test
  public void testAmountOfEmptySearch() {
    System.out.print("\n\n***** Тестовый метод testAmountOfEmptySearch() *****\n");
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    final String searchLine = "kflkdjjklfnhj";
    searchPageObject.typeSearchLine(searchLine);
    searchPageObject.waitForEmptyResultLabel();
    searchPageObject.assertThereIsNoResultOfSearch();
  }

  @Test
  public void testChangeScreenOrientationOnSearchResults() {
    System.out.print("\n\n***** Тестовый метод testChangeScreenOrientationOnSearchResults() *****\n");

    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

    ArticlePageObject articlePageObject = new ArticlePageObject(driver);
    final String titleBeforeRotation = articlePageObject.getArticleTitle();

    this.rotateScreenLandscape();
    final String titleAfterRotation = articlePageObject.getArticleTitle();
    Assert.assertEquals("Article title have been changed after screen rotation", titleBeforeRotation, titleAfterRotation);

    this.rotateScreenPortrait();
    final String titleAfterSecondRotation = articlePageObject.getArticleTitle();
    Assert.assertEquals("Article title have been changed after screen rotation", titleBeforeRotation, titleAfterSecondRotation);
  }

  @Test
  public void testCheckSearchArticleInBackground() {
    System.out.print("\n\n***** Тестовый метод testCheckSearchArticleInBackground() *****\n");
    SearchPageObject searchPageObject = new SearchPageObject(driver);
    searchPageObject.initSearchInput();
    searchPageObject.typeSearchLine("Java");
    searchPageObject.waitForSearchResult("Object-oriented programming language");
    this.backgroundApp(5);
    searchPageObject.waitForSearchResult("Object-oriented programming language");
  }

  @Test
  public void testSaveTwoArticles() {
    System.out.print("\n\n***** Тестовый метод testSaveTwoArticles() *****\n");

    final String searchLine = "Java";
    final String firstArticleTitle = "Java (programming language)";
    final String secondArticleTitle = "Java (software platform)";
    final String nameOfFolder = "Learning programming";

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
        "Can not find 'Search Wikipedia' input",
        5
    );

    mainPageObject.waitForElementAndSendKeys(
        By.xpath("//*[contains(@text, 'Search…')]"),
        searchLine,
        "Can not find 'Search…' input",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + firstArticleTitle + "']"),
        "Can not find 'Object-oriented programming language' topic, searching by '" + searchLine + "'",
        5
    );

    mainPageObject.waitForElementPresent(
        By.id("org.wikipedia:id/view_page_title_text"),
        "Can not find article title",
        10
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_toolbar']//*[@class='android.widget.ImageView']"),
        "Can not find button to open article options",
        5
    );

    mainPageObject.waitForElementPresent(
        By.xpath("//*[@class='android.widget.FrameLayout']/*[@class='android.widget.ListView']"),
        "Не дождались контейнер, содержащий пункты меню"
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@text='Add to reading list']"),
        "Can not find option to add article to reading list",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.id("org.wikipedia:id/onboarding_button"),
        "Can not find 'Got it' tip overlay",
        5
    );

    mainPageObject.waitForElementAndClear(
        By.id("org.wikipedia:id/text_input"),
        "Can not find input to set name of articles folder",
        5
    );

    mainPageObject.waitForElementAndSendKeys(
        By.id("org.wikipedia:id/text_input"),
        nameOfFolder,
        "Can not put text into articles folder input",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@text='OK']"),
        "Can not press OK button",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_toolbar']/*[@class='android.widget.ImageButton']"),
        "Can not close article, can not find X link",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
        "Can not find 'Search Wikipedia' input",
        5
    );

    mainPageObject.waitForElementAndSendKeys(
        By.xpath("//*[contains(@text, 'Search…')]"),
        searchLine,
        "Can not find 'Search…' input",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + secondArticleTitle + "']"),
        "Can not find 'Java (software platform)' topic, searching by '" + searchLine + "'",
        5
    );

    mainPageObject.waitForElementPresent(
        By.id("org.wikipedia:id/view_page_title_text"),
        "Can not find article title",
        10
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_toolbar']//*[@class='android.widget.ImageView']"),
        "Can not find button to open article options",
        5
    );

    mainPageObject.waitForElementPresent(
        By.xpath("//*[@class='android.widget.FrameLayout']/*[@class='android.widget.ListView']"),
        "Не дождались контейнер, содержащий пункты меню"
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@text='Add to reading list']"),
        "Can not find option to add article to reading list",
        5
    );

    mainPageObject.waitForElementPresent(
        By.id("org.wikipedia:id/lists_container"),
        "Не дождались контейнер, содержащий папки со статьями"
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@text='" + nameOfFolder + "']"),
        "Can not find folder named '" + nameOfFolder + "'",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_toolbar']/*[@class='android.widget.ImageButton']"),
        "Can not close article, can not find X link",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/fragment_main_nav_tab_layout']//*[@content-desc='My lists']"),
        "Can not find navigation button to My list",
        5
    );

    mainPageObject.waitForElementPresent(
        By.xpath("//*[@resource-id='org.wikipedia:id/reading_list_list']"),
        "Не дождались контейнер, содержащий папки с сохранёнными статьями"
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@text='" + nameOfFolder + "']"),
        "Can not find created folder",
        5
    );

    mainPageObject.swipeElementToLeft(
        By.xpath("//*[@text='" + firstArticleTitle + "']"),
        "Can not find saved article"
    );

    mainPageObject.waitForElementNotPresent(
        By.xpath("//*[@text='" + firstArticleTitle + "']"),
        "Can not delete saved article",
        5
    );

    mainPageObject.waitForElementPresent(
        By.xpath("//*[@text='" + secondArticleTitle + "']"),
        "Can not find second article in the folder",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@text='" + secondArticleTitle + "']"),
        "Can not click on second article",
        5
    );

    final String checkSecondArticleTitle = mainPageObject.waitForElementAndGetAttribute(
        By.id("org.wikipedia:id/view_page_title_text"),
        "text",
        "Can not find title of article",
        10
    );

    Assert.assertEquals(
        "Article title have been changed",
        secondArticleTitle,
        checkSecondArticleTitle
    );
  }

  @Test
  public void testAssertTitle() {
    System.out.print("\n\n***** Тестовый метод testAssertTitle() *****\n");

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
        "Can not find 'Search Wikipedia' input",
        5
    );

    final String searchLine = "Java";

    mainPageObject.waitForElementAndSendKeys(
        By.xpath("//*[contains(@text, 'Search…')]"),
        searchLine,
        "Can not find 'Search…' input",
        5
    );

    mainPageObject.waitForElementAndClick(
        By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
        "Can not find 'Object-oriented programming language' topic, searching by '" + searchLine + "'",
        15
    );

    final String titleLocator = "//*[@resource-id='org.wikipedia:id/view_page_header_container']/*[@resource-id='org.wikipedia:id/view_page_title_text']";

    mainPageObject.assertElementPresent(
        By.xpath(titleLocator),
        "У статьи отсутствует элемент title"
    );
  }
}
