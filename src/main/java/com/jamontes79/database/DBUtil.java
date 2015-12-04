package com.jamontes79.database;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import com.jamontes79.MyContext;
import com.jamontes79.MyUtil;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by alberto on 8/9/15.
 */
public class DBUtil {
    public static String TEMP_TABLE_PREFIX = "TMP";
    public static String PRIMARY_KEY = "sqlite_autoindex_";
    public static String NULLABLE = "0";
    public static SQLiteDatabase m_appConnection;

    public DBUtil() {
    }

    public static boolean openDatabase(Context pContext, String pFolder, String pDatabaseName, Vector<String> pTables, Vector<String[]> pFields, Vector<String[]> pKeys, Vector<String[]> pTypes) {
        boolean createSomeTable = false;

        try {
            if(pFolder != null) {
                if(MyUtil.hasStorage(pContext, true, pFolder)) {
                    if(m_appConnection == null) {
                        m_appConnection = (new MyContext(pContext)).openOrCreateDatabase(pFolder + "/" + "data" + "/" + pDatabaseName + ".DB3", 268435456, (SQLiteDatabase.CursorFactory)null);
                        createSomeTable = checkTables(pTables, pFields, pKeys, pTypes);
                    }
                } else {
                   // MyUtil.MessageBox(pContext, "Error", "No se encontrÃ³ tarjeta de memoria, introduzca una o cambie las preferencias de la aplicaciÃ³n.");
                }
            } else {
                if(m_appConnection == null || !m_appConnection.isOpen()) {
                    m_appConnection = pContext.openOrCreateDatabase(pDatabaseName + ".DB3", 268435456, (SQLiteDatabase.CursorFactory)null);
                }

                createSomeTable = checkTables(pTables, pFields, pKeys, pTypes);
            }
        } catch (Exception var10) {
            String err = var10.getMessage();
            err = err + " ";
        }

        return createSomeTable;
    }

    private static boolean checkTables(Vector<String> pTables, Vector<String[]> pFields, Vector<String[]> pKeys, Vector<String[]> pTypes) throws Exception {
        boolean createSomeTable = false;

        for(int i = 0; i < pTables.size(); ++i) {
            createSomeTable = checkTable((String)pTables.get(i), (String[])pFields.get(i), (String[])pKeys.get(i), (String[])pTypes.get(i)) || createSomeTable;
        }

        return createSomeTable;
    }

    private static void createConnection() {
        m_appConnection = SQLiteDatabase.openOrCreateDatabase("maraldesarrollosVIDEO.DB3", (SQLiteDatabase.CursorFactory)null);
    }

    public static boolean createTable(String strTableName, String[] fields, String[] keys, String[] types) throws Exception {
        Vector aKeys = null;
        boolean bRet = false;

        try {
            int ex;
            int j;
            if(keys != null) {
                aKeys = new Vector();
                ex = 0;

                for(j = keys.length; ex < j; ++ex) {
                    aKeys.add(keys[ex]);
                }
            }

            String strSQL = "CREATE TABLE " + strTableName + "(";
            ex = 0;

            for(j = fields.length; ex < j; ++ex) {
                if(ex > 0) {
                    strSQL = strSQL + ",";
                }

                strSQL = strSQL + fields[ex] + " " + types[ex] + " " + (aKeys != null && aKeys.contains(fields[ex])?"NOT NULL":"NULL") + " COLLATE NOCASE";
            }

            if(aKeys != null && aKeys.size() > 0) {
                strSQL = strSQL + ",constraint PK_" + strTableName + " primary key (";
                ex = 0;

                for(j = aKeys.size(); ex < j; ++ex) {
                    if(ex > 0) {
                        strSQL = strSQL + ",";
                    }

                    strSQL = strSQL + (String)aKeys.get(ex);
                }

                strSQL = strSQL + ")";
            }

            strSQL = strSQL + ")";
            m_appConnection.execSQL(strSQL);
            bRet = true;
            return bRet;
        } catch (Exception var12) {
            throw var12;
        } finally {
            if(aKeys != null) {
                aKeys.clear();
            }

        }
    }

