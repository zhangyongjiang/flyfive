package com.flyerfive.client.controller;

import java.util.HashMap;
import java.util.Map;

import net.auroris.ColorPicker.client.ColorListener;

import com.flyerfive.client.ExternalFunctions;
import com.flyerfive.client.FlyerContext;
import com.flyerfive.client.controller.Toolbar.ToolbarType;
import com.flyerfive.client.data.ColorProperty;
import com.flyerfive.client.data.DoubleProperty;
import com.flyerfive.client.data.FontFamilyProperty;
import com.flyerfive.client.data.IntProperty;
import com.flyerfive.client.data.NodeProperty;
import com.flyerfive.client.data.PrimeProperty;
import com.flyerfive.client.data.ResourceProperty;
import com.flyerfive.client.data.SelectProperty;
import com.flyerfive.client.data.TextProperty;
import com.flyerfive.client.data.ZindexProperty;
import com.flyerfive.client.message.MsgHandler;
import com.flyerfive.client.message.NodeMoveEndMsg;
import com.flyerfive.client.message.NodeMoveMsg;
import com.flyerfive.client.message.NodeSelectedMsg;
import com.flyerfive.client.message.ToolbarMsg;
import com.flyerfive.client.model.Fnode;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PropertyEditor extends DialogBox {
    private static final int width = 300;
    private static final int height = 600;
    private static final String strWidth = width + "px";
    private static final String strHeight = height + "px";
    
    private FlyerContext ctx;
    private FlexTable table;
    private Fnode currentNode;
    private Map<String, PropertyCell> propCellMap;
    private Button btnRemove;

    public PropertyEditor(FlyerContext cxt) {
        this.ctx = cxt;
        propCellMap = new HashMap<String, PropertyCell>();
        
        setModal(false);
        int clientHeight = Window.getClientHeight();
        int clientWidth = Window.getClientWidth();
        setPopupPosition(clientWidth - width - 50, 10);

        setTitle("Parameters");
        setWidth(strWidth);
        setHeight(strHeight);
        
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth(strWidth);
        panel.setHeight(strHeight);

        table = new FlexTable();
        table.setWidth(strWidth);
        panel.add(table);
        
        btnRemove = new Button("Remove");
        btnRemove.setWidth(strWidth);
        btnRemove.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ctx.getMb().sendMsg(new ToolbarMsg(ToolbarType.Remove));
            }
        });
        btnRemove.setVisible(false);
        
        setWidget(panel);
        
        setupMsgHandler();
    }
    
    public void setTitle(String title) {
        setHTML("<div style='text-align:center;width:100%;'>" + title + "</div>");
    }

    private void setupMsgHandler() {
        ctx.getMb().registerHandler(NodeMoveMsg.class, new MsgHandler<NodeMoveMsg>() {
            @Override
            public void processMsg(NodeMoveMsg msg) {
                Fnode fnode = msg.getFnode();
                if(!fnode.equals(currentNode)) {
                    setCurrentNode(fnode);
                }
                else {
                    updateCurrentNode();
                }
            }
        });
        ctx.getMb().registerHandler(NodeMoveEndMsg.class, new MsgHandler<NodeMoveEndMsg>() {
            @Override
            public void processMsg(NodeMoveEndMsg msg) {
                msg.getFnode();
            }
        });
        ctx.getMb().registerHandler(NodeSelectedMsg.class, new MsgHandler<NodeSelectedMsg>() {
            @Override
            public void processMsg(NodeSelectedMsg msg) {
                Fnode fnode = msg.getFnode();
                setCurrentNode(fnode);
            }
        });
    }

    protected void updateCurrentNode() {
        NodeProperty properties = currentNode.getResourceProperty();
        for (ResourceProperty child : properties.getChildren()) {
            if (child.isChanged()) {
                PrimeProperty pp = (PrimeProperty) child;
                child.setChanged(false);
                String name = child.getName();
                PropertyCell cell = propCellMap.get(name);
                cell.updateView();
            }
        }
    }

    protected void setCurrentNode(Fnode fnode) {
        if(fnode == null) {
            table.removeAllRows();
            propCellMap.clear();
            currentNode = null;
            setTitle("Parameters");
            btnRemove.setVisible(false);
            hide();
            return;
        }
        
        if(fnode.equals(currentNode))
            return;
        
        show();
        table.removeAllRows();
        propCellMap.clear();
        btnRemove.setVisible(true);
        
        currentNode = fnode;
        NodeProperty properties = currentNode.getResourceProperty();
        setTitle(properties.getType() + " Parameters");
        int row = 0;
        for(ResourceProperty rp : properties.getChildren()) {
            if(rp instanceof PrimeProperty) {
                PrimeProperty pp = (PrimeProperty) rp;
                String name = rp.getName();
                if(TextProperty.NameFontFamily.equals(name)) {
                    HTML html = new HTML("<a href='http://www.google.com/webfonts' target='googlewebfonts'>font-family</>");
                    table.setWidget(row, 0, html);
                }
                else {
                    Label label = new Label(name);
                    table.setWidget(row, 0, label);
                }
                PropertyCell cellWidget = getCellWidget(pp);
                table.setWidget(row, 1, cellWidget);
//                table.getCellFormatter().addStyleName(row,0,"FlexTable-Cell");
//                table.getCellFormatter().addStyleName(row,1,"FlexTable-Cell");
                propCellMap.put(name, cellWidget);
                row++;
            }
        }
        table.setWidget(row, 0, btnRemove);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
    }
    
    private PropertyCell getCellWidget(final PrimeProperty prop) {
        if(prop instanceof ColorProperty) {
            return getColorWidget(prop);
        }
        else if(prop instanceof SelectProperty) {
            return getSelectWidget(prop);
        }
        else if(prop instanceof ZindexProperty) {
            return getZindexWidget(prop);
        }
        else if(prop instanceof IntProperty) {
            return getIntWidget(prop);
        }
        else if(prop instanceof DoubleProperty) {
            return getDoubleWidget(prop);
        }
        
        final PropertyCell cell = new PropertyCell(prop, ctx);
        cell.setHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                NodeProperty np = currentNode.getResourceProperty();
                String value = cell.getStringValue();
                cell.setOriginalValue(value);
                prop.setStringValue(value);
                np.updateView(currentNode, prop);
                currentNode.getFlayer().getLayer().draw();
                ctx.setDefaultValue(prop.getName(), value);
            }
        });
        
        return cell;
    }

    private PropertyCell getSelectWidget(final PrimeProperty prop) {
        final SelectCell cell = new SelectCell<SelectProperty>((SelectProperty) prop, ctx);
        
        cell.setChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if(prop instanceof FontFamilyProperty) {
                    String fontFamily = cell.getValue();
                    ExternalFunctions.loadFont(fontFamily, null, null);
                }
                final NodeProperty np = currentNode.getResourceProperty();
                String value = cell.getValue();
                prop.setStringValue(value);
                ctx.setDefaultValue(prop.getName(), value);
                if(prop instanceof FontFamilyProperty) {
                    Timer timer = new Timer() {
                        @Override
                        public void run() {
//                            currentNode.getFlayer().toggleSelection(currentNode);
                            np.updateView(currentNode, prop);
                            currentNode.getFlayer().getNode().draw();
                        }
                    };
                    timer.schedule(600);
                }
                else {
                    np.updateView(currentNode, prop);
                    currentNode.getFlayer().getLayer().draw();
                }
            }
        });
        return cell;
    }

    private PropertyCell getZindexWidget(PrimeProperty prop) {
        final ZindexCell cell = new ZindexCell((ZindexProperty) prop, ctx);
        cell.setListener(new ZindexDialog.ChangeListener() {
            @Override
            public void moveUp() {
                currentNode.getFstage().moveUp(currentNode.getFlayer().getResourceProperty(), currentNode.getResourceProperty());
                cell.updateView();
            }
            
            @Override
            public void moveDown() {
                currentNode.getFstage().moveDown(currentNode.getFlayer().getResourceProperty(), currentNode.getResourceProperty());
                cell.updateView();
            }
        });
        return cell;
    }

    private IntegerCell getIntWidget(final PrimeProperty prop) {
        final IntegerCell cell = new IntegerCell((IntProperty) prop, ctx);
        cell.setHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                NodeProperty np = currentNode.getResourceProperty();
                int value = cell.getValue();
                prop.setValue(value);
                np.updateView(currentNode, prop);
                currentNode.getFlayer().getLayer().draw();
                ctx.setDefaultValue(prop.getName(), value);
            }
        });
        return cell;
    }

    private DoubleCell getDoubleWidget(final PrimeProperty prop) {
        final DoubleCell cell = new DoubleCell((DoubleProperty) prop, ctx);
        cell.setHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                NodeProperty np = currentNode.getResourceProperty();
                double value = cell.getValue();
                prop.setValue(value);
                np.updateView(currentNode, prop);
                currentNode.getFlayer().getLayer().draw();
                ctx.setDefaultValue(prop.getName(), value);
            }
        });
        return cell;
    }

    private ColorCell getColorWidget(final PrimeProperty prop) {
        final ColorCell colorCell = new ColorCell((ColorProperty) prop, ctx);
        colorCell.setHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                NodeProperty np = currentNode.getResourceProperty();
                String value = colorCell.getValue();
                colorCell.setOriginalValue(value);
                prop.setStringValue(value);
                np.updateView(currentNode, prop);
                currentNode.getFlayer().getLayer().draw();
                ctx.setDefaultValue(prop.getName(), value);
            }
        });
        colorCell.setListener(new ColorListener() {
            @Override
            public void onOk(String color) {
                ctx.setDefaultValue(prop.getName(), colorCell.getValue());
            }
            
            @Override
            public void onColor(String color) {
                NodeProperty np = currentNode.getResourceProperty();
                prop.setStringValue(colorCell.getValue());
                np.updateView(currentNode, prop);
                currentNode.getFlayer().getLayer().draw();
            }
            
            @Override
            public void onCancel() {
                NodeProperty np = currentNode.getResourceProperty();
                prop.setStringValue(colorCell.getOriginalValue());
                np.updateView(currentNode, prop);
                currentNode.getFlayer().getLayer().draw();
            }
        });
        return colorCell;
    }

}
