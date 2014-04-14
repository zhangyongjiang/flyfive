package com.gaoshin.fbobuilder.client.resourcemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.edzard.kinetic.Colour;

import com.gaoshin.fbobuilder.client.FlyerContext;
import com.gaoshin.fbobuilder.client.data.ColorProperty;
import com.gaoshin.fbobuilder.client.data.FileProperty;
import com.gaoshin.fbobuilder.client.data.FlyerFolderProperty;
import com.gaoshin.fbobuilder.client.data.FlyerProperty;
import com.gaoshin.fbobuilder.client.data.FontFamilyProperty;
import com.gaoshin.fbobuilder.client.data.ImageProperty;
import com.gaoshin.fbobuilder.client.data.LayerProperty;
import com.gaoshin.fbobuilder.client.data.LineJoinProperty;
import com.gaoshin.fbobuilder.client.data.NodeProperty;
import com.gaoshin.fbobuilder.client.data.PolygonProperty;
import com.gaoshin.fbobuilder.client.data.PrimeProperty;
import com.gaoshin.fbobuilder.client.data.PropertyType;
import com.gaoshin.fbobuilder.client.data.ResourceProperty;
import com.gaoshin.fbobuilder.client.data.ShapeProperty;
import com.gaoshin.fbobuilder.client.data.TextAlignProperty;
import com.gaoshin.fbobuilder.client.data.TextProperty;
import com.gaoshin.fbobuilder.client.message.CopyMsg;
import com.gaoshin.fbobuilder.client.message.DeleteShapeMsg;
import com.gaoshin.fbobuilder.client.message.DownMsg;
import com.gaoshin.fbobuilder.client.message.EmbedMsg;
import com.gaoshin.fbobuilder.client.message.ExportImgMsg;
import com.gaoshin.fbobuilder.client.message.LinkMsg;
import com.gaoshin.fbobuilder.client.message.MsgHandler;
import com.gaoshin.fbobuilder.client.message.NewFlyerMsg;
import com.gaoshin.fbobuilder.client.message.NewFolderMsg;
import com.gaoshin.fbobuilder.client.message.NewLayerMsg;
import com.gaoshin.fbobuilder.client.message.NewNodeMsg;
import com.gaoshin.fbobuilder.client.message.NodeMoveEndMsg;
import com.gaoshin.fbobuilder.client.message.NodeMoveMsg;
import com.gaoshin.fbobuilder.client.message.NodeSelectedMsg;
import com.gaoshin.fbobuilder.client.message.PasteMsg;
import com.gaoshin.fbobuilder.client.message.PostToFbMsg;
import com.gaoshin.fbobuilder.client.message.RefreshMsg;
import com.gaoshin.fbobuilder.client.message.RemoveMsg;
import com.gaoshin.fbobuilder.client.message.SaveFlyerMsg;
import com.gaoshin.fbobuilder.client.message.SaveMsg;
import com.gaoshin.fbobuilder.client.message.ShareMsg;
import com.gaoshin.fbobuilder.client.message.TreeItemChangedMsg;
import com.gaoshin.fbobuilder.client.message.UnshareMsg;
import com.gaoshin.fbobuilder.client.message.UpMsg;
import com.gaoshin.fbobuilder.client.message.ZoomInMsg;
import com.gaoshin.fbobuilder.client.message.ZoomOutMsg;
import com.gaoshin.fbobuilder.client.model.Fimage;
import com.gaoshin.fbobuilder.client.model.Flayer;
import com.gaoshin.fbobuilder.client.model.Fnode;
import com.gaoshin.fbobuilder.client.model.Fpolygon;
import com.gaoshin.fbobuilder.client.model.Ftext;
import com.gaoshin.fbobuilder.client.model.StageDataCallback;
import com.gaoshin.fbobuilder.client.resourcemanager.image.ResourceTreeImages;
import com.gaoshin.fbobuilder.client.service.FileManagerCallback;
import com.gaoshin.fbobuilder.client.service.ListCallback;
import com.gaoshin.fbobuilder.client.service.WebService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.desktopapp.client.canvas.CanvasViewImpl;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

public class ResourceManager implements IsWidget {
	private static final Logger logger = Logger
	        .getLogger("ResourceManagerTree");

	class KeyProvider implements ModelKeyProvider<ResourceProperty> {
		@Override
		public String getKey(ResourceProperty item) {
			return String.valueOf(item.hashCode());
		}
	}

	public class ResourceIconProvider implements IconProvider<ResourceProperty>{
		@Override
	    public ImageResource getIcon(ResourceProperty model) {
			if(!model.isLoaded()) {
				return ResourceTreeImages.INSTANCE.DBGreenArrowRight_16();
			}
			if(store.getChildCount(model) == 0 && (model instanceof FlyerFolderProperty))
				return ResourceTreeImages.INSTANCE.folder_closed_16();
			if(model instanceof FileProperty) {
				String fileType = ((FileProperty)model).getFileType();
				ImageResource ir = ResourceTreeImages.ImgType.get(fileType);
				if(ir != null)
					return ir;
			}
			return model.getType().getImg();
	    }
	}
	
	private TreeStore<ResourceProperty> store;
	private FlyerFolderProperty root = null; // new ResourceData().getFliers();
	private TreeGrid<ResourceProperty> tree;
	private FlyerFolderContextMenu flyerFolderContextMenu;
	private FontMenu fontMenu;
	private FlyerColorMenu colorMenu;
	private NodeContextMenu nodeMenu;
	private GridSelectionModel<ResourceProperty> selectionModel;
	private FlyerContextMenu flyerContextMenu;
	private CanvasViewImpl canvasViewImpl;
	private String clipboard;
	private FilePropertyMenu fileMenu;
	private LineJoinMenu lineJoinMenu;
	private TextAlignMenu textAlignMenu;

