package com.flowingcode.vaadin.addons.errorwindow;

import java.util.Optional;

import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class WindowErrorHandler implements ErrorHandler {

    private final Optional<String> errorMessage;

    private final UI ui;

    public WindowErrorHandler(final UI ui) {
        this(ui, null);
    }

    public WindowErrorHandler(final UI ui, final String errorMessage) {
        this.ui = ui;
        this.errorMessage = Optional.ofNullable(errorMessage);
    }

    @Override
    public void error(final ErrorEvent event) {
        new ErrorWindow(event.getThrowable(), errorMessage.orElseGet(() -> null)).open(ui);
    }

}
