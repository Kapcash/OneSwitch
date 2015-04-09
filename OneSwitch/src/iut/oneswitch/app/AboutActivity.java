package iut.oneswitch.app;

import iut.oneswitch.R;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Permettant l'affichage de l'activité A propos.
 * @author OneSwitch B
 *
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Charge le .xml
		setContentView(R.layout.activity_about);

		//Enlève l'icone et modifie le titre de la ActionBar
		ActionBar ab = getActionBar();
		ab.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#368aff")));
		ab.setTitle("A propos");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		//Clic sur "retour" dans la ActionBar
		if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
