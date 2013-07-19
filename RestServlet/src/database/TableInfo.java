package database;

public enum TableInfo 
{
	TablePoint("point"),
	TPointUid("uid"),
	TPointValue("value"),
	TPointTimestamp("timestamp"),
	
	TableUser("user"),
	TUserUid("uid"),
	TUserName("name"),
	TUserLastName("lastname");
	    
    private final String typeId;

    private TableInfo(String typeId) 
    {
        this.typeId = typeId;
    }

    public String toString() 
    {
        return typeId;
    }
}
