
module com.github.richardroda.unit.test {
    /**
     * Build fails when this requires is missing and succeeds
     * when it is present.  This verifies that the module is
     * present and enforced.
     */
    requires com.github.richardroda.util.closeit;
}