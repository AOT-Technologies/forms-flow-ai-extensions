*** Settings ***
Documentation    Browser Keywords for web scrapper
Library          RPA.Browser.Selenium
Library          RPA.Tables
Library          RPA.Excel.Files
Library          String

*** Variables ***
${inputEl}     //input[@id="twotabsearchtextbox"]
${dataEl}      //div[@class="s-result-item s-asin sg-col-0-of-12 sg-col-16-of-20 sg-col sg-col-12-of-16"]
${itemNameEl}    xpath://span[@class="a-size-medium a-color-base a-text-normal"]
${itemPriceEl}   xpath://span[@class="a-price-whole"]

*** Keywords ***
Open Browser And Do Web Scrapping And Generate Excel
     [Arguments]    ${URL}  ${SEARCH_KEY}  ${OUTPUT_FILE} 
     Open Available Browser   ${URL}
     Input Text When Element Is Visible   ${inputEl}   ${SEARCH_KEY}
     Sleep    1
     Submit Form
     Sleep    1
     ${itemNames} =  Get WebElements    ${itemNameEl}
     ${itemPrices} =  Get WebElements    ${itemPriceEl}
     Create Workbook    ${OUTPUT_FILE}
     FOR    ${itemName}    ${itemPrice}   IN ZIP    ${itemNames}    ${itemPrices}
        ${productName} =   Get Text    ${itemName}
        ${productPrice} =  Get Text    ${itemPrice}
        ${productPrice} =   Remove String Using Regexp
        ...                 ${productPrice}   ,
        ${productPrice} =   Convert To Number    ${productPrice}
        &{row} =   Create Dictionary
        ...        Product Name      ${productName}
        ...        Price (In INR)    ${productPrice}
        ...        Price (In USD)    ${productPrice / 74.13}
        Append Rows To Worksheet     ${row}   header=${TRUE}
     END
     Save Workbook
     Close Workbook
     Close Browser
