package com.jamontes79.database;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.jamontes79.StringTokenizer;

 

public class Table
{
	public static int TB_EQUALS = 0;  //result for Table comparison
	public static int TB_ALLDIFFERENT = -1;  //result for Table comparison
	public static int TB_DIFFERENT = -2;  //result for Table comparison

	public String name;
	public Vector<Field> fields = new Vector<Field>();
	public Hashtable<String,Index> indexes = new Hashtable<String,Index>();
	public Vector<String> relations = new Vector<String>();
	public long maxCounter = 0;
	public long maxUserCounter = 0;
	public long minUserCounter = 0;
	public boolean hasMV = false;

	public Table()
	{
	}

	/**
	 * Construct a Table from its description.
	 * 
	 * @param desc Description
	 */
	public Table(String desc)
	{
		StringTokenizer st = new StringTokenizer( desc, "\n" );
		String s = st.nextToken();
		Index index;

		name = s.substring( 13 ).trim();

		while ( st.hasMoreTokens() )
		{
			s = st.nextToken();

			if ( s.startsWith( "FIELD ") )
			{
				fields.add( new Field( s.substring( 6 ) ) );
			}
			else if ( s.startsWith( "INDEX " ) )
			{
				index = new Index( s.substring( 6 ), this );

				if ( index != null )
					indexes.put( index.name, index );
			}
		}
	}

	/// <summary>
	/// Compare this Table with "other", output results to vectors "newfields" etc.
	/// The algorithm is pessimistic: when a difference is found between tables,
	/// that cannot be reduced simply to "new non-primary field, new non-primary index,
	/// removed non-primary index, changed non-primary index", tables are declared "too
	/// different"to try an optimization.
	/// </summary>
	/// <param name="table">The Table to compare to</param>
	/// <returns>
	/// TB_EQUALS (no difference),
	/// TB_DIFFERENT (some difference reported in vectors),
	/// TB_ALLDIFFERENT (too many differences, the table should be rebuild)
	/// </returns>
	/*
	public int CompareTo(Table table, ArrayList newfields, ArrayList newindexes, ArrayList deletedindexes, ArrayList modifiedindexes)
	{
		int iRet = TB_EQUALS;

		if ( table == null )
			return TB_ALLDIFFERENT;

		if ( this == table )
			return TB_EQUALS;

		if ( !name.Equals( table.name ) )
			return TB_ALLDIFFERENT;

		//Look immediatly at primary index (if it exists)
		foreach ( Index index in indexes.Values )
			if ( index.primary )
			{
				bool bOk = false;

				foreach ( Index index1 in table.indexes.Values )
					if ( index1.primary && index.Equals( index1 ) ) 
					{
						bOk = true;
						break;
					}

				if ( !bOk )
					return TB_ALLDIFFERENT;

				break;
			}

		//Search for modified fields (this is serious, we'll return TB_ALLDIFFERENT)
		foreach ( Field field in fields )
		{
			Field field1 = null;

			foreach ( Field f in table.fields )
				if ( f.name.Equals( field.name ) ) 
				{
					field1 = f;
					break;
				}

			if ( field1 != null && !field.Equals( field1 ) )
				return TB_ALLDIFFERENT;
		}

		// Search for deleted fields
		// SQLite doesn't support "alter table drop"... then returns TB_ALLDIFFERENT
		foreach ( Field field in fields )
			if ( !field.name.ToLower().Equals( "vsynflag" ) )
			{
				Field field1 = null;

				foreach ( Field f in table.fields )
					if ( f.name.Equals( field.name ) ) 
					{
						field1 = f;
						break;
					}

				if ( field1 == null )
					return TB_ALLDIFFERENT;
			}

		//Search for added fields NOT NULLABLE (we'll return TB_ALLDIFFERENT)
		foreach ( Field field in table.fields )
		{
			Field field1 = null;

			foreach ( Field f in fields )
				if ( f.name.Equals( field.name ) ) 
				{
					field1 = f;
					break;
				}

			if ( field1 == null && !field.nullable )
				return TB_ALLDIFFERENT;
		}

		//Search for added fields
		foreach ( Field field in table.fields )
		{
			Field field1 = null;

			foreach ( Field f in fields )
				if ( f.name.Equals( field.name ) ) 
				{
					field1 = f;
					break;
				}

			if ( field1 == null )
			{
				newfields.Add( this ); //the Table...
				newfields.Add( field ); //...and the Field to add

				iRet = TB_DIFFERENT;
			}
		}

		//Search for deleted or modified indexes
		foreach ( Index index in indexes.Values )
			if ( !index.primary )
			{
				Index index1 = (Index) table.indexes[index.name];

				if ( index1 == null )
				{
					deletedindexes.Add( this );
					deletedindexes.Add( index );

					iRet = TB_DIFFERENT;
				}
				else if ( !index.Equals( index1 ) ) 
				{
					modifiedindexes.Add( this );
					modifiedindexes.Add( index1 );

					iRet = TB_DIFFERENT;
				}
			}

		//Search for new indexes
		foreach ( Index index in table.indexes.Values )
			if ( !index.primary )
			{
				Index index1 = (Index) indexes[index.name];

				if ( index1 == null )
				{
					if ( index.primary )
						return TB_ALLDIFFERENT;
					else 
					{
						newindexes.Add( this );
						newindexes.Add( index );

						iRet = TB_DIFFERENT;
					}
				}
			}

		return iRet;
	}
	*/
	
