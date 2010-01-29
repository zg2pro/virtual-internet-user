package Main;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * This custom formatter formats parts of a log record to a single line
 */
public class LogsFormater extends Formatter {
	
	/**
	 * This method is called for every log records
	 */
	public String format(LogRecord theLogRecord){
		StringBuffer theStringBuffer = new StringBuffer(1000);
		// Bold any levels >= WARNING

		if (theLogRecord.getLevel().intValue() >= Level.WARNING.intValue())
		{
			if (theLogRecord.getLevel().intValue() > Level.WARNING.intValue())
				theStringBuffer.append("<tr class='Severe'>");
			else theStringBuffer.append("<tr class='Warning'>");
		} else
		{
			theStringBuffer.append("<tr>");
		}
		theStringBuffer.append("<td>");
		theStringBuffer.append(theLogRecord.getLevel());
		theStringBuffer.append("</td>");
		theStringBuffer.append("<td>");
		theStringBuffer.append(getDate(theLogRecord.getMillis()));
		theStringBuffer.append("</td><td>");
		theStringBuffer.append(formatMessage(theLogRecord));
		theStringBuffer.append('\n');
		theStringBuffer.append("</td>");
		theStringBuffer.append("</tr>\n");
		return theStringBuffer.toString();
	}//format

	/**
	 * 
	 * @param millisecs
	 * @return
	 */
	private String getDate(long millisecs){
		SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date theDate = new Date(millisecs);
		return aSimpleDateFormat.format(theDate);
	}//getDate

	/**
	 *  This method is called just after the handler using this
	 *  formatter is created
	 */
	public String getHead(Handler h){
		return "<html>\n<head>\n" + (new Date()) + "\n" +
				"<script type='text/javascript' src='Tables.js'></script>" +
				"<style> .Severe {" +
				"	color:red;  " +
				"font-weight: bold; " +
				"font-size: 12pt;" +
				"} " +
				".Warning {" +
				"	color:blue;  " +
				"font-weight: bold; " +
				"font-size: 11pt;" +
				"}</style>" +
				"</head>\n<body>\n"
				+ "<table border  class='sortable'>  "
				+ "<tr><th>Niveau de l'evenement</th><th>Date</th><th>Evenement releve</th></tr>\n";
	}//getHead

	/**
	 *  This method is called just after the handler using this
	 *  formatter is closed
	 */
	public String getTail(Handler h) {
		return "</table></body>\n</html>\n";
	}//getTail
	
}//class

