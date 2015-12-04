package com.jamontes79.database;

import com.jamontes79.database.Field;
import com.jamontes79.database.IndexField;



import java.util.Vector;

import com.jamontes79.StringTokenizer;
import com.jamontes79.database.Table;


public class Index
{
	public Vector<IndexField> indexfields = new Vector<IndexField>();
	private String tbname = "";
	public String name;
	public boolean primary = false;

	public Index(Table table)
	{
		tbname = table.name;
	}

	public Index(String str, Table table)
	{
		StringTokenizer st = null;
		IndexField ifld;
		String ss;
		String fname;
		Field field;
		Field f;
		boolean ascending;

		st = new StringTokenizer( str, ";" );

		tbname = st.nextToken();
		name = st.nextToken().toUpperCase();
		primary = st.nextToken().equals( "true" );
		
		while ( st.hasMoreTokens() ) 
		{
			ss = st.nextToken();
			ascending = ( ss.startsWith( "+" ) );
			fname = ss.substring( 1 );

			f = null;

			for ( int i = 0, j = table.fields.size(); i < j; i ++)
			{
				field = table.fields.get( i );
				
				if ( field.name.equals( fname ) ) 
				{
					f = field;
					break;
				}
			}

			ifld = new IndexField();

			ifld.ascending = ascending;
			ifld.field = f;

			indexfields.add( ifld );
		}
	}
	
	@Override
	public String toString()
	{
		IndexField ifld;
		String s = tbname + ";" + name + ";" + primary;

		for ( int i = 0, j = indexfields.size(); i < j; i ++ )
		{
			ifld = indexfields.get( i );
			
			s += ";" + ( ifld.ascending ? "+" : "-" ) + ifld.field.name;
		}

		return s;
	}

	public String toSQL() 
	{
		String s;
		String delimsx = "";
		String delimdx = "";

		if ( primary )
		{
			s = "constraint " + name +	" primary key (";

			for ( int i = 0; i < indexfields.size(); i ++ ) 
			{
				IndexField idxf = (IndexField) indexfields.get( i );

				if ( i > 0 )
					s += ",";

				s += delimsx + idxf.field.name + delimdx;

				if ( !idxf.ascending )
					s += " DESC";
			}

			s += ")";
		}
		else 
		{
			s = "create index " + name + " on " + tbname + "(";

			for ( int i = 0; i < indexfields.size(); i ++ ) 
			{
				IndexField idxf = (IndexField) indexfields.get( i );

				if ( i > 0 )
					s += ",";

				s += delimsx + idxf.field.name + delimdx;

				if ( !idxf.ascending )
					s += " DESC";
			}

			s += ")";
		}

		return s;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if ( obj == null )
			return false;

		if ( !(obj instanceof Index ) )
			return false;

		Index x = (Index) obj;

		if ( this == x )
			return true;

		if ( !name.equals( x.name ) && !primary )
			return false;

		if ( primary != x.primary )
			return false;

		if ( indexfields.size() != x.indexfields.size() )
			return false;

		for ( int i = 0; i < indexfields.size(); i ++ )
		{
			IndexField ifld = (IndexField) indexfields.get( i );
			IndexField ifld1 = (IndexField) x.indexfields.get( i );

			if ( !ifld.field.name.equals( ifld1.field.name ) )
				return false;

			if ( ifld.ascending != ifld1.ascending )
				return false;
		}

		return true;
	}

}
