# Feature: E2E Workflow — نظام المنح الملكية
# يغطي: المواطن → موظف المنح → مدير المنح
# Tag: User_MAHMOUD

@E2E @Regression @GrantsWorkflow @User_MAHMOUD
Feature: E2E Grant Workflow — User_MAHMOUD

  @Step1 @Citizen @User_MAHMOUD
  Scenario Outline: TC-E2E-01-MAHMOUD — Citizen <username> submits grant with <grantArea>
    Given I navigate to the portal login page
    When I login with username "<username>" and password "Aa1357@a"
    Then I should be redirected to the apps page
    When I click on "المنح الملكية" app card
    Then I should be on the grants portal page
    When I extract the JWT token from the URL
    And I open localhost grants app with the JWT token
    And I click on "معاملة جديدة" dropdown
    And I select "طلب منح ملكية" from the dropdown
    And I select owner type radio "فرد"
    And I click button to go to next tab
    And I handle owner validation error if present
    And I fill royal decree number "883"
    And I select royal decree date "1447/01/09"
    And I select royal grant area "<grantArea>"
    And I click button to go to next tab
    And I click add a new note row
    And I fill note text "ملاحظة اختبار تلقائي - <grantArea>"
    And I click button to go to next tab
    Then the summary page should be displayed
    And I click the "ارسال" button to submit the transaction
    Then I should see success message containing "تم حفظ وارسال المعاملة بنجاح"
    And I save the submitted transaction number

    Examples:
      | username   | grantArea            |
      | mahmoud    | مساحة ٢٠٠ × ٢٠٠      |
      | mahmoud    | مساحة ١٠٠ × ١٠٠      |
      | mahmoud    | مساحة ٥٠ × ٥٠        |
      | mahmoud    | مساحة ٤٠ × ٤٠        |
      | mahmoud    | مساحة ٣٠ × ٣٠        |
      | mahmoud    | مساحة ٢٥ × ٢٥        |
      | mahmoud    | مساحة ٢٠ × ٢٠        |

  @Step2 @GrantEmployee @User_MAHMOUD
  Scenario Outline: TC-E2E-02-MAHMOUD — Grant Employee processes transaction <txIndex>
    Given I navigate to the portal login page
    When I login with username "grant_emp" and password "Aa1357@a"
    Then I should be redirected to the apps page
    When I click on "المنح الملكية" app card
    Then I should be on the grants portal page
    When I extract the JWT token from the URL
    And I open localhost grants app with the JWT token
    And I click on "المعاملات الواردة" from the sidebar
    And I click "عرض" button on transaction number "<txIndex>"
    Then the summary tab should be active and read-only
    And I click button to go to next tab
    And I fill file opening data tab with:
      | fileNo              | F-<txIndex>-001  |
      | fileOpeningDate     | 1447/01/10       |
      | orderSource         | ديوان الملك      |
      | hafeezaNumber       | H-<txIndex>-001  |
      | hafeezaDate         | 1447/09/10       |
      | hafeezaSource       | وزارة العدل      |
      | customizationNumber | C-<txIndex>-001  |
      | customizationDate   | 1447/09/10       |
      | operationNo         | OP-<txIndex>-001 |
      | operationDate       | 1447/09/10       |
      | grantType           | منح ملكية        |
    And I click button to go to next tab
    And I fill employee note "ملاحظة موظف المنح - <txIndex> - اختبار تلقائي"
    And I click the "ارسال" button to submit the transaction
    Then I should see success message containing "تم"

    Examples:
      | txIndex |
      | TX-01     |
      | TX-02     |
      | TX-03     |
      | TX-04     |
      | TX-05     |
      | TX-06     |
      | TX-07     |

  @Step3 @GrantManager @User_MAHMOUD
  Scenario Outline: TC-E2E-03-MAHMOUD — Grant Manager finalizes transaction <txIndex>
    Given I navigate to the portal login page
    When I login with username "grant_manager" and password "Aa1357@a"
    Then I should be redirected to the apps page
    When I click on "المنح الملكية" app card
    Then I should be on the grants portal page
    When I extract the JWT token from the URL
    And I open localhost grants app with the JWT token
    And I click on "المعاملات الواردة" from the sidebar
    And I click "عرض" button on transaction number "<txIndex>"
    Then the summary tab should be displayed with owner data
    And I click button to go to next tab
    And I fill manager note "اعتماد مدير المنح - <txIndex> - اختبار تلقائي"
    And I click the "انهاء" button to submit the transaction

    Examples:
      | txIndex |
      | TX-01     |
      | TX-02     |
      | TX-03     |
      | TX-04     |
      | TX-05     |
      | TX-06     |
      | TX-07     |
