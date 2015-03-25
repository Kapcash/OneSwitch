package iut.oneswitch.action;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

/**
 * Permet de gèrer les actions sur les boutons du menu sur appuie long.
 * @author OneSwitch B
 *
 */
public class ActionButton {

	private static boolean volumeStop = true;
	private static int count;
	private static boolean changeWay = false;

	/**
	 * Permet de gèrer le retour.
	 */
	public static void back(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_BACK);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Permet de gèrer le multi Taches
	 */
	public static void taches(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_APP_SWITCH);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Permet de gèrer l'accès à l'accueil.
	 */
	public static void home(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_HOME);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Permet de gèrer l'accès au menu s'il y en a un.
	 */
	public static void menu(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_MENU);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Permet de gèrer le volume.
	 * @param context
	 * @throws IOException
	 */
	public static void volumeUp(final Context context) throws IOException{
		final Handler handler = new Handler();
		volumeStop = false;
		final SharedPreferences sp;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		final int iterations = Integer.parseInt(sp.getString("iterations","3"));
		count = 0;
		
		final AudioManager myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		boolean bool = myAudioManager.isMusicActive();
		int s = 0;
		if(bool)
			s = AudioManager.STREAM_MUSIC;
		else
			s = AudioManager.STREAM_RING;
		final int stream = s;

		Runnable a = new Runnable() {

			public void run() {
		
				try {
					if(count != iterations && !volumeStop) {
						if(!changeWay) {
							if(myAudioManager.getStreamVolume(stream) != myAudioManager.getStreamMaxVolume(stream)) {
								myAudioManager.adjustStreamVolume(stream,
			                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
							}
							else {
								changeWay = true;
							}
						}
						if(changeWay && count != iterations) {
							if(myAudioManager.getStreamVolume(stream) != 0) {
								myAudioManager.adjustStreamVolume(stream,
				                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
								
							}
							else {
								changeWay = false;
								count++;
							}
							
						}
						if(count != iterations){
							handler.postDelayed(this, 250);
						}
					}
					else {
						changeWay = false;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		a.run();
	}



	/**
	 * Permet de gèrer le vérouillage.
	 */
	public static void lock(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_POWER);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Permet de gèrer le fait d'éteindre l'appareil.
	 */
	public static void shutdown(){
		try{
			//Runtime.getRuntime().exec("su -c shutdown");
			Runtime.getRuntime().exec("su -c reboot -p");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Stopper l'augmentation du volume.
	 */
	public static void stopVolumeChange() {
		volumeStop = true;
	}

	/**
	 * Savoir si le volume est stoppé.
	 * @return true si le volume est stoppé, false sinon.
	 */
	public static boolean getVolumeStop() {
		return volumeStop;
	}
}
