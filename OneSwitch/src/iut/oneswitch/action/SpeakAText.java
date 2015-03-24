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

	private static  TextToSpeech ttobj;
	private static boolean ready = false;

	/**
	 * Initialisation de la synthèse vocale
	 * @param context
	 * @param text
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
	 * Permet de commencer une phrase.
	 * @param context
	 * @param text
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
	 * Perme de retrouver le nom du contact qui appelle
	 * @param context
	 * @param number le numéro du téléphone
	 * @return le nom de la personne qui appelle.
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
	 * Permet de faire un son su l'apparition d'une barre.
	 * @param context
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