package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  @author Victor Chen
 */
class Menubar {

    /** Fonts. */
    private static final Font
            SIGN_FONT = new Font("LucidaGrande", Font.BOLD, 14);

    /** Commands. */
    private static final String
            SIGN = "Victor", FILE = "File", EDIT = "Edit", VIEW = "View",
            SEL = "Selection", HELP = "Help", ABOUT = "About ...", QUIT = "Quit",

            NEW_WINDOW = "New Window", NEW_FILE = "New File", OPEN = "Open",
            REOPEN = "Reopen", REOPEN_LAST = "Reopen Last Item", SAVE = "Save",
            SAVE_AS = "Save As...", SAVE_ALL = "Save All", CLOSE_TAB = "Close Tab",
            CLOSE_ALL = "Close All", CLEAR_H = "Clear History",

            UNDO = "Undo", REDO = "Redo", CUT = "Cut", COPY = "Copy",
            COPY_PATH = "Copy Path", COPY_REF = "Copy Reference", PASTE = "Paste",
            DELETE = "Delete", FIND = "Find", REPLACE = "Replace",
            GOTOLINE = "Go to Line", SELECT_ALL = "Select All", TD = "Time & Date",

            SEL_TO_TOP = "Select to Top", SEL_TO_BOTTOM = "Select to Bottom",
            SEL_LINE = "Select Line", SEL_WORD = "Select Word",
            SEL_TO_BoW = "Select to Beginning of Word",
            SEL_TO_BoL = "Select to Beginning of Line",
            SEL_TO_EoW = "Select to End of Word",
            SEL_TO_EoL = "Select to End of Line",
            SEL_TO_FCoL = "Select to First Character of Line",
            SEL_IB = "Select Inside Brackets",

            STATUS_BAR = "Status Bar", INDEX_BAR = "Index Bar",
            TEXT_C = "Text Color", PAD_C = "Pad Color";

    Menubar(GUI gui) {
        _menuBar = new JMenuBar();
        _manager = new Commands(gui);
    }

    /** Create all the menus in menuBar. */
    JMenuBar createMenuBar() {
        createSignMenu();
        createFileMenu();
        createEditMenu();
        createViewMenu();
        createSelectionMenu();
        createHelpMenu();
        return _menuBar;
    }

    /** Get file manager. */
    Commands getManager() {
        return _manager;
    }

    /** Get IndexBar from current IndexBar. */
    JCheckBoxMenuItem getIndexBar() {
        return _indexbar;
    }

    /** Create history JMenuItems in REOPEN JMenu. */
    void createHistory() {
        if (_manager.getHistory().size() == 0) {
            _reopen.setEnabled(false);
            _reopenlast.setEnabled(false);
        } else {
            _reopen.setEnabled(true);
            _reopenlast.setEnabled(true);
            _reopen.removeAll();
            createMenuItem(CLEAR_H, _reopen, e -> _manager.clearHistory());
            _reopen.addSeparator();
            for (Editor ed : _manager.getHistory()) {
                JMenuItem newItem = new JMenuItem(ed.getFile().getAbsolutePath());
                newItem.addActionListener(e -> _manager.open(ed.getFile()));
                _reopen.add(newItem, 2);
            }
        }
    }

    /** Create Signature Menu. */
    private void createSignMenu() {
        JMenu menu = createMenu(SIGN, _menuBar);
        menu.setFont(SIGN_FONT);
        createMenuItem(ABOUT, menu, null);
        menu.addSeparator();
        createMenuItem(QUIT, menu, KeyEvent.VK_Q, false, e -> System.exit(0));
    }

    /** Create File Menu, including all the commands of file operation. */
    private void createFileMenu() {
        JMenu menu = createMenu(FILE, _menuBar);
        createMenuItem(NEW_WINDOW, menu, KeyEvent.VK_N, true, e -> new GUI());
        createMenuItem(NEW_FILE, menu, KeyEvent.VK_N, false, e -> _manager.newFile());
        createMenuItem(OPEN, menu, KeyEvent.VK_O, false, e -> _manager.openFile());
        _reopen = createMenu(REOPEN, menu);
        _reopen.setEnabled(false);
        _reopenlast = createMenuItem(REOPEN_LAST, menu, KeyEvent.VK_T, true, e -> _manager.openLastFile());
        _reopenlast.setEnabled(false);
        menu.addSeparator();
        createMenuItem(SAVE, menu, KeyEvent.VK_S, false, e -> _manager.saveFile());
        createMenuItem(SAVE_AS, menu, KeyEvent.VK_S, true, e -> _manager.saveAsFile());
        createMenuItem(SAVE_ALL, menu, e -> _manager.saveAll());
        menu.addSeparator();
        createMenuItem(CLOSE_TAB, menu, KeyEvent.VK_W, false, e -> _manager.closeFile());
        createMenuItem(CLOSE_ALL, menu, e -> _manager.closeAll());
    }

