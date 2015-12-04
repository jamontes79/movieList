package com.jamontes79.database;

import com.jamontes79.StringTokenizer;


 
/**
 * Describes a single table column.
 * The supported DataType are expressed as java.sql.Types values
 * and are restricted to these values:
 * BIT for boolean types (-7)
 * DATE (91)
 * CHAR (1)
 * VARCHAR (12)
 * DOUBLE (8)
 * INTEGER (4)
 * DECIMAL (3)
 */
public class Field
{
	public class DataType
	{
		public static final int BIT = -7;
		public static final int CHAR = 1;
		public static final int DATE = 91;
		public static final int DECIMAL = 3;
		public static final int DOUBLE = 8;
		public static final int INTEGER = 4;
		public static final int REAL = 7;
		public static final int VARCHAR = 12;
	}

	public int type;
	public String name;
	public long size,decimals;
	public boolean nullable = true;
	/// <summary>primary key field</summary>
	public boolean primary = false;

	public Field()
	{
	}

	/**
	 * A new Field from its description.
	 * @param s Field description
	 */
	public Field(String s)
	{
		StringTokenizer tk = new StringTokenizer( s, ";" );

		name = tk.nextToken();
		type = Integer.parseInt( tk.nextToken() );
		size = Integer.parseInt( tk.nextToken() );
		decimals = Integer.parseInt( tk.nextToken() );
		nullable = tk.nextToken().equals( "NULL" );
		primary = tk.nextToken().equals( "true" );
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if ( obj == null )
			return false;

		if ( !(obj instanceof  Field) )
			return false;

		if ( this == obj )
			return true;

		Field x = (Field) obj;

		if ( !name.equals( x.name ) )
			return false;

		if ( !toSQL().equals( x.toSQL() ) )
			return false;

		if ( primary != x.primary )
			return false;

		return true;
	}
	
	@Override
	public String toString()
	{
		String nl = "NOT NULL";

		if ( !primary && nullable && ( type == DataType.CHAR || type == DataType.VARCHAR || type == DataType.DATE ) )
			nl = "NULL";

		if ( !primary && nullable && ( type == DataType.DECIMAL || type == DataType.DOUBLE || type == DataType.INTEGER || type == DataType.REAL ) )
			nl = "NULL";

		return name + ";" + type + ";" + size + ";" + decimals + ";" + nl + ";" + primary;
	}

	/**
	 * The correct SQL creation statement.
	 */
	public String toSQL() 
	{
		String s = name + " ";

		switch ( type ) 
		{
			case DataType.BIT:
				s += "BIT";
				break;

			case DataType.DATE:
				s += "DATETIME";
				break;

			case DataType.DOUBLE:
				s += "FLOAT";
				break;

			case DataType.REAL:
				s += "REAL";
				break;

			case DataType.VARCHAR:
			case DataType.CHAR:
				if ( size > 255 )
					s += "NTEXT";
				else
					s += "NVARCHAR(" + size + ")";

				break;

			case DataType.INTEGER:
				s += "INTEGER";
				break;

			case DataType.DECIMAL:
				s += "NUMERIC(" + size + "," + decimals + ")";
				break;

			default:
				s += "UNSUPPORTED:" + type;
				break;
		}

		s += nullable ? " NULL" : " NOT NULL";

		return s;
	}

	/**
	 * Return the corrected SQL expression for "value".
	 * 
	 * @param strValue Value
	 * @return Corrected value
	 */
	/*
	public String SQLValue(String strValue)
	{
		String s;

		if ( strValue == null )
			s = "";
		else if ( strValue.equals( " " ) )
			s = strValue;
		else
			s = strValue.trim();

		switch ( type )
		{
			case Field.DataType.BIT:

				if ( s.equalsIgnoreCase( "false" ) || s.equals( "0" ) || s.equals( "" ) )
					s = "false";
				else
					s = "true";

				break;

			case Field.DataType.DATE:

				if ( s.equals( "" ) )
					s = "null";
				else
					s = DateUtil.getSQLdate( DateUtil.getDate( s ) );

				break;

			case Field.DataType.DECIMAL:
			case Field.DataType.DOUBLE:
			case Field.DataType.REAL:
			case Field.DataType.INTEGER:

				if ( s.equals( "" ) )
					s = "0";
				else
					s = s.replace( ',', '.' );

				break;

			case Field.DataType.CHAR:
			case Field.DataType.VARCHAR:

				if ( s.length() > size )
					s = s.substring( 0, (int) size );

				s = "'" + s + "'";

				break;

			default:

				s = "'" + s + "'";
				break;
		}

		return s;
	}
	*/

	/// <summary>
	/// Return the corrected SQL expression for "value" for a prepared command.
	/// </summary>
	/// <param name="strValue">Value</param>
	/// <returns>Corrected value</returns>
	public String SQLPreparedValue(String strValue)
	{
		String s;

		if ( strValue == null )
			s = "";
		else if ( strValue.equals( " " ) )
			s = strValue;
		else
			s = strValue.trim();

		switch ( type )
		{
			case Field.DataType.BIT:

				if ( s.equalsIgnoreCase( "false" ) || s.equals( "0" ) || s.equals( "" ) )
					s = "0";
				else
					s = "1";

				break;

			case Field.DataType.DECIMAL:
			case Field.DataType.DOUBLE:
			case Field.DataType.REAL:
			case Field.DataType.INTEGER:

				if ( s.equals( "" ) )
					s = "0";
				else
					s = s.replace( ',', '.' );

				break;

			case Field.DataType.CHAR:
			case Field.DataType.VARCHAR:
			case Field.DataType.DATE:
			default:

				if ( s.length() > size )
					s = s.substring( 0, (int) size );

				break;
		}

		return s;
	}

}
