package helloworld.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import helloworld.HelloWorldLogic;

public class HelloWorldActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Calling the application logic
        String helloMessage = HelloWorldLogic.helloMessage();

        // Displaying the message
        TextView textView = (TextView) findViewById(R.id.helloTextView);
        textView.setText(helloMessage);
    }
}
