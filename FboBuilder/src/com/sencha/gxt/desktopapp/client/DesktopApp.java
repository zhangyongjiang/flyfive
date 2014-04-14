package com.sencha.gxt.desktopapp.client;

import com.gaoshin.fbobuilder.client.DetailsBorderLayoutContainer;
import com.gaoshin.fbobuilder.client.FlierBuilderBorderLayoutContainer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.desktopapp.client.canvas.CanvasViewImpl;
import com.sencha.gxt.desktopapp.client.event.LoginEvent;
import com.sencha.gxt.desktopapp.client.event.LoginEvent.LoginHandler;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent;
import com.sencha.gxt.desktopapp.client.event.LogoutEvent.LogoutHandler;
import com.sencha.gxt.desktopapp.client.service.LoginServiceProvider;
import com.sencha.gxt.desktopapp.client.service.ProfileServiceProvider;
import com.sencha.gxt.desktopapp.client.utility.Prompt;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

public class DesktopApp implements EntryPoint {
	private static CanvasViewImpl canvasImpl;

	private DesktopBus desktopBus;
	private DesktopAppPresenter desktopAppPresenter;
	private LoginHandler loginHandler;
	private LoginServiceProvider loginServiceProvider;
	private ProfileServiceProvider profileServiceProvider;
	private LogoutHandler logoutHandler;
	private LoginPresenter loginPresenter;
	private ProfilePresenter profilePresenter;

	public void loadModule(HasWidgets hasWidgets) {
		setBackground(hasWidgets);
		initializeDesktopBus(hasWidgets);
		if (!getDesktopAppPresenter().isLocalStorageSupported()) {
			loadApplicationAfterAlertingUser(hasWidgets);
		} else {
			loadApplication(hasWidgets);
		}
	}

	@Override
	public void onModuleLoad() {
		kickoff();
		exportGwtFunctions();
		setUncaughtExceptionHandler();
	}
	
