/*-
 * #%L
 * Error Window Add-on
 * %%
 * Copyright (C) 2017 - 2021 Flowing Code
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

package com.flowingcode.vaadin.addons.errorwindow;

public final class ErrorManager {

  private static ErrorWindowFactory errorWindowFactory = new DefaultErrorWindowFactory();

  private ErrorManager() {
    throw new IllegalStateException("Utility class not meant to be instantiated");
  }

  public static void showError(Throwable throwable) {
    showError(throwable, throwable.getLocalizedMessage());
  }

  public static void showError(Throwable throwable, String cause) {
    ErrorDetails details = new ErrorDetails(throwable, cause);
    errorWindowFactory.showError(details);
  }

  public static void setErrorWindowFactory(ErrorWindowFactory errorWindowFactory) {
    ErrorManager.errorWindowFactory = errorWindowFactory;
  }

  public static ErrorWindowFactory getErrorWindowFactory() {
    return errorWindowFactory;
  }
}
