//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jamontes79;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.DetailedState;
import android.os.Environment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;
import com.jamontes79.database.DBUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import junit.framework.Assert;

public class MyUtil {
    public static final String DATA_FOLDER = "data";
    public static final String INITIAL_COUNTER = "0000";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public MyUtil() {
    }

    public static String getNewCounter(String pCounterType) throws Exception {
        return getNewCounter(pCounterType, (String)null);
    }

    public static String getNewCounter(String pCounterType, String pTableName) throws Exception {
        String counter = "";
        boolean exists = false;
        String table = pTableName;
        if(pTableName == null) {
            table = "MD_CONTADORES";
        }

        String sql = "SELECT VALOR FROM " + table + " WHERE CONTADORID =\'" + pCounterType + "\'";
        Cursor cur = null;

        String var9;
        try {
            cur = DBUtil.m_appConnection.rawQuery(sql, (String[])null);
            boolean iCounter = false;
            if(cur.moveToNext()) {
                counter = cur.getString(0);
                exists = true;
            } else {
                counter = "0000";
            }

            int iCounter1 = Integer.parseInt(counter) + 1;
            counter = padLeft(Integer.toString(iCounter1), "0", 4);
            if(exists) {
                sql = "UPDATE " + table + " SET VALOR = \'" + counter + "\' WHERE CONTADORID =\'" + pCounterType + "\'";
            } else {
                sql = "INSERT INTO " + table + " (CONTADORID, VALOR) " + " VALUES (" + "\'" + pCounterType + "\'," + "\'" + counter + "\')";
            }

            DBUtil.m_appConnection.execSQL(sql);
            var9 = counter;
        } finally {
            if(cur != null) {
                cur.close();
            }

        }

        return var9;
    }

    public static String padLeft(String pValue, String pChar, int pLength) {
        String value = pValue;

        for(int i = pValue.length(); i < pLength; ++i) {
            value = pChar + value;
        }

        return value;
    }



    public static boolean hasStorage(Context pContext, boolean pRequireWriteAccess, String pFolder) throws Exception {
        String state = Environment.getExternalStorageState();
        if("mounted".equals(state)) {
            if(pRequireWriteAccess) {
                File f = new File(pFolder + "/testwrite.dat");
                boolean writable = true;

                try {
                    f.mkdirs();
                    f.createNewFile();
                    f.delete();
                } catch (IOException var7) {
                    writable = false;
                }

                return writable;
            } else {
                return true;
            }
        } else if(!pRequireWriteAccess && "mounted_ro".equals(state)) {
            return true;
        } else {

            return false;
        }
    }

    public static int getResId(String variableName, Context context) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(variableName);
        int id = context.getResources().getIdentifier(variableName, "drawable", context.getPackageName());
        return id;
    }

    public static void closeStreamQuietly(InputStream inputStream) {
        try {
            if(inputStream != null) {
                inputStream.close();
            }
        } catch (IOException var2) {
            ;
        }

    }



    public static boolean checkConex(Context ctx) {
        boolean bTieneConexion = false;
        ConnectivityManager connec = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for(int i = 0; i < redes.length; ++i) {
            if(redes[i].isAvailable() && redes[i].getDetailedState() == DetailedState.CONNECTED) {
                bTieneConexion = true;
            }
        }

        return bTieneConexion;
    }
    /*
        public static String cleanForSearch(String pText, boolean pUrl) {
            String cleanedText = "";
            cleanedText = scape(pText.toUpperCase());
            cleanedText = cleanedText.replaceAll("[^A-Z0-9]", " ");
            cleanedText = cleanedText.replaceAll("\\s\\s+", "%20");
            return cleanedText;
        }

        public static String cleanForSearch(String pText) {
            return cleanForSearch(pText, false);
        }

        public static String scape(String pValue) {
            String scapedText = pValue.replaceAll("ï¿½", "A").replaceAll("ï¿½", "E").replaceAll("ï¿½", "I").replaceAll("ï¿½", "O").replaceAll("ï¿½", "U");
            return scapedText;
        }
    */
    public static boolean isMissing(String s) {
        return s == null || s.trim().equals("");
    }

    public static String formatIntoHHMMSS(int secsIn) {
        int hours = secsIn / 3600;
        int remainder = secsIn % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;
        String ret = "";
        if(hours > 0) {
            ret = (hours < 10?"0":"") + hours + ":";
        }

        return ret + (minutes < 10?"0":"") + minutes + ":" + (seconds < 10?"0":"") + seconds;
    }

    @SuppressLint({"NewApi"})
    public static boolean isTablet(Context context) {
        boolean xlarge = (context.getResources().getConfiguration().screenLayout & 15) == 4;
        boolean large = (context.getResources().getConfiguration().screenLayout & 15) == 3;
        return xlarge || large;
    }

    public static Date getDateFromString(String pDate, String pFormat) {
        Date date = null;

        try {
            SimpleDateFormat e = new SimpleDateFormat(pFormat);
            date = e.parse(pDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException var5) {
            System.out.println("Exception :" + var5);
        }

        return date;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, 4);
        return bd.floatValue();
    }

    public static Date getDateFromDatePicket(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public static void copy(File src, File dst) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];

        int len;
        while((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    public static String stringEscapeToSQL(String str) {
        String strSQL = "";
        if(str != null && str.length() > 0) {
            strSQL = str.replaceAll("\'", "\'\'");
        }

        return strSQL;
    }

    public static String getDateNow() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static Date DateFromString(String pDate) throws ParseException {
        return (new SimpleDateFormat("dd/MM/yyyy")).parse(pDate);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;

            for(int halfWidth = width / 2; halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth; inSampleSize *= 2) {
                ;
            }
        }

        return inSampleSize;
    }

    public static String getRealPathFromURI(Context pCntx, Uri contentURI) {
        Cursor cursor = pCntx.getContentResolver().query(contentURI, (String[])null, (String)null, (String[])null, (String)null);
        String result;
        if(cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex("_data");
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), (Paint)null);
        canvas.drawBitmap(bmp2, 0.0F, 0.0F, (Paint)null);
        return bmOverlay;
    }

    public static Bitmap combineImages(Bitmap c, Bitmap s) {
        Bitmap cs = null;
        boolean height = false;
        int width;
        int height1;
        if(c.getWidth() < s.getWidth()) {
            width = c.getWidth();
            height1 = c.getHeight();
        } else {
            width = s.getWidth();
            height1 = s.getHeight();
        }

        cs = Bitmap.createBitmap(width, height1, Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, new Matrix(), (Paint)null);
        comboImage.drawBitmap(s, new Matrix(), (Paint)null);
        return cs;
    }
}
