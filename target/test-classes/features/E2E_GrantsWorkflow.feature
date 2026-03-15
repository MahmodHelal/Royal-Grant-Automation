# Feature: E2E Workflow — نظام المنح الملكية (Steps 1→2→3)
# Portal: geoservices1.syadtech.com → JWT → localhost:8080
# يغطي: المواطن → موظف المنح → مدير المنح

 @Regression @GrantsWorkflow
Feature: End-to-End Grant Workflow — سير العمل الكامل للمنح الملكية

  # ──────────────────────────────────────────────────────
  # STEP 1 — Citizen submits a new grant request
  # الخطوة 1 — المواطن يقدم طلب منحة ملكية جديد
  # ──────────────────────────────────────────────────────
  @Step1 @Citizen @smoke
  Scenario: TC-E2E-01 — Citizen submits a new grant transaction
    # المواطن يسجل دخوله ويقدم طلب منحة جديد
    Given I navigate to the portal login page
    When I login with username "mamdouh" and password "Aa1357@a"
    Then I should be redirected to the apps page
    When I click on "المنح الملكية" app card
    Then I should be on the grants portal page
    When I extract the JWT token from the URL
    And I open localhost grants app with the JWT token

    # إنشاء معاملة جديدة / Create new transaction
    And I click on "معاملة جديدة" dropdown
    And I select "طلب منح ملكية" from the dropdown

    # Tab 1 — بيانات المالك / Owner Data
    And I select owner type radio "فرد"
#    And I add the owner
    And I click button to go to next tab
    And I handle owner validation error if present

    # Tab 2 — بيانات الأمر السامي / Royal Decree Data
    And I fill royal decree number "883"
    And I select royal decree date "1447/01/09"
    And I select royal grant area "مساحة ٢٠ × ٢٠"
    And I click button to go to next tab

    # Tab 3 — ملاحظات / Notes
    And I click add a new note row
    And I fill note text "ملاحظة اختبار تلقائي - E2E"
    And I click button to go to next tab

    # Tab 4 — الملخص / Summary
    Then the summary page should be displayed
    And I click the "ارسال" button to submit the transaction
    Then I should see success message containing "تم حفظ وارسال المعاملة بنجاح"
    And I save the submitted transaction number

  # ──────────────────────────────────────────────────────
  # STEP 2 — Grant Employee reviews the transaction
  # الخطوة 2 — موظف المنح يراجع المعاملة
  # ──────────────────────────────────────────────────────
  @Step2 @GrantEmployee
  Scenario: TC-E2E-02 — Grant Employee processes and forwards the transaction
    # موظف المنح يسجل دخول ويراجع المعاملة الواردة
    Given I navigate to the portal login page
    When I login with username "grant_emp" and password "Aa1357@a"
    Then I should be redirected to the apps page
    When I click on "المنح الملكية" app card
    Then I should be on the grants portal page
    When I extract the JWT token from the URL
    And I open localhost grants app with the JWT token

    # فتح المعاملات الواردة / Open incoming transactions
    And I click on "المعاملات الواردة" from the sidebar
    And I click "عرض" button on the first transaction row
#    And I click "متابعة" button to process the transaction

    # Tab 1 — الملخص (read-only) / Summary Tab
    Then the summary tab should be active and read-only
    And I click button to go to next tab

#     Tab 2 — بيانات فتح الملف / File Opening Data

    And I fill file opening data tab with:
      | fileNo              | F-TEST-001  |
      | fileOpeningDate     | 1447/01/10  |
      | orderSource         | ديوان الملك |
      | hafeezaNumber       | H-001       |
      | hafeezaDate         | 1447/09/10  |
      | hafeezaSource       | وزارة العدل |
      | customizationNumber | C-001       |
      | customizationDate   | 1447/09/10  |
      | operationNo         | OP-001      |
      | operationDate       | 1447/09/10  |
      | grantType           | منح ملكية   |
    And I click button to go to next tab

# Tab 3 — الملاحظات
    And I fill employee note "ملاحظة موظف المنح - اختبار تلقائي"
    And I click the "ارسال" button to submit the transaction
    Then I should see success message containing "تم"
    And I save the submitted transaction number


  # ──────────────────────────────────────────────────────
  # STEP 3 — Grant Manager finalizes the transaction
  # الخطوة 3 — مدير المنح ينهي المعاملة للقرعة الإلكترونية
  # ──────────────────────────────────────────────────────
  @Step3 @GrantManager
  Scenario: TC-E2E-03 — Grant Manager finalizes transaction for lottery
    # مدير المنح يسجل دخول وينهي المعاملة
    Given I navigate to the portal login page
    When I login with username "grant_manager" and password "Aa1357@a"
    Then I should be redirected to the apps page
    When I click on "المنح الملكية" app card
    Then I should be on the grants portal page
    When I extract the JWT token from the URL
    And I open localhost grants app with the JWT token

    # فتح المعاملات الواردة / Open incoming transactions
    And I click on "المعاملات الواردة" from the sidebar
    And I click "عرض" button on the first transaction row

    # Tab 1 — الملخص (read-only) / Summary Tab
    Then the summary tab should be displayed with owner data
    And I click button to go to next tab

    # Tab 2 — الملاحظات / Notes Tab
    And I fill manager note "اعتماد مدير المنح الملكية - اختبار تلقائي"
    And I click the "انهاء" button to submit the transaction

