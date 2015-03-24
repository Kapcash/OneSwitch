package iut.oneswitch.app;

import iut.oneswitch.R;
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
		setContentView(R.layout.activity_about);
		
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("A propos");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
