package iut.oneswitch.action;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.speech.tts.TextToSpeech;

public class SpeakAText{

	private static  TextToSpeech ttobj;
	private static boolean ready = false;


	private static void init(Context context, final String text){
		ttobj=new TextToSpeech(context,
				new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {

				ttobj.setLanguage(Locale.FRENCH);

				ready = true;

				ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			}
		});
	}

	public static void speak(Context context, final String text){

		if(ready){
			ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
		else{
			init(context, text);
		}              
	}

	public static String retrieveContact(Context context, String number) {

		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if (cursor == null) {
			return "Inconnu";
		}
		String contactName = null;
		if(cursor.moveToFirst()) {
			contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}

		if(cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;

	}
}