	public ResourceManager(CanvasViewImpl canvasViewImpl) {
		this.canvasViewImpl = canvasViewImpl;
		setupNodeMoveMsgHandler();
		setupNodeMoveEndMsgHandler();
		setupNodeSelectedMsgHandler();
		setupZoomoutHandler();
		setupZoominHandler();
		setupDeleteMsgHandler();
		setupCreateLayerMsgHandler();
		setupNewNodeMsgHandler();
		setupNewFlyerFolderMsgHandler();
		setupNewFlyerHandler();
		setupRefreshMsgHandler();
		setupCopyMsgHandler();
		setupPasteMsgHandler();
		setupSaveFlyerMsgHandler();
		setupRemoveMsgHandler();
		setupSaveMsgHandler();
		setupUpMsgHandler();
		setupDownMsgHandler();
		setupShareMsgHandler();
		setupUnshareMsgHandler();
		setupLinkMsgHandler();
		setupEmbedMsgHandler();
		setupExportImgHandler();
		setupPostToFbMsgHandler();
	}

	private void setupPostToFbMsgHandler() {
		FlyerContext.getMb().registerHandler(PostToFbMsg.class, new MsgHandler<PostToFbMsg>() {
			@Override
            public void processMsg(PostToFbMsg msg) {
				final FlyerProperty current = (FlyerProperty) selectionModel.getSelectedItem();
				if(!current.isPublic()) {
					Window.alert("You need to make your flyer public accessible first");
					return;
				}
				final ResourceProperty parent = store.getParent(current);
				final String path = getPath(parent);
				String name = current.getName();
				final String imgname = name.substring(0, name.length()-4) + ".png";
				String userId = WebService.getUserId();
				String message = current.getName();
				String imgPath = "/" + userId + path + "/" + imgname;
				String flyerId = "/" + userId + path + "/" + name;
				String imageUrl = WebService.getFileManagerUrl() + "?action=get-raw-content%26id=" + URL.encodeQueryString(imgPath);
				String link = Window.Location.getHref();
				int pos = link.lastIndexOf("?");
				if(pos != -1)
					link = link.substring(0,  pos);
				pos = link.lastIndexOf("/");
				link = link.substring(0, pos) + "/details.php?id=" + URL.encodeQueryString(flyerId);
				try {
	                WebService.postToFacebook(message, link, imageUrl, new FileManagerCallback() {
	                	@Override
	                	public void onSuccess(JSONObject jo) {
	                    	Window.alert("Succeed!");
	                	}
	                	
	                	@Override
	                	public void onError(int code, String msg) {
	                    	Window.alert("Error\n" + msg);
	                	}
	                });
                } catch (Exception e) {
                	Window.alert("Error\n" + e.getMessage());
                }
            }
		});
    }

	private void setupLinkMsgHandler() {
		FlyerContext.getMb().registerHandler(LinkMsg.class,
				new MsgHandler<LinkMsg>() {
					@Override
                    public void processMsg(LinkMsg msg) {
						final ResourceProperty current = selectionModel.getSelectedItem();
						String userId = WebService.getUserId();
						String id = "/" + userId + getPath(current);
						String url = Window.Location.getHref();
						int pos = url.indexOf("?");
						if(pos != -1) {
							url = url.substring(0,  pos);
						}
						url += "?id=" + URL.encodeQueryString(id);
						Window.alert(url);
                    }
				});
    }
	
	private boolean hasChild(ResourceProperty parent, String name) {
		List<ResourceProperty> items;
		if(parent == null) {
			items = store.getRootItems();
		}
		else {
			items = store.getChildren(parent);
		}
		for(ResourceProperty rp : items) {
			if(rp.getName().equals(name))
				return true;
		}
		return false;
	}

	private void setupExportImgHandler() {
		FlyerContext.getMb().registerHandler(ExportImgMsg.class,
				new MsgHandler<ExportImgMsg>() {
					@Override
                    public void processMsg(final ExportImgMsg msg) {
						final ResourceProperty current = selectionModel.getSelectedItem();
						canvasViewImpl.getImage(new StageDataCallback() {
							@Override
							public void receive(String url) {
								final ResourceProperty parent = store.getParent(current);
								final String path = getPath(parent);
								int pos = url.indexOf(",");
								url = url.substring(pos + 1);
								String name = current.getName();
								final String imgname = name.substring(0, name.length()-4) + ".png";
								try {
									final String imgPath = path + "/" + imgname;
	                                WebService.saveFile(path, imgname, url, new FileManagerCallback() {
	                                	@Override
	                                	public void onSuccess(JSONObject jo) {
                                			if(!hasChild(parent, imgname)) {
		                                		try {
		                                            WebService.list(path, new ListCallback() {
		                                            	@Override
		                                            	public void list(String dir, FlyerFolderProperty ffp) {
		                                            		for(ResourceProperty rp : ffp.getChildren()) {
		                                            			if(rp.getName().equals(imgname)) {
		                                            				store.add(parent, rp);
		                                            				return;
		                                            			}
		                                            		}
		                                            	}
		                                            });
	                                            } catch (Exception e) {
	                                            	Window.alert("Error\n" + e.getMessage());
	                                            }
                                			}
	                                		if(msg.isOpenNewWindow()) {
		                                		String url = WebService.getFileManagerUrl() 
		                                				+ "?action=get-file-content&dir=" + URL.encodeQueryString(imgPath)
		                                				+ "&user=" + URL.encodeQueryString(WebService.getUserId());
		                                		Window.open(url, "_blank", "");
	                                		}
	                                	}
	                                	
	                                	@Override
	                                	public void onError(int code, String msg) {
	                                	}
	                                }, "0777", true);
                                } catch (Exception e) {
                                	Window.alert("Error\n" + e.getMessage());
                                }
							}
						});
                    }
				});
    }

	private void setupEmbedMsgHandler() {
		FlyerContext.getMb().registerHandler(EmbedMsg.class,
				new MsgHandler<EmbedMsg>() {
					@Override
                    public void processMsg(EmbedMsg msg) {
						final FlyerProperty current = (FlyerProperty) selectionModel.getSelectedItem();
						int w = current.getWidth().intValue();
						int h = current.getHeight().intValue();
						
						String userId = WebService.getUserId();
						String id = "/" + userId + getPath(current);
						String url = Window.Location.getHref();
						int pos = url.indexOf("?");
						if(pos != -1) {
							url = url.substring(0,  pos);
						}
						url += "?id=" + URL.encodeQueryString(id);
						Window.alert("<iframe style='width:" + w + ";height:" + h + ";' src='" + url + "'></iframe>");
                    }
				});
    }

