/*
 Title: ErrorForm
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debugging.info;

/**
 * Represents a bug report containing a unique identifier, description, and resolution status.
 */
public class ErrorForm {

    // Variables

    private String errorID;
    private String errorDescription;

    // Constructors

    /**
     * Constructs an ErrorForm object with an error ID and description.
     *
     * @param errorID          The unique identifier for the error.
     * @param errorDescription The description of the error.
     */
    public ErrorForm(String errorID, String errorDescription) {
        this.errorID = errorID;
        this.errorDescription = errorDescription;
    }

    // Public methods

    /**
     * Gets the unique identifier for the error.
     *
     * @return The error ID.
     */
    public String getErrorID() {
        return errorID;
    }

    /**
     * Gets the description of the error.
     *
     * @return The error description.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Sets the unique identifier for the error.
     *
     * @param errorID The error ID to set.
     */
    public void setErrorID(String errorID) {
        this.errorID = errorID;
    }

    /**
     * Sets the description of the error.
     *
     * @param errorDescription The error description to set.
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}/* End of ErrorForm class */