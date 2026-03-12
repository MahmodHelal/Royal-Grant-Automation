package core.context;

/**
 * TestContext — shared test context injected via PicoContainer across ALL step classes.
 *
 * This is the SINGLE source of truth for cross-step data sharing in a scenario.
 * It wraps ScenarioContext and is instantiated ONCE per scenario by PicoContainer.
 *
 * هذا هو المصدر الوحيد للحقيقة لمشاركة البيانات بين الـ steps داخل scenario واحد.
 * يُنشأ مرة واحدة فقط لكل scenario بواسطة PicoContainer.
 *
 * Problem it solves:
 *   Without TestContext, each step class calling `new ScenarioContext()` gets
 *   its own isolated map — transactionNumber set in CitizenSteps is invisible
 *   to EmployeeSteps and ManagerSteps.
 *
 *   بدون TestContext، كل step class يستدعي new ScenarioContext() يحصل على
 *   map معزولة خاصة به — رقم المعاملة المحفوظ في CitizenSteps يكون غير مرئي
 *   في EmployeeSteps وManagerSteps.
 *
 * Usage in step classes:
 *   public EmployeeSteps(TestContext ctx) { this.ctx = ctx; }
 *   ctx.getScenarioContext().set("transactionNumber", "١٤٤٧ / ٦٠");
 *   String num = ctx.getScenarioContext().get("transactionNumber");
 */
public class TestContext {

    private final ScenarioContext scenarioContext;

    public TestContext() {
        this.scenarioContext = new ScenarioContext();
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    /** Convenience shortcut. */
    public void set(String key, Object value) {
        scenarioContext.set(key, value);
    }

    /** Convenience shortcut — returns String directly (most values are strings). */
    public String get(String key) {
        Object val = scenarioContext.get(key);
        return val != null ? val.toString() : null;
    }

    public void clear() {
        scenarioContext.clear();
    }
}
