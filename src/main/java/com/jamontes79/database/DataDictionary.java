package com.jamontes79.database;

import java.util.Vector;

public class DataDictionary {

	/**
	 * Data dictionary
	 */
	
	
	public static final String DV_PELICULAS = "DV_PELICULAS";
	public static final String[] DV_PELICULAS_FIELDS = { "PELICULAID", "TITULO", "FORMATO", "GENERO", "PRESTADA", "DURACION", "SINOPSIS", "PUNTUACION", "CARATULA","TITULOSINACENTO","TRAILER","BUSCADA","THUMBNAIL","IMDBID","VSYNFLAG","NEW_IMDBID" };
	public static final String[] DV_PELICULAS_TYPES = { "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "BLOB"/*"TEXT"*/ ,"TEXT" ,"TEXT","TEXT","BLOB","TEXT","TEXT","TEXT" };
	public static final String[] DV_PELICULAS_KEYS = { "PELICULAID" };
	
	public static final String DV_CONTADORES = "DV_CONTADORES";
	public static final String[] DV_CONTADORES_FIELDS = { "CONTADORID", "VALOR" };
	public static final String[] DV_CONTADORES_TYPES = { "TEXT", "TEXT" };
	public static final String[] DV_CONTADORES_KEYS = { "CONTADORID" };
	
	public static final String DV_PELICULAS_BARCODE = "DV_PELICULAS_BARCODE";
	public static final String[] DV_PELICULAS_BARCODE_FIELDS = { "PELICULAID", "BARCODE","VSYNFLAG" };
	public static final String[] DV_PELICULAS_BARCODE_TYPES = { "TEXT", "TEXT", "TEXT" };
	public static final String[] DV_PELICULAS_BARCODE_KEYS = { "PELICULAID" };
	
	public static final String DV_PELICULAS_COVERFLOW = "DV_PELICULAS_COVERFLOW";
	public static final String[] DV_PELICULAS_COVERFLOW_FIELDS = { "PELICULAID", "COVER","VSYNFLAG" };
	public static final String[] DV_PELICULAS_COVERFLOW_TYPES = { "TEXT", "BLOB", "TEXT" };
	public static final String[] DV_PELICULAS_COVERFLOW_KEYS = { "PELICULAID" };
	
	public static Vector<String> getTables() {
		 Vector<String> vTables = new Vector<String>();
		 vTables.add(DV_PELICULAS);
		 vTables.add(DV_CONTADORES);
		 vTables.add(DV_PELICULAS_BARCODE);
		 vTables.add(DV_PELICULAS_COVERFLOW);
		 
		 return vTables;
	}
	public static Vector<String[]> getTablesFields() {
		 Vector<String[]> vTablesFields = new Vector<String[]>();
		 vTablesFields.add(DV_PELICULAS_FIELDS);
		 vTablesFields.add(DV_CONTADORES_FIELDS);
		 vTablesFields.add(DV_PELICULAS_BARCODE_FIELDS);
		 vTablesFields.add(DV_PELICULAS_COVERFLOW_FIELDS);
		 return vTablesFields;
	}
	public  static Vector<String[]> getTableTypes() {
		 Vector<String[]> vTableTypes = new Vector<String[]>();
		 vTableTypes.add(DV_PELICULAS_TYPES);
		 vTableTypes.add(DV_CONTADORES_TYPES);
		 vTableTypes.add(DV_PELICULAS_BARCODE_TYPES);
		 vTableTypes.add(DV_PELICULAS_COVERFLOW_TYPES);
		 return vTableTypes;
	}
	public  static Vector<String[]> getTableKeys() {
		 Vector<String[]> vTableKeys = new Vector<String[]>();
		 vTableKeys.add(DV_PELICULAS_KEYS);
		 vTableKeys.add(DV_CONTADORES_KEYS);
		 vTableKeys.add(DV_PELICULAS_BARCODE_KEYS);
		 vTableKeys.add(DV_PELICULAS_COVERFLOW_KEYS);
		 return vTableKeys;
	}
}
