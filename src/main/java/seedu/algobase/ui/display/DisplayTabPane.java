package seedu.algobase.ui.display;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import seedu.algobase.commons.core.index.Index;
import seedu.algobase.model.gui.TabManager;
import seedu.algobase.model.gui.WriteOnlyTabManager;
import seedu.algobase.storage.SaveStorageRunnable;
import seedu.algobase.ui.UiPart;

/**
 * Pane containing the different tabs.
 */
public class DisplayTabPane extends UiPart<Region> {

    private static final String FXML = "DisplayTabPane.fxml";

    @FXML
    private TabPane tabsPlaceholder;

    public DisplayTabPane(TabManager tabManager, SaveStorageRunnable saveStorageRunnable, DisplayTab... displayTabs) {
        super(FXML);

        addTabsToTabPane(displayTabs);
        selectTab(tabManager.getDisplayTabPaneIndex().getValue().intValue());

        addListenerForIndexChange(tabManager.getDisplayTabPaneIndex());
        addListenerToTabPaneIndexChange(tabManager, saveStorageRunnable);
    }

    /**
     * Adds a list of display tabs to the tab pane.
     *
     * @param displayTabs List of tabs to be displayed.
     */
    private void addTabsToTabPane(DisplayTab... displayTabs) {
        for (DisplayTab displayTab: displayTabs) {
            this.tabsPlaceholder.getTabs().add(displayTab.getTab());
        }
    }

    /**
     * Adds a listener to the tab pane that watches for an index change.
     *
     * @param displayTabPaneIndex The observable index.
     */
    private void addListenerForIndexChange(ObservableIntegerValue displayTabPaneIndex) {
        displayTabPaneIndex.addListener((observable, oldValue, newValue) -> {
            selectTab((newValue.intValue()));
        });
    }

    /**
     * Adds an index change listener to the tab pane.
     *
     * @param tabManager A callback function for when the index of the tabPane changes.
     */
    private void addListenerToTabPaneIndexChange(
        WriteOnlyTabManager tabManager, SaveStorageRunnable saveStorageRunnable) {
        this.tabsPlaceholder.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tabManager.switchDisplayTab(Index.fromZeroBased(newValue.intValue()));
                saveStorageRunnable.save();
            }
        });
    }

    /**
     * Selects the tab to be displayed.
     *
     * @param index the index of the tab in the tab pane to be selected.
     */
    public void selectTab(int index) {
        tabsPlaceholder.getSelectionModel().select(index);
    }
}