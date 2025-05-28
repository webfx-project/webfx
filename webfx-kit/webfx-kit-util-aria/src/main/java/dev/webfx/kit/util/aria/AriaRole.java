package dev.webfx.kit.util.aria;

/**
 * Enum representing ARIA (Accessible Rich Internet Applications) roles as defined by W3C.
 * These roles are organized into six categories:
 * 1. Document Structure roles - Used to describe the structure of a document
 * 2. Widget roles - Used to describe common UI widgets
 * 3. Landmark roles - Used to identify large content areas for easy navigation
 * 4. Live Region roles - Used to indicate regions that may change dynamically
 * 5. Window roles - Used to describe dialog windows
 * 6. Abstract roles - Base roles that are typically not used directly
 *
 * @author Bruno Salmon
 * @see <a href="https://www.w3.org/TR/wai-aria-1.1/#role_definitions">W3C ARIA Role Definitions</a>
 */
public enum AriaRole {

    // ========== Document Structure roles ==========
    /**
     * A section of a page that consists of a composition that forms an independent part of a document, page, or site.
     */
    ARTICLE("article"),

    /**
     * A cell in a tabular container.
     */
    CELL("cell"),

    /**
     * A cell containing header information for a column.
     */
    COLUMNHEADER("columnheader"),

    /**
     * A definition of a term or concept.
     */
    DEFINITION("definition"),

    /**
     * A list of references to members of a group, such as a static table of contents.
     */
    DIRECTORY("directory"),

    /**
     * A region containing related information that is declared as document content.
     */
    DOCUMENT("document"),

    /**
     * A scrollable list of articles where scrolling may cause articles to be added to or removed from the DOM.
     */
    FEED("feed"),

    /**
     * A perceivable section containing content that is tangential to the main content of the resource.
     */
    FIGURE("figure"),

    /**
     * A set of user interface objects which are not intended to be included in a page summary or table of contents.
     */
    GROUP("group"),

    /**
     * A heading for a section of the page.
     */
    HEADING("heading"),

    /**
     * A container for a collection of elements that form an image.
     */
    IMG("img"),

    /**
     * A group of non-interactive list items.
     */
    LIST("list"),

    /**
     * A single item in a list or directory.
     */
    LISTITEM("listitem"),

    /**
     * Content that represents a mathematical expression.
     */
    MATH("math"),

    /**
     * An element whose implicit native role semantics will not be mapped to the accessibility API.
     */
    NONE("none"),

    /**
     * A section whose content is parenthetic or ancillary to the main content of the resource.
     */
    NOTE("note"),

    /**
     * An element whose implicit native role semantics will not be mapped to the accessibility API.
     * Same as 'none'.
     */
    PRESENTATION("presentation"),

    /**
     * A row of cells in a tabular container.
     */
    ROW("row"),

    /**
     * A structure containing one or more row elements in a tabular container.
     */
    ROWGROUP("rowgroup"),

    /**
     * A cell containing header information for a row in a grid.
     */
    ROWHEADER("rowheader"),

    /**
     * A divider that separates and distinguishes sections of content or groups of menuitems.
     */
    SEPARATOR("separator"),

    /**
     * A section containing data arranged in rows and columns.
     */
    TABLE("table"),

    /**
     * A word or phrase with a corresponding definition.
     */
    TERM("term"),

    /**
     * A collection of commonly used function buttons or controls represented in compact visual form.
     */
    TOOLBAR("toolbar", true),

    /**
     * A contextual popup that displays a description for an element.
     */
    TOOLTIP("tooltip"),

    // ========== Widget roles ==========
    /**
     * An input that allows for user-triggered actions when clicked or pressed.
     */
    BUTTON("button", AriaSelectedAttribute.PRESSED),

    /**
     * A checkable input that has three possible values: true, false, or mixed.
     */
    CHECKBOX("checkbox", AriaSelectedAttribute.CHECKED),

    /**
     * A presentation of a select; usually similar to a textbox where users can type ahead to select an option.
     */
    COMBOBOX("combobox", true),

    /**
     * A cell in a grid or treegrid.
     */
    GRIDCELL("gridcell", AriaSelectedAttribute.SELECTED),

    /**
     * An interactive reference to an internal or external resource.
     */
    LINK("link", true),

    /**
     * A type of widget that offers a list of choices to the user.
     */
    MENU("menu", true),

    /**
     * A presentation of menu that usually remains visible and is usually presented horizontally.
     */
    MENUBAR("menubar", true),

    /**
     * An option in a set of choices contained by a menu or menubar.
     */
    MENUITEM("menuitem", AriaSelectedAttribute.SELECTED),

    /**
     * A menuitem with a checkable state whose possible values are true, false, or mixed.
     */
    MENUITEMCHECKBOX("menuitemcheckbox", AriaSelectedAttribute.CHECKED),

    /**
     * A checkable menuitem in a set of elements with the same role, only one of which can be checked at a time.
     */
    MENUITEMRADIO("menuitemradio", AriaSelectedAttribute.CHECKED),

    /**
     * A selectable item in a select list.
     */
    OPTION("option", AriaSelectedAttribute.SELECTED),

    /**
     * An element that displays the progress status for tasks that take a long time.
     */
    PROGRESSBAR("progressbar"),

    /**
     * A checkable input in a group of elements with the same role, only one of which can be checked at a time.
     */
    RADIO("radio", AriaSelectedAttribute.CHECKED),

    /**
     * A group of radio buttons where only one radio button can be checked at a time.
     */
    RADIOGROUP("radiogroup", true),

