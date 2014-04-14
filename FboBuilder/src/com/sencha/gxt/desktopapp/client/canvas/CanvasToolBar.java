package com.sencha.gxt.desktopapp.client.canvas;

import com.gaoshin.fbobuilder.client.FlyerContext;
import com.gaoshin.fbobuilder.client.message.CopyMsg;
import com.gaoshin.fbobuilder.client.message.DeleteShapeMsg;
import com.gaoshin.fbobuilder.client.message.NewFlyerMsg;
import com.gaoshin.fbobuilder.client.message.NewFolderMsg;
import com.gaoshin.fbobuilder.client.message.NewLayerMsg;
import com.gaoshin.fbobuilder.client.message.NewNodeMsg;
import com.gaoshin.fbobuilder.client.message.NewVideoMsg;
import com.gaoshin.fbobuilder.client.message.PasteMsg;
import com.gaoshin.fbobuilder.client.message.ZoomInMsg;
import com.gaoshin.fbobuilder.client.message.ZoomOutMsg;
import com.gaoshin.fbobuilder.client.model.Fcircle;
import com.gaoshin.fbobuilder.client.model.Fellipse;
import com.gaoshin.fbobuilder.client.model.Fimage;
import com.gaoshin.fbobuilder.client.model.Fline;
import com.gaoshin.fbobuilder.client.model.Fpolygon;
import com.gaoshin.fbobuilder.client.model.Frectangle;
import com.gaoshin.fbobuilder.client.model.FregularPolygon;
import com.gaoshin.fbobuilder.client.model.Ftext;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.desktopapp.client.canvas.images24.Images24;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class CanvasToolBar implements IsWidget {

	private static final String FILE_TYPE = "fileType";

	private ToolBar toolBar;
	private TextButton btnNewLine;
	private TextButton btnNewText;
	private TextButton createProgramTextButton;
	private TextButton btnNewEllipse;
	private TextButton btnNewCircle;
	private TextButton btnNewRect;
	private TextButton btnRemoveSelected;
	private TextButton zoomoutButton;
	private TextButton zoominButton;
	private TextButton layersButton;
	private SelectHandler newLineHandler;
	private SelectHandler circleSelectHandler;
	private SelectHandler rectSelectHandler;
	private SelectHandler deleteSelectHandler;
	private SelectHandler newEllipseHandler;
	private TextButton btnNewPolygon;
	private TextButton btnNewRegularPolygon;
	private SelectHandler newTextHandler;
	private SelectHandler zoominHandler;
	private SelectHandler zoomoutHandler;
	private SelectHandler newPolygonHandler;
	private SelectHandler newRegularPolygonHandler;
	private SelectHandler newImageHandler;
	private SelectHandler layersHandler;
	private TextButton btnNewFlyer;
	private TextButton btnNewVideo;
	private TextButton btnPolygon;
	private TextButton btnFolder;
	private TextButton btnCopy;
	private TextButton btnPaste;
	private TextButton btnHOme;
	private SelectHandler newFlyerHandler;
	private SelectHandler newVideoHandler;
	private SelectHandler polygonHandler;
	private SelectHandler folderHandler;

	public Widget asWidget() {
		return getToolBar();
	}

	private SelectHandler getNewCircleHandler() {
		if (circleSelectHandler == null) {
			circleSelectHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Fcircle.class.getName()));
				}
			};
		}
		return circleSelectHandler;
	}

	private SelectHandler getNewRectangleHandler() {
		if (rectSelectHandler == null) {
			rectSelectHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Frectangle.class.getName()));
				}
			};
		}
		return rectSelectHandler;
	}

	private TextButton getNewCircleButton() {
		if (btnNewCircle == null) {
			btnNewCircle = new TextButton();
			btnNewCircle.setToolTip("New&nbsp;Circle");
			btnNewCircle.setIcon(Images24.getImageResources().circle_24());
			btnNewCircle.addSelectHandler(getNewCircleHandler());
		}
		return btnNewCircle;
	}

	private TextButton getNewRectButton() {
		if (btnNewRect == null) {
			btnNewRect = new TextButton();
			btnNewRect.setToolTip("New&nbsp;Rectangle");
			btnNewRect.setIcon(Images24.getImageResources().rectangle_24());
			btnNewRect.addSelectHandler(getNewRectangleHandler());
		}
		return btnNewRect;
	}

	private TextButton getNewEllipseButton() {
		if (btnNewEllipse == null) {
			btnNewEllipse = new TextButton();
			btnNewEllipse.setToolTip("New&nbsp;Ellipse");
			btnNewEllipse.setIcon(Images24.getImageResources().ellipse_24());
			btnNewEllipse.setData(FILE_TYPE, FileType.BOOKMARK);
			btnNewEllipse.addSelectHandler(getNewEllipseHandler());
		}
		return btnNewEllipse;
	}

	private TextButton getNewImageButton() {
		if (createProgramTextButton == null) {
			createProgramTextButton = new TextButton();
			createProgramTextButton.setToolTip("New&nbsp;Image");
			createProgramTextButton.setIcon(Images24.getImageResources()
			        .picture_24());
			createProgramTextButton.setData(FILE_TYPE, FileType.PROGRAM);
			createProgramTextButton.addSelectHandler(getNewImageHandler());
		}
		return createProgramTextButton;
	}

	private TextButton getNewLineButton() {
		if (btnNewLine == null) {
			btnNewLine = new TextButton();
			btnNewLine.setToolTip("New&nbsp;Line");
			btnNewLine.setIcon(Images24.getImageResources().line_24());
			btnNewLine.setData(FILE_TYPE, FileType.PROGRAM);
			btnNewLine.addSelectHandler(getNewLineHandler());
		}
		return btnNewLine;
	}

	private SelectHandler getNewEllipseHandler() {
		if (newEllipseHandler == null) {
			newEllipseHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Fellipse.class.getName()));
				}
			};
		}
		return newEllipseHandler;
	}

	private SelectHandler getNewImageHandler() {
		if (newImageHandler == null) {
			newImageHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Fimage.class.getName()));
				}
			};
		}
		return newImageHandler;
	}

	private SelectHandler getNewLineHandler() {
		if (newLineHandler == null) {
			newLineHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Fline.class.getName()));
				}
			};
		}
		return newLineHandler;
	}

	private TextButton getNewTextButton() {
		if (btnNewText == null) {
			btnNewText = new TextButton();
			btnNewText.setToolTip("New&nbsp;Text");
			btnNewText.setIcon(Images24.getImageResources().text_icon_24());
			btnNewText.setData(FILE_TYPE, FileType.SPREADSHEET);
			btnNewText.addSelectHandler(getNewTextHandler());
		}
		return btnNewText;
	}

	private SelectHandler getNewTextHandler() {
		if (newTextHandler == null) {
			newTextHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Ftext.class.getName()));
				}
			};
		}
		return newTextHandler;
    }

	private SelectHandler getDeleteSelectHandler() {
		if (deleteSelectHandler == null) {
			deleteSelectHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new DeleteShapeMsg());
				}
			};
		}
		return deleteSelectHandler;
	}

	private TextButton getRemoveSelectedButton() {
		if (btnRemoveSelected == null) {
			btnRemoveSelected = new TextButton();
			btnRemoveSelected.setToolTip("Remove&nbsp;Selected");
			btnRemoveSelected.setIcon(Images24.getImageResources().remove_24());
			btnRemoveSelected.addSelectHandler(getDeleteSelectHandler());
		}
		return btnRemoveSelected;
	}

	private TextButton getZoominButton() {
		if (zoominButton == null) {
			zoominButton = new TextButton();
			zoominButton.setToolTip("Zoom&nbsp;In");
			zoominButton.setIcon(Images24.getImageResources().zoomin_24());
			zoominButton.addSelectHandler(getZoominHandler());
		}
		return zoominButton;
	}

	private TextButton getLayersButton() {
		if (layersButton == null) {
			layersButton = new TextButton();
			layersButton.setToolTip("New&nbsp;Layer");
			layersButton.setIcon(Images24.getImageResources().layers_34());
			layersButton.addSelectHandler(getLayersHandler());
		}
		return layersButton;
	}

	private TextButton getNewFlyerButton() {
		if (btnNewFlyer == null) {
			btnNewFlyer = new TextButton();
			btnNewFlyer.setToolTip("New&nbsp;Flyer");
			btnNewFlyer.setIcon(Images24.getImageResources().f5_blue_24());
			btnNewFlyer.addSelectHandler(getNewFlyerHandler());
		}
		return btnNewFlyer;
	}

	private TextButton getNewVideoButton() {
		if (btnNewVideo == null) {
			btnNewVideo = new TextButton();
			btnNewVideo.setToolTip("New&nbsp;Video");
			btnNewVideo.setIcon(Images24.getImageResources().video_24());
			btnNewVideo.addSelectHandler(getNewVideoHandler());
		}
		return btnNewVideo;
	}

	private TextButton getPolygonButton() {
		if (btnPolygon == null) {
			btnPolygon = new TextButton();
			btnPolygon.setToolTip("Polygon");
			btnPolygon.setIcon(Images24.getImageResources().polygon_24());
			btnPolygon.addSelectHandler(getPlayHandler());
		}
		return btnPolygon;
	}

	private TextButton getFolderButton() {
		if (btnFolder == null) {
			btnFolder = new TextButton();
			btnFolder.setToolTip("New Folder");
			btnFolder.setIcon(Images24.getImageResources().folder_24());
			btnFolder.addSelectHandler(getFolderHandler());
		}
		return btnFolder;
	}

	private SelectHandler getZoominHandler() {
		if (zoominHandler == null) {
			zoominHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new ZoomInMsg());
				}
			};
		}
		return zoominHandler;
    }

	private SelectHandler getPlayHandler() {
		if (polygonHandler == null) {
			polygonHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Fpolygon.class.getName()));
				}
			};
		}
		return polygonHandler;
    }

	private SelectHandler getFolderHandler() {
		if (folderHandler == null) {
			folderHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewFolderMsg());
				}
			};
		}
		return folderHandler;
    }

	private SelectHandler getLayersHandler() {
		if (layersHandler == null) {
			layersHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewLayerMsg());
				}
			};
		}
		return layersHandler;
    }

	private SelectHandler getNewFlyerHandler() {
		if (newFlyerHandler == null) {
			newFlyerHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewFlyerMsg());
				}
			};
		}
		return newFlyerHandler;
    }

	private SelectHandler getNewVideoHandler() {
		if (newVideoHandler == null) {
			newVideoHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewVideoMsg());
				}
			};
		}
		return newVideoHandler;
    }

	private SelectHandler getZoomoutHandler() {
		if (zoomoutHandler == null) {
			zoomoutHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new ZoomOutMsg());
				}
			};
		}
		return zoomoutHandler;
    }

	private SelectHandler getNewPolygonHandler() {
		if (newPolygonHandler == null) {
			newPolygonHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(Fpolygon.class.getName()));
				}
			};
		}
		return newPolygonHandler;
    }

	private SelectHandler getNewRegularPolygonHandler() {
		if (newRegularPolygonHandler == null) {
			newRegularPolygonHandler = new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					FlyerContext.getMb().sendMsg(new NewNodeMsg(FregularPolygon.class.getName()));
				}
			};
		}
		return newRegularPolygonHandler;
    }

	private TextButton getZoomoutButton() {
		if (zoomoutButton == null) {
			zoomoutButton = new TextButton();
			zoomoutButton.setToolTip("Zoom&nbsp;Out");
			zoomoutButton.setIcon(Images24.getImageResources().zoomout_24());
			zoomoutButton.addSelectHandler(getZoomoutHandler());
		}
		return zoomoutButton;
	}

	private TextButton getNewPolygonButton() {
		if (btnNewPolygon == null) {
			btnNewPolygon = new TextButton();
			btnNewPolygon.setToolTip("New&nbsp;Polygon");
			btnNewPolygon.setIcon(Images24.getImageResources().polygon3_24());
			btnNewPolygon.addSelectHandler(getNewPolygonHandler());
		}
		return btnNewPolygon;
	}

	private TextButton getNewRegularPolygonButton() {
		if (btnNewRegularPolygon == null) {
			btnNewRegularPolygon = new TextButton();
			btnNewRegularPolygon.setToolTip("New&nbsp;Regular&nbsp;Polygon");
			btnNewRegularPolygon.setIcon(Images24.getImageResources().polygon3_24());
			btnNewRegularPolygon.addSelectHandler(getNewRegularPolygonHandler());
		}
		return btnNewRegularPolygon;
	}

	private Widget getToolBar() {
		if (toolBar == null) {
			toolBar = new ToolBar();
			toolBar.setBorders(false);
			
			toolBar.add(createHomeButton());
			toolBar.add(getFolderButton());
			toolBar.add(getNewFlyerButton());
			toolBar.add(getLayersButton());
			
			toolBar.add(new SeparatorToolItem());
			toolBar.add(getNewLineButton());
			toolBar.add(getNewTextButton());
			toolBar.add(getNewImageButton());
			toolBar.add(getNewCircleButton());
			toolBar.add(getNewRectButton());
			toolBar.add(getNewEllipseButton());
			toolBar.add(getNewRegularPolygonButton());
			toolBar.add(getPolygonButton());
			toolBar.add(getNewVideoButton());
			
			toolBar.add(new SeparatorToolItem());
			toolBar.add(getZoominButton());
			toolBar.add(getZoomoutButton());
			
			toolBar.add(new SeparatorToolItem());
			toolBar.add(getRemoveSelectedButton());
			toolBar.add(createCopyButton());
			toolBar.add(createPasteButton());
		}
		return toolBar;
	}

	private TextButton createButton(String tip, ImageResource img, SelectHandler handler) {
		TextButton btn = new TextButton();
		btn.setToolTip(tip);
		btn.setIcon(img);
		btn.addSelectHandler(handler);
		return btn;
	}
	
	private TextButton createCopyButton() {
		return createButton("copy", Images24.getImageResources().copy_24(), new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				FlyerContext.getMb().sendMsg(new CopyMsg());
			}
		});
	}
	
	private TextButton createPasteButton() {
		return createButton("paste", Images24.getImageResources().paste_24(), new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				FlyerContext.getMb().sendMsg(new PasteMsg());
			}
		});
	}
	
	private TextButton createHomeButton() {
		return createButton("Home", Images24.getImageResources().home_24(), new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Window.Location.assign("index.php");
			}
		});
	}
}
