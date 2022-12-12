# +
*** Settings ***
Documentation   Background check for New Business License Application
...             Capture screenshots of the below categories and append it to a PDF file.
...             1.	Social media company profile like Linkedin, Twitter, Facebook and Instagram.
...             2.	Checking if Business have a working website.
...             3.	Google search results about company.
...             4.	Validating if company is blacklisted or not. (Assuming the business is going to start in Canada)


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
${verify-bc-org-url}=        https://www.orgbook.gov.bc.ca/en/home

*** Keywords ***

Get Data From Vault
  Log To Console  START : Getting data from the vault
  ${secret}=  Get Secret  my_vault
  Log To Console  START : Getting data from the vault
  [Return]  ${secret}

Gather The Social Media Profiles
  
  New Pdf
  
  Add New Heading   Validate If Company Was Blacklisted
  
  Validate If Company Was Blacklisted     ${businessOperatingName}
  
  Add New Page   Search In Intranet About The Company
  
  Search In Intranet About The Company     ${businessOperatingName}
  
  ${profiles} =  Create List   facebook  linkedin  twitter  instagram
    
  Search And Append Social Media Search Results     ${businessOperatingName}   ${profiles}
  
  Add New Page   Validate Company Website
  
  Open And Validate Company Website     ${businessWebsite}

  Log To Console    Checks completed
  
  Save Pdf File   ${FINAL_DIR}${/}${working-dir}.pdf


Search And Append Social Media Search Results
  [Arguments]     ${businessOperatingName}   ${profiles}
  Log To Console    Started : Searching in facebook for company profiles
  Open Available Browser  ${search-engine-url}
  FOR    ${profile}    IN    @{profiles}
    Input Text    //input[@title="Search"]      ${businessOperatingName} ${profile}
    Submit Form
    Wait Until Element Is Visible    //div[@id="center_col"]
    Take a screenshot of the page   //div[@id="center_col"]   ${profile}
    Add New Page   ${profile}
    Add Image File   ${RUNTIME_DIR}${/}${profile}.png
    Go Back
  END
  Log To Console    Completed : Searching in facebook for company profiles
  [Teardown]   Close Intranet Browser

Search In Intranet About The Company
  [Arguments]     ${businessOperatingName}
  Log To Console    Started : Searching in intranet about the company
  Open Available Browser  ${search-engine-url}
  Input Text    //input[@title="Search"]      ${businessOperatingName}
  Submit Form
  Wait Until Element Is Visible    //div[@id="rcnt"]
  Take a screenshot of the page   //div[@id="rcnt"]   searchResults
  Add Image File   ${RUNTIME_DIR}${/}searchResults.png
  Log To Console    Completed : Searching in intranet about the company
  [Teardown]   Close Intranet Browser

Open And Validate Company Website
  [Arguments]     ${businessWebsite}
  Log To Console    Started : Open And Validate Company Website
  ${length}=   Get Length      ${businessWebsite}
  ${fileName}=   Set Variable    ${RUNTIME_DIR}${/}companyWebsite.png
  IF    ${length} > 0
    Open Available Browser    ${businessWebsite}
    Capture Page Screenshot   ${fileName}
    Add Image File   ${fileName}
  END
  Log To Console    End : Open And Validate Company Website
  [Teardown]   Close Intranet Browser

Validate If Company Was Blacklisted
  [Arguments]     ${businessOperatingName}
  Log To Console    Started : Validate If Company Was Blacklisted
  Open Available Browser  ${verify-bc-org-url}
  Wait Until Element Is Visible    //input[@id="searchBar"]
  Input Text    //input[@id="searchBar"]     ${businessOperatingName}
  Click Button    //button[@id="searchBarButton"]
  Sleep   5s
  Take a screenshot of the page    //div[@class="v-main__wrap"]   blacklistdata
  Add Image File   ${RUNTIME_DIR}${/}blacklistdata.png
  Log To Console    End : Validate If Company Was Blacklisted
  [Teardown]   Close Intranet Browser

Take a screenshot of the page
  [Arguments]  ${webEl}   ${name}
  Sleep  1s
  Wait Until Page Contains Element   ${webEl}  timeout=20
  Screenshot    ${webEl}  ${RUNTIME_DIR}${/}${name}.png


  
Close Intranet Browser
  Close Browser

*** Tasks ***
Do Background check for the New Business Application
  Gather The Social Media Profiles
  [Teardown]  Close Intranet Browser