    /** Create Edit Menu, including all the commands of file editing. */
    private void createEditMenu() {
        JMenu menu = createMenu(EDIT, _menuBar);
        createMenuItem(UNDO, menu, KeyEvent.VK_Z, false, e -> _manager.undo(true));
        createMenuItem(REDO, menu, KeyEvent.VK_Z, true, e -> _manager.undo(false));
        menu.addSeparator();
        createMenuItem(CUT, menu, KeyEvent.VK_X, false, e -> _manager.simpleEditCommand(1));
        createMenuItem(COPY, menu, KeyEvent.VK_C, false, e -> _manager.simpleEditCommand(2));
        createMenuItem(COPY_PATH, menu, KeyEvent.VK_C, true, e -> _manager.copyInfo(true));
        createMenuItem(COPY_REF, menu, e -> _manager.copyInfo(false));
        createMenuItem(PASTE, menu, KeyEvent.VK_V, false, e -> _manager.simpleEditCommand(3));
        JMenuItem delete = createMenuItem(DELETE, menu, e -> _manager.simpleEditCommand(4));
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menu.addSeparator();
        createMenuItem(FIND, menu, KeyEvent.VK_F, false, e -> _manager.search());
        createMenuItem(REPLACE, menu, KeyEvent.VK_R, false, e -> _manager.search());
        createMenuItem(GOTOLINE, menu, KeyEvent.VK_G, false, e -> _manager.goTo());
        menu.addSeparator();
        createMenuItem(SELECT_ALL, menu, KeyEvent.VK_A, false, e -> _manager.simpleEditCommand(5));
        JMenuItem time = createMenuItem(TD, menu, e -> _manager.simpleEditCommand(6));
        time.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    }

    /** Create View Menu. */
    private void createViewMenu() {
        JMenu menu = createMenu(VIEW, _menuBar);
        JCheckBoxMenuItem _statusbar = createJCheckBoxMenuItem(STATUS_BAR, menu);
        _statusbar.addActionListener(e -> _manager.showStatusBar(_statusbar.isSelected()));
        _indexbar = createJCheckBoxMenuItem(INDEX_BAR, menu);
        _indexbar.addActionListener(e -> _manager.showIndexBar(_indexbar.isSelected()));
        menu.addSeparator();
        createStyleMenu(menu, _manager.getGUI().getFrame());
        menu.addSeparator();
        createMenuItem(TEXT_C, menu, e -> _manager.setColor(true));
        createMenuItem(PAD_C, menu, e -> _manager.setColor(false));
    }

    /** Create Selection Menu, including all the commands of editor selection options. */
    private void createSelectionMenu() {
        JMenu menu = createMenu(SEL, _menuBar);
        createMenuItem(SEL_TO_TOP, menu, KeyEvent.VK_UP, true, e -> _manager.select(1));
        createMenuItem(SEL_TO_BOTTOM, menu, KeyEvent.VK_DOWN, true, e -> _manager.select(2));
        menu.addSeparator();
        createMenuItem(SEL_LINE, menu, KeyEvent.VK_L, false, e -> _manager.select(3));
        createMenuItem(SEL_WORD, menu, KeyEvent.VK_W, true, e -> _manager.select(4));
        createMenuItem(SEL_TO_BoW, menu, KeyEvent.VK_LEFT, false, e -> _manager.select(5));
        createMenuItem(SEL_TO_BoL, menu, e -> _manager.select(6));
        createMenuItem(SEL_TO_FCoL, menu, KeyEvent.VK_LEFT, true, e -> _manager.select(7));
        createMenuItem(SEL_TO_EoW, menu, KeyEvent.VK_RIGHT, false, e -> _manager.select(8));
        createMenuItem(SEL_TO_EoL, menu, KeyEvent.VK_RIGHT, true, e -> _manager.select(9));
        createMenuItem(SEL_IB, menu, KeyEvent.VK_M, true, e -> _manager.select(0));
    }

    /** Create Help Menu. */
    private void createHelpMenu() {
        JMenu menu = createMenu(HELP, _menuBar);
    }

    /** Add a Menu with title NAME to JMenubar menuBar. */
    private JMenu createMenu(String name, JMenuBar menuBar) {
        JMenu menu = new JMenu(name);
        menuBar.add(menu);
        return menu;
    }

    /** Add a Menu with title NAME to parental Menu toMenu. */
    private JMenu createMenu(String name, JMenu toMenu) {
        JMenu addMenu = new JMenu(name);
        toMenu.add(addMenu);
        return addMenu;
    }

    /** Add a Menuitem without accelerator to MENU. */
    private JMenuItem createMenuItem(String command, JMenu toMenu,
                                ActionListener actListener) {
        return createMenuItem(command, toMenu, 0, false, actListener);
    }

    /** Add a JCheckBoxMenuitem to MENU. */
    private JCheckBoxMenuItem createJCheckBoxMenuItem(String name, JMenu toMenu) {
        JCheckBoxMenuItem boxitem = new JCheckBoxMenuItem(name);
        boxitem.setSelected(true);
        toMenu.add(boxitem);
        return boxitem;
    }

    /** Add a menuitem to MENU. If it has an accelerator key, set ctrl or
     * ctrl + shift commend based on boolean ADVANCE. */
    private JMenuItem createMenuItem(String command, JMenu toMenu, int aclKey,
                                boolean advance, ActionListener actListener) {
        JMenuItem menuitem = new JMenuItem(command);
        menuitem.addActionListener(actListener);
        if (aclKey != 0) {
            if (advance) {
                menuitem.setAccelerator(KeyStroke.getKeyStroke(aclKey,
                        InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
            } else {
                menuitem.setAccelerator(KeyStroke.getKeyStroke(aclKey,
                        InputEvent.CTRL_MASK));
            }
        }
        toMenu.add(menuitem);
        return menuitem;
    }

    /** Create styleMenu. */
    private void createStyleMenu(JMenu menu, Component component) {
        new GUIStyles(menu, component);
    }

    /** Menu INDEXBAR. */
    private JCheckBoxMenuItem _indexbar;

    /** Menu REOPEN_LAST. */
    private JMenuItem _reopenlast;

    /** Menu REOPEN. */
    private JMenu _reopen;

    /** MenuBar. */
    private JMenuBar _menuBar;

    /** File Commends Manager . */
    private Commands _manager;
}
