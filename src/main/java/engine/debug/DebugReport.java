/*
 * Title: BugReport
 * Date: 2023-11-09
 * Author: Kyle St John
 */
package engine.debug;

/**
 * BugReport class representing a report of a software bug.
 */
public class DebugReport {

    // Unique identifier for the bug
    private String bugID;

    // Description of the bug
    private String bugDescription;

    // Flag indicating whether the bug is resolved
    private boolean resolved = false;

    /**
     * Constructor for creating an unresolved bug report.
     *
     * @param bugID          The unique identifier of the bug.
     * @param bugDescription The description of the bug.
     */
    public DebugReport(String bugID, String bugDescription) {
        this.bugID = bugID;
        this.bugDescription = bugDescription;
    }

    /**
     * Constructor for creating a bug report with a specified resolution status.
     * Used for deserializing reports.
     *
     * @param bugID          The unique identifier of the bug.
     * @param bugDescription The description of the bug.
     * @param resolved       The resolution status of the bug.
     */
    public DebugReport(String bugID, String bugDescription, Boolean resolved) {
        this.bugID = bugID;
        this.bugDescription = bugDescription;
        this.resolved = resolved;
    }

    /**
     * Gets the unique identifier of the bug.
     *
     * @return The bug ID.
     */
    public String getBugID() {
        return bugID;
    }

    /**
     * Gets the description of the bug.
     *
     * @return The bug description.
     */
    public String getBugDescription() {
        return bugDescription;
    }

    /**
     * Sets the unique identifier of the bug.
     *
     * @param bugID The new bug ID.
     */
    public void setBugID(String bugID) {
        this.bugID = bugID;
    }

    /**
     * Sets the description of the bug.
     *
     * @param bugDescription The new bug description.
     */
    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    /**
     * Checks if the bug is resolved.
     *
     * @return True if the bug is resolved, false otherwise.
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Sets the resolution status of the bug.
     *
     * @param resolved The new resolution status.
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
/* End of BugReport class */