//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jamontes79;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import java.io.File;
import java.io.IOException;

public class MyContext extends ContextWrapper {
    public MyContext(Context base) {
        super(base);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
        File f = new File(name);
        File fDir = new File(f.getParent());
        if(!f.exists()) {
            try {
                fDir.mkdirs();
                f.createNewFile();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f, factory);
        return db;
    }
}
