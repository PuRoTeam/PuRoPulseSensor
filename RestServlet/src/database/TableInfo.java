package database;

public enum TableInfo 
{
	TablePoint("point"),
	TPointUid("uid"),
	TPointValue("value"),
	TPointTimestamp("timestamp"),
	
	TablePatient("patient"),
	TPatientUid("uid"),
	TPatientFirstName("firstname"),
	TPatientLastName("lastname"),
	
	TableUser("user"),
	TUserUid("uid"),
	TUserFirstName("firstname"),
	TUserLastName("lastname"),
	TUserUserName("username"),
	TUserPassword("password");
	    
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
