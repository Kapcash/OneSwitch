package iut.oneswitch.action;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.speech.tts.TextToSpeech;

/**
 * Classe permettant la gestion de la synthèse vocale.
 * @author OneSwitch B
 *
 */
public class SpeakAText{

	/**
	 * Objet fondamental permettant l'implémentation de la synthèse vocale. Il permet de définir la langue de la voix.
	 */
	private static  TextToSpeech ttobj;

	/**
	 * Indique si "ttobj" a été initialisé.
	 */
	private static boolean ready = false;

	/**
	 * Initialisation de la synthèse vocale.
	 * @param context Le contexte de l'application
	 * @param text Le texte qui doit être prononcé.
	 */
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

	/**
	 * Permet la prononciation d'une phrase.
	 * @param context Le contexte de l'application.
	 * @param text Le texte qui doit être prononcé.
	 */
	public static void speak(Context context, final String text){
		if(ready){
			ttobj.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
		else{
			init(context, text);
		}
	}

	/**
	 * Permet la prononciation d'une phrase en plus de vider la file d'attente.
	 * @param context Le contexte de l'application.
	 * @param text Le texte qui doit être prononcé.
	 */
	public static void resetSpeak(Context context, final String text) {
		if(ready){
			ttobj.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
		else{
			init(context,text);
		}
	}


	/**
	 * Permet de retrouver le nom d'un contact à partir de son numéro de téléphone.
	 * @param context Le contexte de l'application
	 * @param number le numéro du téléphone
	 * @return Le nom de la personne qui appelle.
	 */
	public static String retrieveContact(Context context, String number) {

		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if (cursor == null) {
			return null;
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

	/**
	 * Permet d'émettre un son.
	 * @param context Le contexte de l'application
	 */
	public static void playSound(Context context) {

		final MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse("file:///system/media/audio/ui/KeypressStandard.ogg"));

		if (mediaPlayer != null)
		{
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			mediaPlayer.setVolume(1, 1);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(MediaPlayer mediaPlayer)
				{
					if (mediaPlayer != null)
					{
						mediaPlayer.stop();
						mediaPlayer.release();
						mediaPlayer = null;
					}
				}
			});
			mediaPlayer.start();
		}
	}
}