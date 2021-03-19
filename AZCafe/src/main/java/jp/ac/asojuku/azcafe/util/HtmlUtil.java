/**
 *
 */
package jp.ac.asojuku.azcafe.util;

/**
 * @author nishino
 *
 */
public class HtmlUtil {

	/**
	 * 改行を＜BR>に変換する
	 * @param text
	 * @return
	 */
	public static String nl2be(String text){

		if( text == null ){
			return "";
		}

		text = text.replaceAll("&", "&amp;");
		text = text.replaceAll("<", "&lt;");
		text = text.replaceAll(">", "&gt;");
		text = text.replaceAll("\"", "&quot;");
		text = text.replaceAll("\n", "<BR>");
		text = text.replaceAll("\r\n", "<BR>");
		text = text.replaceAll("\t", "&nbsp&nbsp&nbsp&nbsp");
		text = text.replaceAll(" ", "&nbsp");
		text = text.replaceAll("　", "&nbsp");

		return text;
	}
}


