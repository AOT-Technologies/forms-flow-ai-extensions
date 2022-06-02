*** Settings ***
Documentation   Web Scrapper with excel output
Resource        ./Keywords.Library/Browser.Library/browser.robot

#Imported Libraries

Library  RPA.Robocloud.Secrets
Library  RPA.Browser.Selenium
Library  RPA.FileSystem
Library  Collections
Library  RPA.Archive
Library  pdfGen.py
Library  RPA.HTTP
Library  RPA.PDF
Library  DateTime
# -


*** Variables ***
${GLOBAL_RETRY_AMOUNT}=      3x
${GLOBAL_RETRY_INTERVAL}=    1s
${MAX_SEARCH_RESULT_CLICKABLE}=   3
${RUNTIME_DIR}=              ${CURDIR}${/}output${/}${working-dir}${/}runtime
${FINAL_DIR}=                ${CURDIR}${/}output${/}${working-dir}${/}final
${search-engine-url}=        https://google.com

*** Keywords ***

Gather Organization Details
  
  New Pdf
  
  Search Organization Details    ${organizationName}

  Log To Console    Checks completed
  
  Save Pdf File   ${FINAL_DIR}${/}${working-dir}.pdf

Search Organization Details
  [Arguments]     ${organizationName}
  Log To Console    Started : Searching in intranet about the company
  Open Available Browser  ${search-engine-url}
  Input Text    //input[@title="Search"]      ${organizationName}
  Submit Form
  Wait Until Element Is Visible    //div[@id="rcnt"]
  Take a screenshot of the page   //div[@id="rcnt"]   searchResults
  Add Image File   ${RUNTIME_DIR}${/}searchResults.png
  Log To Console    Completed : Searching in intranet about the company
  [Teardown]   Close Intranet Browser



Take a screenshot of the page
  [Arguments]  ${webEl}   ${name}
  Sleep  1s
  Wait Until Page Contains Element   ${webEl}  timeout=20
  Screenshot    ${webEl}  ${RUNTIME_DIR}${/}${name}.png


  
Close Intranet Browser
  Close Browser

*** Tasks ***
Do Background check for the Freedom of Information Application
  Gather Organization Details
  [Teardown]  Close Intranet Browser