	private void setupUnshareMsgHandler() {
		FlyerContext.getMb().registerHandler(UnshareMsg.class,
				new MsgHandler<UnshareMsg>() {
					@Override
                    public void processMsg(UnshareMsg msg) {
						final ResourceProperty current = selectionModel.getSelectedItem();
						String path = getPath(current);
						try {
	                        WebService.share(path, false, new FileManagerCallback() {
	                        	@Override
	                        	public void onSuccess(JSONObject jo) {
	                        		if(current instanceof FlyerFolderProperty) {
	                        			((FlyerFolderProperty)current).setPrivate();
	                        		}
	                        		else if(current instanceof FlyerProperty) {
	                        			((FlyerProperty)current).setPrivate();
	                        		}
	                        		FlyerContext.getMb().sendMsg(new TreeItemChangedMsg(current));
	                        	}
	                        	
	                        	@Override
	                        	public void onError(int code, String msg) {
	                        	}
	                        });
                        } catch (Exception e) {
	                        e.printStackTrace();
                        }
                    }
				});
    }

	private void setupShareMsgHandler() {
		FlyerContext.getMb().registerHandler(ShareMsg.class,
				new MsgHandler<ShareMsg>() {
					@Override
                    public void processMsg(ShareMsg msg) {
						final ResourceProperty current = selectionModel.getSelectedItem();
						String path = getPath(current);
						try {
	                        WebService.share(path, true, new FileManagerCallback() {
	                        	@Override
	                        	public void onSuccess(JSONObject jo) {
	                        		if(current instanceof FlyerFolderProperty) {
	                        			((FlyerFolderProperty)current).setPublic();
	                        		}
	                        		else if(current instanceof FlyerProperty) {
	                        			((FlyerProperty)current).setPublic();
	                        			FlyerContext.getMb().sendMsg(new ExportImgMsg(false));
	                        		}
	                        		FlyerContext.getMb().sendMsg(new TreeItemChangedMsg(current));
	                        	}
	                        	
	                        	@Override
	                        	public void onError(int code, String msg) {
	                        	}
	                        });
                        } catch (Exception e) {
	                        e.printStackTrace();
                        }
                    }
				});
    }

	private void setupDownMsgHandler() {
		FlyerContext.getMb().registerHandler(DownMsg.class,
				new MsgHandler<DownMsg>() {
					@Override
                    public void processMsg(DownMsg msg) {
						ResourceProperty current = selectionModel.getSelectedItem();
						LayerProperty layer = getCurrentLayer();
						boolean down = canvasViewImpl.moveDown(layer, (NodeProperty) current);
						if(down) {
							int index = store.indexOf(current);
							ResourceProperty sibling = store.getNextSibling(current);
							store.remove(sibling);
							store.insert(layer, index, sibling);
							processFolder(store, sibling);
						}
                    }
				});
    }

	private void setupUpMsgHandler() {
		FlyerContext.getMb().registerHandler(UpMsg.class,
				new MsgHandler<UpMsg>() {
					@Override
                    public void processMsg(UpMsg msg) {
						ResourceProperty current = selectionModel.getSelectedItem();
						LayerProperty layer = getCurrentLayer();
						boolean up = canvasViewImpl.moveUp(layer, (NodeProperty) current);
						if(up) {
							int index = store.indexOf(current);
							ResourceProperty sibling = store.getPreviousSibling(current);
							store.remove(sibling);
							store.insert(layer, index, sibling);
							processFolder(store, sibling);
						}
                    }
				});
    }

	private void setupSaveMsgHandler() {
		FlyerContext.getMb().registerHandler(SaveMsg.class,
				new MsgHandler<SaveMsg>() {
					@Override
                    public void processMsg(SaveMsg msg) {
						ResourceProperty item = msg.getRp();
						if(item instanceof FlyerProperty) {
							FlyerProperty fp = (FlyerProperty) item;
							String path = getPath(store.getParent(fp));
							getTree(fp);
							String json = fp.toJsonString();
							try {
	                            WebService.save(path, fp.getName(), json, null);
                            } catch (Exception e) {
	                            e.printStackTrace();
                            }
						}
                    }
				});
    }

	private void setupRemoveMsgHandler() {
		FlyerContext.getMb().registerHandler(RemoveMsg.class,
				new MsgHandler<RemoveMsg>() {
					@Override
                    public void processMsg(RemoveMsg msg) {
						final ResourceProperty item = selectionModel.getSelectedItem();
						boolean confirm = Window.confirm("Are you sure?");
						if(confirm) {
							try {
                                WebService.remove(getPath(item), new FileManagerCallback() {
                                	@Override
                                	public void onSuccess(JSONObject jo) {
        								ResourceProperty data = canvasViewImpl.getData();
        								while(data != null && store.hasRecord(data)) {
        									if(item.equals(data)) {
        										canvasViewImpl.reset();
        										break;
        									}
        									data = store.getParent(data);
        								}
        								
        								FlyerFolderProperty parent = (FlyerFolderProperty) store.getParent(item);
        								store.remove(item);
        								if(parent != null) {
        									getTree(parent);
        								}
                                	}
                                	
                                	@Override
                                	public void onError(int code, String msg) {
                                    	Window.alert("Error\n" + msg);
                                	}
                                });
                            } catch (Exception e) {
                            	Window.alert("Error\n" + e.getMessage());
                            }
						}
                    }
				});
    }

	private void setupSaveFlyerMsgHandler() {
		FlyerContext.getMb().registerHandler(SaveFlyerMsg.class,
				new MsgHandler<SaveFlyerMsg>() {
					@Override
                    public void processMsg(SaveFlyerMsg msg) {
						ResourceProperty item = msg.getResourceProperty();
						if(item == null)
							item = selectionModel.getSelectedItem();
						while(item != null && !(item instanceof FlyerProperty)) {
							item = store.getParent(item);
						}
						if(item != null) {
							getTree(item);
				        	String json = item.toJsonString();
				        	String path = getPath(store.getParent(item));
				        	try {
	                            WebService.save(path, item.getName(), json, new FileManagerCallback() {
									@Override
									public void onSuccess(JSONObject jsonValue) {
//										FlyerContext.getMb().sendMsg(new ExportImgMsg(false));
									}
									
									@Override
									public void onError(int code, String msg) {
										Window.alert("Error\n" + msg);
									}
								});
                            } catch (Exception e) {
	                            e.printStackTrace();
                            }
						}
                    }
				});
    }

