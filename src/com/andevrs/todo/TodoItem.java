package com.andevrs.todo;

import com.codename1.ui.CheckBox;

import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import static com.codename1.ui.CN.*;


public class TodoItem extends Container {
    private TextField nameText;
    private CheckBox done = new CheckBox();

    public TodoItem(String name, boolean checked, ActionListener onChange) {
        super(new BorderLayout());
        nameText = new TextField(name);
        nameText.setUIID("Label");
        add(CENTER, nameText);
        add(EAST, done);
        done.setSelected(checked);

//        Style s = getAllStyles();
//        s.setPaddingUnit(Style.UNIT_TYPE_PIXELS);
//        s.setPadding(0,2, 0, 0);
//        s.setBorder(Border.createLineBorder(2, 0xcccccc));
        setUIID("Task");

        done.setToggle(true);
        FontImage.setMaterialIcon(done,
                FontImage.MATERIAL_CHECK, 4);

        nameText.addActionListener(onChange);
        done.addActionListener(onChange);


    }
    public void edit() {
        nameText.startEditingAsync();
    }
    public boolean isChecked() {
        return done.isSelected();
    }
    public String getText() {
        return nameText.getText();
    }
}
