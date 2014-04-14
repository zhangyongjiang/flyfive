package net.auroris.ColorPicker.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ColorPickerDialog extends DialogBox
{
    private ColorPicker picker;

    public ColorPickerDialog()
    {
        setText("Color Picker");

        setWidth("435px");
        setHeight("350px");
        
        int clientWidth = Window.getClientWidth();
        setPopupPosition(clientWidth - 500, 10);

        // Define the panels
        VerticalPanel panel = new VerticalPanel();
        FlowPanel okcancel = new FlowPanel();
        picker = new ColorPicker();

        // Define the buttons
        Button ok = new Button("Ok"); // ok button
        ok.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ColorPickerDialog.this.hide();
                if(picker.getListener() != null)
                    picker.getListener().onOk(picker.getHexColor());
            }
        });

        Button cancel = new Button("Cancel"); // cancel button
        cancel.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event)
            {
                ColorPickerDialog.this.hide();
                if(picker.getListener() != null)
                    picker.getListener().onCancel();
            }
        });
        okcancel.add(ok);
        okcancel.add(cancel);

        // Put it together
        panel.add(picker);
        panel.add(new HTML("<div style='height:10px;'>&nbsp;</div>"));
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel.add(okcancel);

        setWidget(panel);
    }
    
    public void setColorListener(ColorListener listener) {
        picker.setListener(listener);
    }
}