    public static boolean checkTable(String strTableName, String[] fields, String[] keys, String[] types) throws Exception {
        boolean bRecreate = false;
        boolean createSomeTable = false;
        String sql = "";

        try {
            Table table = getTable(strTableName);
            if(table != null) {
                int ex = 0;

                boolean bExist;
                int j;
                int ix;
                int jx;
                for(j = fields.length; ex < j; ++ex) {
                    bExist = false;
                    ix = 0;

                    for(jx = table.fields.size(); ix < jx; ++ix) {
                        if(((Field)table.fields.get(ix)).name.equalsIgnoreCase(fields[ex])) {
                            bExist = true;
                            break;
                        }
                    }

                    if(!bExist) {
                        bRecreate = true;
                        break;
                    }
                }

                if(!bRecreate) {
                    Index index = table.primaryKey();
                    if((keys == null || index != null) && (keys != null || index == null)) {
                        if(keys != null && index != null) {
                            if(index.indexfields.size() != keys.length) {
                                bRecreate = true;
                            } else {
                                ex = 0;

                                for(j = keys.length; ex < j; ++ex) {
                                    bExist = false;
                                    ix = 0;

                                    for(jx = index.indexfields.size(); ix < jx; ++ix) {
                                        if(((IndexField)index.indexfields.get(ex)).field.name.equalsIgnoreCase(keys[ex])) {
                                            bExist = true;
                                            break;
                                        }
                                    }

                                    if(!bExist) {
                                        bRecreate = true;
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        bRecreate = true;
                    }
                }

                if(bRecreate) {
                    strTableName = TEMP_TABLE_PREFIX + "_" + strTableName;
                    if(tableExists(strTableName)) {
                        sql = "DROP TABLE " + strTableName;
                        m_appConnection.execSQL(sql);
                    }

                    createTable(strTableName, fields, keys, types);
                    StringBuilder sb = new StringBuilder();
                    ex = 0;

                    for(j = table.fields.size(); ex < j; ++ex) {
                        if(ex > 0) {
                            sb.append(",");
                        }

                        sb.append(((Field)table.fields.get(ex)).name.toUpperCase());
                    }

                    sql = " INSERT INTO " + strTableName + " (" + sb.toString() + " ) SELECT " + sb.toString() + " FROM " + table.name;
                    m_appConnection.execSQL(sql);
                    sql = "DROP TABLE " + table.name;
                    m_appConnection.execSQL(sql);
                    createTable(table.name, fields, keys, types);
                    sql = " INSERT INTO " + table.name + " (" + sb.toString() + " ) SELECT " + sb.toString() + " FROM " + strTableName;
                    m_appConnection.execSQL(sql);
                    sql = "DROP TABLE " + strTableName;
                    m_appConnection.execSQL(sql);
                }
            } else {
                createTable(strTableName, fields, keys, types);
                createSomeTable = true;
            }

            return createSomeTable;
        } catch (Exception var15) {
            throw var15;
        }
    }

    public static String stringToSQL(String str) {
        String strSQL = "";
        if(str != null && str.length() > 0) {
            str = str.replace('\'', ' ');
            strSQL = "\'" + str + "\'";
        } else {
            strSQL = "\'\'";
        }

        return strSQL;
    }

    public static String stringEscapeToSQL(String str) {
        String strSQL = "";
        if(str != null && str.length() > 0) {
            strSQL = str.replaceAll("\'", "\'\'");
        } else {
            strSQL = "\'\'";
        }

        return strSQL;
    }

    public static Table getTable(String strTable) throws Exception {
        SQLiteCursor dbReader = null;
        Vector indexColumns = null;
        Table table = null;

        try {
            dbReader = (SQLiteCursor)m_appConnection.rawQuery("PRAGMA table_info(" + strTable + ")", (String[])null);
            if(dbReader.moveToFirst()) {
                table = new Table();
                table.name = strTable;

                Field field;
                String str;
                do {
                    if(!dbReader.isNull(0) && !dbReader.isNull(1)) {
                        field = new Field();
                        field.name = dbReader.getString(1).toUpperCase();
                        String strType = !dbReader.isNull(2)?dbReader.getString(2).toUpperCase():"";
                        field.nullable = dbReader.isNull(3)?false:dbReader.getString(3).equals(NULLABLE);
                        if(strType.startsWith("INTEGER")) {
                            field.type = 4;
                            field.size = 10L;
                            field.decimals = 0L;
                        } else if(!strType.startsWith("NUMERIC") && !strType.startsWith("DECIMAL")) {
                            if(!strType.startsWith("NVARCHAR") && !strType.startsWith("VARCHAR") && !strType.startsWith("NCHAR") && !strType.startsWith("CHAR")) {
                                if(!strType.startsWith("DATETIME") && !strType.startsWith("SMALLDATETIME") && !strType.startsWith("TIMESTAMP")) {
                                    if(dbReader.getString(2).startsWith("BIT")) {
                                        field.type = -7;
                                        field.size = 1L;
                                    } else if(!strType.startsWith("FLOAT") && !strType.startsWith("DOUBLE")) {
                                        if(strType.startsWith("REAL")) {
                                            field.type = 7;
                                            field.size = 24L;
                                            field.decimals = 4L;
                                        } else if(strType.startsWith("NTEXT") || strType.startsWith("TEXT")) {
                                            field.type = 12;
                                            field.size = 0L;
                                        }
                                    } else {
                                        field.type = 8;
                                        field.size = 24L;
                                        field.decimals = 4L;
                                    }
                                } else {
                                    field.type = 91;
                                    field.size = 19L;
                                }
                            } else {
                                field.type = 12;
                                str = strType.substring(strType.indexOf("(") + 1);
                                field.size = (long)Integer.parseInt(str.substring(0, str.indexOf(")")));
                            }
                        } else {
                            field.type = 3;
                            str = strType.substring(strType.indexOf("(") + 1);
                            field.size = (long)Integer.parseInt(str.substring(0, str.indexOf(",")));
                            str = strType.substring(strType.indexOf(",") + 1);
                            field.decimals = (long)Integer.parseInt(str.substring(0, str.indexOf(")")));
                        }

                        table.fields.add(field);
                    }
                } while(dbReader.moveToNext());

                dbReader.close();
                dbReader = null;
                dbReader = (SQLiteCursor)m_appConnection.rawQuery("PRAGMA index_list(" + table.name + ")", (String[])null);
                if(dbReader.moveToFirst()) {
                    do {
                        if(!dbReader.isNull(1)) {
                            str = dbReader.getString(1).toUpperCase();
                            boolean bPrimary = str.startsWith(PRIMARY_KEY.toUpperCase());
                            boolean bAsc = true;
                            Index index = (Index)table.indexes.get(str);
                            if(index == null) {
                                index = new Index(table);
                                index.name = str;
                                index.primary = bPrimary;
                                table.indexes.put(str, index);
                            }

                            indexColumns = getIndexColumns(str);
                            int ex = 0;

                            for(int j = indexColumns.size(); ex < j; ++ex) {
                                field = null;
                                int ix = 0;

                                for(int jx = table.fields.size(); ix < jx; ++ix) {
                                    if(((Field)table.fields.get(ix)).name.equalsIgnoreCase((String)indexColumns.get(ex))) {
                                        field = (Field)table.fields.get(ix);
                                        field.primary |= index.primary;
                                        break;
                                    }
                                }

                                IndexField idxf = new IndexField();
                                idxf.ascending = bAsc;
                                idxf.field = field;
                                index.indexfields.add(idxf);
                            }

                            indexColumns.clear();
                            indexColumns = null;
                        }
                    } while(dbReader.moveToNext());
                }
            }
        } catch (Exception var18) {
            throw var18;
        } finally {
            if(dbReader != null) {
                dbReader.close();
            }

            if(indexColumns != null) {
                indexColumns.clear();
            }

        }

        return table;
    }

    public static Vector<String> getIndexColumns(String strIndexName) throws Exception {
        SQLiteCursor dbReader = null;
        Vector indexColumns = null;

        try {
            indexColumns = new Vector();
            dbReader = (SQLiteCursor)m_appConnection.rawQuery("PRAGMA index_info(" + strIndexName + ")", (String[])null);
            if(dbReader.moveToFirst()) {
                do {
                    if(!dbReader.isNull(2)) {
                        indexColumns.add(dbReader.getString(2));
                    }
                } while(dbReader.moveToNext());
            }
        } catch (Exception var7) {
            throw var7;
        } finally {
            if(dbReader != null) {
                dbReader.close();
            }

        }

        return indexColumns;
    }

    public static boolean tableExists(String strTable) throws Exception {
        SQLiteCursor dbReader = null;
        boolean bRet = false;

        try {
            dbReader = (SQLiteCursor)m_appConnection.rawQuery(" SELECT name FROM SQLITE_MASTER  WHERE type=" + stringToSQL("table") + " AND name=" + stringToSQL(strTable), (String[])null);
            bRet = dbReader.moveToFirst();
        } catch (Exception var7) {
            throw var7;
        } finally {
            if(dbReader != null) {
                dbReader.close();
            }

        }

        return bRet;
    }

    public static String dbGetvSynFlagString(boolean bAnd) {
        return dbGetvSynFlagString(bAnd, (String)null);
    }

    public static String dbGetvSynFlagString(boolean bAnd, String strAlias) {
        String str = "";
        if(bAnd) {
            str = str + " AND ";
        }

        if(strAlias != null && strAlias.length() > 0) {
            str = str + " (" + strAlias + ".vSynFlag<>" + stringToSQL("D") + " OR " + strAlias + ".vSynFlag IS NULL) ";
        } else {
            str = str + " (vSynFlag<>" + stringToSQL("D") + " OR vSynFlag IS NULL) ";
        }

        return str;
    }

    public static String formatTableName(String strTable) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for(int j = strTable.length(); i < j; ++i) {
            char c = strTable.charAt(i);
            if(c != 45 && c != 46) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }

        return sb.toString();
    }

    public static String getvSynFlagStringForPurge(boolean bAnd) {
        return getvSynFlagStringForPurge(bAnd, (String)null);
    }

    public static String getvSynFlagStringForPurge(boolean bAnd, String strAlias) {
        String str = "";
        if(bAnd) {
            str = str + " AND ";
        }

        if(strAlias != null && strAlias.length() > 0) {
            str = str + strAlias + ".vSynFlag NOT IN (" + stringToSQL("") + "," + stringToSQL("D") + ") AND " + strAlias + ".vSynFlag IS NOT NULL";
        } else {
            str = str + "vSynFlag NOT IN (" + stringToSQL("") + "," + stringToSQL("D") + ") AND vSynFlag IS NOT NULL";
        }

        return str;
    }

    public static ArrayList<String> getTableList() throws Exception {
        SQLiteCursor dbReader = null;
        ArrayList tables = null;

        try {
            tables = new ArrayList();
            dbReader = (SQLiteCursor)m_appConnection.rawQuery(" SELECT name FROM SQLITE_MASTER  WHERE type=" + stringToSQL("table"), (String[])null);
            if(dbReader.moveToFirst()) {
                do {
                    tables.add(dbReader.getString(0));
                } while(dbReader.moveToNext());
            }
        } catch (Exception var6) {
            throw var6;
        } finally {
            if(dbReader != null) {
                dbReader.close();
            }

        }

        return tables;
    }

    public class Operation {
        public static final String INSERT = "I";
        public static final String UPDATE = "U";
        public static final String DELETE = "D";
        public static final String TRANSMIT = "T";
        public static final String SUSPENDED = "S";
        public static final String LOCKED = "L";
        public static final String NULL = "";

        public Operation() {
        }
    }
}