	private void setupRefreshMsgHandler() {
		FlyerContext.getMb().registerHandler(RefreshMsg.class,
		        new MsgHandler<RefreshMsg>() {

			        @Override
			        public void processMsg(RefreshMsg msg) {
			        	FlyerProperty flyer = getCurrentFlyer();
			        	if(flyer != null) {
			        		loadFile(flyer);
			        		return;
			        	}
			        	
			        	FlyerFolderProperty folder = getCurrentFlyerFolder();
			        	if(folder != null) {
			        		loadFolder(folder);
			        		return;
			        	}
			        }
		        });
	}

	private void setupCopyMsgHandler() {
		FlyerContext.getMb().registerHandler(CopyMsg.class,
		        new MsgHandler<CopyMsg>() {

			        @Override
			        public void processMsg(CopyMsg msg) {
				        ResourceProperty item = selectionModel.getSelectedItem();
				        getTree(item);
				        JSONObject jobj = item.toJsonObject(null);
				        clipboard = jobj.toString();
			        }
		        });
	}

	private void setupPasteMsgHandler() {
    	FlyerContext.getMb().registerHandler(PasteMsg.class, new MsgHandler<PasteMsg>() {
			@Override
            public void processMsg(PasteMsg msg) {
				ResourceProperty item = selectionModel.getSelectedItem();
				JSONObject jo = (JSONObject) JSONParser.parse(clipboard);
                ResourceProperty rp =  null;
                try {
                    rp = ResourceProperty.buildResourcePropertyFromJsonObject(jo);
                } catch (Exception e) {
                	throw new RuntimeException(e);
                }
                if(rp instanceof FlyerProperty) {
                	final FlyerFolderProperty ffp = getCurrentFlyerFolder();
                    final FlyerProperty tmp =  (FlyerProperty) rp;
                    tmp.setId(null);
    				store.add(ffp, rp);
    				processFolder(store, rp);
    				tree.setExpanded(ffp, true);
    				canvasViewImpl.load(tmp);
    				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
    					   public void execute() {
			    				selectionModel.select(false, tmp);
    					   }
    					});
                }
            }
		});
    }

	@Override
	public Widget asWidget() {
		VerticalLayoutContainer con = new VerticalLayoutContainer();
		con.setScrollMode(ScrollMode.AUTO);
		con.addStyleName("margin-10");
		con.setWidth(300);

		store = new TreeStore<ResourceProperty>(new KeyProvider());

		ColumnConfig<ResourceProperty, String> cc1 = new ColumnConfig<ResourceProperty, String>(
		        new ValueProvider<ResourceProperty, String>() {

			        @Override
			        public String getValue(ResourceProperty object) {
				        return object.getName();
			        }

			        @Override
			        public void setValue(ResourceProperty object, String value) {
				        object.setName(value);
			        }

			        @Override
			        public String getPath() {
				        return "name";
			        }
		        });
		cc1.setHeader(SafeHtmlUtils.fromString("Name"));

		ColumnConfig<ResourceProperty, String> cc2 = new ColumnConfig<ResourceProperty, String>(
		        new ValueProvider<ResourceProperty, String>() {

			        @Override
			        public String getValue(ResourceProperty object) {
				        if (object instanceof PrimeProperty) {
					        PrimeProperty pp = (PrimeProperty) object;
					        return pp.getStringValue();
				        }
				        if(!object.isLoaded())
				        	return "Double click to load";
				        return "";
			        }

			        @Override
			        public void setValue(ResourceProperty object, String value) {
				        if (object instanceof PrimeProperty) {
					        PrimeProperty pp = (PrimeProperty) object;
					        pp.setValue(value);
				        }
			        }

			        @Override
			        public String getPath() {
				        return "value";
			        }
		        });
		cc2.setHeader(SafeHtmlUtils.fromString("Value"));
		cc2.setCell(new TextCell());
		cc2.setWidth(150);

		List<ColumnConfig<ResourceProperty, ?>> l = new ArrayList<ColumnConfig<ResourceProperty, ?>>();
		l.add(cc1);
		l.add(cc2);

		ColumnModel<ResourceProperty> cm = new ColumnModel<ResourceProperty>(l);

		tree = new TreeGrid<ResourceProperty>(store, cm, cc1) {
			@Override
			protected void onDoubleClick(Event e) {
				super.onDoubleClick(e);
				final ResourceProperty selectedItem = selectionModel
				        .getSelectedItem();
				if(selectedItem == null)
					return;
				final ResourceProperty parent = ResourceManager.this.store
				        .getParent(selectedItem);
				if (selectedItem instanceof FlyerFolderProperty) {
					if(!selectedItem.isLoaded()) {
						loadFolder((FlyerFolderProperty)selectedItem);
					}
					
				}
				else if (selectedItem instanceof FlyerProperty) {
					if(!selectedItem.isLoaded()) {
						loadFile(selectedItem);
					}

				}
				else if (selectedItem instanceof PrimeProperty
				        && parent instanceof NodeProperty) {
					if(!selectedItem.isModifiable())
						return;
					PrimeProperty pp = (PrimeProperty) selectedItem;
					String value = Window.prompt("Please enter new value:",
					        pp.getStringValue());
					if (value != null) {
						try {
							if (value == null || value.length() == 0)
								pp.setValue(null);
							else
								pp.setStringValue(value);
							store.update(pp);
							updatePrimeProperty((NodeProperty) parent, pp);
							if (parent instanceof TextProperty
							        && pp.getName().equals(
							                TextProperty.NameText)) {
								store.update(parent);
							}
							if (parent instanceof FlyerProperty) {
								store.update(parent);
							}
						} catch (Exception ee) {
							Window.alert("Cannot set value to " + value
							        + ". Error: " + ee.getMessage());
						}
					}
				}
			}
		};

		cc1.setWidth(200);
		cc2.setWidth(100);

		tree.getView().setAutoExpandColumn(cc1);
		tree.setIconProvider(new ResourceIconProvider());
		createContextMenu();

		selectionModel = new GridSelectionModel<ResourceProperty>();
//		selectionModel.addBeforeSelectionHandler(new BeforeSelectionHandler<ResourceProperty>() {
//			@Override
//			public void onBeforeSelection(BeforeSelectionEvent<ResourceProperty> event) {
//				ResourceProperty current = selectionModel.getSelectedItem();
//			}
//		});
		selectionModel.addSelectionHandler(new SelectionHandler<ResourceProperty>() {
			        @Override
			        public void onSelection(SelectionEvent<ResourceProperty> event) {
				        selectedItemChanged();
			        }
		        });
		tree.setSelectionModel(selectionModel);
		tree.setExpanded(root, true);

		con.add(tree);

		try {
	        WebService.list("/", new ListCallback() {
	        	@Override
	        	public void list(String dir, FlyerFolderProperty ffp) {
	        		loadStore(store, ffp);
	        	}
	        });
        } catch (Exception e1) {
        	Window.alert("Error:\n" + e1.getMessage());
        }

		return con;
	}
	
	private String getPath(ResourceProperty rp) {
		String path = "";
		while(rp != null) {
			path = "/" + rp.getName() + path;
			rp = store.getParent(rp);
		}
		return path;
	}

	protected void loadFolder(final FlyerFolderProperty folder) {
		String path = getPath(folder);
		try {
	        WebService.list(path, new ListCallback() {
	        	@Override
	        	public void list(String dir, FlyerFolderProperty ffp) {
            		try {
            			int index = store.indexOf(folder);
            			ResourceProperty parent = store.getParent(folder);
            			store.remove(folder);
            			if(parent != null)
            				store.insert(parent, index, ffp);
            			else
            				store.insert(index, ffp);
            			processFolder(store, ffp);
        				folder.setLoaded(true);
                		tree.setExpanded(ffp, true);
                		selectionModel.select(false, ffp);
                    } catch (Exception e) {
                    	e.printStackTrace();
                    	Window.alert("Error\n" + e.getMessage());
                    }
	        	}
	        });
        } catch (Exception e) {
        	Window.alert("Error loading " + path + "\n" + e.getMessage());
        }
    }

	private void updatePrimeProperty(NodeProperty parent, PrimeProperty pp) {
		canvasViewImpl.updatePrimeProperty(parent, pp);
//		FlyerContext.getMb().sendMsg(new SaveFlyerMsg(parent));
	}

	protected void selectedItemChanged() {
		final ResourceProperty selectedItem = selectionModel.getSelectedItem();
		FlyerContext.getMb().sendMsg(new TreeItemChangedMsg(selectedItem));
		tree.setContextMenu(null);

		if (selectedItem.getType().equals(PropertyType.FlyerFolder)) {
			tree.setContextMenu(flyerFolderContextMenu);
		}
		else if (selectedItem.getType().equals(PropertyType.File)) {
			tree.setContextMenu(fileMenu);
		} else if (selectedItem.getType().equals(PropertyType.Flyer)) {
			if(selectedItem.isLoaded()) {
				tree.setContextMenu(flyerContextMenu);
				FlyerProperty currentFlyer = getCurrentFlyer();
				if (currentFlyer != null
				        && canvasViewImpl.getData() == currentFlyer)
					return;
				getTree(selectedItem);
				canvasViewImpl.load((FlyerProperty) selectedItem);
			}
		} else if (isChildOfFlyer(selectedItem)) {
			if (selectedItem instanceof FontFamilyProperty) {
				tree.setContextMenu(fontMenu);
				fontMenu.setListener(new F5Menu.MenuItemSelectedListener() {
					@Override
					public void onMenuItemSelected(String fontFamily) {
						PrimeProperty pp = (PrimeProperty) selectedItem;
						pp.setStringValue(fontFamily);
						store.update(pp);
						NodeProperty parent = (NodeProperty) store
						        .getParent(selectedItem);
						updatePrimeProperty((NodeProperty) parent, pp);
					}
				});
			}
			if (selectedItem instanceof LineJoinProperty) {
				tree.setContextMenu(lineJoinMenu);
				lineJoinMenu.setListener(new F5Menu.MenuItemSelectedListener() {
					@Override
					public void onMenuItemSelected(String lineJoin) {
						LineJoinProperty pp = (LineJoinProperty) selectedItem;
						pp.setStringValue(lineJoin);
						store.update(pp);
						NodeProperty parent = (NodeProperty) store.getParent(selectedItem);
						updatePrimeProperty((NodeProperty) parent, pp);
					}
				});
			}
			if (selectedItem instanceof TextAlignProperty) {
				tree.setContextMenu(textAlignMenu);
				textAlignMenu.setListener(new F5Menu.MenuItemSelectedListener() {
					@Override
					public void onMenuItemSelected(String text) {
						TextAlignProperty pp = (TextAlignProperty) selectedItem;
						pp.setStringValue(text);
						store.update(pp);
						NodeProperty parent = (NodeProperty) store.getParent(selectedItem);
						updatePrimeProperty((NodeProperty) parent, pp);
					}
				});
			}
			if (selectedItem instanceof ColorProperty) {
				tree.setContextMenu(colorMenu);
				colorMenu
				        .setListener(new FlyerColorMenu.ColorChangedListener() {
					        @Override
					        public void onColorChange(Colour color) {
						        colorMenu.hide();
						        ColorProperty pp = (ColorProperty) selectedItem;
						        Colour old = pp.getValue();
						        if (old != null)
							        color.setAlpha(old.getAlpha());
						        pp.setValue(color);
						        store.update(pp);
						        NodeProperty parent = (NodeProperty) store
						                .getParent(selectedItem);
						        updatePrimeProperty((NodeProperty) parent, pp);
					        }
				        });
			}

			ResourceProperty rp = selectedItem;
			while (!(rp instanceof FlyerProperty)) {
				rp = store.getParent(rp);
			}
			FlyerProperty fp = (FlyerProperty) rp;
			if (canvasViewImpl.getData() != null
			        && fp.hashCode() != canvasViewImpl.getData().hashCode()) {
				getTree(rp);
				canvasViewImpl.load((FlyerProperty) rp);
			}
			if (selectedItem instanceof NodeProperty) {
				if (selectedItem instanceof ShapeProperty) {
					tree.setContextMenu(nodeMenu);
				}
				canvasViewImpl.selectNode(selectedItem);
			} else {
				ResourceProperty parent = store.getParent(selectedItem);
				canvasViewImpl.selectNode(parent);
			}
		}
	}

	private void loadFile(final ResourceProperty selectedItem) {
		String path = getPath(selectedItem);
		try {
            WebService.getFile(path, new FileManagerCallback() {
            	@Override
            	public void onSuccess(JSONObject result) {
            		JSONObject jsonValue = (JSONObject) result.get("data");
            		try {
            			ResourceProperty parent = store.getParent(selectedItem);
            			int index = store.indexOf(selectedItem);
                        store.remove(selectedItem);
                        
                        FlyerProperty fp = (FlyerProperty) ResourceProperty.buildResourcePropertyFromJsonObject(jsonValue);
                        fp.setPermission(((JSONString)result.get("permission")).stringValue());
                        fp.sortByZindex();
        				canvasViewImpl.load(fp);
                        fp.setLoaded(true);
                        store.insert(parent, index, fp);
                		processFolder(store, fp);
                		tree.setExpanded(parent, true);
        				selectionModel.select(false, fp);
                    } catch (Exception e) {
                    	e.printStackTrace();
                    	Window.alert("Error\n" + e.getMessage());
                    }
            	}
            	
            	@Override
            	public void onError(int code, String msg) {
                	Window.alert("Error\n" + msg);
            	}
            });
        } catch (Exception e) {
            Window.alert("Error\n" + e.getMessage());
        }
    }

	public boolean isChildOfFlyer(ResourceProperty rp) {
		if (rp == null)
			return false;
		if (rp.getType().equals(PropertyType.Flyer))
			return true;
		return isChildOfFlyer(store.getParent(rp));
	}

	private void loadStore(TreeStore<ResourceProperty> store,
	        ResourceProperty root) {
		for (ResourceProperty base : root.getChildren()) {
			store.add(base);
			processFolder(store, base);
		}
	}

	private void processFolder(TreeStore<ResourceProperty> store,
	        ResourceProperty folder) {
		for (ResourceProperty child : folder.getChildren()) {
			store.add(folder, child);
			processFolder(store, child);
		}
	}

	private void createContextMenu() {
		flyerFolderContextMenu = new FlyerFolderContextMenu();
		flyerContextMenu = new FlyerContextMenu();
		fontMenu = new FontMenu();
		colorMenu = new FlyerColorMenu();
		nodeMenu = new NodeContextMenu();
		fileMenu = new FilePropertyMenu();
		lineJoinMenu = new LineJoinMenu();
		textAlignMenu = new TextAlignMenu();
	}

	public void setupNewFlyerFolderMsgHandler() {
		FlyerContext.getMb().registerHandler(NewFolderMsg.class,
		        new MsgHandler<NewFolderMsg>() {
			        @Override
			        public void processMsg(final NewFolderMsg msg) {
				        FlyerProperty currentFlyer = getCurrentFlyer();
				        if (currentFlyer != null) {
					        Window.alert("Cannot create a folder in a flyer");
					        return;
				        }

				        final FlyerFolderProperty current = (FlyerFolderProperty) getCurrentFlyerFolder();
				        final String folderName = msg.getName() == null ? Window.prompt(
				                "Please give a folder name", "Untitled") : msg.getName();
				        if (folderName == null)
					        return;
				        if(current != null) {
					        if(getChildByName(current, folderName)!=null) {
					        	Window.alert("Duplicated name. Please pick up another name.");
					        	return;
					        }
				        }
				        else {
				        	List<ResourceProperty> rootItems = store.getRootItems();
				        	for(ResourceProperty rp : rootItems) {
				        		if(folderName.equals(rp.getName())) {
						        	Window.alert("Duplicated name. Please pick up another name.");
						        	return;
				        		}
				        	}
				        }
				        try {
				        	String path = "/";
				        	if(current != null)
				        		path = getPath(current);
	                        WebService.mkdir(path, folderName, new FileManagerCallback() {
	                        	@Override
	                        	public void onSuccess(JSONObject jsonValue) {
	        				        FlyerFolderProperty child = new FlyerFolderProperty();
	        				        child.setName(folderName);
	        				        if(current != null) {
		        				        store.add(current, child);
		        				        tree.setExpanded(current, true);
	        				        }
	        				        else {
	        				        	store.add(child);
	        				        }
	        				        selectionModel.select(false, child);
	        				        if(msg.getCallback() != null)
	        				        	msg.getCallback().onCreate(child);
	                        	}
	                        	
	                        	@Override
	                        	public void onError(int code, String msg) {
	                            	Window.alert("Error:\n" + msg);
	                        	}
	                        });
                        } catch (Exception e) {
                        	Window.alert("Error:\n" + e.getMessage());
                        }
			        }
		        });
	}
	
	public ResourceProperty getChildByName(ResourceProperty parent, String childName) {
		List<ResourceProperty> children = store.getChildren(parent);
		for(ResourceProperty rp : children) {
			if(childName.equalsIgnoreCase(rp.getName())) {
				return rp;
			}
		}
		return null;
	}

	public void setupNodeMoveMsgHandler() {
		FlyerContext.getMb().registerHandler(NodeMoveMsg.class,
		        new MsgHandler<NodeMoveMsg>() {
			        @Override
			        public void processMsg(NodeMoveMsg msg) {
				        Fnode node = msg.getFnode();
				        ResourceProperty property = node.getResourceProperty();
				        for (ResourceProperty child : property.getChildren()) {
					        if (child.isChanged()) {
						        store.update(child);
						        child.setChanged(false);
					        }
				        }
			        }
		        });
	}

	public void setupNodeMoveEndMsgHandler() {
		FlyerContext.getMb().registerHandler(NodeMoveEndMsg.class,
		        new MsgHandler<NodeMoveEndMsg>() {
			        @Override
			        public void processMsg(NodeMoveEndMsg msg) {
//				        Fnode node = msg.getFnode();
//				        ResourceProperty property = node.getResourceProperty();
//			        	FlyerContext.getMb().sendMsg(new SaveFlyerMsg(property));
			        }
		        });
	}
	
	private FlyerProperty getFlyer(ResourceProperty rp) {
		while (rp != null && !(rp instanceof FlyerProperty)) {
			rp = store.getParent(rp);
		}
		return (FlyerProperty) rp;
	}
	
	public void setupNodeSelectedMsgHandler() {
		FlyerContext.getMb().registerHandler(NodeSelectedMsg.class,
		        new MsgHandler<NodeSelectedMsg>() {
			        @Override
			        public void processMsg(NodeSelectedMsg msg) {
				        Fnode fnode = msg.getFnode();
				        NodeProperty property = fnode.getResourceProperty();
				        selectionModel.select(false, property);
			        }
		        });
	}

	public CanvasViewImpl getCanvasView() {
		return canvasViewImpl;
	}

	private void getTree(ResourceProperty rp) {
		List<ResourceProperty> children = store.getChildren(rp);
		if (children == null || children.size() == 0)
			return;
		rp.setChildren(children);
		for (ResourceProperty child : children) {
			getTree(child);
		}
	}

	public boolean isNodeCurrentSelected() {
		return selectionModel.getSelectedItem() instanceof NodeProperty;
	}

	public boolean isLayerCurrentSelected() {
		return selectionModel.getSelectedItem() instanceof LayerProperty;
	}

	public boolean isFlyerCurrentSelected() {
		return selectionModel.getSelectedItem() instanceof FlyerProperty;
	}

	public ShapeProperty getCurrentShape() {
		ResourceProperty current = selectionModel.getSelectedItem();
		while (current != null && !(current instanceof ShapeProperty)) {
			current = store.getParent(current);
		}
		return (ShapeProperty) current;
	}

	public LayerProperty getCurrentLayer() {
		ResourceProperty current = selectionModel.getSelectedItem();
		while (current != null && !(current instanceof LayerProperty)) {
			current = store.getParent(current);
		}
		return (LayerProperty) current;
	}

	public FlyerProperty getCurrentFlyer() {
		ResourceProperty current = selectionModel.getSelectedItem();
		while (current != null && !(current instanceof FlyerProperty)) {
			current = store.getParent(current);
		}
		return (FlyerProperty) current;
	}

	public FlyerFolderProperty getCurrentFlyerFolder() {
		ResourceProperty current = selectionModel.getSelectedItem();
		while (current != null && !(current instanceof FlyerFolderProperty)) {
			current = store.getParent(current);
		}
		return (FlyerFolderProperty) current;
	}

	public void setupDeleteMsgHandler() {
		FlyerContext.getMb().registerHandler(DeleteShapeMsg.class,
		        new MsgHandler<DeleteShapeMsg>() {
			        @Override
			        public void processMsg(DeleteShapeMsg msg) {
				        ShapeProperty sp = getCurrentShape();
				        if (sp != null) {
					        boolean confirm = Window.confirm("Are you sure?");
					        if (!confirm)
						        return;
					        ResourceProperty parent = store.getParent(sp);
					        parent.setChildren(new ArrayList<ResourceProperty>());
					        store.remove(sp);
					        canvasViewImpl.deleteShape(sp);
					        selectionModel.select(false, parent);
				        }
			        }
		        });
	}

	public void setupCreateLayerMsgHandler() {
		FlyerContext.getMb().registerHandler(NewLayerMsg.class,
		        new MsgHandler<NewLayerMsg>() {
			        @Override
			        public void processMsg(NewLayerMsg msg) {
				        FlyerProperty fp = getCurrentFlyer();
				        if (fp == null) {
					        Window.alert("Please select a flyer.");
					        return;
				        }
				        Flayer flayer = canvasViewImpl.createLayer();
				        LayerProperty child = flayer.getResourceProperty();
				        store.add(fp, child);
				        tree.setExpanded(fp, true);
				        tree.setExpanded(child, true);
				        selectionModel.select(false, child);
			        }
		        });
	}

	public void setupNewFlyerHandler() {
		FlyerContext.getMb().registerHandler(NewFlyerMsg.class,
		        new MsgHandler<NewFlyerMsg>() {
			        @Override
			        public void processMsg(NewFlyerMsg msg) {
				        FlyerFolderProperty ffp = getCurrentFlyerFolder();
				        if(ffp == null) {
				        	Window.alert("Please select a folder or create a new folder to put your flyer. ");
				        	return;
				        }
				        String flyerName = Window.prompt(
				                "Please give a name for the new flyer", "Untitled");
				        if (flyerName == null)
					        return;
						if(!flyerName.endsWith(".fly"))
							flyerName = flyerName + ".fly";
						
						if(ffp != null) {
					        if(getChildByName(ffp, flyerName)!=null) {
					        	Window.alert("Duplicated name. Please pick up another name.");
					        	return;
					        }
						}
						else {
							for(ResourceProperty rp : store.getRootItems()) {
								if(rp.getName().equals(flyerName)) {
						        	Window.alert("Duplicated name. Please pick up another name.");
						        	return;
								}
							}
							
						}
				        
				        String size = Window.prompt(
				                "What's the size in pixel?", "800x600");
				        if (size == null)
					        return;
				        canvasViewImpl.reset();
				        String[] wh = size.split("[, x;]+");
				        canvasViewImpl.createStage(Integer.parseInt(wh[0]),
				                Integer.parseInt(wh[1]));
				        FlyerProperty child = canvasViewImpl.getData();
				        child.setName(flyerName);
				        
				        if(ffp != null) {
					        store.add(ffp, child);
					        tree.setExpanded(ffp, true);
				        }
				        else {
				        	store.add(child);
				        }
				        processFolder(store, child);
				        selectionModel.select(false, child);
				        
				        getTree(child);
				        String content = child.toJsonString();
				        String path = (ffp == null) ? "/" : getPath(ffp);
				        try {
	                        WebService.save(path, flyerName, content, new FileManagerCallback() {
	                        	@Override
	                        	public void onSuccess(JSONObject jsonValue) {
	                        	}
	                        	
	                        	@Override
	                        	public void onError(int code, String msg) {
	                            	Window.alert("Error\n" + msg);
	                        	}
	                        });
                        } catch (Exception e) {
                        	Window.alert("Error\n" + e.getMessage());
                        }
			        }
		        });
	}
	
	public void setupNewFlyerHandler2() {
		FlyerContext.getMb().registerHandler(NewFlyerMsg.class,
		        new MsgHandler<NewFlyerMsg>() {
			        @Override
			        public void processMsg(NewFlyerMsg msg) {
				        final String flyerFolderName = Window.prompt(
				                "Please give a name for the new flyer", "Untitled");
				        if (flyerFolderName == null)
					        return;
				        final String size = Window.prompt(
				                "What's the size in pixel?", "800x600");
				        if (size == null)
					        return;
				        NewFolderMsg folderMsg = new NewFolderMsg();
				        folderMsg.setName(flyerFolderName);
				        folderMsg.setCallback(new NewFolderMsg.Callback() {
							@Override
							public void onCreate(FlyerFolderProperty ffp) {
						        canvasViewImpl.reset();
						        String[] wh = size.split("[, x;]+");
						        canvasViewImpl.createStage(Integer.parseInt(wh[0]),
						                Integer.parseInt(wh[1]));
						        String flyerName = flyerFolderName + ".fly";
						        FlyerProperty child = canvasViewImpl.getData();
						        child.setName(flyerName);
						        store.add(ffp, child);
						        processFolder(store, child);
						        tree.setExpanded(ffp, true);
						        selectionModel.select(false, child);
						        
						        getTree(child);
						        String content = child.toJsonString();
						        String path = getPath(ffp);
						        try {
			                        WebService.save(path, flyerName, content, new FileManagerCallback() {
			                        	@Override
			                        	public void onSuccess(JSONObject jsonValue) {
			                        	}
			                        	
			                        	@Override
			                        	public void onError(int code, String msg) {
			                            	Window.alert("Error\n" + msg);
			                        	}
			                        });
		                        } catch (Exception e) {
		                        	Window.alert("Error\n" + e.getMessage());
		                        }
							}
						});
				        FlyerContext.getMb().sendMsg(folderMsg);
			        }
		        });
	}

	public void setupZoomoutHandler() {
		FlyerContext.getMb().registerHandler(ZoomOutMsg.class,
		        new MsgHandler<ZoomOutMsg>() {
			        @Override
			        public void processMsg(ZoomOutMsg msg) {
			        	canvasViewImpl.zoomOut(1.2f);
			        }
		        });
	}

	public void setupZoominHandler() {
		FlyerContext.getMb().registerHandler(ZoomInMsg.class,
		        new MsgHandler<ZoomInMsg>() {
			        @Override
			        public void processMsg(ZoomInMsg msg) {
			        	canvasViewImpl.zoomIn(1.2f);
			        }
		        });
	}

	private int getNumberOfLayers(FlyerProperty fp) {
		int cnt = 0;
		for (ResourceProperty rp : store.getChildren(fp)) {
			if (rp instanceof LayerProperty)
				cnt++;
		}
		return cnt;
	}

	private LayerProperty getFirstLayer(FlyerProperty fp) {
		for (ResourceProperty rp : store.getChildren(fp)) {
			if (rp instanceof LayerProperty)
				return (LayerProperty) rp;
		}
		return null;
	}

	public void setupNewNodeMsgHandler() {
		FlyerContext.getMb().registerHandler(NewNodeMsg.class,
		        new MsgHandler<NewNodeMsg>() {
			        @Override
			        public void processMsg(NewNodeMsg msg) {
				        String class1 = msg.getNodeCls();
				        LayerProperty current = (LayerProperty) getCurrentLayer();
				        if (current == null) {
					        FlyerProperty fp = getCurrentFlyer();
					        if (fp == null) {
						        Window.alert("Please select a layer to put your new shape.");
						        return;
					        } else if (getNumberOfLayers(fp) == 0) {
						        FlyerContext.getMb().sendMsg(new NewLayerMsg());
						        current = getFirstLayer(fp);
					        } else if (getNumberOfLayers(fp) == 1) {
						        current = getFirstLayer(fp);
					        } else {
						        Window.alert("Please select a layer to put your new shape.");
						        return;
					        }
				        }
				        
				        NodeProperty np = null;
				        if (class1.equals(Fimage.class.getName())) {
					        ImageProperty ip = new ImageProperty();
					        String url = Window
					                .prompt("Please give the URL for the image",
					                        "http://www.solarfeeds.com/wp-content/uploads/sale-tags1.jpg");
					        if (url == null)
						        return;
					        if (url != null && url.length() > 0) {
						        ip.addStrProperty(ImageProperty.NameUrl, url);
					        }
					        np = ip;
				        } else if (class1.equals(Ftext.class.getName())) {
					        TextProperty ip = new TextProperty();
					        String text = Window.prompt("Text?",
					                "Html5 Flyer is great. No flash.");
					        if (text == null)
						        return;
					        ip.addStrProperty(TextProperty.NameText, text);
					        np = ip;
				        } else if (class1.equals(Fpolygon.class.getName())) {
							String sides = Window.prompt("sides?", "5");
							try {
								int num = Integer.parseInt(sides);
								PolygonProperty pp = new PolygonProperty();
								pp.addIntProperty(PolygonProperty.NameSides, num);
						        np = pp;
							}
							catch (Exception e) {
								Window.alert("must give a number");
								return;
							}
				        }
				        Fnode fnode = canvasViewImpl.createNewNode(class1, current, np);
				        ResourceProperty property = fnode.getResourceProperty();
				        store.add(current, property);
				        processFolder(store, property);
				        tree.setExpanded(current, true);
				        selectionModel.select(false, property);
				        
//				        FlyerContext.getMb().sendMsg(new SaveMsg(getCurrentFlyer()));
			        }
		        });
	}

}