    /**
     * A graphical object that controls the scrolling of content within a viewing area.
     */
    SCROLLBAR("scrollbar", true),

    /**
     * A type of textbox intended for specifying search criteria.
     */
    SEARCHBOX("searchbox", true),

    /**
     * A user input where the user selects a value from within a given range.
     */
    SLIDER("slider", true),

    /**
     * A form of range that expects the user to select from among discrete choices.
     */
    SPINBUTTON("spinbutton", true),

    /**
     * A widget that may be expanded or collapsed, showing or hiding its content.
     */
    DISCLOSURE("disclosure", true),

    /**
     * A type of checkbox that represents on/off values, as opposed to checked/unchecked values.
     */
    SWITCH("switch", AriaSelectedAttribute.CHECKED),

    /**
     * A grouping label providing a mechanism for selecting the tab content that is to be rendered to the user.
     */
    TAB("tab", AriaSelectedAttribute.SELECTED),

    /**
     * A container for the resources associated with a tab.
     */
    TABPANEL("tabpanel"),

    /**
     * Input that allows free-form text as its value.
     */
    TEXTBOX("textbox", true),

    /**
     * An option item of a tree. This is an element within a tree that may be expanded or collapsed.
     */
    TREEITEM("treeitem", AriaSelectedAttribute.SELECTED),

    // ========== Landmark roles ==========
    /**
     * A region that contains mostly site-oriented content, rather than page-specific content.
     */
    BANNER("banner"),

    /**
     * A supporting section of the document, designed to be complementary to the main content.
     */
    COMPLEMENTARY("complementary"),

    /**
     * A large perceivable region that contains information about the parent document.
     */
    CONTENTINFO("contentinfo"),

    /**
     * A landmark region that contains a collection of items and objects that, as a whole, combine to create a form.
     */
    FORM("form", true),

    /**
     * The main content of a document.
     */
    MAIN("main"),

    /**
     * A collection of navigational elements (usually links) for navigating the document or related documents.
     */
    NAVIGATION("navigation", true),

    /**
     * A perceivable section containing content that is relevant to a specific, author-specified purpose.
     */
    REGION("region"),

    /**
     * A landmark region that contains a search feature for the site.
     */
    SEARCH("search", true),

    // ========== Live Region roles ==========
    /**
     * A type of live region with important, and usually time-sensitive, information.
     */
    ALERT("alert"),

    /**
     * A type of live region where new information is added in meaningful order and old information may disappear.
     */
    LOG("log"),

    /**
     * A type of live region where non-essential information changes frequently.
     */
    MARQUEE("marquee"),

    /**
     * A container whose content is advisory information for the user but is not important enough to justify an alert.
     */
    STATUS("status"),

    /**
     * A type of live region containing a numerical counter which indicates an amount of elapsed time from a start point.
     */
    TIMER("timer"),

    // ========== Window roles ==========
    /**
     * A type of dialog that contains an alert message where initial focus goes to an element within the dialog.
     */
    ALERTDIALOG("alertdialog", true),

    /**
     * A dialog is a window overlaid on either the primary window or another dialog window.
     */
    DIALOG("dialog", true),

    // ========== Abstract roles ==========
    /**
     * A form of widget that performs an action but does not receive input data.
     */
    COMMAND("command", true),

    /**
     * A widget that may contain navigable descendants or owned children.
     */
    COMPOSITE("composite", true),

    /**
     * A generic type of widget that allows user input.
     */
    INPUT("input", true),

    /**
     * A region of a page intended as a navigational landmark.
     */
    LANDMARK("landmark", true),

    /**
     * An input representing a range of values that can be set by the user.
     */
    RANGE("range", true),

    /**
     * The base role from which all other roles inherit.
     */
    ROLETYPE("roletype"),

    /**
     * A structure containing related elements.
     */
    SECTION("section"),

    /**
     * A structure that labels or summarizes the topic of its related section.
     */
    SECTIONHEAD("sectionhead"),

    /**
     * A form widget that allows the user to make selections from a set of choices.
     */
    SELECT("select", true),

    /**
     * A document structural element.
     */
    STRUCTURE("structure"),

    /**
     * An interactive component of a graphical user interface.
     */
    WIDGET("widget", true),

    /**
     * A browser or application window.
     */
    WINDOW("window", true);

    private final String ariaRole;
    private final boolean focusable;
    private final AriaSelectedAttribute selectedAttribute;

    AriaRole(String ariaRole) {
        this(ariaRole, false);
    }

    AriaRole(String ariaRole, boolean focusable) {
        this(ariaRole, focusable, null);
    }

    AriaRole(String ariaRole, AriaSelectedAttribute stateAttribute) {
        this(ariaRole, true, stateAttribute);
    }

    AriaRole(String ariaRole, boolean focusable, AriaSelectedAttribute stateAttribute) {
        this.ariaRole = ariaRole;
        this.focusable = focusable;
        this.selectedAttribute = stateAttribute;
    }

    /**
     * Returns the string representation of this ARIA role as defined by W3C.
     * 
     * @return the string representation of this ARIA role
     */
    @Override
    public String toString() {
        return ariaRole;
    }

    /**
     * Returns whether this ARIA role is focusable.
     * Focusable roles may need a tabindex attribute to be focusable in HTML.
     * 
     * @return true if this role is focusable, false otherwise
     */
    public boolean isFocusable() {
        return focusable;
    }

    /**
     * Returns the ARIA state attribute that should be used to reflect the state of this role.
     * 
     * @return the ARIA state attribute for this role
     */
    public AriaSelectedAttribute getSelectedAttribute() {
        return selectedAttribute;
    }
}
