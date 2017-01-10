
public class Message {

	enum msgType{
		BUTTON,
		SERVICE,
		DATA,
		OTHER
	}
	enum sourceType{
		PAIR,
		UNPAIR,
		STARTSESS,
		FINISHSESS,
		ADDCMD,
		EDITCMD,
		ADDTEMPLATE,
		EDITTEMPLATE,
		RUN,
		STOP
	}
	enum serviceType{
		SAVE,
		EDIT,
		INSERT,
		DELETE,
		DELETEALL,
		OTHER
	}
	msgType iMsgType;
	sourceType iSource;
	serviceType iServiceType;
	String data;
}
