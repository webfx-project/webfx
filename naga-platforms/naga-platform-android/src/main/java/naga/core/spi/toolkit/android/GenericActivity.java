package naga.core.spi.toolkit.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Bruno Salmon
 */
public class GenericActivity extends Activity {

    public static View VIEW;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        //View view = (View) intent.getExtras().get("VIEW");

        // Creating a new RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(this);

        // Defining the RelativeLayout layout parameters.
        // In this case I want to fill its parent
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        // Creating a new TextView
        TextView tv = new TextView(this);
        tv.setText("Test");

        // Defining the layout parameters of the TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);

        // Setting the parameters on the TextView
        tv.setLayoutParams(lp);

        // Adding the TextView to the RelativeLayout as a child
        relativeLayout.addView(tv);

        // Setting the RelativeLayout as our content view
        setContentView(relativeLayout, rlp);
    }

}