	public static void kickoff() {
        StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));
        String id = Window.Location.getParameter("id");
		if(id == null) {
			FlierBuilderBorderLayoutContainer container = new FlierBuilderBorderLayoutContainer();
			RootPanel.get().add(container);
			canvasImpl = container.getCanvas();
        }
		else {
			DetailsBorderLayoutContainer container = new DetailsBorderLayoutContainer();
			canvasImpl = container.getCanvas();
		}
	}
	
	public static void allFontsLoaded() {
		canvasImpl.onFontsLoaded();
	}
	
	public static void imgLoaded() {
		canvasImpl.onImgLoaded();
	}
	
	public static native void exportGwtFunctions() /*-{
	   $wnd.allFontsLoaded = 
	      $entry(@com.sencha.gxt.desktopapp.client.DesktopApp::allFontsLoaded());
	   $wnd.imgLoaded = 
	      $entry(@com.sencha.gxt.desktopapp.client.DesktopApp::imgLoaded());
	}-*/;
	
	public static native void loadImg(String url) /*-{
	  $wnd.loadImg(url);
	}-*/;
	
	private static native String b64decode(String a) /*-{
	  return window.atob(a);
	}-*/;
	
	private String getDataFromUrl() {
        String data = Window.Location.getParameter("data");
        if(data == null || data.length() == 0)
        	return null;
        data = b64decode(data);
        return data;
	}

	private void checkForLogin(HasWidgets hasWidgets) {
		if (getDesktopAppPresenter().isLoggedIn()) {
			displayView(hasWidgets);
		} else {
			getDesktopBus().invokeLoginService();
		}
	}

	private void displayView(HasWidgets hasWidgets) {
		getDesktopAppPresenter().go(hasWidgets);
	}

	private DesktopAppPresenter getDesktopAppPresenter() {
		if (desktopAppPresenter == null) {
			desktopAppPresenter = new DesktopAppPresenterImpl(getDesktopBus());
		}
		return desktopAppPresenter;
	}

	private DesktopBus getDesktopBus() {
		if (desktopBus == null) {
			desktopBus = new DesktopBus();
		}
		return desktopBus;
	}

	private LoginHandler getLoginHandler(final HasWidgets hasWidgets) {
		if (loginHandler == null) {
			loginHandler = new LoginHandler() {
				@Override
				public void onLogin(LoginEvent loginEvent) {
					displayView(hasWidgets);
					if (loginEvent.isNewUser()) {
						getDesktopBus().invokeProfileService();
					}
				}
			};
		}
		return loginHandler;
	}

	private LoginPresenter getLoginPresenter() {
		if (loginPresenter == null) {
			loginPresenter = new LoginPresenterImpl(getDesktopAppPresenter());
		}
		return loginPresenter;
	}

	private LoginServiceProvider getLoginServiceProvider(HasWidgets rootPanel) {
		if (loginServiceProvider == null) {
			loginServiceProvider = new LoginServiceProvider(
			        getLoginPresenter(), rootPanel);
		}
		return loginServiceProvider;
	}

	private LogoutHandler getLogoutHandler() {
		if (logoutHandler == null) {
			logoutHandler = new LogoutHandler() {
				@Override
				public void onLogout(LogoutEvent logoutEvent) {
					com.google.gwt.user.client.Window.Location.reload();
				}
			};
		}
		return logoutHandler;
	}

	private ProfilePresenter getProfilePresenter() {
		if (profilePresenter == null) {
			profilePresenter = new ProfilePresenterImpl(
			        getDesktopAppPresenter());
		}
		return profilePresenter;
	}

	private ProfileServiceProvider getProfileServiceProvider(
	        HasWidgets hasWidgets) {
		if (profileServiceProvider == null) {
			profileServiceProvider = new ProfileServiceProvider(
			        getProfilePresenter(), hasWidgets);
		}
		return profileServiceProvider;
	}

	private void initializeDesktopBus(HasWidgets hasWidgets) {
		getDesktopBus().registerLoginService(
		        getLoginServiceProvider(hasWidgets));
		getDesktopBus().registerProfileService(
		        getProfileServiceProvider(hasWidgets));
		getDesktopBus().addLoginHandler(getLoginHandler(hasWidgets));
		getDesktopBus().addLogoutHandler(getLogoutHandler());
	}

	private void loadApplication(HasWidgets hasWidgets) {
		if ("clear".equals(History.getToken())) {
			promptToClearStorage(hasWidgets);
		} else {
			checkForLogin(hasWidgets);
		}
	}

	private void loadApplicationAfterAlertingUser(final HasWidgets hasWidgets) {
		Prompt.get()
		        .alert("Local Storage is Not Supported",
		                "Either your browser does not support HTML5 Local Storage or it is not configured for use by this application.<br/><br/>This application will continue to run, but anything you create will be discarded when the browser terminates or the browser window is refreshed.",
		                new Runnable() {
			                @Override
			                public void run() {
				                loadApplication(hasWidgets);
			                }
		                });
	}

	private void promptToClearStorage(final HasWidgets hasWidgets) {
		Prompt.get()
		        .confirm(
		                "Desktop",
		                "Would you like to clear this domain's local storage before continuing?",
		                new Runnable() {
			                @Override
			                public void run() {
				                getDesktopAppPresenter().clearLocalStorage();
				                checkForLogin(hasWidgets);
			                }
		                }, new Runnable() {
			                @Override
			                public void run() {
				                checkForLogin(hasWidgets);
			                }
		                });
	}

	private void setBackground(HasWidgets hasWidgets) {
		if (hasWidgets instanceof UIObject) {
			((UIObject) hasWidgets).addStyleName("x-desktop");
		}
	}

	private void setUncaughtExceptionHandler() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
				Throwable rootCause = getRootCause(e);
				new AlertMessageBox("Exception", rootCause.toString()).show();
			}
		});
	}

	private Throwable getRootCause(Throwable e) {
		Throwable lastCause;
		do {
			lastCause = e;
		} while ((e = e.getCause()) != null);
		return lastCause;
	}

}