	@Override
	public String toString()
	{
		String crlf = "\r\n";
		String s = "CREATE TABLE " + name;
		Index[] idxs;

		for ( int i = 0, j = fields.size(); i < j; i ++ )
			s += crlf + "FIELD " + fields.get( i );
		
		idxs = (Index[]) indexes.values().toArray();

		for ( int i = 0, j = idxs.length; i < j; i ++ )
			s += crlf + "INDEX " + idxs[i];

		s += crlf + "END TABLE";

		return s;
	}

	/// <summary>
	/// Add to the ArrayList v the correct SQL creation statements.
	/// </summary>
	/// <param name="v"></param>
	/*
	public void AddSQL(ArrayList v) 
	{
		string s = "";
		bool bMissingVsynflag = true;

		v.Add( "drop table " + name );

		for ( int i = 0; i < fields.Count; i ++ ) 
		{
			Field f = (Field) fields[i];

			if ( i > 0 )
				s += ",";

			s += f.ToSQL();

			if ( f.name.ToLower().Equals( "vsynflag" ) )
				bMissingVsynflag = false;
		}

		//If the vsynFlag field is missed, in ACCESS mode, add it...
		if ( bMissingVsynflag ) 
		{
			Field nf = new Field();

			nf.name = "VSYNFLAG";
			nf.decimals = 0;
			nf.primary = false;
			nf.nullable = true;
			nf.size = 1;
			nf.type = Field.DataType.CHAR;

			s = nf.ToSQL() + "," + s;
		}

		s = "create table " + name + "(" + s;

		//Add primary key constraint
		foreach ( Index f in indexes.Values )
			if ( f.primary ) 
			{
				s += "," + f.ToSQL();
				break;
			}

		s += ")";

		v.Add( s );

		foreach ( Index f in indexes.Values )
			if ( !f.primary ) 
			{
				s = f.ToSQL();

				if ( s != null )
					v.Add( s );
			}
	}
	*/

	/**
	 * Return primaryKey if exist.
	 * 
	 * @return Index instance
	 */
	public Index primaryKey()
	{
		Iterator<Index> iterator = indexes.values().iterator();
		Index idx;
		
		while ( iterator.hasNext() )
		{
			idx = iterator.next();
				
			if ( idx.primary )
				return idx;
		}

		return null;
	}

	/**
	 * The correct SQL index CREATE statements (NOT PRIMARY !!!).
	 * 
	 * @param index Index
	 * @return SQL statement
	 */
	public String AddIndexSQL(Index index) 
	{
		return !index.primary ? index.toSQL() : "";
	}

	/**
	 * The correct SQL index DROP statements.
	 * 
	 * @param index Index
	 * @return SQL statement
	 */
	public String DropIndexSQL(Index index) 
	{
		return "drop index " + index.name;
	}

}
