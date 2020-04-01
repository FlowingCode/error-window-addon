package com.flowingcode.vaadin.addons.errorwindow;

/*-
 * #%L
 * Error Window Add-on
 * %%
 * Copyright (C) 2017 - 2018 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

/**
 * Component to visualize an error, caused by an exception, as a modal sub-window. <br> 
 * When in production mode it shows a code to report. <br>
 * When in debug mode it allows to visualize the stack trace of the error.
 * 
 * @author pbartolo
 *
 */
@SuppressWarnings("serial")
public class ErrorWindow extends Dialog {

    private static final Logger logger = LoggerFactory.getLogger(ErrorWindow.class);

    private static final String DEFAULT_CAPTION = "<h3 style='margin-top:0px;text-align:center'>An error has occurred</h3>";

    private static final String DEFAULT_ERROR_LABEL_MESSAGE = "Please contact the system administrator for more information.";

    private VerticalLayout exceptionTraceLayout;

    private final Throwable cause;

    private final String errorMessage;

    private final String uuid;

	private boolean productionMode;

    public ErrorWindow(final Throwable cause) {
        this(cause, null, false);
    }

    public ErrorWindow(final Throwable cause, final String errorMessage) {
        this(cause, errorMessage, false);
    }

    public ErrorWindow(final Throwable cause, final String errorMessage, boolean productionMode) {
        super();
        assert cause != null;

        uuid = UUID.randomUUID().toString();
        this.cause = cause;
        this.errorMessage = errorMessage;
        this.productionMode = productionMode;
        initWindow();
    }
    
    public ErrorWindow(ErrorDetails errorDetails, boolean productionMode) {
    	this(errorDetails.getThrowable(),errorDetails.getCause(),productionMode);
    }

    private void initWindow() {
        logger.error(String.format("Error occurred %s", uuid), cause);
        setWidth("800px");
        setCloseOnEsc(true);
        add(createMainLayout());
        
    }

    /**
     * Creates the main layout of the ErrorWindow.
     */
    private VerticalLayout createMainLayout() {
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
       
        final Html title = new Html(DEFAULT_CAPTION);
        title.getElement().getStyle().set("width", "100%");
        mainLayout.add(title);
        
        final Html errorLabel = createErrorLabel();
        mainLayout.add(errorLabel);
        mainLayout.setHorizontalComponentAlignment(Alignment.START,errorLabel);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(false);
        buttonsLayout.setMargin(false);
        
        if (!productionMode) {
        	Button button = createDetailsButtonLayout();
        	buttonsLayout.add(button);
            mainLayout.add(createExceptionTraceLayout());
        }

        final Button closeButton = new Button("Close", event -> close());
        buttonsLayout.add(closeButton);
        mainLayout.add(buttonsLayout);
        mainLayout.setHorizontalComponentAlignment(Alignment.END, buttonsLayout);
        
        return mainLayout;
    }

    private Button createDetailsButtonLayout() {
        final Button errorDetailsButton = new Button("Show error detail", event -> {
    		boolean visible = !exceptionTraceLayout.isVisible();
        	exceptionTraceLayout.setVisible(visible);
        	if(visible) {
        		event.getSource().setIcon(VaadinIcon.MINUS.create());
        	} else {
                event.getSource().setIcon(VaadinIcon.PLUS.create());
        	}
        });
        errorDetailsButton.setIcon(VaadinIcon.PLUS.create());
        return errorDetailsButton;
    }

    
    private VerticalLayout createExceptionTraceLayout() {
        exceptionTraceLayout = new VerticalLayout();
        exceptionTraceLayout.setSpacing(false);
        exceptionTraceLayout.setMargin(false);
        exceptionTraceLayout.setPadding(false);
        exceptionTraceLayout.add(createStackTraceArea());
        exceptionTraceLayout.setVisible(false);
        return exceptionTraceLayout;
    }

    protected TextArea createStackTraceArea() {
        final TextArea area = new TextArea();
        area.setWidthFull();
        area.setHeight("15em");
//        area.setWordWrap(false);
//        area.setWidth(100, Unit.PERCENTAGE);
//        area.setRows(15);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintWriter pw = new PrintWriter(baos);
//        if (cause instanceof MethodException) {
//            cause.getCause().printStackTrace(pw);
//        } else {
            cause.printStackTrace(pw);
//        }
        pw.flush();
        area.setValue(baos.toString());
        return area;
    }

    protected Html createErrorLabel() {
        String label = errorMessage == null ? DEFAULT_ERROR_LABEL_MESSAGE : errorMessage;
        if (productionMode) {
            label = label.concat(String.format("<br />Please report the following code to system administrator:<h4><p><center>%s<center/></p></h4>", uuid));
        }
        final Html errorLabel = new Html("<span>" + label + "</span>");
        errorLabel.getElement().getStyle().set("width", "100%");
        return errorLabel;
    }

}