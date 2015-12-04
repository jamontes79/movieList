package com.jamontes79.database;


public class IndexField
{
	public Field field;
	public boolean ascending;
	
	@Override
	public boolean equals(Object obj)
	{
		if ( obj == null )
			return false;

		if ( !(obj instanceof  IndexField) )
			return false;

		IndexField x = (IndexField) obj;

		if ( this == x )
			return true;

		if ( !field.equals( x.field ) )
			return false;

		if ( ascending != x.ascending )
			return false;

		return true;
	}

}
