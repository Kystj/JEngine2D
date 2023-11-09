/*
 Title: BugReport
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.debug;

public class BugReport {
    private String bugID;
    private String bugDescription;
    private boolean resolved = false;

    public BugReport(String bugID, String bugDescription) {
        this.bugID = bugID;
        this.bugDescription = bugDescription;
    }

    public BugReport(String bugID, String bugDescription, Boolean resolved) {
        this.bugID = bugID;
        this.bugDescription = bugDescription;
        this.resolved = resolved;
    }


    public String getBugID() {
        return bugID;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugID(String bugID) {
        this.bugID = bugID;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
/*End of BugReport class*/
