package com.gaoshin.fbobuilder.client.resourcemanager;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.TextField;

public class FileUploadPanel {
	private FramedPanel panel;

	public Widget asWidget() {
		if (panel == null) {
			panel = new FramedPanel();
			panel.setHeadingText("File Upload");
			panel.setButtonAlign(BoxLayoutPack.CENTER);
			panel.setWidth(350);
			panel.getElement().setMargins(10);

			final FormPanel form = new FormPanel();
			form.setAction("myurl");
			form.setEncoding(Encoding.MULTIPART);
			form.setMethod(Method.POST);
			panel.add(form);

			VerticalLayoutContainer p = new VerticalLayoutContainer();
			form.add(p);

			TextField firstName = new TextField();
			firstName.setAllowBlank(false);
			p.add(new FieldLabel(firstName, "Name"), new VerticalLayoutData(
			        -18, -1));

			final FileUploadField file = new FileUploadField();
			file.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
				}
			});
			file.setName("uploadedfile");
			file.setAllowBlank(false);

			p.add(new FieldLabel(file, "File"), new VerticalLayoutData(-18, -1));

			TextButton btn = new TextButton("Reset");
			btn.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					form.reset();
					file.reset();
				}
			});

			panel.addButton(btn);

			btn = new TextButton("Submit");
			btn.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					if (!form.isValid()) {
						return;
					}
					form.submit();
					MessageBox box = new MessageBox("File Upload Example",
					        "Your file was uploaded.");
					box.setIcon(MessageBox.ICONS.info());
					box.show();
				}
			});
			panel.addButton(btn);
		}
		return panel;
	}
}
