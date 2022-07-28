package com.andevrs.todo;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.io.Log;
import com.codename1.ui.Component;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.codename1.ui.CN.*;


public class TodoForm extends Form {
    private ActionListener saver;

    public TodoForm() {
        super("Todo App", BoxLayout.y());

        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        fab.bindFabToContainer(this);
        fab.addActionListener(e -> addNewItem());

        getToolbar().addMaterialCommandToRightBar("",
                FontImage.MATERIAL_CLEAR_ALL, e -> clearAll());
        load();
    }

    private void save() {
        try(DataOutputStream dos = new DataOutputStream(createStorageOutputStream("todo-list-of-items"));) {
            dos.writeInt(getContentPane().getComponentCount());
                for(Component c : getContentPane()) {
                    TodoItem i = (TodoItem)c;
                    dos.writeBoolean(i.isChecked());
                    dos.writeUTF(i.getText());
                }
        } catch(IOException err) {
            Log.e(err);
            Log.sendLogAsync();
            ToastBar.showErrorMessage("Error saving todo list!");
        }
    }
    private void load() {
        if(existsInStorage("todo-list-of-items")) {
            try(DataInputStream dis = new DataInputStream(createStorageInputStream("todo-List-of-items"));) {
                int size = dis.readInt();
                for(int iter = 0; iter < size; iter++) {
                    boolean checked = dis.readBoolean();
                    TodoItem i = new TodoItem(dis.readUTF(), checked, getAutoSave());
                    add(i);
                }
            } catch(IOException err) {
                Log.e(err);
                Log.sendLogAsync();
                ToastBar.showErrorMessage("Error loading todo list!");
            }
        }
    }

    private ActionListener getAutoSave() {
        if(saver == null) {
            saver = (e) -> save();
        }
        return saver;
    }

    private void addNewItem() {
        TodoItem td = new TodoItem("", false, getAutoSave());
        add(td);
        revalidate();
        td.edit();
    }

    private void clearAll() {
        for(int i = getContentPane().getComponentCount() - 1 ; i >= 0 ; i--) {
            TodoItem t = (TodoItem)getContentPane().getComponentAt(i);
            if(t.isChecked()) {
                t.remove();
            }
        }
        save();
        getContentPane().animateLayout(300);
    }

}
