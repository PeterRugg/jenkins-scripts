// https://stackoverflow.com/questions/39920437/how-to-access-junit-test-counts-in-jenkins-pipeline-project

import hudson.tasks.test.AbstractTestResultAction
import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def testStatuses() {
    def testStatus = ""
    def rawBuild = currentBuild.rawBuild;
    if (rawBuild == null)
        return testStatus
    AbstractTestResultAction testResultAction = rawBuild.getAction(AbstractTestResultAction.class)
    if (testResultAction != null) {
        def total = testResultAction.totalCount
        def failed = testResultAction.failCount
        def skipped = testResultAction.skipCount
        def passed = total - failed - skipped
        testStatus = "Test Status:\n  Passed: ${passed}, Failed: ${failed} ${testResultAction.failureDiffString}, Skipped: ${skipped}"

        if (failed == 0) {
            currentBuild.result = 'SUCCESS'
        }
    }
    return testStatus
}

def call() {
    return testStatuses()
}
